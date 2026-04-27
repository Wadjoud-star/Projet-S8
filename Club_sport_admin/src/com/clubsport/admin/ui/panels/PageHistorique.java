package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// =======================================================
// MODIF BDD (commenté) : import du DAO
// import com.clubsport.dao.HistoriqueConnexionDAO;
// import com.clubsport.model.HistoriqueConnexion;
// =======================================================

public class PageHistorique extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    // =======================================================
    // MODIF BDD (commenté) : DAO
    // private HistoriqueConnexionDAO historiqueDAO = new HistoriqueConnexionDAO();
    // =======================================================

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
        String[] colonnes = {"Nom & Prénom", "Date de connexion"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Charger les données fictives
        chargerHistorique();
    }

    private void chargerHistorique() {
        model.setRowCount(0);

        // =======================================================
        // VERSION ACTUELLE : données fictives (affichage comme avant)
        // =======================================================
        List<ConnexionLog> logs = getFakeLogs();

        for (ConnexionLog log : logs) {
            model.addRow(new Object[]{
                    log.getNom() + " " + log.getPrenom(),
                    log.getDate()
            });
        }

        // =======================================================
        // MODIF BDD (commenté) : récupération depuis MySQL
        /*
        List<HistoriqueConnexion> logsBDD = historiqueDAO.getAll();

        for (HistoriqueConnexion h : logsBDD) {
            model.addRow(new Object[]{
                    h.getNom() + " " + h.getPrenom(),
                    h.getDateHeure().toString()
            });
        }
        */
        // =======================================================
    }

    // --- Données fictives en attendant la BDD ---
    private List<ConnexionLog> getFakeLogs() {
        List<ConnexionLog> list = new ArrayList<>();

        list.add(new ConnexionLog("Claire", "Martin", "16/04/2026 14:32"));
        list.add(new ConnexionLog("Jean", "Dupont", "16/04/2026 13:10"));
        list.add(new ConnexionLog("Luc", "Durand", "15/04/2026 18:45"));
        list.add(new ConnexionLog("Sophie", "Morel", "15/04/2026 09:22"));

        return list;
    }

    // --- Classe modèle fictive ---
    class ConnexionLog {
        private String nom;
        private String prenom;
        private String date;

        public ConnexionLog(String nom, String prenom, String date) {
            this.nom = nom;
            this.prenom = prenom;
            this.date = date;
        }

        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getDate() { return date; }
    }
}
