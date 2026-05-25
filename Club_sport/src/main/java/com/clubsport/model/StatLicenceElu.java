package com.clubsport.model;

/**
 * Données affichées pour une ligne de {@code statistique_licencies}
 * avec libellés commune / fédération (jointures).
 */
public class StatLicenceElu {

    private String codeRegion;
    private String nomRegion;
    private String codeDepartement;
    /** Libellé du département (table {@code departement}), utile pour l'affichage périmètre département. */
    private String nomDepartement;
    private String codeCommune;
    private String nomCommune;
    private String codeFederation;
    private String nomFederation;
    private String genre;
    private int totalLicencies;
    private int licenciesFemmes;
    private int licenciesHommes;
    private int valeurGenre;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getValeurGenre() {
        return valeurGenre;
    }

    public void setValeurGenre(int valeurGenre) {
        this.valeurGenre = valeurGenre;
    }
}
