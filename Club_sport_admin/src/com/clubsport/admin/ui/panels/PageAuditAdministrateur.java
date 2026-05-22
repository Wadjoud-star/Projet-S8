package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class PageAuditAdministrateur extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public PageAuditAdministrateur() {
        setTitle("Gestion administrateur - Audit des actions");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Le titre de ma page 
        JLabel titre = new JLabel("Audit des actions administratives", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titre, BorderLayout.NORTH);

        // Le tableau
        model = new DefaultTableModel(
                new Object[]{"Administrateur", "Action", "Date"},
                0
        );

        table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- DONNÉES FICTIVES AU LANCEMENT ---
        chargerDonneesFictives();

        //Un bouton pour actualiser la page 
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setPreferredSize(new Dimension(120, 35));
        btnActualiser.addActionListener(e -> {
            model.setRowCount(0); // vide le tableau
            chargerDonneesFictives(); // recharge les données
        });

        //LE bouton pour fermer la page 
        JButton btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(120, 35));
        btnFermer.addActionListener(e -> dispose());

        //Bas de la page 
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnActualiser);
        bottomPanel.add(btnFermer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Méthode pour ajouter une ligne
    public void ajouterAction(String admin, String action, String date) {
        model.addRow(new Object[]{admin, action, date});
    }

    // Méthode pour charger des données fictives
    private void chargerDonneesFictives() {
        model.addRow(new Object[]{"AdminA", "Ajout d’un utilisateur", LocalDateTime.now()});
        model.addRow(new Object[]{"AdminB", "Suppression d’un club", LocalDateTime.now()});
        model.addRow(new Object[]{"AdminC", "Modification d’un profil", LocalDateTime.now()});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PageAuditAdministrateur().setVisible(true));
    }
}
