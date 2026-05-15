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

import com.clubsport.model.LicenceExportRow;
import com.clubsport.model.LicenceSearchExtras;
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
        String sql = "SELECT z.cd AS code_departement, z.cr AS code_region, "
                + "COALESCE(d.nom_departement, CONCAT('Département ', z.cd)) AS label "
                + "FROM ( SELECT DISTINCT " + DEPT_SQL + " AS cd, c.code_region AS cr FROM commune c ) z "
                + "LEFT JOIN departement d ON d.code_departement = z.cd "
                + "ORDER BY z.cd, z.cr";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<Map<String, String>> options = new ArrayList<>();
            while (rs.next()) {
                String code = rs.getString("code_departement");
                Map<String, String> row = new LinkedHashMap<>();
                row.put("code", code);
                row.put("label", rs.getString("label"));
                row.put("parentRegion", rs.getString("code_region"));
                options.add(row);
            }
            return options;
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    /** Fédérations pour liste déroulante : {@code code}, {@code label} (nom + code). */
    public List<Map<String, String>> listFederations() throws SQLException {
        String sql = "SELECT code_federation, nom_federation FROM federation ORDER BY nom_federation";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<Map<String, String>> out = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                String code = rs.getString("code_federation");
                String nom = rs.getString("nom_federation");
                row.put("code", code);
                row.put("label", nom + " (" + code + ")");
                out.add(row);
            }
            return out;
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public Optional<String> findCommuneLabel(String codeCommune) throws SQLException {
        if (codeCommune == null || codeCommune.isBlank()) {
            return Optional.empty();
        }
        String sql = "SELECT nom_commune FROM commune WHERE code_commune = ?";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeCommune.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString("nom_commune"));
                }
            }
        } finally {
            ConnexionDB.fermer(conn);
        }
        return Optional.empty();
    }

    /**
     * Autocomplétion : {@code query} sur le nom ou le début du code INSEE.
     * Clés : {@code code}, {@code label} (nom + dept), {@code nom}.
     */
    public List<Map<String, String>> searchCommunes(String query, String codeRegion, String codeDepartement, int limit)
            throws SQLException {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String q = query.trim();
        if (q.length() < 2) {
            return List.of();
        }
        String region = codeRegion == null ? "" : codeRegion.trim();
        String dept = codeDepartement == null ? "" : codeDepartement.trim();
        int lim = Math.min(Math.max(limit, 1), 50);
        String sql = "SELECT c.code_commune, c.nom_commune, c.code_region, "
                + "COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") AS code_departement_eff, "
                + "COALESCE(d.nom_departement, '') AS nom_departement "
                + "FROM commune c "
                + "LEFT JOIN departement d ON d.code_departement = c.code_departement "
                + "WHERE (c.nom_commune LIKE ? OR c.code_commune LIKE ?) "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") = ?) "
                + "ORDER BY c.nom_commune "
                + "LIMIT " + lim;

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String likeNom = "%" + q + "%";
            String likeCode = q + "%";
            ps.setString(1, likeNom);
            ps.setString(2, likeCode);
            ps.setString(3, region);
            ps.setString(4, region);
            ps.setString(5, dept);
            ps.setString(6, dept);
            try (ResultSet rs = ps.executeQuery()) {
                List<Map<String, String>> out = new ArrayList<>();
                while (rs.next()) {
                    Map<String, String> row = new LinkedHashMap<>();
                    String code = rs.getString("code_commune");
                    String nom = rs.getString("nom_commune");
                    String cdept = rs.getString("code_departement_eff");
                    String ndept = rs.getString("nom_departement");
                    row.put("code", code);
                    row.put("nom", nom);
                    row.put("codeRegion", rs.getString("code_region"));
                    row.put("codeDepartement", cdept);
                    String deptPart = (ndept != null && !ndept.isBlank()) ? " (" + ndept + ")" : (" (" + cdept + ")");
                    row.put("label", nom + deptPart + " — " + code);
                    out.add(row);
                }
                return out;
            }
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public boolean communeDansPerimetre(String codeCommune, String codeRegion, String codeDepartement)
            throws SQLException {
        if (codeCommune == null || codeCommune.isBlank()) {
            return true;
        }
        String region = codeRegion == null ? "" : codeRegion.trim();
        String dept = codeDepartement == null ? "" : codeDepartement.trim();
        String sql = "SELECT 1 FROM commune c WHERE c.code_commune = ? "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") = ?)";
        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeCommune.trim());
            ps.setString(2, region);
            ps.setString(3, region);
            ps.setString(4, dept);
            ps.setString(5, dept);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    /**
     * Détail exportable : une ligne par commune du périmètre (mêmes filtres que la synthèse).
     */
    public List<LicenceExportRow> findDetailForExport(
            String codeFederation, String codeRegion, String codeDepartement, String codeCommune)
            throws SQLException {
        String fed = codeFederation == null ? "" : codeFederation.trim();
        if (fed.isEmpty()) {
            return List.of();
        }
        String region = codeRegion == null ? "" : codeRegion.trim();
        String dept = codeDepartement == null ? "" : codeDepartement.trim();
        String com = codeCommune == null ? "" : codeCommune.trim();

        String sql = "SELECT c.code_region, r.nom_region, "
                + "COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") AS code_departement_eff, "
                + "COALESCE(NULLIF(d.nom_departement, ''), "
                + "CASE WHEN COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") = '75' THEN 'Paris' "
                + "ELSE CONCAT('Département ', COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ")) END) AS nom_departement, "
                + "c.code_commune, c.nom_commune, COALESCE(c.population, 0) AS population, "
                + "f.code_federation, f.nom_federation, "
                + "SUM(sl.total_licencies) AS total_licencies, "
                + "SUM(sl.licencies_femmes) AS licencies_femmes, "
                + "SUM(sl.licencies_hommes) AS licencies_hommes "
                + "FROM statistique_licencies sl "
                + "JOIN commune c ON c.code_commune = sl.code_commune "
                + "JOIN region r ON r.code_region = c.code_region "
                + "LEFT JOIN departement d ON d.code_departement = COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") "
                + "JOIN federation f ON f.code_federation = sl.code_federation "
                + "WHERE sl.code_federation = ? "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR " + DEPT_SQL + " = ?) "
                + "AND (? = '' OR c.code_commune = ?) "
                + "GROUP BY c.code_region, r.nom_region, code_departement_eff, nom_departement, "
                + "c.code_commune, c.nom_commune, c.population, f.code_federation, f.nom_federation "
                + "ORDER BY r.nom_region, code_departement_eff, c.nom_commune";

        Connection conn = ConnexionDB.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fed);
            ps.setString(2, region);
            ps.setString(3, region);
            ps.setString(4, dept);
            ps.setString(5, dept);
            ps.setString(6, com);
            ps.setString(7, com);
            try (ResultSet rs = ps.executeQuery()) {
                List<LicenceExportRow> out = new ArrayList<>();
                while (rs.next()) {
                    LicenceExportRow row = new LicenceExportRow();
                    row.setCodeRegion(rs.getString("code_region"));
                    row.setNomRegion(rs.getString("nom_region"));
                    row.setCodeDepartement(rs.getString("code_departement_eff"));
                    row.setNomDepartement(rs.getString("nom_departement"));
                    row.setCodeCommune(rs.getString("code_commune"));
                    row.setNomCommune(rs.getString("nom_commune"));
                    row.setPopulation(rs.getInt("population"));
                    row.setCodeFederation(rs.getString("code_federation"));
                    row.setNomFederation(rs.getString("nom_federation"));
                    row.setTotalLicencies(rs.getInt("total_licencies"));
                    row.setLicenciesFemmes(rs.getInt("licencies_femmes"));
                    row.setLicenciesHommes(rs.getInt("licencies_hommes"));
                    out.add(row);
                }
                return out;
            }
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public Optional<StatLicenceElu> findWithFilters(
            String codeFederation, String genre, String codeRegion, String codeDepartement, String codeCommune)
            throws SQLException {
        String sumSql = "SELECT SUM(sl.total_licencies) AS total_licencies, "
                + "SUM(sl.licencies_femmes) AS licencies_femmes, "
                + "SUM(sl.licencies_hommes) AS licencies_hommes "
                + "FROM statistique_licencies sl "
                + "JOIN commune c ON c.code_commune = sl.code_commune "
                + "WHERE sl.code_federation = ? "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR " + DEPT_SQL + " = ?) "
                + "AND (? = '' OR c.code_commune = ?)";

        Connection conn = ConnexionDB.getConnection();
        try {
            int total = 0;
            int femmes = 0;
            int hommes = 0;
            try (PreparedStatement ps = conn.prepareStatement(sumSql)) {
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
                    total = rs.getInt("total_licencies");
                    femmes = rs.getInt("licencies_femmes");
                    hommes = rs.getInt("licencies_hommes");
                }
            }

            StatLicenceElu s = new StatLicenceElu();
            s.setCodeFederation(codeFederation);
            s.setNomFederation(loadFederationName(conn, codeFederation));
            s.setTotalLicencies(total);
            s.setLicenciesFemmes(femmes);
            s.setLicenciesHommes(hommes);
            s.setGenre(genre);
            s.setValeurGenre(resolveGenreValue(genre, s));

            applyGeoLabels(conn, s, codeRegion, codeDepartement, codeCommune);
            return Optional.of(s);
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    private String loadFederationName(Connection conn, String codeFederation) throws SQLException {
        String sql = "SELECT nom_federation FROM federation WHERE code_federation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeFederation);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_federation");
                }
            }
        }
        return "";
    }

    private void applyGeoLabels(Connection conn, StatLicenceElu s, String codeRegion, String codeDepartement,
            String codeCommune) throws SQLException {
        if (codeCommune != null && !codeCommune.isBlank()) {
            fillCommuneScope(conn, s, codeCommune.trim());
            return;
        }
        if (codeDepartement != null && !codeDepartement.isBlank()) {
            fillDepartementScope(conn, s, codeRegion, codeDepartement.trim());
            return;
        }
        if (codeRegion != null && !codeRegion.isBlank()) {
            fillRegionScope(conn, s, codeRegion.trim());
            return;
        }
        s.setCodeRegion("");
        s.setNomRegion("France — vue nationale (toutes les régions)");
        s.setCodeDepartement("");
        s.setNomDepartement("");
        s.setCodeCommune("");
        s.setNomCommune("");
    }

    private void fillCommuneScope(Connection conn, StatLicenceElu s, String codeCommune) throws SQLException {
        String sql = "SELECT c.code_commune, c.nom_commune, c.code_region, r.nom_region, "
                + "COALESCE(NULLIF(c.code_departement, ''), " + DEPT_SQL + ") AS code_departement_eff, "
                + "COALESCE(d.nom_departement, '') AS nom_departement "
                + "FROM commune c "
                + "JOIN region r ON r.code_region = c.code_region "
                + "LEFT JOIN departement d ON d.code_departement = c.code_departement "
                + "WHERE c.code_commune = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeCommune);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.setCodeCommune(rs.getString("code_commune"));
                    s.setNomCommune(rs.getString("nom_commune"));
                    s.setCodeRegion(rs.getString("code_region"));
                    s.setNomRegion(rs.getString("nom_region"));
                    s.setCodeDepartement(rs.getString("code_departement_eff"));
                    s.setNomDepartement(rs.getString("nom_departement"));
                } else {
                    s.setCodeCommune(codeCommune);
                    s.setNomCommune("(commune inconnue en base)");
                    s.setCodeRegion("");
                    s.setNomRegion("");
                    s.setCodeDepartement("");
                    s.setNomDepartement("");
                }
            }
        }
    }

    private void fillDepartementScope(Connection conn, StatLicenceElu s, String codeRegion, String codeDepartement)
            throws SQLException {
        String sql = "SELECT d.code_departement, d.nom_departement, d.code_region, r.nom_region "
                + "FROM departement d "
                + "JOIN region r ON r.code_region = d.code_region "
                + "WHERE d.code_departement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeDepartement);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.setCodeDepartement(rs.getString("code_departement"));
                    s.setNomDepartement(rs.getString("nom_departement"));
                    s.setCodeRegion(rs.getString("code_region"));
                    s.setNomRegion(rs.getString("nom_region"));
                    s.setCodeCommune("");
                    s.setNomCommune("");
                    return;
                }
            }
        }
        // Département absent de la table (ex. code issu uniquement des communes) : déduire depuis une commune
        String sql2 = "SELECT " + DEPT_SQL + " AS cd, MIN(c.code_region) AS cr "
                + "FROM commune c WHERE " + DEPT_SQL + " = ? "
                + "GROUP BY " + DEPT_SQL;
        try (PreparedStatement ps = conn.prepareStatement(sql2)) {
            ps.setString(1, codeDepartement);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String cr = rs.getString("cr");
                    s.setCodeDepartement(codeDepartement);
                    s.setNomDepartement("");
                    s.setCodeRegion(cr != null ? cr : "");
                    if (cr != null && !cr.isBlank()) {
                        try (PreparedStatement psr = conn.prepareStatement("SELECT nom_region FROM region WHERE code_region = ?")) {
                            psr.setString(1, cr);
                            try (ResultSet rsr = psr.executeQuery()) {
                                if (rsr.next()) {
                                    s.setNomRegion(rsr.getString("nom_region"));
                                }
                            }
                        }
                    } else {
                        s.setNomRegion("");
                    }
                    s.setCodeCommune("");
                    s.setNomCommune("");
                } else {
                    s.setCodeDepartement(codeDepartement);
                    s.setNomDepartement("");
                    s.setCodeRegion(codeRegion != null ? codeRegion : "");
                    s.setNomRegion("");
                    s.setCodeCommune("");
                    s.setNomCommune("");
                }
            }
        }
    }

    private void fillRegionScope(Connection conn, StatLicenceElu s, String codeRegion) throws SQLException {
        String sql = "SELECT code_region, nom_region FROM region WHERE code_region = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeRegion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.setCodeRegion(rs.getString("code_region"));
                    s.setNomRegion(rs.getString("nom_region"));
                } else {
                    s.setCodeRegion(codeRegion);
                    s.setNomRegion("(région inconnue en base)");
                }
            }
        }
        s.setCodeDepartement("");
        s.setNomDepartement("");
        s.setCodeCommune("");
        s.setNomCommune("");
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

    /**
     * Compteurs, population couverte, synthèse open data et classement des communes (mêmes filtres géo que l'agrégat).
     */
    public LicenceSearchExtras loadSearchExtras(String codeFederation, String codeRegion, String codeDepartement,
            String codeCommune, String triCommunes, int limit) throws SQLException {
        LicenceSearchExtras ex = new LicenceSearchExtras();
        String region = codeRegion == null ? "" : codeRegion.trim();
        String dept = codeDepartement == null ? "" : codeDepartement.trim();
        String com = codeCommune == null ? "" : codeCommune.trim();
        String fed = codeFederation == null ? "" : codeFederation.trim();
        if (fed.isEmpty()) {
            return ex;
        }
        int lim = Math.min(Math.max(limit, 5), 50);
        String orderBy = orderByTriCommunes(triCommunes);

        Connection conn = ConnexionDB.getConnection();
        try {
            String baseFrom = "FROM statistique_licencies sl "
                    + "JOIN commune c ON c.code_commune = sl.code_commune "
                    + "WHERE sl.code_federation = ? "
                    + "AND (? = '' OR c.code_region = ?) "
                    + "AND (? = '' OR " + DEPT_SQL + " = ?) "
                    + "AND (? = '' OR c.code_commune = ?)";

            String countSql = "SELECT COUNT(DISTINCT sl.code_commune) AS n " + baseFrom;
            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                bindFedGeo(ps, fed, region, dept, com);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ex.setNombreCommunes(rs.getInt("n"));
                    }
                }
            }

            String popSql = "SELECT COALESCE(SUM(t.pop), 0) AS pop_sum FROM ( "
                    + "SELECT DISTINCT c.code_commune, COALESCE(c.population, 0) AS pop "
                    + baseFrom
                    + ") t";
            try (PreparedStatement ps = conn.prepareStatement(popSql)) {
                bindFedGeo(ps, fed, region, dept, com);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ex.setPopulationCouverture(rs.getLong("pop_sum"));
                    }
                }
            }

            String topSql = "SELECT c.code_commune, c.nom_commune, "
                    + "SUM(sl.total_licencies) AS total_licencies, "
                    + "SUM(sl.licencies_femmes) AS licencies_femmes, "
                    + "SUM(sl.licencies_hommes) AS licencies_hommes "
                    + baseFrom
                    + " GROUP BY c.code_commune, c.nom_commune "
                    + "ORDER BY " + orderBy + " "
                    + "LIMIT " + lim;
            try (PreparedStatement ps = conn.prepareStatement(topSql)) {
                bindFedGeo(ps, fed, region, dept, com);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Map<String, String>> rows = new ArrayList<>();
                    int rank = 1;
                    while (rs.next()) {
                        Map<String, String> row = new LinkedHashMap<>();
                        row.put("rang", String.valueOf(rank++));
                        row.put("code", rs.getString("code_commune"));
                        row.put("nom", rs.getString("nom_commune"));
                        row.put("total", String.valueOf(rs.getInt("total_licencies")));
                        row.put("f", String.valueOf(rs.getInt("licencies_femmes")));
                        row.put("h", String.valueOf(rs.getInt("licencies_hommes")));
                        rows.add(row);
                    }
                    ex.setClassementCommunes(rows);
                }
            }

            loadOpenDataSummary(conn, ex, fed, region, dept, com);
            return ex;
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    private void bindFedGeo(PreparedStatement ps, String fed, String region, String dept, String com)
            throws SQLException {
        ps.setString(1, fed);
        ps.setString(2, region);
        ps.setString(3, region);
        ps.setString(4, dept);
        ps.setString(5, dept);
        ps.setString(6, com);
        ps.setString(7, com);
    }

    private String orderByTriCommunes(String tri) {
        if (tri == null) {
            return "SUM(sl.total_licencies) DESC, c.nom_commune ASC";
        }
        switch (tri.trim().toLowerCase()) {
            case "f":
            case "femmes":
                return "SUM(sl.licencies_femmes) DESC, SUM(sl.total_licencies) DESC, c.nom_commune ASC";
            case "h":
            case "hommes":
                return "SUM(sl.licencies_hommes) DESC, SUM(sl.total_licencies) DESC, c.nom_commune ASC";
            case "nom":
            case "alpha":
                return "c.nom_commune ASC, c.code_commune ASC";
            default:
                return "SUM(sl.total_licencies) DESC, c.nom_commune ASC";
        }
    }

    private void loadOpenDataSummary(Connection conn, LicenceSearchExtras ex, String fed, String region, String dept,
            String com) {
        String openFrom = "FROM statistique_licencies_opendata slo "
                + "JOIN commune c ON c.code_commune = slo.code_commune "
                + "WHERE slo.code_federation = ? "
                + "AND (? = '' OR c.code_region = ?) "
                + "AND (? = '' OR " + DEPT_SQL + " = ?) "
                + "AND (? = '' OR c.code_commune = ?)";
        String sql = "SELECT COUNT(*) AS nl, "
                + "COALESCE(SUM(slo.total_licencies), 0) AS st, "
                + "SUM(CASE WHEN slo.details_json IS NOT NULL AND JSON_LENGTH(slo.details_json) > 0 THEN 1 ELSE 0 END) AS nj "
                + openFrom;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindFedGeo(ps, fed, region, dept, com);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ex.setOpenDataNbLignes(rs.getInt("nl"));
                    ex.setOpenDataTotalLicencies(rs.getInt("st"));
                    ex.setOpenDataLignesAvecJson(rs.getInt("nj"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Open data ou JSON : optionnel selon base / version MySQL
        }
    }
}
