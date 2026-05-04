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

    private JTextField txtDate; // champ pour saisir la date
    private JButton btnFiltrer; // bouton filtrer

    // connexion au DAO Historique de connexions 
    private HistoriqueConnexionDAO historiqueDAO = new HistoriqueConnexionDAO();

    public PageHistorique() {
        setTitle("Historique des connexions");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //BLoc du haut 
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Voici les dernières connexions au site :");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(titre);

        // Pannel permettant de filtrer par date les connexions 
        JPanel filtrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        filtrePanel.add(new JLabel("Entrez une date (AAAA-MM-JJ) :"));
        txtDate = new JTextField(10);
        filtrePanel.add(txtDate);
        btnFiltrer = new JButton("Sélectionner");
        btnFiltrer.setBackground(new Color(0, 120, 215));
        btnFiltrer.setForeground(Color.WHITE);
        btnFiltrer.setFocusPainted(false);
        btnFiltrer.setPreferredSize(new Dimension(110, 30));

        filtrePanel.add(btnFiltrer);// ajout a le panel du bouton filtre 

        topPanel.add(filtrePanel);// ajout dans le boutondans le panel  du haut 

        add(topPanel, BorderLayout.NORTH);

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

        // Action du bouton Filtrer
        btnFiltrer.addActionListener(e -> filtrerParDate());
    }

    private void chargerHistorique() {
        model.setRowCount(0);

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

    private void filtrerParDate() {
        String date = txtDate.getText().trim();// on recupere les infos rentrées par l'utilisateur et on enleve les espaces 

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une date.");
            return;
        }

        model.setRowCount(0);

        List<HistoriqueConnexion> logsBDD = historiqueDAO.getHistoriqueParDate(date);

        if (logsBDD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune connexion trouvée pour cette date.");
            return;
        }

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
