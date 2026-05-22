package com.clubsport.admin.dao;

import com.clubsport.admin.model.AuditAction;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO {

    public List<AuditAction> getAllActions() {
        // TODO : ajouter la requête SQL quand la BDD sera disponible
        return new ArrayList<>();
    }

    public void enregistrerAction(String adminNom, String typeAction) {
        // TODO : INSERT INTO audit (...)
    }
}
