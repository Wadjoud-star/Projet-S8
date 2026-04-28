package com.clubsport.admin.model;


public class Commune {

    private String codeCommune;   
    private String nomCommune;    
    private int population;       
    private Region region;        
    /**
     * Constructeur complet utilisé lors du chargement depuis la base de données.
     */
    public Commune(String codeCommune, String nomCommune, int population, Region region) {
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.population = population;
        this.region = region;
    }

    // --- Getters ---

    public String getCodeCommune() {
        return codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public int getPopulation() {
        return population;
    }

    public Region getRegion() {
        return region;
    }

    // --- Setters  ---

    /** Mise à jour de la population  */
    public void setPopulation(int population) {
        this.population = population;
    }

    // --- Méthode utilitaire ---

    /**
     * Permet d'afficher la commune dans une JComboBox ou dans les logs.
     */
    @Override
    public String toString() {
        return nomCommune;
    }
}
