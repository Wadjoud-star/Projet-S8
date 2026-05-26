/**
 * 
 */
const sliderRayon = document.getElementById('sliderRayon');
const labelRayon = document.getElementById('labelRayon');
sliderRayon.addEventListener('input', () => {
    labelRayon.textContent = sliderRayon.value;
});

var map = L.map('map').setView([46.603354, 1.888334], 6);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
}).addTo(map);
var markersCluster = L.markerClusterGroup();
map.addLayer(markersCluster);

fetch('/api/federations')
    .then(res => res.json())
    .then(data => {
        const select = document.getElementById('selectFederation');
        data.forEach(fed => {
            const option = document.createElement('option');
            option.value = fed.codeFederation;
            option.textContent = fed.nomFederation;
            select.appendChild(option);
        });
    });

fetch('/api/regions')
    .then(res => res.json())
    .then(data => {
        const select = document.getElementById('selectRegion');
        data.forEach(region => {
            const option = document.createElement('option');
            option.value = region.codeRegion;
            option.textContent = region.nomRegion
            select.appendChild(option);
        });
    });

fetch("regions.geojson")
    .then(res => res.json())
    .then(data => {

        function styleRegion() {
            return {
                color: "#2563eb",
                weight: 2,
                fillOpacity: 0.1
            };
        }
        function onEachRegion(feature, layer) {
            layer.on({
                click: function() {
                    const codeRegion = feature.properties.code;
                    const codeFederation = document.getElementById('selectFederation').value;
                    document.getElementById('selectRegion').value = codeRegion;
                    lancerRecherche(codeRegion, null, codeFederation, layer);
                },
                mouseover: function(e) {
                    e.target.setStyle({ fillOpacity: 0.3 });
                },
                mouseout: function(e) {
                    e.target.setStyle({ fillOpacity: 0.1 });
                }
            });
        }

        L.geoJSON(data, {
            style: styleRegion,
            onEachFeature: onEachRegion
        }).addTo(map);
    });
document.getElementById('inputCodePostal').addEventListener('input', function() {
    const recherche = this.value.trim();
    const old = document.getElementById('suggestions');
    if (old) old.remove();

    if (recherche.length < 2) return;

    fetch(`/api/communes?recherche=${encodeURIComponent(recherche)}`)
        .then(res => res.json())
        .then(communes => {
            if (communes.length === 0) return;
            const liste = document.createElement('ul');
            liste.id = 'suggestions';

            communes.forEach(c => {
                const item = document.createElement('li');
                item.textContent = c.nomCommune;
                item.addEventListener('click', () => {
                    document.getElementById('inputCodePostal').value = c.nomCommune;
                    liste.remove();
                });
                liste.appendChild(item);
            });

            document.getElementById('inputCodePostal').after(liste);
        });
});

document.addEventListener('click', (e) => {
    if (e.target.id !== 'inputCodePostal') {
        const old = document.getElementById('suggestions');
        if (old) old.remove();
    }
});
document.getElementById('btnRechercher').addEventListener('click', () => {
    const codeRegion = document.getElementById('selectRegion').value;
    const nomCommune = document.getElementById('inputCodePostal').value.trim();
    const codeFederation = document.getElementById('selectFederation').value;

    if (!codeRegion && !nomCommune) {
        alert("Veuillez choisir une région ou saisir une commune.");
        return;
    }

    lancerRecherche(codeRegion, nomCommune, codeFederation, null);
});
function lancerRecherche(codeRegion, nomCommune, codeFederation, layer) {
    let url = '/api/stats?';

    if (codeRegion) {
        url += `code_region=${encodeURIComponent(codeRegion)}`;
    } else if (nomCommune) {
        url += `nom_commune=${encodeURIComponent(nomCommune)}`;
    }

    if (codeFederation) {
        url += `&code_federation=${encodeURIComponent(codeFederation)}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => {
            afficherListeClubs(data);
            if (layer) afficherPopup(data, layer);
        })
        .catch(err => console.error("Erreur :", err));
}
function afficherListeClubs(data) {
    const container = document.getElementById('listeClubs');

    if (!data.licences || data.licences.length === 0) {
        container.innerHTML = '<p class="text-muted small">Aucun résultat.</p>';
        document.getElementById('compteurResultats').textContent = '0 résultat(s)';
        return;
    }
    let totalClubs = 0;
    let totalH = 0;
    let totalF = 0;

    data.clubs.forEach(c => { totalClubs += c.nombreClubs; });
    data.licences.forEach(l => { totalH += l.licenciesHommes; totalF += l.licenciesFemmes; });

    document.getElementById('compteurResultats').textContent =
        `${totalClubs} club(s) — ${totalH + totalF} licencié(s)`;
    let html = `
		       <div class="mb-3 p-2 border rounded">
		           <p class="mb-1"><strong>Total clubs :</strong> ${totalClubs}</p>
		           <p class="mb-1"><strong>Licenciés :</strong> ${totalH + totalF}</p>
				   <p style="color:#3b82f6">H : ${totalH}</p>
				   <p style="color:#ec4899">F : ${totalF}</p>
		       </div>
		       <hr class="my-2">
		   `;
    const Premiers = data.licences.slice(0, 1000);
    Premiers.forEach(l => {
        const clubs = data.clubs.find(c => c.codeCommune === l.codeCommune) || {};
        html += `
		               <div class="mb-2 p-2 border rounded">
		                   <p class="mb-1">
		                       <strong>${l.nomCommune}</strong>
		                       <small class="text-muted">${l.nomFederation || ''}</small>
		                   </p>
		                   <p class="mb-1 small"> ${clubs.nombreClubs || 0} club(s)</p>
						   <p style="color:#3b82f6">H : ${l.licenciesHommes}</p>
						   <p style="color:#ec4899">F : ${l.licenciesFemmes}</p>
		               </div>
		           `;
    });
    container.innerHTML = html;
}
function afficherPopup(data, layer) {
    if (!data.licences || data.licences.length === 0) return;

    let totalClubs = 0;
    let totalH = 0;
    let totalF = 0;

    data.clubs.forEach(c => { totalClubs += c.nombreClubs; });
    data.licences.forEach(l => { totalH += l.licenciesHommes; totalF += l.licenciesFemmes; });

    const nomRegion = data.licences[0].nomRegion || '';

    layer.bindPopup(`
	        <strong>${nomRegion}</strong><br>
	        <strong>${totalClubs}</strong> club(s)<br>
	        <span style="color:#3b82f6">H : ${totalH}</span> 
	        <br>
	        <span style="color:#ec4899">F : ${totalF}</span>
	    `).openPopup();
}
