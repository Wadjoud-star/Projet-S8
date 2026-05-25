package com.clubsport.model;

/**
 * Une ligne du fichier CSV exporté (détail par commune).
 */
public class LicenceExportRow {

    private String codeRegion;
    private String nomRegion;
    private String codeDepartement;
    private String nomDepartement;
    private String codeCommune;
    private String nomCommune;
    private int population;
    private String codeFederation;
    private String nomFederation;
    private int totalLicencies;
    private int licenciesFemmes;
    private int licenciesHommes;

    public String getCodeRegion() {
        return codeRegion;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public void setNomRegion(String nomRegion) {
        this.nomRegion = nomRegion;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
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
