package com.clubsport.admin.model;

import java.util.Date;

public class HistoriqueConnexion {

    private int id;
    private Date dateHeure;
    private String adresseIP;
    private String login;
    private boolean succes;
    private Utilisateur utilisateur;

    public HistoriqueConnexion(int id, Date dateHeure, String adresseIP, String login, boolean succes, Utilisateur utilisateur) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.adresseIP = adresseIP;
        this.login = login;
        this.succes = succes;
        this.utilisateur = utilisateur;
    }

    public int getId() {
        return id;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public String getLogin() {
        return login;
    }

    public boolean isSucces() {
        return succes;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return login + " - " + dateHeure + (succes ? " (OK)" : " (Échec)");
    }
}
