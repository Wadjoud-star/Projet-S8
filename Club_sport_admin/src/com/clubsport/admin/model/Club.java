package com.clubsport.admin.model;

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
    private Commune commune;
    private Federation federation;
    private EspaceClub espaceClub;

// création d'un contructeur d'un club 
    public Club(int id, String nom, String adresse, String codePostal,double latitude, double longitude,
                int nbLicencies, int nbHommes, int nbFemmes,Commune commune, Federation federation, 
                EspaceClub espaceClub) {

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
        this.espaceClub = espaceClub;
    }


    public int getId() { 
    	return id; }
    public String getNom() { 
    	return nom; }
    public String getAdresse() { 
    	return adresse; }
    public String getCodePostal() { 
    	return codePostal; }
    public double getLatitude() { 
    	return latitude; }
    public double getLongitude() { 
    	return longitude; }
    public int getNbLicencies() { 
    	return nbLicencies; }
    public int getNbHommes() { 
    	return nbHommes; }
    public int getNbFemmes() { 
    	return nbFemmes; }
    public Commune getCommune() { 
    	return commune; }
    public Federation getFederation() { 
    	return federation; }
    public EspaceClub getEspaceClub() { 
    	return espaceClub; }


// retourner l'adresse d'un club 
    public String getAdresseComplete() {
        if (commune == null) return adresse + ", " + codePostal;
        return adresse + ", " + codePostal + " " + commune.getNomCommune();
    }

// calculer taux de femmes 
    public double getTauxFemmes() {
        return nbLicencies == 0 ? 0 : (double) nbFemmes / nbLicencies;
    }

// calculer le taux d'hommes 
    public double getTauxHommes() {
        return nbLicencies == 0 ? 0 : (double) nbHommes / nbLicencies;// on vérifie qu'on divise pas par 0
    }

// obtenir le nom de la commune 
    public String getNomCommune() {
        return commune != null ? commune.getNomCommune() : "";
    }

//obtenir les noms de federation et on vérifie que c'est non null
    public String getNomFederation() {
        return federation != null ? federation.getNomFederation() : "";
    }

//pour inserer dans la tableau
    public Object[] toTableRow() {
        return new Object[]{
                nom,
                adresse,
                codePostal,
                nbLicencies,
                nbHommes,
                nbFemmes
        };
    }

// pour les combobox
    @Override
    public String toString() {
        return nom;
    }
}
