package com.clubsport.model;

public class Club {

    private int idClub;
    private String nom;
    private String adresse;
    private String codePostal;
    private double latitude;
    private double longitude;
    private int nbLicencies;
    private int nbFemmes;
    private int nbHommes;
    private String codeFederation;
    private String codeCommune;

    private String actualite;
    private String horaires;
    private String cotisation;

    public Club() {
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNbLicencies() {
        return nbLicencies;
    }

    public void setNbLicencies(int nbLicencies) {
        this.nbLicencies = nbLicencies;
    }

    public int getNbFemmes() {
        return nbFemmes;
    }

    public void setNbFemmes(int nbFemmes) {
        this.nbFemmes = nbFemmes;
    }

    public int getNbHommes() {
        return nbHommes;
    }

    public void setNbHommes(int nbHommes) {
        this.nbHommes = nbHommes;
    }

    public String getCodeFederation() {
        return codeFederation;
    }

    public void setCodeFederation(String codeFederation) {
        this.codeFederation = codeFederation;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getActualite() {
        return actualite;
    }

    public void setActualite(String actualite) {
        this.actualite = actualite;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getCotisation() {
        return cotisation;
    }

    public void setCotisation(String cotisation) {
        this.cotisation = cotisation;
    }
}