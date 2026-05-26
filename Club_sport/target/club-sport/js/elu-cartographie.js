(function () {
    'use strict';

    const ctx = window.ELU_CARTO_CTX || '';
    const geoJsonUrl = window.ELU_CARTO_GEOJSON || ctx + '/regions.geojson';
<<<<<<< HEAD
    const PALETTE = ['#ffffcc', '#d9f0a3', '#addd8e', '#78c679', '#31a354', '#006837'];
=======
    /** Dégradé lisible : faible → fort volume de licences */
    const PALETTE = ['#fff7bc', '#fec44f', '#fe9929', '#ec7014', '#cc4c02', '#8c2d04'];
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7

    const regionSelect = document.getElementById('codeRegion');
    const departementSelect = document.getElementById('codeDepartement');
    const federationEl = document.getElementById('codeFederation');
<<<<<<< HEAD
    const genreSelect = document.getElementById('genre');
=======
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
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
<<<<<<< HEAD
    let lastMin = 0;
    let lastMax = 0;
=======

    /** Format français : 3 456 789 (espaces entre les milliers) */
    function fmtNum(n) {
        const v = Math.round(Number(n) || 0);
        return v.toLocaleString('fr-FR');
    }

    function parseStats(row) {
        return {
            total: parseInt(row.total, 10) || 0,
            f: parseInt(row.f, 10) || 0,
            h: parseInt(row.h, 10) || 0,
            nom: row.nom || ''
        };
    }

    function statsMapByCode(rows) {
        const m = new Map();
        (rows || []).forEach(function (row) {
            if (row && row.code) {
                m.set(String(row.code), parseStats(row));
            }
        });
        return m;
    }

    function popupHtml(nom, stats) {
        return '<strong>' + nom + '</strong><br>'
            + 'Total : <strong>' + fmtNum(stats.total) + '</strong><br>'
            + 'Licenci&eacute;es femme : <strong>' + fmtNum(stats.f) + '</strong><br>'
            + 'Licenci&eacute;s homme : <strong>' + fmtNum(stats.h) + '</strong>';
    }

    /** Arrondit un pas de légende à une valeur "ronde" (500, 1 000, 1 600, 2 000…) */
    function roundLegendStep(raw) {
        if (raw <= 0) return 100;
        if (raw >= 100000) return Math.ceil(raw / 10000) * 10000;
        if (raw >= 10000) return Math.ceil(raw / 5000) * 5000;
        if (raw >= 1000) return Math.ceil(raw / 200) * 200;
        if (raw >= 100) return Math.ceil(raw / 50) * 50;
        if (raw >= 10) return Math.ceil(raw / 10) * 10;
        return Math.ceil(raw);
    }

    /**
     * Paliers lisibles : 0–1 600, 1 601–3 200… (pas 1 667, 3 333).
     * Retourne { step, top, ranges }.
     */
    function buildLegendScale(max) {
        const n = PALETTE.length;
        if (max <= 0) {
            return { step: 0, top: 0, ranges: [{ label: 'Aucune licence', color: PALETTE[0] }] };
        }
        const step = roundLegendStep((max * 1.05) / (n - 1));
        const top = step * (n - 1);
        const ranges = [];
        for (let i = 0; i < n; i++) {
            const lo = i * step;
            const hi = (i + 1) * step;
            let label;
            if (i === 0) {
                label = 'De 0 &agrave; ' + fmtNum(hi);
            } else if (i === n - 1) {
                label = 'Plus de ' + fmtNum(lo);
            } else {
                label = 'De ' + fmtNum(lo + 1) + ' &agrave; ' + fmtNum(hi);
            }
            ranges.push({ label: label, color: PALETTE[i], lo: lo, hi: hi });
        }
        return { step: step, top: top, ranges: ranges };
    }

    function colorFor(value, scale) {
        if (!scale || scale.top <= 0) return PALETTE[0];
        const t = Math.max(0, Math.min(1, value / scale.top));
        const idx = Math.min(PALETTE.length - 1, Math.floor(t * (PALETTE.length - 1)));
        return PALETTE[idx];
    }
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7

    function setStatus(msg) {
        if (statusEl) statusEl.textContent = msg || '';
    }

<<<<<<< HEAD
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

=======
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
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

<<<<<<< HEAD
    function statsMap(rows, genre) {
        const m = new Map();
        (rows || []).forEach(function (row) {
            if (row && row.code) m.set(String(row.code), metric(row, genre));
        });
        return m;
    }

    function updateLegend(min, max, label) {
=======
    function updateLegend(scale) {
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
        if (legendControl) {
            map.removeControl(legendControl);
            legendControl = null;
        }
<<<<<<< HEAD
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
=======
        const ranges = scale.ranges;
        legendControl = L.control({ position: 'bottomright' });
        legendControl.onAdd = function () {
            const div = L.DomUtil.create('div', 'carto-legend');
            let html = '<strong>Nombre de licences (total)</strong><br>'
                + '<span class="text-muted" style="font-size:11px">Couleur = volume total</span><br>';
            for (let i = 0; i < ranges.length; i++) {
                html += '<i style="background:' + ranges[i].color + '"></i> ' + ranges[i].label + '<br>';
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
            }
            div.innerHTML = html;
            return div;
        };
        legendControl.addTo(map);
    }

<<<<<<< HEAD
    function renderRegions(geojson, rows, genre) {
        const values = statsMap(rows, genre);
        const nums = Array.from(values.values());
        const min = nums.length ? Math.min.apply(null, nums) : 0;
        const max = nums.length ? Math.max.apply(null, nums) : 0;
        lastMin = min;
        lastMax = max;
=======
    function renderRegions(geojson, rows) {
        const byCode = statsMapByCode(rows);
        const totals = Array.from(byCode.values()).map(function (s) { return s.total; });
        const max = totals.length ? Math.max.apply(null, totals) : 0;
        const legendScale = buildLegendScale(max);
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7

        if (regionsLayer) {
            map.removeLayer(regionsLayer);
        }

        regionsLayer = L.geoJSON(geojson, {
            style: function (feature) {
                const code = feature.properties && feature.properties.code;
<<<<<<< HEAD
                const v = values.get(String(code)) || 0;
                return {
                    color: '#475569',
                    weight: 1,
                    fillColor: colorFor(v, min, max),
                    fillOpacity: v > 0 ? 0.75 : 0.15
=======
                const stats = byCode.get(String(code)) || { total: 0, f: 0, h: 0 };
                const v = stats.total;
                return {
                    color: '#475569',
                    weight: 1,
                    fillColor: colorFor(v, legendScale),
                    fillOpacity: v > 0 ? 0.78 : 0.12
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
                };
            },
            onEachFeature: function (feature, layer) {
                const code = feature.properties && feature.properties.code;
                const nom = feature.properties && feature.properties.nom;
<<<<<<< HEAD
                const v = values.get(String(code)) || 0;
                layer.bindPopup(
                    '<strong>' + (nom || code) + '</strong><br>'
                    + 'Licences : <strong>' + v.toLocaleString('fr-FR') + '</strong>'
                );
=======
                const stats = byCode.get(String(code)) || { total: 0, f: 0, h: 0 };
                layer.bindPopup(popupHtml(nom || code, stats));
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
            }
        }).addTo(map);

        try {
            map.fitBounds(regionsLayer.getBounds(), { padding: [24, 24], maxZoom: 7 });
        } catch (e) { /* ignore */ }

<<<<<<< HEAD
        updateLegend(min, max, 'Licences par r\u00e9gion');
=======
        updateLegend(legendScale);
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
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

<<<<<<< HEAD
    function renderCommunes(statsRows, geoCommunes, genre) {
        communesLayer.clearLayers();
        const stats = new Map();
        (statsRows || []).forEach(function (row) {
            stats.set(String(row.code), row);
        });

        let maxV = 0;
=======
    function renderCommunes(statsRows, geoCommunes) {
        communesLayer.clearLayers();
        const stats = statsMapByCode(statsRows);

        let maxTotal = 0;
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
        const markers = [];

        (geoCommunes || []).forEach(function (c) {
            const row = stats.get(String(c.code));
<<<<<<< HEAD
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
=======
            if (!row || row.total <= 0 || !c.centre || !c.centre.coordinates) return;
            maxTotal = Math.max(maxTotal, row.total);
            const lng = c.centre.coordinates[0];
            const lat = c.centre.coordinates[1];
            markers.push({ lat: lat, lng: lng, nom: c.nom, stats: row });
        });

        markers.forEach(function (m) {
            const r = 4 + 14 * Math.sqrt(m.stats.total / (maxTotal || 1));
            const circle = L.circleMarker([m.lat, m.lng], {
                radius: r,
                color: '#c2410c',
                weight: 1,
                fillColor: '#f97316',
                fillOpacity: 0.6
            });
            circle.bindPopup(popupHtml(m.nom, m.stats));
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
            communesLayer.addLayer(circle);
        });

        if (markers.length > 0) {
            const bounds = L.latLngBounds(markers.map(function (m) { return [m.lat, m.lng]; }));
            map.fitBounds(bounds.pad(0.08), { maxZoom: 11 });
<<<<<<< HEAD
            setStatus(markers.length + ' commune(s) affich\u00e9e(s).');
=======
            setStatus(fmtNum(markers.length) + ' commune(s) affich\u00e9e(s).');
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
        } else {
            setStatus('Aucune commune avec donn\u00e9es pour ce p\u00e9rim\u00e8tre.');
        }
    }

    function loadCarto() {
        const codeFederation = federationEl ? federationEl.value.trim() : '';
        const codeRegion = regionSelect.value.trim();
        const codeDepartement = departementSelect.value.trim();
<<<<<<< HEAD
        const genre = genreSelect ? genreSelect.value : 'TOTAL';
=======
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7

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
<<<<<<< HEAD
                renderRegions(res[1], res[0], genre);

                const gouvUrl = gouvCommunesUrl(codeRegion, codeDepartement);
                if (!gouvUrl) {
                    setStatus((res[0] || []).length + ' r\u00e9gion(s) avec donn\u00e9es. S\u00e9lectionnez une r\u00e9gion ou un d\u00e9partement pour le d\u00e9tail communal.');
=======
                renderRegions(res[1], res[0]);

                const gouvUrl = gouvCommunesUrl(codeRegion, codeDepartement);
                if (!gouvUrl) {
                    const n = (res[0] || []).length;
                    setStatus(fmtNum(n) + ' r\u00e9gion(s). S\u00e9lectionnez une r\u00e9gion ou un d\u00e9partement pour le d\u00e9tail communal.');
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
                    return null;
                }

                return Promise.all([
                    fetchJson(dataUrl('communes', params)),
                    fetch(gouvUrl).then(function (r) { return r.json(); })
                ]);
            })
            .then(function (pair) {
                if (!pair) return;
<<<<<<< HEAD
                renderCommunes(pair[0], pair[1], genre);
=======
                renderCommunes(pair[0], pair[1]);
>>>>>>> 70456a02a26d5c415f5b8e221905472a7e3f0dd7
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
