package com.clubsport.admin.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoriqueConnexion {

    private int id;
    private Date dateHeure;
    private String adresseIP;
    private String login;
    private boolean succes;
    private Utilisateur utilisateur;

// Construire un objet 
    public HistoriqueConnexion(int id, Date dateHeure, String adresseIP,String login, boolean succes,
    		Utilisateur utilisateur) {

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



// permet d'afficher la date au bon format et l'afficher 
    public String getDateFormatee() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");// convertir une date en texte 
        return sdf.format(dateHeure);// transforme enntexte lisible 
    }

//afficher suucces ou echec 
    public String getStatutTexte() {
    	if (succes) {
    	    return "Succès";
    	} else {
    	    return "Échec";
    	}

    }

// Pour inserer dans le tableau 
    public Object[] toTableRow() {
        return new Object[]{
                getDateFormatee(),login,adresseIP, getStatutTexte()
        };
    }

 // Permet afficher les informations de manière lisible 
    @Override
    public String toString() {
        return login + " - " + getDateFormatee() + " (" + getStatutTexte() + ")";
    }
}
