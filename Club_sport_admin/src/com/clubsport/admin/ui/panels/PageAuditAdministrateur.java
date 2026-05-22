package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.AuditDAO;
import com.clubsport.admin.model.AuditAction;

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

    // Format d'affichage de la date
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PageAuditAdministrateur() {

        // --- Configuration de la fenêtre ---
        setTitle("Gestion administrateur - Audit des actions");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Titre ---
        JLabel titre = new JLabel("Audit des actions administratives", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titre, BorderLayout.NORTH);

        // --- Tableau ---
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

        // --- Bouton Actualiser ---
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setPreferredSize(new Dimension(120, 35));
        btnActualiser.addActionListener(e -> {
            model.setRowCount(0);   // vide le tableau
            chargerDonneesReelles(); // recharge depuis la BDD
        });

        // --- Bouton Fermer ---
        JButton btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(120, 35));
        btnFermer.addActionListener(e -> dispose());

        // --- Bas de page ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnActualiser);
        bottomPanel.add(btnFermer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Charge les actions d’audit depuis la base de données
     * et les ajoute dans le tableau.
     */
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PageAuditAdministrateur().setVisible(true));
    }
}
