package com.clubsport.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.clubsport.dao.EluLicenceDAO;
import com.clubsport.model.StatLicenceElu;

/**
 * Couche métier fine : aujourd'hui délègue au DAO (sprint 2).
 */
public class EluLicenceService {

    private final EluLicenceDAO dao = new EluLicenceDAO();

    public List<Map<String, String>> listerRegions() throws SQLException {
        return dao.listRegions();
    }

    public List<Map<String, String>> listerDepartements() throws SQLException {
        return dao.listDepartements();
    }

    public Optional<StatLicenceElu> consulterLicences(
            String codeFederation, String genre, String codeRegion, String codeDepartement, String codeCommune)
            throws SQLException {
        if (codeFederation == null || codeFederation.isBlank()) {
            return Optional.empty();
        }
        String cleanedGenre = normalize(genre);
        String cleanedRegion = normalize(codeRegion);
        String cleanedDepartement = normalize(codeDepartement);
        String cleanedCommune = normalize(codeCommune);
        return dao.findWithFilters(codeFederation.trim(), cleanedGenre, cleanedRegion, cleanedDepartement, cleanedCommune);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
