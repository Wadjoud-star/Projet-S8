package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.clubsport.model.StatLicenceElu;
import com.clubsport.util.ConnexionDB;

public class EluLicenceDAO {

    private static final String DEPT_SQL = "CASE "
            + "WHEN c.code_commune LIKE '97%' OR c.code_commune LIKE '98%' THEN LEFT(c.code_commune, 3) "
            + "ELSE LEFT(c.code_commune, 2) END";

    /** Lignes pour les selects JSP : keys {@code code}, {@code label}. */
    public List<Map<String, String>> listRegions() throws SQLException {
        String sql = "SELECT DISTINCT r.code_region, r.nom_region "
                + "FROM region r "
                + "INNER JOIN commune c ON c.code_region = r.code_region "
                + "ORDER BY r.nom_region";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<Map<String, String>> options = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("code", rs.getString("code_region"));
                row.put("label", rs.getString("nom_region"));
                options.add(row);
            }
            return options;
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    /** Keys {@code code}, {@code label}, {@code parentRegion} (code région du département). */
    public List<Map<String, String>> listDepartements() throws SQLException {
        String sql = "SELECT DISTINCT " + DEPT_SQL + " AS code_departement, c.code_region "
                + "FROM commune c "
                + "ORDER BY code_departement, c.code_region";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<Map<String, String>> options = new ArrayList<>();
            while (rs.next()) {
                String code = rs.getString("code_departement");
                Map<String, String> row = new LinkedHashMap<>();
                row.put("code", code);
                row.put("label", "Departement " + code);
                row.put("parentRegion", rs.getString("code_region"));
                options.add(row);
            }
            return options;
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public Optional<StatLicenceElu> findWithFilters(
            String codeFederation, String genre, String codeRegion, String codeDepartement, String codeCommune) throws SQLException {
        String sql = "SELECT MAX(f.code_federation) AS code_federation, MAX(f.nom_federation) AS nom_federation, "
                + "MAX(c.code_region) AS code_region, MAX(r.nom_region) AS nom_region, "
                + "MAX(" + DEPT_SQL + ") AS code_departement, "
                + "MAX(c.code_commune) AS code_commune, MAX(c.nom_commune) AS nom_commune, "
                + "SUM(sl.total_licencies) AS total_licencies, "
                + "SUM(sl.licencies_femmes) AS licencies_femmes, "
                + "SUM(sl.licencies_hommes) AS licencies_hommes "
                + "FROM statistique_licencies sl "
                + "JOIN commune c ON c.code_commune = sl.code_commune "
                + "JOIN region r ON r.code_region = c.code_region "
                + "JOIN federation f ON f.code_federation = sl.code_federation "
                + "WHERE sl.code_federation = ? "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR " + DEPT_SQL + " = ?) "
                + "AND (? = '' OR c.code_commune = ?)";

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeFederation);
            ps.setString(2, codeRegion);
            ps.setString(3, codeRegion);
            ps.setString(4, codeDepartement);
            ps.setString(5, codeDepartement);
            ps.setString(6, codeCommune);
            ps.setString(7, codeCommune);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getObject("total_licencies") == null) {
                    return Optional.empty();
                }
                StatLicenceElu s = new StatLicenceElu();
                s.setCodeRegion(rs.getString("code_region"));
                s.setNomRegion(rs.getString("nom_region"));
                s.setCodeDepartement(rs.getString("code_departement"));
                s.setCodeCommune(rs.getString("code_commune"));
                s.setNomCommune(rs.getString("nom_commune"));
                s.setCodeFederation(rs.getString("code_federation"));
                s.setNomFederation(rs.getString("nom_federation"));
                s.setTotalLicencies(rs.getInt("total_licencies"));
                s.setLicenciesFemmes(rs.getInt("licencies_femmes"));
                s.setLicenciesHommes(rs.getInt("licencies_hommes"));
                s.setGenre(genre);
                s.setValeurGenre(resolveGenreValue(genre, s));
                return Optional.of(s);
            }
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    private int resolveGenreValue(String genre, StatLicenceElu stat) {
        if ("F".equalsIgnoreCase(genre)) {
            return stat.getLicenciesFemmes();
        }
        if ("H".equalsIgnoreCase(genre)) {
            return stat.getLicenciesHommes();
        }
        return stat.getTotalLicencies();
    }
}
