/**
 * 
 */
package com.clubsport.model;

/**
 * Commune est une classe qui represente une commune francaise
 * 
 * @author bpenw
 * @version 1.0
 */
public class Commune {
	/**
	 * @param codeCommune
	 * @param nomCommune
	 * @param codeDepartement
	 * @param codeRegion
	 */
	public Commune(String codeCommune, String nomCommune, String codeDepartement, String codeRegion) {
		super();
		this.codeCommune = codeCommune;
		this.nomCommune = nomCommune;
		this.codeDepartement = codeDepartement;
		this.codeRegion = codeRegion;
	}
	private String codeCommune;
	private String nomCommune;
	private String codeDepartement;
	private String codeRegion;
	/**
	 * @return the codeCommune
	 */
	public String getCodeCommune() {
		return codeCommune;
	}
	/**
	 * @param codeCommune the codeCommune to set
	 */
	public void setCodeCommune(String codeCommune) {
		this.codeCommune = codeCommune;
	}
	/**
	 * @return the nomCommune
	 */
	public String getNomCommune() {
		return nomCommune;
	}
	/**
	 * @param nomCommune the nomCommune to set
	 */
	public void setNomCommune(String nomCommune) {
		this.nomCommune = nomCommune;
	}
	/**
	 * @return the codeDepartement
	 */
	public String getCodeDepartement() {
		return codeDepartement;
	}
	/**
	 * @param codeDepartement the codeDepartement to set
	 */
	public void setCodeDepartement(String codeDepartement) {
		this.codeDepartement = codeDepartement;
	}
	/**
	 * @return the codeRegion
	 */
	public String getCodeRegion() {
		return codeRegion;
	}
	/**
	 * @param codeRegion the codeRegion to set
	 */
	public void setCodeRegion(String codeRegion) {
		this.codeRegion = codeRegion;
	}
}
