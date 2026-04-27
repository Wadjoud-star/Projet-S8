package com.clubsport.admin.model;

import java.util.Date;

public class EspaceClub {

    private int id;
    private String actualites;
    private String horaires;
    private String cotisations;
    private Date dateMaj;
    private int idClub;

    public EspaceClub(int id, String actualites, String horaires, String cotisations, Date dateMaj, int idClub) {
        this.id = id;
        this.actualites = actualites;
        this.horaires = horaires;
        this.cotisations = cotisations;
        this.dateMaj = dateMaj;
        this.idClub = idClub;
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

    public String getCotisations() {
        return cotisations;
    }

    public Date getDateMaj() {
        return dateMaj;
    }

    public int getIdClub() {
        return idClub;
    }
}
