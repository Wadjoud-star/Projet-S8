package com.clubsport.admin.model;


public class StatistiqueLicencies {

    private int id;
    private int totalLicencies;
    private int licenciesFemmes;
    private int licenciesHommes;
    private Commune commune;
    private Federation federation;

    // permet de construire un objet 
    public StatistiqueLicencies(int id, int totalLicencies, int licenciesFemmes,int licenciesHommes,
    		Commune commune, Federation federation ) {

        this.id = id;
        this.totalLicencies = totalLicencies;
        this.licenciesFemmes = licenciesFemmes;
        this.licenciesHommes = licenciesHommes;
        this.commune = commune;
        this.federation = federation;
}


    public int getId() {
        return id; }

    public int getTotalLicencies() {
        return totalLicencies;}

    public int getLicenciesFemmes() {
        return licenciesFemmes;}

    public int getLicenciesHommes() {
        return licenciesHommes; }

    public Commune getCommune() {
        return commune;}

    public Federation getFederation() {
        return federation;}


// calcul la propo de femmes 
    public double getProportionFemmes() {
        return totalLicencies == 0 ? 0 : (double) licenciesFemmes / totalLicencies;// pas de division par 0
    }

// calculer la propotion d'homme 
    public double getProportionHommes() {
        return totalLicencies == 0 ? 0 : (double) licenciesHommes / totalLicencies;// verifie pas division par 0
    }

// afficher de maniere lisible dans les pages 
    @Override
    public String toString() {
        return "Statistiques - " + commune.getNomCommune() + " / " + federation.getNomFederation();
    }
}
