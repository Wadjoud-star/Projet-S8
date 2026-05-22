package com.clubsport.admin.model;

import java.time.LocalDate;

public class AuditAction {

    private String adminNom;
    private String typeAction;
    private LocalDate dateAction;

    public AuditAction(String adminNom, String typeAction, LocalDate dateAction) {
        this.adminNom = adminNom;
        this.typeAction = typeAction;
        this.dateAction = dateAction;
    }

    public String getAdminNom() { return adminNom; }
    public String getTypeAction() { return typeAction; }
    public LocalDate getDateAction() { return dateAction; }
}

