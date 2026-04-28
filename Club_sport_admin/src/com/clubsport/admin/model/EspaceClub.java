package com.clubsport.admin.model;

import java.util.Date;

public class EspaceClub {

    private int id;               
    private String actualites;    
    private String horaires;      
    private String cotisations;   
    private Date dateMaj;        
    private Club club;           

    /**
     * Constructeur complet utilisé lors du chargement depuis la base de données.
     */
    public EspaceClub(int id, String actualites, String horaires, String cotisations, Date dateMaj, Club club) {
        this.id = id;
        this.actualites = actualites;
        this.horaires = horaires;
        this.cotisations = cotisations;
        this.dateMaj = dateMaj;
        this.club = club;
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getActualites() {
        return actualites;
    }

    public String getHoraires() {
        return horaires;
    }

    public String getCotisations() {
        return cotisations;
    }

    public Date getDateMaj() {
        return dateMaj;
    }

    public Club getClub() {
        return club;
    }


    /** Mise à jour des actualités */
    public void setActualites(String actualites) {
        this.actualites = actualites;
    }

    /** Mise à jour des horaires */
    public void setHoraires(String horaires) {
        this.horaires = horaires;
        this.dateMaj = new Date();
    }

    /** Mise à jour des cotisations */
    public void setCotisations(String cotisations) {
        this.cotisations = cotisations;
        this.dateMaj = new Date();
    }


    // --- Méthode utilitaire ---

    /**
     * Permet d'afficher l'espace club dans une liste ou un log.
     */
    @Override
    public String toString() {
        return "Espace du club : " + (club != null ? club.getNom() : "Inconnu");
    }
}
