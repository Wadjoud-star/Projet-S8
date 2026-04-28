package com.clubsport.admin.model;

import java.util.ArrayList;
import java.util.List;


public class Utilisateur {

    private int id;                    
    private String nom;
    private String prenom;
    private String email;               
    private String motDePasseHash;     
    private String role;               

    private List<HistoriqueConnexion> historiques; // Historique des connexions

    /**
     * Constructeur complet utilisé lors du chargement depuis la base de données.
     */
    public Utilisateur(int id, String nom, String prenom, String email, String motDePasseHash, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom= prenom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.historiques = new ArrayList<>();
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
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

    // --- Setters utiles (pas de setters inutiles) ---

    /** Mise à jour du nom */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** Mise à jour de l'email */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Mise à jour du mot de passe hashé */
    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    /** Mise à jour du rôle */
    public void setRole(String role) {
        this.role = role;
    }

    // --- Méthodes utilitaires ---

    /**
     * Ajoute une entrée d'historique de connexion à l'utilisateur.
     */
    public void ajouterConnexion(HistoriqueConnexion historique) {
        this.historiques.add(historique);
    }

    /**
     * Affichage lisible dans une JComboBox ou dans les logs.
     */
    @Override
    public String toString() {
        return nom + " (" + role + ")";
    }
}
