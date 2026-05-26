package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.AuditDAO;
import com.clubsport.admin.model.AuditAction;
import com.clubsport.admin.model.Utilisateur; // ➕ admin connecté

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PageAuditAdministrateur extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    // DAO pour récupérer les actions depuis la base
    private AuditDAO auditDAO = new AuditDAO();

    private Utilisateur adminConnecte; // ➕ admin connecté (peut être null maintenant)

    // Format d'affichage de la date
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // --- Constructeur modifié pour recevoir l’admin connecté ---
    public PageAuditAdministrateur(Utilisateur adminConnecte) {
        this.adminConnecte = adminConnecte; // ➕ on stocke l’admin (peut être null → accès libre OK)

        //Configuration de la fenêtre
        setTitle("Gestion administrateur - Audit des actions");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Titre 
        JLabel titre = new JLabel("Audit des actions administratives", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titre, BorderLayout.NORTH);

        // Tableau 
        model = new DefaultTableModel(
                new Object[]{"Administrateur", "Action", "Détails", "Date"},
                0
        );

        table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Chargement initial des données réelles
        chargerDonneesReelles();

        // Bouton Actualiser
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setPreferredSize(new Dimension(120, 35));
        btnActualiser.addActionListener(e -> {
            model.setRowCount(0);   // vide le tableau
            chargerDonneesReelles(); // recharge depuis la BDD
        });

        // Bouton Fermer 
        JButton btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(120, 35));
        btnFermer.addActionListener(e -> dispose());

        //Bas de page 
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnActualiser);
        bottomPanel.add(btnFermer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // charger les données de la BDD et les ajouter au tableau 
    private void chargerDonneesReelles() {
        List<AuditAction> actions = auditDAO.getAllActions();

        for (AuditAction a : actions) {
            model.addRow(new Object[]{
                    a.getAdminNom(),
                    a.getTypeAction(),
                    a.getDetails(),
                    a.getDateAction().format(formatter)
            });
        }
    }
}
