package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.clubsport.model.StatLicenceElu;
import com.clubsport.util.ConnexionDB;

public class EluLicenceDAO {

    public Optional<StatLicenceElu> findByCommuneAndFederation(String codeCommune, String codeFederation)
            throws SQLException {
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
                if (!rs.next()) {
                    return Optional.empty();
                }
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
        } finally {
            ConnexionDB.fermer(conn);
        }
    }
}
