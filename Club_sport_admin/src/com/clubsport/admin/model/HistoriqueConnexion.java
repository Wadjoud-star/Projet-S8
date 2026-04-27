	package com.clubsport.admin.model;

	import java.util.Date;

	public class HistoriqueConnexion {

	    private int id;
	    private Date dateHeure;
	    private String adresseIP;
	    private String login;
	    private boolean succes;

	    public HistoriqueConnexion(int id, Date dateHeure, String adresseIP, String login, boolean succes) {
	        this.id = id;
	        this.dateHeure = dateHeure;
	        this.adresseIP = adresseIP;
	        this.login = login;
	        this.succes = succes;
	    }

	    public int getId() {
	        return id;
	    }

	    public Date getDateHeure() {
	        return dateHeure;
	    }

	    public String getAdresseIP() {
	        return adresseIP;
	    }

	    public String getLogin() {
	        return login;
	    }

	    public boolean isSucces() {
	        return succes;
	    }
	}

