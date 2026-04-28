package com.clubsport.admin.model;

/**
 * Représente un club sportif avec ses informations principales,
 */
public class Club {

    private int id;
    private String nom;
    private String adresse;
    private String codePostal;
    private double latitude;
    private double longitude;
    private int nbLicencies;
    private int nbHommes;
    private int nbFemmes;

    // Relations
    private Commune commune;
    private Federation federation;
    private StatistiqueLicencies statistiques;
    private EspaceClub espaceClub;

    /**
     * Constructeur complet utilisé par les DAO lors du chargement depuis la base.
     */
    public Club(int id,String nom,String adresse,String codePostal,double latitude,double longitude,
            int nbLicencies,int nbHommes,int nbFemmes,Commune commune,Federation federation,StatistiqueLicencies statistiques,EspaceClub espaceClub) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbLicencies = nbLicencies;
        this.nbHommes = nbHommes;
        this.nbFemmes = nbFemmes;
        this.commune = commune;
        this.federation = federation;
        this.statistiques = statistiques;
        this.espaceClub = espaceClub;
    }

    // --- Getters ---

    public int getId() { return id; }
    
    public String getNom() { return nom; }
    
    public String getAdresse() { return adresse; }
    
    public String getCodePostal() { return codePostal; }
    
    public double getLatitude() { return latitude; }
    
    public double getLongitude() { return longitude; }
    
    public int getNbLicencies() { return nbLicencies; }
    public int getNbHommes() { return nbHommes; }
    public int getNbFemmes() { return nbFemmes; }
    public Commune getCommune() { return commune; }
    public Federation getFederation() { return federation; }
    public StatistiqueLicencies getStatistiques() { return statistiques; }
    public EspaceClub getEspaceClub() { return espaceClub; }

    // --- Setters  ---

    /** Mise à jour adresse du club */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /** Mise à jour du code postal */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /** Mise à jour des statistiques simples */
    public void setNbLicencies(int nbLicencies) {
        this.nbLicencies = nbLicencies;
    }

    public void setNbHommes(int nbHommes) {
        this.nbHommes = nbHommes;
    }

    public void setNbFemmes(int nbFemmes) {
        this.nbFemmes = nbFemmes;
    }

    /** Mise à jour de l'espace club */
    public void setEspaceClub(EspaceClub espaceClub) {
        this.espaceClub = espaceClub;
    }

    // --- Méthodes utilitaires ---

    /** Retourne l'adresse complète sous forme lisible */
    public String getAdresseComplete() {
        return adresse + ", " + codePostal + " " + (commune != null ? commune.getNomCommune() : "");
    }

    /** Pourcentage de femmes dans le club */
    public double getTauxFemmes() {
        return nbLicencies == 0 ? 0 : (double) nbFemmes / nbLicencies;
    }

    /** Pourcentage d'hommes dans le club */
    public double getTauxHommes() {
        return nbLicencies == 0 ? 0 : (double) nbHommes / nbLicencies;
    }

    /** Permets d'afficher le nom d'un club de la JComboBOX */
    @Override
    public String toString() {
        return nom;
    }
}
