/**
 * 
 */
package com.clubsport.model;

import java.sql.Date;

/**
 *La classe publication represente le modele d'une publication
 *
 *@author bpenw
 *@version 1.0
 */
public class Publication {
	/**
	 * @param titre
	 * @param contenu
	 * @param imageUrl
	 * @param nomAuteur
	 * @param datePublication
	 */
	public Publication(String titre, String contenu, String imageUrl, String nomAuteur, Date datePublication) {
		super();
		this.titre = titre;
		this.contenu = contenu;
		this.imageUrl = imageUrl;
		this.nomAuteur = nomAuteur;
		this.datePublication = datePublication;
	}
	/**
	 * @param titre
	 * @param contenu
	 * @param imageUrl
	 * @param datePublication
	 */
	public Publication(String titre, String contenu, String imageUrl, Date datePublication) {
		super();
		this.titre = titre;
		this.contenu = contenu;
		this.imageUrl = imageUrl;
		this.datePublication = datePublication;
	}
	private String titre;
	private String contenu;
	private String imageUrl;
	private String nomAuteur;
	private Date datePublication;
	/**
	 * @return the titre
	 */
	public String getTitre() {
		return titre;
	}
	/**
	 * @param titre the titre to set
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}
	/**
	 * @return the contenu
	 */
	public String getContenu() {
		return contenu;
	}
	/**
	 * @param contenu the contenu to set
	 */
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the datePublication
	 */
	public Date getDatePublication() {
		return datePublication;
	}
	/**
	 * @param datePublication the datePublication to set
	 */
	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}
	/**
	 * @return the nomAuteur
	 */
	public String getNomAuteur() {
		return nomAuteur;
	}
	/**
	 * @param nomAuteur the nomAuteur to set
	 */
	public void setNomAuteur(String nomAuteur) {
		this.nomAuteur = nomAuteur;
	}
}
