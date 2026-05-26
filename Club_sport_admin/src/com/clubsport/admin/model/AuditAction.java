package com.clubsport.admin.model;

import java.time.LocalDateTime;

public class AuditAction {

    private int id;               // id de la ligne 
    private int idAdmin;          // id de l’admin 
    private String adminNom;      // nom 
    private String typeAction;    // colonne "action"
    private String details;       // colonne "details"
    private LocalDateTime dateAction; // timestamp SQL

    public AuditAction(int id, int idAdmin, String adminNom,
                       String typeAction, String details,
                       LocalDateTime dateAction) {

        this.id = id;
        this.idAdmin = idAdmin;
        this.adminNom = adminNom;
        this.typeAction = typeAction;
        this.details = details;
        this.dateAction = dateAction;
    }

    public int getId() { return id; }
    public int getIdAdmin() { return idAdmin; }
    public String getAdminNom() { return adminNom; }
    public String getTypeAction() { return typeAction; }
    public String getDetails() { return details; }
    public LocalDateTime getDateAction() { return dateAction; }
}
