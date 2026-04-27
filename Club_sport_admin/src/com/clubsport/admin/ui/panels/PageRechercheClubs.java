package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// =======================================================
// MODIF BDD (commenté) : import du DAO
// import com.clubsport.dao.ClubDAO;
// import com.clubsport.model.Club;
// =======================================================

public class PageRechercheClubs extends JFrame {

    private JComboBox<String> comboCritere;
    private JTable table;
    private DefaultTableModel model;

    // =======================================================
    // MODIF BDD (commenté) : DAO
    // private ClubDAO clubDAO = new ClubDAO();
    // =======================================================

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
        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout());

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

        JButton btnChercher = new JButton("Chercher");
        btnChercher.addActionListener(e -> rechercherClubs());

        zoneRecherche.add(labelCritere);
        zoneRecherche.add(comboCritere);
        zoneRecherche.add(btnChercher);

        panelCentre.add(zoneRecherche, BorderLayout.NORTH);

        // --- TABLEAU DES RÉSULTATS ---
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

        // =======================================================
        // VERSION ACTUELLE : données fictives (affichage comme avant)
        // =======================================================
        List<ClubFake> clubs = getFakeClubs();

        for (ClubFake c : clubs) {
            model.addRow(new Object[]{
                    c.id, c.nom, c.adresse, c.codePostal, c.nbLicencies, c.nbHommes, c.nbFemmes
            });
        }

        // =======================================================
        // MODIF BDD (commenté) : conversion du critère → colonne SQL
        /*
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

        // MODIF BDD (commenté) : récupération depuis MySQL
        List<Club> clubsBDD = clubDAO.rechercherPar(colonne, "valeurRecherchee");

        for (Club c : clubsBDD) {
            model.addRow(new Object[]{
                    c.getId(), c.getNom(), c.getAdresse(), c.getCodePostal(),
                    c.getNbLicencies(), c.getNbHommes(), c.getNbFemmes()
            });
        }
        */
        // =======================================================
    }

    // --- Données fictives en attendant la BDD ---
    private List<ClubFake> getFakeClubs() {
        List<ClubFake> list = new ArrayList<>();

        list.add(new ClubFake(1, "Club de Foot", "12 rue du Stade", "76000", 120, 80, 40));
        list.add(new ClubFake(2, "Club de Tennis", "5 avenue des Sports", "76100", 60, 35, 25));
        list.add(new ClubFake(3, "Club de Natation", "Piscine Municipale", "76200", 90, 50, 40));

        return list;
    }

    // --- Classe modèle fictive ---
    class ClubFake {
        int id;
        String nom;
        String adresse;
        String codePostal;
        int nbLicencies;
        int nbHommes;
        int nbFemmes;

        public ClubFake(int id, String nom, String adresse, String codePostal,
                        int nbLicencies, int nbHommes, int nbFemmes) {
            this.id = id;
            this.nom = nom;
            this.adresse = adresse;
            this.codePostal = codePostal;
            this.nbLicencies = nbLicencies;
            this.nbHommes = nbHommes;
            this.nbFemmes = nbFemmes;
        }
    }
}
