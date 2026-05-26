package com.clubsport.model;

/**
 * Compte utilisateur (élu, acteur, utilisateur lambda, admin).
 */
public class User {

	private int id;
	private String email;
	private String nom;
	private String password;
	private String role;
	private String identitePath;
	private String statut;
	private String photoProfil;
	private String telephone;
	private String bio;

	public User(String email, String nom, String password, String role, String identitePath) {
		this.email = email;
		this.nom = nom;
		this.password = password;
		this.role = role;
		this.statut = "EN_ATTENTE";
		this.identitePath = identitePath;
	}

	public User(String email, String nom, String password, String role, String identitePath, String statut) {
		this.email = email;
		this.nom = nom;
		this.password = password;
		this.role = role;
		this.statut = statut;
		this.identitePath = identitePath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getIdentitePath() {
		return identitePath;
	}

	public void setIdentitePath(String identitePath) {
		this.identitePath = identitePath;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getPhotoProfil() {
		return photoProfil;
	}

	public void setPhotoProfil(String photoProfil) {
		this.photoProfil = photoProfil;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
}
