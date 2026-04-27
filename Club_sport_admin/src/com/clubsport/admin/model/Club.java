package com.clubsport.admin.model;

public class Club {
	private int id;
	private String nom;
	private String adresse;
	private String codePostal;
	private int nbLicencies;
	private int nbHommes;
	private int nbFemmes;

	public Club(int id, String nom, String adresse, String codePostal, int nbLicencies, int nbHommes, int nbFemmes) {
		this.id = id;
		this.nom = nom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.nbLicencies = nbLicencies;
		this.nbHommes = nbHommes;
		this.nbFemmes = nbFemmes;
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public int getNbLicencies() {
		return nbLicencies;
	}

	public int getNbHommes() {
		return nbHommes;
	}

	public int getNbFemmes() {
		return nbFemmes;
	}

}
