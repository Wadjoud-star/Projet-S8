package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubsport.model.ClassementCommune;
import com.clubsport.util.ConnexionDB;

public class EluVisualisationDAO {

    public List<String> listerRegions() throws SQLException {
        List<String> regions = new ArrayList<>();
        String sql = "SELECT DISTINCT nom_region FROM region ORDER BY nom_region";

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                regions.add(rs.getString("nom_region"));
            }
        } finally {
            ConnexionDB.fermer(conn);
        }

        return regions;
    }

    public List<String> listerFederations() throws SQLException {
        List<String> federations = new ArrayList<>();
        String sql = "SELECT code_federation, nom_federation FROM federation ORDER BY nom_federation";

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                federations.add(
                    rs.getString("code_federation") + " — " + rs.getString("nom_federation")
                );
            }
        } finally {
            ConnexionDB.fermer(conn);
        }

        return federations;
    }

    public List<ClassementCommune> getClassementCommunes(
            String nomRegion,
            String codeFederation,
            String codeCommune) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT c.code_commune, c.nom_commune, " +
            "COALESCE(SUM(sl.total_licencies), 0) AS total_licencies, " +
            "ROUND(SUM(sl.total_licencies) * 100.0 / SUM(SUM(sl.total_licencies)) OVER (), 2) AS taux " +
            "FROM statistique_licencies sl " +
            "JOIN commune c ON c.code_commune = sl.code_commune " +
            "JOIN region r ON r.code_region = c.code_region " +
            "JOIN federation f ON f.code_federation = sl.code_federation " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (nomRegion != null && !nomRegion.isBlank()) {
            sql.append("AND r.nom_region = ? ");
            params.add(nomRegion);
        }

        if (codeFederation != null && !codeFederation.isBlank()) {
            sql.append("AND f.code_federation = ? ");
            params.add(codeFederation);
        }

        if (codeCommune != null && !codeCommune.isBlank()) {
            sql.append("AND c.code_commune = ? ");
            params.add(codeCommune);
        }

        sql.append(
            "GROUP BY c.code_commune, c.nom_commune " +
            "ORDER BY total_licencies DESC LIMIT 30"
        );

        List<ClassementCommune> liste = new ArrayList<>();
        Connection conn = ConnexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClassementCommune cc = new ClassementCommune();
                    cc.setNomCommune(rs.getString("nom_commune"));
                    cc.setTotalLicencies(rs.getInt("total_licencies"));
                    cc.setTauxLicencies(rs.getDouble("taux"));
                    liste.add(cc);
                }
            }
        } finally {
            ConnexionDB.fermer(conn);
        }

        return liste;
    }

    public int getTotalFiltre(String nomRegion, String codeFederation, String codeCommune) throws SQLException {
        return executerTotal(nomRegion, codeFederation, codeCommune, "sl.total_licencies", "total");
    }

    public int getTotalFemmesFiltre(String nomRegion, String codeFederation, String codeCommune) throws SQLException {
        return executerTotal(nomRegion, codeFederation, codeCommune, "sl.licencies_femmes", "resultat");
    }

    public int getTotalHommesFiltre(String nomRegion, String codeFederation, String codeCommune) throws SQLException {
        return executerTotal(nomRegion, codeFederation, codeCommune, "sl.licencies_hommes", "resultat");
    }

    private int executerTotal(
            String nomRegion,
            String codeFederation,
            String codeCommune,
            String colonne,
            String alias) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT COALESCE(SUM(" + colonne + "), 0) AS " + alias + " " +
            "FROM statistique_licencies sl " +
            "JOIN commune c ON c.code_commune = sl.code_commune " +
            "JOIN region r ON r.code_region = c.code_region " +
            "JOIN federation f ON f.code_federation = sl.code_federation " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (nomRegion != null && !nomRegion.isBlank()) {
            sql.append("AND r.nom_region = ? ");
            params.add(nomRegion);
        }

        if (codeFederation != null && !codeFederation.isBlank()) {
            sql.append("AND f.code_federation = ? ");
            params.add(codeFederation);
        }

        if (codeCommune != null && !codeCommune.isBlank()) {
            sql.append("AND c.code_commune = ? ");
            params.add(codeCommune);
        }

        Connection conn = ConnexionDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(alias);
                }
            }
        } finally {
            ConnexionDB.fermer(conn);
        }

        return 0;
    }
}