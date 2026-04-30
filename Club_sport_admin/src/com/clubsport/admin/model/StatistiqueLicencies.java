package com.clubsport.admin.model;


public class StatistiqueLicencies {

    private int id;                 
    private int totalLicencies;     
    private int licenciesFemmes;    
    private int licenciesHommes;    
    private Club club;              

    /**
     * Constructeur complet utilisé lors du chargement depuis la base de données.
     */
    public StatistiqueLicencies(int id, int totalLicencies, int licenciesFemmes, int licenciesHommes, Club club) {
        this.id = id;
        this.totalLicencies = totalLicencies;
        this.licenciesFemmes = licenciesFemmes;
        this.licenciesHommes = licenciesHommes;
        this.club = club;
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public int getTotalLicencies() {
        return totalLicencies;
    }

    public int getLicenciesFemmes() {
        return licenciesFemmes;
    }

    public int getLicenciesHommes() {
        return licenciesHommes;
    }

    public Club getClub() {
        return club;
    }

    // --- Setters utiles (pas de setters inutiles) ---

    /** Mise à jour du total des licenciés */
    public void setTotalLicencies(int totalLicencies) {
        this.totalLicencies = totalLicencies;
    }

    /** Mise à jour du nombre de femmes */
    public void setLicenciesFemmes(int licenciesFemmes) {
        this.licenciesFemmes = licenciesFemmes;
    }

    /** Mise à jour du nombre d'hommes */
    public void setLicenciesHommes(int licenciesHommes) {
        this.licenciesHommes = licenciesHommes;
    }


    // --- Méthodes utilitaires ---

    /**
     * Calcule la proportion de femmes dans le club.
     */
    public double getProportionFemmes() {
        return totalLicencies == 0 ? 0 : (double) licenciesFemmes / totalLicencies;
    }

    /**
     * Calcule la proportion d'hommes dans le club.
     */
    public double getProportionHommes() {
        return totalLicencies == 0 ? 0 : (double) licenciesHommes / totalLicencies;
    }

    /**
     * retourne la proportion de femmes par rapport au total .
     */
    public double calculerProportion() {
        return getProportionFemmes();
    }

    @Override
    public String toString() {
        return "Statistiques du club : " + (club != null ? club.getNom() : "Inconnu");
    }
}
