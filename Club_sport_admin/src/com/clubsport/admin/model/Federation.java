package com.clubsport.admin.model;

public class Federation {

    private String codeFederation;
    private String nomFederation;

    public Federation(String codeFederation, String nomFederation) {
        this.codeFederation = codeFederation;
        this.nomFederation = nomFederation;
    }

    public String getCodeFederation() {
        return codeFederation;
    }

    public String getNomFederation() {
        return nomFederation;
    }
}
