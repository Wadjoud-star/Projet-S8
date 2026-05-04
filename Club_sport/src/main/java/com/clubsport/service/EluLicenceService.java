package com.clubsport.service;

import java.sql.SQLException;
import java.util.Optional;

import com.clubsport.dao.EluLicenceDAO;
import com.clubsport.model.StatLicenceElu;

/**
 * Couche métier fine : aujourd'hui délègue au DAO (sprint 2).
 */
public class EluLicenceService {

    private final EluLicenceDAO dao = new EluLicenceDAO();

    public Optional<StatLicenceElu> consulterLicences(String codeCommune, String codeFederation)
            throws SQLException {
        if (codeCommune == null || codeCommune.isBlank() || codeFederation == null || codeFederation.isBlank()) {
            return Optional.empty();
        }
        return dao.findByCommuneAndFederation(codeCommune, codeFederation);
    }
}
