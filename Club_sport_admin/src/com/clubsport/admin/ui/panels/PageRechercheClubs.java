package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.clubsport.admin.dao.ClubDAO;
import com.clubsport.admin.model.Club;

public class PageRechercheClubs extends JFrame {

    private JComboBox<String> comboCritere;
    private JTextField txtRecherche;
    private JTable table;
    private DefaultTableModel model;

    // DAO réel
    private ClubDAO clubDAO = new ClubDAO();

    public PageRechercheClubs() {
        setTitle("Recherche de clubs");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TEXTE EN HAUT ---
        JLabel titre = new JLabel("Vous allez pouvoir chercher des clubs");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titre, BorderLayout.NORTH);

        // --- PANEL GLOBAL CENTRE ---
        JPanel panelCentre = new JPanel(new BorderLayout());

        // --- ZONE DE RECHERCHE ---
        JPanel zoneRecherche = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));

        JLabel labelCritere = new JLabel("Quel type de clubs cherchez-vous ?");
        labelCritere.setFont(new Font("Arial", Font.PLAIN, 14));

        comboCritere = new JComboBox<>(new String[]{
                "ID",
                "Nom",
                "Adresse",
                "Code postal",
                "Nombre de licenciés",
                "Nombre d'hommes",
                "Nombre de femmes"
        });

        txtRecherche = new JTextField(15);

        JButton btnChercher = new JButton("Chercher");
        btnChercher.addActionListener(e -> rechercherClubs());

        zoneRecherche.add(labelCritere);
        zoneRecherche.add(comboCritere);
        zoneRecherche.add(txtRecherche);
        zoneRecherche.add(btnChercher);

        panelCentre.add(zoneRecherche, BorderLayout.NORTH);

        // --- TABLEAU ---
        String[] colonnes = {"ID", "Nom", "Adresse", "Code postal", "Licenciés", "Hommes", "Femmes"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        panelCentre.add(new JScrollPane(table), BorderLayout.CENTER);

        add(panelCentre, BorderLayout.CENTER);
    }

    private void rechercherClubs() {
        model.setRowCount(0);

        String critere = (String) comboCritere.getSelectedItem();
        String valeur = txtRecherche.getText().trim();

        // Conversion critère → colonne SQL
        String colonne = switch (critere) {
            case "ID" -> "id_club";
            case "Nom" -> "nom";
            case "Adresse" -> "adresse";
            case "Code postal" -> "code_postal";
            case "Nombre de licenciés" -> "nb_licencies";
            case "Nombre d'hommes" -> "nb_hommes";
            case "Nombre de femmes" -> "nb_femmes";
            default -> "nom";
        };

        // Récupération depuis MySQL
        List<Club> clubsBDD = clubDAO.rechercherPar(colonne, valeur);

        for (Club c : clubsBDD) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNom(),
                    c.getAdresse(),
                    c.getCodePostal(),
                    c.getNbLicencies(),
                    c.getNbHommes(),
                    c.getNbFemmes()
            });
        }
    }
}
