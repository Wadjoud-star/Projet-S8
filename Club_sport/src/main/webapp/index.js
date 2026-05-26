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
var userIcon = L.icon({
    iconUrl: 'https://cdn-icons-png.flaticon.com/512/64/64113.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32]
});
var userLatLng = null;
var marqueurCourant = null;
var cercleCourant = null;
document.getElementById("btn-geoloc").addEventListener("click", function() {
    if (!navigator.geolocation) {
        alert("La géolocalisation n'est pas supportée par votre navigateur.");
        return;
    }

    navigator.geolocation.getCurrentPosition(
        function(position) {
            var lat = position.coords.latitude;
            var lon = position.coords.longitude;

            userLatLng = L.latLng(lat, lon);
            map.setView([lat, lon], 13);
            if (marqueurCourant) {
                map.removeLayer(marqueurCourant);
            }

            marqueurCourant = L.marker([lat, lon], { icon: userIcon })
                .addTo(map)
                .bindPopup("Vous êtes ici")
                .openPopup();

            fetch(`https://geo.api.gouv.fr/communes?lat=${lat}&lon=${lon}&fields=nom,code&format=json`)
                .then(res => res.json())
                .then(data => {
                    if (data.length > 0) {
                        const commune = data[0];
                        document.getElementById('inputCodePostal').value = commune.nom;
                        const codeFederation = document.getElementById('selectFederation').value;
                        lancerRecherche(null, commune.nom, codeFederation, null);
                    }
                });
        }
    );
});
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
    const rayon = sliderRayon.value;
    if (userLatLng) {
        const rayon = sliderRayon.value;
        const codeFederation = document.getElementById('selectFederation').value;
        fetch(`https://geo.api.gouv.fr/communes?lat=${userLatLng.lat}&lon=${userLatLng.lng}&distanceMaxKm=${rayon}&fields=nom,codeRegion&format=json`)
            .then(res => res.json())
            .then(communes => {
                if (communes.length === 0) {
                    alert("Aucune commune trouvée dans ce rayon.");
                    return;
                }
                const codeRegion = communes[0].codeRegion;
				if (cercleCourant) {
				    map.removeLayer(cercleCourant);
				}
               cercleCourant = L.circle([userLatLng.lat, userLatLng.lng], {
                    radius: rayon * 1000,
                    color: '#2563eb',
                    fillOpacity: 0.1
                }).addTo(map);
                lancerRecherche(codeRegion, null, codeFederation, null);
            });
			return;
    }
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
function separateur(nombre) {
    return nombre.toLocaleString('fr-FR');
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
        `${separateur(totalClubs)} club(s) — ${separateur(totalH + totalF)} licencié(s)`;
    let html = `
		       <div class="mb-3 p-2 border rounded">
		           <p class="mb-1"><strong>Total clubs :</strong> ${separateur(totalClubs)}</p>
		           <p class="mb-1"><strong>Licenciés :</strong> ${separateur(totalH + totalF)}</p>
				   <p style="color:#3b82f6">H : ${separateur(totalH)}</p>
				   <p style="color:#ec4899">F : ${separateur(totalF)}</p>
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
		                   <p class="mb-1 small"> ${clubs.nombreClubs ? separateur(clubs.nombreClubs) : 0} club(s)</p>
						   <p style="color:#3b82f6">H : ${separateur(l.licenciesHommes)}</p>
						   <p style="color:#ec4899">F : ${separateur(l.licenciesFemmes)}</p>
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
	        <strong>${separateur(totalClubs)}</strong> club(s)<br>
	        <span style="color:#3b82f6">H : ${separateur(totalH)}</span> 
	        <br>
	        <span style="color:#ec4899">F : ${separateur(totalF)}</span>
	    `).openPopup();
}
