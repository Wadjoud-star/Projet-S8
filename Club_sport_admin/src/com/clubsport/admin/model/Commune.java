package com.clubsport.admin.model;


public class Commune {

    private String codeCommune;
    private String nomCommune;
    private int population;
    private Region region;

 // Contruire une commune 
    public Commune(String codeCommune, String nomCommune, int population, Region region) {
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.population = population;
        this.region = region;
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

    public Region getRegion() {
        return region;
    }



// renvoyer les noms des Regions de manière correcte
    public String getNomRegion() {
        return region != null ? region.getNomRegion() : "";
    }

// methode pour afficher 
    @Override
    public String toString() {
        return nomCommune;
    }
}
