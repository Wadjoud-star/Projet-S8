/**
 * 
 */
package com.clubsport.model;

/**
 *StatClubs est une classe qui caractérise les statistiques et 
 *caractéristiques d'un club sa region sa fédération
 *
 *@author bpenw
 *@version 1.0
 */
public class StatClubs {
	
	private int nombreClubs;
	private int nombreEtablissements;
	private int totalStructure;
	private String codeCommune;
	private String codeFederation;
	
	public StatClubs(int nombreClubs, int nombreEtablissements, int totalStructure, String codeCommune,
			String codeFederation) {
		super();
		this.nombreClubs = nombreClubs;
		this.nombreEtablissements = nombreEtablissements;
		this.totalStructure = totalStructure;
		this.codeCommune = codeCommune;
		this.codeFederation = codeFederation;
	}
	
	public int getNombreClubs() {
		return nombreClubs;
	}
	public void setNombreClubs(int nombreClubs) {
		this.nombreClubs = nombreClubs;
	}
	public int getNombreEtablissements() {
		return nombreEtablissements;
	}
	public void setNombreEtablissements(int nombreEtablissements) {
		this.nombreEtablissements = nombreEtablissements;
	}
	public int getTotalStructure() {
		return totalStructure;
	}
	public void setTotalStructure(int totalStructure) {
		this.totalStructure = totalStructure;
	}
	public String getCodeCommune() {
		return codeCommune;
	}
	public void setCodeCommune(String codeCommune) {
		this.codeCommune = codeCommune;
	}
	public String getCodeFederation() {
		return codeFederation;
	}
	public void setCodeFederation(String codeFederation) {
		this.codeFederation = codeFederation;
	}
}
