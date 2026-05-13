package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.clubsport.model.ClassementCommune;
import com.clubsport.model.StatLicenceElu;
import com.clubsport.util.ConnexionDB;

public class EluVisualisationDAO {

    // ── Listes pour les filtres ──────────────────────────────────────────────

    public List<String> listerRegions() throws SQLException {
        List<String> regions = new ArrayList<>();
        String sql = "SELECT DISTINCT nom_region FROM region ORDER BY nom_region";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) regions.add(rs.getString("nom_region"));
        } finally { ConnexionDB.fermer(conn); }
        return regions;
    }

    public List<String> listerFederations() throws SQLException {
        List<String> federations = new ArrayList<>();
        String sql = "SELECT nom_federation FROM federation ORDER BY nom_federation";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) federations.add(rs.getString("nom_federation"));
        } finally { ConnexionDB.fermer(conn); }
        return federations;
    }

    // ── Stats H/F pour une commune et fédération ────────────────────────────

    public Optional<StatLicenceElu> findByCommuneAndFederation(
            String codeCommune, String codeFederation) throws SQLException {

        String sql = "SELECT c.code_commune, c.nom_commune, f.code_federation, f.nom_federation, "
                + "sl.total_licencies, sl.licencies_femmes, sl.licencies_hommes "
                + "FROM statistique_licencies sl "
                + "JOIN commune c ON c.code_commune = sl.code_commune "
                + "JOIN federation f ON f.code_federation = sl.code_federation "
                + "WHERE sl.code_commune = ? AND sl.code_federation = ?";

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeCommune.trim());
            ps.setString(2, codeFederation.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                StatLicenceElu s = new StatLicenceElu();
                s.setCodeCommune(rs.getString("code_commune"));
                s.setNomCommune(rs.getString("nom_commune"));
                s.setCodeFederation(rs.getString("code_federation"));
                s.setNomFederation(rs.getString("nom_federation"));
                s.setTotalLicencies(rs.getInt("total_licencies"));
                s.setLicenciesFemmes(rs.getInt("licencies_femmes"));
                s.setLicenciesHommes(rs.getInt("licencies_hommes"));
                return Optional.of(s);
            }
        } finally { ConnexionDB.fermer(conn); }
    }

    // ── Classement top 10 communes par taux de licenciés ────────────────────

    public List<ClassementCommune> getClassementCommunes(
            String nomRegion, String nomFederation) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT c.nom_commune, "
            + "COALESCE(SUM(sl.total_licencies), 0) AS total_licencies, "
            + "ROUND(COALESCE(SUM(sl.total_licencies), 0) / c.population * 100, 2) AS taux "
            + "FROM statistique_licencies sl "
            + "JOIN commune c ON c.code_commune = sl.code_commune "
            + "JOIN region r ON r.code_region = c.code_region "
            + "JOIN federation f ON f.code_federation = sl.code_federation "
            + "WHERE c.population > 0 "
        );

        List<Object> params = new ArrayList<>();

        if (nomRegion != null && !nomRegion.isBlank()) {
            sql.append("AND r.nom_region = ? ");
            params.add(nomRegion);
        }
        if (nomFederation != null && !nomFederation.isBlank()) {
            sql.append("AND f.nom_federation = ? ");
            params.add(nomFederation);
        }

        sql.append("GROUP BY c.nom_commune, c.population "
                + "ORDER BY taux DESC LIMIT 10");

        List<ClassementCommune> liste = new ArrayList<>();
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClassementCommune cc = new ClassementCommune();
                    cc.setNomCommune(rs.getString("nom_commune"));
                    cc.setTotalLicencies(rs.getInt("total_licencies"));
                    cc.setTauxLicencies(rs.getDouble("taux"));
                    liste.add(cc);
                }
            }
        } finally { ConnexionDB.fermer(conn); }
        return liste;
    }

    // ── Totaux H/F avec filtres ──────────────────────────────────────────────

    public int getTotalFiltre(String nomRegion, String nomFederation) throws SQLException {
        return executerTotal(nomRegion, nomFederation, "sl.total_licencies", "total");
    }

    public int getTotalFemmesFiltre(String nomRegion, String nomFederation) throws SQLException {
        return executerTotal(nomRegion, nomFederation, "sl.licencies_femmes", "resultat");
    }

    public int getTotalHommesFiltre(String nomRegion, String nomFederation) throws SQLException {
        return executerTotal(nomRegion, nomFederation, "sl.licencies_hommes", "resultat");
    }

    private int executerTotal(String nomRegion, String nomFederation,
                               String colonne, String alias) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COALESCE(SUM(" + colonne + "), 0) AS " + alias + " "
            + "FROM statistique_licencies sl "
            + "JOIN commune c ON c.code_commune = sl.code_commune "
            + "JOIN region r ON r.code_region = c.code_region "
            + "JOIN federation f ON f.code_federation = sl.code_federation "
            + "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (nomRegion != null && !nomRegion.isBlank()) {
            sql.append("AND r.nom_region = ? ");
            params.add(nomRegion);
        }
        if (nomFederation != null && !nomFederation.isBlank()) {
            sql.append("AND f.nom_federation = ? ");
            params.add(nomFederation);
        }
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(alias);
            }
        } finally { ConnexionDB.fermer(conn); }
        return 0;
    }
}