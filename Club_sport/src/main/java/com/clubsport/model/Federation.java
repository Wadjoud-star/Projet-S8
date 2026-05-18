/**
 * 
 */
package com.clubsport.model;

/**
 * Federation est une classe qui represente une federation sportive francaise
 * 
 * @author bpenw
 * @version 1.0
 */
public class Federation {
	private String codeFederation;
	private String nomFederation;
	
	public Federation(String codeFederation, String nomFederation) {
		this.codeFederation = codeFederation;
		this.nomFederation = nomFederation;
	}

	/**
	 * @return the codeFederation
	 */
	public String getCodeFederation() {
		return codeFederation;
	}

	/**
	 * @param codeFederation the codeFederation to set
	 */
	public void setCodeFederation(String codeFederation) {
		this.codeFederation = codeFederation;
	}

	/**
	 * @return the nomFederation
	 */
	public String getNomFederation() {
		return nomFederation;
	}

	/**
	 * @param nomFederation the nomFederation to set
	 */
	public void setNomFederation(String nomFederation) {
		this.nomFederation = nomFederation;
	}
	
}
