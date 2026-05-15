package com.clubsport.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.clubsport.dao.EluLicenceDAO;
import com.clubsport.model.LicenceExportRow;
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

    public List<Map<String, String>> rechercherCommunes(String query, String codeRegion, String codeDepartement, int limit)
            throws SQLException {
        return dao.searchCommunes(query, normalize(codeRegion), normalize(codeDepartement), limit);
    }

    public boolean communeDansPerimetre(String codeCommune, String codeRegion, String codeDepartement)
            throws SQLException {
        return dao.communeDansPerimetre(normalize(codeCommune), normalize(codeRegion), normalize(codeDepartement));
    }

    public Optional<String> libelleCommune(String codeCommune) throws SQLException {
        return dao.findCommuneLabel(codeCommune);
    }

    public List<Map<String, String>> listerFederations() throws SQLException {
        return dao.listFederations();
    }

    public List<LicenceExportRow> exporterLicencesDetail(
            String codeFederation, String genre, String codeRegion, String codeDepartement, String codeCommune)
            throws SQLException {
        if (codeFederation == null || codeFederation.isBlank()) {
            return List.of();
        }
        return dao.findDetailForExport(
                codeFederation.trim(), normalize(codeRegion), normalize(codeDepartement), normalize(codeCommune));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
