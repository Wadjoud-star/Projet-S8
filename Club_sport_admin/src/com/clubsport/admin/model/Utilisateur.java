package com.clubsport.admin.model;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private int id;
    private String nom;
    private String email;
    private String motDePasseHash;
    private String role;
    private List<HistoriqueConnexion> historiques;// liste des connections 
    
    public Utilisateur() {}

// Construire un objet utilisateur 
    public Utilisateur(int id, String nom, String email,String motDePasseHash, String role) {

        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.historiques = new ArrayList<>();
    }
    public Utilisateur(int id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }


    public int getId() {
        return id;}

    public String getNom() {
        return nom;}

    public String getEmail() {
        return email;}

    public String getMotDePasseHash() {
        return motDePasseHash;}

    public String getRole() {
        return role;}

    public List<HistoriqueConnexion> getHistoriques() {
        return historiques;}


// ajouter une connexion 
    public void ajouterConnexion(HistoriqueConnexion historique) {
        this.historiques.add(historique);
    }

// verifier qu'on est bien admin 
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

// verifier que le gestionnaire ait un role 
    public boolean isGestionnaire() {
        return "gestionnaire".equalsIgnoreCase(role);
    }

// verifier qu'un utilisateur ait un utilisateur 
    public boolean isClub() {
        return "club".equalsIgnoreCase(role);
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }


// Afficher de facon lisible 
    @Override
    public String toString() {
        return nom + " (" + role + ")";
    }
}