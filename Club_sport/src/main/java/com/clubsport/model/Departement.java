/**
 * 
 */
package com.clubsport.model;

/**
 * Departement est une classe qui represente un département francais
 * 
 * @author bpenw
 * @version 1.0
 */
public class Departement {
	private String codeDepartement;
	private String codeRegion;
	
	public Departement(String codeDepartement, String codeRegion) {
		this.codeDepartement = codeDepartement;
		this.codeRegion = codeRegion;
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
