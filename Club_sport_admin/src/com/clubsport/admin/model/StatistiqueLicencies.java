package com.clubsport.admin.model;

public class StatistiqueLicencies {

    private int id;
    private int totalLicencies;
    private int licenciesFemmes;
    private int licenciesHommes;
    private int idClub;

    public StatistiqueLicencies(int id, int totalLicencies, int licenciesFemmes, int licenciesHommes, int idClub) {
        this.id = id;
        this.totalLicencies = totalLicencies;
        this.licenciesFemmes = licenciesFemmes;
        this.licenciesHommes = licenciesHommes;
        this.idClub = idClub;
    }

    public int getId() {
        return id;
    }

    public int getTotalLicencies() {
        return totalLicencies;
    }

    public int getLicenciesFemmes() {
        return licenciesFemmes;
    }

    public int getLicenciesHommes() {
        return licenciesHommes;
    }

    public int getIdClub() {
        return idClub;
    }
}
