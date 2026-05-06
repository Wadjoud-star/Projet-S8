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

function afficherClubs(clubs) {
    markersCluster.clearLayers(); // pour vider les anciens marqueurs

    clubs.forEach(club => {
        const marker = L.marker([club.lat, club.lng]);

        marker.bindPopup(`
        <strong>${club.nom}</strong><br>
        <em>${club.federation}</em><br>
        Licenciés H : ${club.licencies_h} · F : ${club.licencies_f}
      `);

        markersCluster.addLayer(marker);
    });

    document.getElementById('compteurResultats').textContent =
        `${clubs.length} club(s) trouvé(s)`;
}
function afficherListeClubs(clubs) {
    let html = "";

    clubs.forEach(club => {
        html += `
                  <p><strong>${club.nom}</strong> <small>${club.federation}</small> ${club.licencies_h} H | ${club.licencies_f} F</p>
          `;
    });

    document.getElementById("listeClubs").innerHTML = html;
}
document.getElementById('btnRechercher').addEventListener('click', () => {
    const federation = document.getElementById('selectFederation').value;
    const codePostal = document.getElementById('inputCodePostal').value;
    const region = document.getElementById('selectRegion').value;
    const rayon = sliderRayon.value;

    afficherClubs([
        { nom: "AS Rouen FC", lat: 49.4431, lng: 1.0993, federation: "FFF", licencies_h: 200, licencies_f: 80 },
        { nom: "Le Havre AC", lat: 49.4938, lng: 0.1079, federation: "FFF", licencies_h: 350, licencies_f: 120 },
        { nom: "Caen Handball", lat: 49.1829, lng: -0.3707, federation: "FFHB", licencies_h: 90, licencies_f: 75 },
    ]);
    afficherListeClubs([
        { nom: "AS Rouen FC", lat: 49.4431, lng: 1.0993, federation: "FFF", licencies_h: 200, licencies_f: 80 },
        { nom: "Le Havre AC", lat: 49.4938, lng: 0.1079, federation: "FFF", licencies_h: 350, licencies_f: 120 },
        { nom: "Caen Handball", lat: 49.1829, lng: -0.3707, federation: "FFHB", licencies_h: 90, licencies_f: 75 },
    ]);
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
          click: function () {
            const nomRegion = feature.properties.nom;
            alert("Région : " + nomRegion);

          },
          mouseover: function (e) {
            e.target.setStyle({ fillOpacity: 0.3 });
          },
          mouseout: function (e) {
            e.target.setStyle({ fillOpacity: 0.1 });
          }
        });
      }

      L.geoJSON(data, {
        style: styleRegion,
        onEachFeature: onEachRegion
      }).addTo(map);
    });
