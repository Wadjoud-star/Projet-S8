package com.clubsport.admin.model;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private int id;
    private String nom;
    private String email;
    private String motDePasseHash;
    private String role;

    // état de l'inscription de l'utilisateur (SUPPRIMÉ DE LA BDD)
    // private String etatInscription = "En cours";

    private List<HistoriqueConnexion> historiques; // liste des connexions

    private String photoIdentite = null; // photo d'identité stockée en BDD
    private String statutVerification = "EN_ATTENTE"; // nouveau champ BDD

    public Utilisateur() {}

    // Construire un objet utilisateur (constructeur complet utilisé par le DAO)
    public Utilisateur(int id, String nom, String email, String motDePasseHash, String role,
                       String photoIdentite, String statutVerification) {

        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.photoIdentite = photoIdentite;
        this.statutVerification = statutVerification;
        this.historiques = new ArrayList<>();
    }
    public Utilisateur(int id, String nom, String email, String motDePasseHash, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }

    // Constructeur simplifié (si besoin ailleurs)
    public Utilisateur(int id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public String getRole() {
        return role;
    }

    public List<HistoriqueConnexion> getHistoriques() {
        return historiques;
    }

    // ajouter une connexion
    public void ajouterConnexion(HistoriqueConnexion historique) {
        this.historiques.add(historique);
    }

    // vérifier qu'on est bien admin
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    // vérifier que le gestionnaire ait un rôle
    public boolean isGestionnaire() {
        return "gestionnaire".equalsIgnoreCase(role);
    }

    // vérifier qu'un utilisateur soit un club
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

    // Afficher de façon lisible
    @Override
    public String toString() {
        return nom + " (" + role + ")";
    }

    // méthodes pour les photos à récupérer dans la bdd
    public String getPhotoIdentite() { return photoIdentite; }
    public void setPhotoIdentite(String photoIdentite) { this.photoIdentite = photoIdentite; }

    // nouveau champ : statut de vérification
    public String getStatutVerification() { return statutVerification; }
    public void setStatutVerification(String statutVerification) { this.statutVerification = statutVerification; }

}
