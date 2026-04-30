package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.clubsport.admin.dao.HistoriqueConnexionDAO;
import com.clubsport.admin.model.HistoriqueConnexion;

public class PageHistorique extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    // DAO réel
    private HistoriqueConnexionDAO historiqueDAO = new HistoriqueConnexionDAO();

    public PageHistorique() {
        setTitle("Historique des connexions");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TEXTE EN HAUT ---
        JLabel titre = new JLabel("Voici les dernières connexions au site :");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titre, BorderLayout.NORTH);

        // --- TABLEAU ---
        String[] colonnes = {"Utilisateur", "Adresse IP", "Date", "Succès"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Charger les données BDD
        chargerHistorique();
    }

    private void chargerHistorique() {
        model.setRowCount(0);

        // --- Récupération depuis MySQL ---
        List<HistoriqueConnexion> logsBDD = historiqueDAO.getHistorique();

        for (HistoriqueConnexion h : logsBDD) {
            model.addRow(new Object[]{
                    h.getUtilisateur().getNom(),
                    h.getAdresseIP(),
                    h.getDateHeure().toString(),
                    h.isSucces() ? "✔" : "✘"
            });
        }
    }
}
