package com.clubsport.admin.model;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private int id;
    private String nom;
    private String email;
    private String motDePasseHash; // mot de passe hashé (SHA-256 ou BCrypt)
    private String role;

    private List<HistoriqueConnexion> historiques; // liste des connexions

    private String photoIdentite = null; // photo d'identité stockée en BDD
    private String statutVerification = "EN_ATTENTE"; // statut de vérification (EN_ATTENTE, VALIDE, REFUSE)

    public Utilisateur() {}

    // --- Constructeur complet utilisé par le DAO ---
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

    // --- Constructeur intermédiaire ---
    public Utilisateur(int id, String nom, String email, String motDePasseHash, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }

    // --- Constructeur simplifié ---
    public Utilisateur(int id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    // --- GETTERS ---
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

    public String getPhotoIdentite() {
        return photoIdentite;
    }

    public String getStatutVerification() {
        return statutVerification;
    }

    // --- SETTERS ---
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhotoIdentite(String photoIdentite) {
        this.photoIdentite = photoIdentite;
    }

    public void setStatutVerification(String statutVerification) {
        this.statutVerification = statutVerification;
    }

    // setter ajouté pour gérer le mot de passe hashé
    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    // --- MÉTHODES UTILES ---

    // ajouter une connexion
    public void ajouterConnexion(HistoriqueConnexion historique) {
        if (this.historiques == null) {
            this.historiques = new ArrayList<>();
        }
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

    // Afficher de façon lisible
    @Override
    public String toString() {
        return nom + " (" + role + ")";
    }
}
