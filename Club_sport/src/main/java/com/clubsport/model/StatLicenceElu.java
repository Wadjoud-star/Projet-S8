package com.clubsport.model;

/**
 * Données affichées pour une ligne de {@code statistique_licencies}
 * avec libellés commune / fédération (jointures).
 */
public class StatLicenceElu {

    private String codeCommune;
    private String nomCommune;
    private String codeFederation;
    private String nomFederation;
    private int totalLicencies;
    private int licenciesFemmes;
    private int licenciesHommes;

    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public String getCodeFederation() {
        return codeFederation;
    }

    public void setCodeFederation(String codeFederation) {
        this.codeFederation = codeFederation;
    }

    public String getNomFederation() {
        return nomFederation;
    }

    public void setNomFederation(String nomFederation) {
        this.nomFederation = nomFederation;
    }

    public int getTotalLicencies() {
        return totalLicencies;
    }

    public void setTotalLicencies(int totalLicencies) {
        this.totalLicencies = totalLicencies;
    }

    public int getLicenciesFemmes() {
        return licenciesFemmes;
    }

    public void setLicenciesFemmes(int licenciesFemmes) {
        this.licenciesFemmes = licenciesFemmes;
    }

    public int getLicenciesHommes() {
        return licenciesHommes;
    }

    public void setLicenciesHommes(int licenciesHommes) {
        this.licenciesHommes = licenciesHommes;
    }
}
