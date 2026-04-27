package com.clubsport.admin.model;

public class Commune {

    private String codeCommune;
    private String nomCommune;
    private int population;
    private String codeRegion;

    public Commune(String codeCommune, String nomCommune, int population, String codeRegion) {
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.population = population;
        this.codeRegion = codeRegion;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public int getPopulation() {
        return population;
    }

    public String getCodeRegion() {
        return codeRegion;
    }
}
