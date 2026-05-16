/**
 * 
 */
package com.clubsport.model;

/**
 *User est une classe qui represente un utilisateur du client leger
 */
public class User {
	private String email;
	private String nom;
	private String password;
	private String role;
	private String identitePath;
	private String statut;
	
	public User(String email, String nom, String password, String role, String identitePath) {
		this.email = email;
		this.nom = nom;
		this.password = password;
		this.role = role;
		this.statut = "En_ATTENTE";
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
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
	
}
