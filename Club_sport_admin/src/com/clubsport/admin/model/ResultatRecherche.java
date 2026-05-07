package com.clubsport.admin.model;

public class ResultatRecherche {
// Attributs de la classe
    private String federation;
    private String commune;
    private String region;
    private String codePostal;
    private int totalLicencies;
    private int hommes;
    private int femmes;
    private int nbClubs;
    private int nbEtablissements;
    private int totalStructures;
// constructeur public pour creer un résultat
    public ResultatRecherche(String federation, String commune, String region, String codePostal,int totalLicencies, int hommes, int femmes,
                             int nbClubs, int nbEtablissements, int totalStructures) {
// affecte les valeurs reçues en paramètre aux champs internes de l’objet grace au this.
        this.federation = federation;
        this.commune = commune;
        this.region = region;
        this.codePostal = codePostal;
        this.totalLicencies = totalLicencies;
        this.hommes = hommes;
        this.femmes = femmes;
        this.nbClubs = nbClubs;
        this.nbEtablissements = nbEtablissements;
        this.totalStructures = totalStructures;
    }

// get
    // lecture des informations 
    public String getFederation() { return federation; }
    public String getCommune() { return commune; }
    public String getRegion() { return region; }
    public String getCodePostal() { return codePostal; }
    public int getTotalLicencies() { return totalLicencies; }
    public int getHommes() { return hommes; }
    public int getFemmes() { return femmes; }
    public int getNbClubs() { return nbClubs; }
    public int getNbEtablissements() { return nbEtablissements; }
    public int getTotalStructures() { return totalStructures; }
}
