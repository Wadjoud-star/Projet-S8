package com.clubsport.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Indicateurs complémentaires pour l'écran licences élus (hors agrégat principal).
 */
public class LicenceSearchExtras {

    private int nombreCommunes;
    private long populationCouverture;
    private int openDataNbLignes;
    private int openDataTotalLicencies;
    private int openDataLignesAvecJson;
    private List<Map<String, String>> classementCommunes = new ArrayList<>();

    public int getNombreCommunes() {
        return nombreCommunes;
    }

    public void setNombreCommunes(int nombreCommunes) {
        this.nombreCommunes = nombreCommunes;
    }

    public long getPopulationCouverture() {
        return populationCouverture;
    }

    public void setPopulationCouverture(long populationCouverture) {
        this.populationCouverture = populationCouverture;
    }

    public int getOpenDataNbLignes() {
        return openDataNbLignes;
    }

    public void setOpenDataNbLignes(int openDataNbLignes) {
        this.openDataNbLignes = openDataNbLignes;
    }

    public int getOpenDataTotalLicencies() {
        return openDataTotalLicencies;
    }

    public void setOpenDataTotalLicencies(int openDataTotalLicencies) {
        this.openDataTotalLicencies = openDataTotalLicencies;
    }

    public int getOpenDataLignesAvecJson() {
        return openDataLignesAvecJson;
    }

    public void setOpenDataLignesAvecJson(int openDataLignesAvecJson) {
        this.openDataLignesAvecJson = openDataLignesAvecJson;
    }

    public List<Map<String, String>> getClassementCommunes() {
        return classementCommunes;
    }

    public void setClassementCommunes(List<Map<String, String>> classementCommunes) {
        this.classementCommunes = classementCommunes != null ? classementCommunes : new ArrayList<>();
    }
}
