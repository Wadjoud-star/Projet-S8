package com.clubsport.model;

public class ClassementCommune {

    private String nomCommune;
    private int totalLicencies;
    private double tauxLicencies;

    public ClassementCommune() {
    }

    public ClassementCommune(String nomCommune, int totalLicencies, double tauxLicencies) {
        this.nomCommune = nomCommune;
        this.totalLicencies = totalLicencies;
        this.tauxLicencies = tauxLicencies;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public int getTotalLicencies() {
        return totalLicencies;
    }

    public void setTotalLicencies(int totalLicencies) {
        this.totalLicencies = totalLicencies;
    }

    public double getTauxLicencies() {
        return tauxLicencies;
    }

    public void setTauxLicencies(double tauxLicencies) {
        this.tauxLicencies = tauxLicencies;
    }
}