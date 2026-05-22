(function () {
    'use strict';

    const ctx = window.ELU_CARTO_CTX || '';
    const geoJsonUrl = window.ELU_CARTO_GEOJSON || ctx + '/regions.geojson';
    const PALETTE = ['#ffffcc', '#d9f0a3', '#addd8e', '#78c679', '#31a354', '#006837'];

    const regionSelect = document.getElementById('codeRegion');
    const departementSelect = document.getElementById('codeDepartement');
    const federationEl = document.getElementById('codeFederation');
    const genreSelect = document.getElementById('genre');
    const btn = document.getElementById('btnAfficherCarto');
    const statusEl = document.getElementById('carto-status');

    if (!regionSelect || !btn || !document.getElementById('carto-map')) {
        return;
    }

    const map = L.map('carto-map', { scrollWheelZoom: true }).setView([46.6, 2.2], 6);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '&copy; OpenStreetMap'
    }).addTo(map);

    let regionsLayer = null;
    let communesLayer = L.layerGroup().addTo(map);
    let legendControl = null;
    let geoJsonCache = null;
    let lastMin = 0;
    let lastMax = 0;

    function setStatus(msg) {
        if (statusEl) statusEl.textContent = msg || '';
    }

    function metric(row, genre) {
        if (genre === 'F') return parseInt(row.f, 10) || 0;
        if (genre === 'H') return parseInt(row.h, 10) || 0;
        return parseInt(row.total, 10) || 0;
    }

    function colorFor(value, min, max) {
        if (max <= min) return PALETTE[0];
        const t = Math.max(0, Math.min(1, (value - min) / (max - min)));
        const idx = Math.min(PALETTE.length - 1, Math.floor(t * (PALETTE.length - 1)));
        return PALETTE[idx];
    }

    function filterDepartements() {
        const region = regionSelect.value;
        const current = departementSelect.value;
        let keep = false;
        Array.from(departementSelect.options).forEach(function (opt, i) {
            if (i === 0) {
                opt.disabled = false;
                opt.hidden = false;
                return;
            }
            const ok = !region || opt.getAttribute('data-region') === region;
            opt.disabled = !ok;
            opt.hidden = !ok;
            if (ok && opt.value === current) keep = true;
        });
        if (!keep) departementSelect.value = '';
    }

    function dataUrl(niveau, params) {
        const q = new URLSearchParams(params);
        q.set('niveau', niveau);
        return ctx + '/elu/cartographie/data?' + q.toString();
    }

    function fetchJson(url) {
        return fetch(url, { headers: { Accept: 'application/json' }, credentials: 'same-origin' })
            .then(function (r) {
                if (!r.ok) throw new Error('HTTP ' + r.status);
                return r.json();
            });
    }

    function loadGeoJson() {
        if (geoJsonCache) return Promise.resolve(geoJsonCache);
        return fetch(geoJsonUrl).then(function (r) { return r.json(); }).then(function (g) {
            geoJsonCache = g;
            return g;
        });
    }

    function statsMap(rows, genre) {
        const m = new Map();
        (rows || []).forEach(function (row) {
            if (row && row.code) m.set(String(row.code), metric(row, genre));
        });
        return m;
    }

    function updateLegend(min, max, label) {
        if (legendControl) {
            map.removeControl(legendControl);
            legendControl = null;
        }
        legendControl = L.control({ position: 'bottomright' });
        legendControl.onAdd = function () {
            const div = L.DomUtil.create('div', 'carto-legend');
            let html = '<strong>' + label + '</strong><br>';
            for (let i = 0; i < PALETTE.length; i++) {
                const v0 = min + (max - min) * (i / PALETTE.length);
                const v1 = min + (max - min) * ((i + 1) / PALETTE.length);
                html += '<i style="background:' + PALETTE[i] + '"></i> '
                    + Math.round(v0).toLocaleString('fr-FR')
                    + ' &ndash; '
                    + Math.round(v1).toLocaleString('fr-FR') + '<br>';
            }
            div.innerHTML = html;
            return div;
        };
        legendControl.addTo(map);
    }

    function renderRegions(geojson, rows, genre) {
        const values = statsMap(rows, genre);
        const nums = Array.from(values.values());
        const min = nums.length ? Math.min.apply(null, nums) : 0;
        const max = nums.length ? Math.max.apply(null, nums) : 0;
        lastMin = min;
        lastMax = max;

        if (regionsLayer) {
            map.removeLayer(regionsLayer);
        }

        regionsLayer = L.geoJSON(geojson, {
            style: function (feature) {
                const code = feature.properties && feature.properties.code;
                const v = values.get(String(code)) || 0;
                return {
                    color: '#475569',
                    weight: 1,
                    fillColor: colorFor(v, min, max),
                    fillOpacity: v > 0 ? 0.75 : 0.15
                };
            },
            onEachFeature: function (feature, layer) {
                const code = feature.properties && feature.properties.code;
                const nom = feature.properties && feature.properties.nom;
                const v = values.get(String(code)) || 0;
                layer.bindPopup(
                    '<strong>' + (nom || code) + '</strong><br>'
                    + 'Licences : <strong>' + v.toLocaleString('fr-FR') + '</strong>'
                );
            }
        }).addTo(map);

        try {
            map.fitBounds(regionsLayer.getBounds(), { padding: [24, 24], maxZoom: 7 });
        } catch (e) { /* ignore */ }

        updateLegend(min, max, 'Licences par r\u00e9gion');
    }

    function gouvCommunesUrl(codeRegion, codeDepartement) {
        const base = 'https://geo.api.gouv.fr/communes?fields=nom,code,centre';
        if (codeDepartement) {
            return base + '&codeDepartement=' + encodeURIComponent(codeDepartement);
        }
        if (codeRegion) {
            return base + '&codeRegion=' + encodeURIComponent(codeRegion);
        }
        return null;
    }

    function renderCommunes(statsRows, geoCommunes, genre) {
        communesLayer.clearLayers();
        const stats = new Map();
        (statsRows || []).forEach(function (row) {
            stats.set(String(row.code), row);
        });

        let maxV = 0;
        const markers = [];

        (geoCommunes || []).forEach(function (c) {
            const row = stats.get(String(c.code));
            if (!row) return;
            const v = metric(row, genre);
            if (v <= 0 || !c.centre || !c.centre.coordinates) return;
            maxV = Math.max(maxV, v);
            const lng = c.centre.coordinates[0];
            const lat = c.centre.coordinates[1];
            markers.push({ lat: lat, lng: lng, v: v, nom: c.nom, code: c.code, row: row });
        });

        markers.forEach(function (m) {
            const r = 4 + 14 * Math.sqrt(m.v / (maxV || 1));
            const circle = L.circleMarker([m.lat, m.lng], {
                radius: r,
                color: '#1d4ed8',
                weight: 1,
                fillColor: '#3b82f6',
                fillOpacity: 0.55
            });
            circle.bindPopup(
                '<strong>' + m.nom + '</strong> (' + m.code + ')<br>'
                + 'Total : ' + (parseInt(m.row.total, 10) || 0).toLocaleString('fr-FR') + '<br>'
                + 'F : ' + (parseInt(m.row.f, 10) || 0).toLocaleString('fr-FR')
                + ' &middot; H : ' + (parseInt(m.row.h, 10) || 0).toLocaleString('fr-FR')
            );
            communesLayer.addLayer(circle);
        });

        if (markers.length > 0) {
            const bounds = L.latLngBounds(markers.map(function (m) { return [m.lat, m.lng]; }));
            map.fitBounds(bounds.pad(0.08), { maxZoom: 11 });
            setStatus(markers.length + ' commune(s) affich\u00e9e(s).');
        } else {
            setStatus('Aucune commune avec donn\u00e9es pour ce p\u00e9rim\u00e8tre.');
        }
    }

    function loadCarto() {
        const codeFederation = federationEl ? federationEl.value.trim() : '';
        const codeRegion = regionSelect.value.trim();
        const codeDepartement = departementSelect.value.trim();
        const genre = genreSelect ? genreSelect.value : 'TOTAL';

        if (!codeFederation) {
            alert('Veuillez choisir une f\u00e9d\u00e9ration.');
            return;
        }

        const params = {
            codeFederation: codeFederation,
            codeRegion: codeRegion,
            codeDepartement: codeDepartement
        };

        btn.disabled = true;
        setStatus('Chargement\u2026');
        communesLayer.clearLayers();

        const regionPromise = fetchJson(dataUrl('regions', params));
        const geoPromise = loadGeoJson();

        Promise.all([regionPromise, geoPromise])
            .then(function (res) {
                renderRegions(res[1], res[0], genre);

                const gouvUrl = gouvCommunesUrl(codeRegion, codeDepartement);
                if (!gouvUrl) {
                    setStatus((res[0] || []).length + ' r\u00e9gion(s) avec donn\u00e9es. S\u00e9lectionnez une r\u00e9gion ou un d\u00e9partement pour le d\u00e9tail communal.');
                    return null;
                }

                return Promise.all([
                    fetchJson(dataUrl('communes', params)),
                    fetch(gouvUrl).then(function (r) { return r.json(); })
                ]);
            })
            .then(function (pair) {
                if (!pair) return;
                renderCommunes(pair[0], pair[1], genre);
            })
            .catch(function (err) {
                console.error(err);
                setStatus('Erreur lors du chargement des donn\u00e9es.');
            })
            .finally(function () {
                btn.disabled = false;
            });
    }

    regionSelect.addEventListener('change', filterDepartements);
    filterDepartements();
    btn.addEventListener('click', loadCarto);
})();
