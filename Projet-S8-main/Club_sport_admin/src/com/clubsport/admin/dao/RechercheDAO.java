package com.clubsport.admin.dao;

import com.clubsport.admin.model.ResultatRecherche;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RechercheDAO {
// selection des colonnes pour remplir mon objet resultatrecherche
    private static final String BASE_SQL = """
        SELECT 
            f.nom_federation, 
            co.nom_commune,
            r.nom_region,
            co.code_commune AS code_postal,
            sl.total_licencies,
            sl.licencies_hommes,
            sl.licencies_femmes,
            sc.nb_clubs,
            sc.nb_etablissements_professionnels,
            sc.total_structures

        FROM statistique_licencies sl
        JOIN federation f ON sl.code_federation = f.code_federation
        JOIN commune co ON sl.code_commune = co.code_commune
        JOIN region r ON co.code_region = r.code_region
        LEFT JOIN statistique_clubs sc 
            ON sc.code_commune = co.code_commune 
            AND sc.code_federation = f.code_federation
        WHERE 
    """;
//co.code_commune AS code_postal renommes la colonne pour l’affichage
// statistique_licencies est la table principale
    //JOIN federation pour récupérer le nom de la fédération ( idem commune ...)
    //LEFT JOIN statistique_clubs permet éviter les erreurs si pas de deonnées 
    // WHERE est  la condition qui  sera ajoutée ensuite selon le critère choisi
    
    public List<ResultatRecherche> rechercher(String colonne, String valeur) {
        List<ResultatRecherche> resultats = new ArrayList<>();// création d'une liste vide pour pour les résultats

        String sql = switch (colonne) {
            case "commune" -> BASE_SQL + "LOWER(co.nom_commune) LIKE LOWER(?)";
            case "federation" -> BASE_SQL + "LOWER(f.nom_federation) LIKE LOWER(?)";
            case "code_postal" -> BASE_SQL + "co.code_commune LIKE ?";
            case "licencies" -> BASE_SQL + "sl.total_licencies >= ?";// on fait une recherche avec un minimum
            default -> throw new IllegalArgumentException("Critère inconnu : " + colonne);
        };

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (colonne.equals("licencies")) {
                stmt.setInt(1, Integer.parseInt(valeur));// si on a un licencie on transforme en nombre 
            } else {
                stmt.setString(1, "%" + valeur + "%");// sinon on affiche directement 
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {// construction des objets, on affecte les valeurs a afficher dans le tableau
                resultats.add(new ResultatRecherche(
                        rs.getString("nom_federation"),
                        rs.getString("nom_commune"),
                        rs.getString("nom_region"),
                        rs.getString("code_postal"),
                        rs.getInt("total_licencies"),
                        rs.getInt("licencies_hommes"),
                        rs.getInt("licencies_femmes"),
                        rs.getInt("nb_clubs"),
                        rs.getInt("nb_etablissements_professionnels"),
                        rs.getInt("total_structures")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultats;// liste est renvoyée a la page swing 
    }
}
