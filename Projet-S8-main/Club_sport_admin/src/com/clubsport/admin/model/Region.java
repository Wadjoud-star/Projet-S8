package com.clubsport.admin.model;

public class Region {

    private String codeRegion;
    private String nomRegion;

// construire une region 
    public Region(String codeRegion, String nomRegion) {
        this.codeRegion = codeRegion;
        this.nomRegion = nomRegion;
    }


    public String getCodeRegion() {
        return codeRegion;}

    public String getNomRegion() {
        return nomRegion;}


// afficher dans les pages 
    @Override
    public String toString() {
        return nomRegion;
    }
}
