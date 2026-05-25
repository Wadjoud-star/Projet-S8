/**
 * 
 */
package com.clubsport.model;

/**
 *Region est une classe qui représente une region francaise
 *
 *@author bpenw
 *@version 1.0
 */
public class Region {
	private String codeRegion;
	private String nomRegion;
	
	public Region(String codeRegion, String nomRegion) {
		this.codeRegion = codeRegion;
		this.nomRegion = nomRegion;
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

	/**
	 * @return the nomRegion
	 */
	public String getNomRegion() {
		return nomRegion;
	}

	/**
	 * @param nomRegion the nomRegion to set
	 */
	public void setNomRegion(String nomRegion) {
		this.nomRegion = nomRegion;
	}
	
}
