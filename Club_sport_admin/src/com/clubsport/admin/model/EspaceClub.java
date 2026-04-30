package com.clubsport.admin.model;

import java.util.Date;

public class EspaceClub {

    private int id;
    private String actualites;
    private String horaires;
    private double cotisations;  
    private Date dateMaj;
    private Club club;

// Contruire un objet Espacede club 
    public EspaceClub(int id, String actualites, String horaires,double cotisations, Date dateMaj, Club club) {

        this.id = id;
        this.actualites = actualites;
        this.horaires = horaires;
        this.cotisations = cotisations;
        this.dateMaj = dateMaj;
        this.club = club;
    }



    public int getId() {
        return id;
    }

    public String getActualites() {
        return actualites;
    }

    public String getHoraires() {
        return horaires;
    }

    public double getCotisations() {
        return cotisations;
    }

    public Date getDateMaj() {
        return dateMaj;
    }

    public Club getClub() {
        return club;
    }


//afficher le nom du club 
    public String getNomClub() {
        return club != null ? club.getNom() : "";
    }

// afficher dans les box 
    @Override
    public String toString() {
        return "Espace du club : " + getNomClub();
    }
}
