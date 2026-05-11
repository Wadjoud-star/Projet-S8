package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

import com.clubsport.admin.dao.RechercheDAO; // nouveau DAO
import com.clubsport.admin.model.ResultatRecherche; // nouveau modèle

public class PageRechercheClubs extends JFrame {

    // --- Boutons radio pour les critères ---
    private JRadioButton rbCommune, rbCodePostal, rbLicencies, rbFederation;

    private JTextField txtRecherche; // champ de recherche de saisie
    private JTable table; // tableau qui affiche les résultats 
    private DefaultTableModel model; // modèle du tableau

    private JComboBox<String> cbTri; // menu déroulant pour trier

    private RechercheDAO rechercheDAO = new RechercheDAO(); // objet de la base de données (nouveau DAO)

    public PageRechercheClubs() {
        setTitle("Recherche de clubs"); // titre de la fenêtre
        setSize(900, 600); // taille de la fenêtre 
        setLocationRelativeTo(null); // position de la page 
        setLayout(new BorderLayout());

        // --- TITRE ---
        JLabel titre = new JLabel("Recherche de clubs");
        titre.setFont(new Font("Arial", Font.BOLD, 20)); // police du texte 
        titre.setHorizontalAlignment(SwingConstants.CENTER); // centré horizontalement 
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titre, BorderLayout.NORTH); // titre placé en haut de la fenêtre

        // --- PANEL GLOBAL HAUT ---
        JPanel panelHaut = new JPanel(); // Panel qui contient les boutons et la zone chercher
        panelHaut.setLayout(new BoxLayout(panelHaut, BoxLayout.Y_AXIS)); // les éléments se superposent 

        // --- BOUTONS CRITÈRES ---
        JPanel panelCriteres = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // boutons centrés 

        // Attribue un nom à chaque bouton 
        rbCommune = new JRadioButton("Commune");
        rbCodePostal = new JRadioButton("Code postal");
        rbLicencies = new JRadioButton("Licenciés minimum"); // recherche minimum
        rbFederation = new JRadioButton("Fédération");

        // creation des boutons (radio) 
        ButtonGroup group = new ButtonGroup();
        group.add(rbCommune);
        group.add(rbCodePostal);
        group.add(rbLicencies);
        group.add(rbFederation);

        // un seul bouton selectionner 
        rbCommune.setSelected(true); // par défaut le bouton commune est sélectionné 

        panelCriteres.add(rbCommune);
        panelCriteres.add(rbCodePostal);
        panelCriteres.add(rbLicencies);
        panelCriteres.add(rbFederation);

        panelHaut.add(panelCriteres);

        // --- ZONE DE RECHERCHE ---
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        txtRecherche = new JTextField(15);
        JButton btnChercher = new JButton("Chercher");

        // si clique sur bouton cela fait appel à la fonction de recherche
        btnChercher.addActionListener(e -> rechercherClubs());

        // --- TRI ---
        JLabel lblTrier = new JLabel("Trier par :");

        String[] optionsTri = {
                "Fédération", "Commune", "Région", "Code postal",
                "Total licenciés", "Hommes", "Femmes",
                "Nb clubs", "Établissements", "Structures"
        };

        cbTri = new JComboBox<>(optionsTri);

        panelRecherche.add(txtRecherche);
        panelRecherche.add(btnChercher);
        panelRecherche.add(lblTrier);
        panelRecherche.add(cbTri);

        panelHaut.add(panelRecherche);

        add(panelHaut, BorderLayout.NORTH);

        //  TABLEAU 
        // colonnes adaptées aux résultats statistiques
        String[] colonnes = {
                "Fédération", "Commune", "Région", "Code postal",
                "Total licenciés", "Hommes", "Femmes",
                "Nb clubs", "Établissements", "Structures"
        };

        model = new DefaultTableModel(colonnes, 0); // tableau vide au départ
        table = new JTable(model);
        table.setRowHeight(28); // grandeur des lignes 

        add(new JScrollPane(table), BorderLayout.CENTER); // permet de dérouler le tableau qui est centré
    }

    private void rechercherClubs() {

        model.setRowCount(0); // on vide le tableau avant d'ajouter les résultats

        String valeur = txtRecherche.getText().trim();
        if (valeur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur.");
            return; // récupère le texte entré et met un message d'erreur si rien saisi
        }

        // colonne par défaut
        String colonne = "commune";

        // détermine quel bouton radio est sélectionné
        if (rbCommune.isSelected()) colonne = "commune";
        if (rbCodePostal.isSelected()) colonne = "code_postal";
        if (rbLicencies.isSelected()) colonne = "total_licencies"; // recherche minimum
        if (rbFederation.isSelected()) colonne = "federation";

        // Récupération MySQL via RechercheDAO
        List<ResultatRecherche> resultats = rechercheDAO.rechercher(colonne, valeur);

        // Ajout des résultats dans le tableau
        for (ResultatRecherche r : resultats) {
            model.addRow(new Object[]{
                    r.getFederation(),
                    r.getCommune(),
                    r.getRegion(),
                    r.getCodePostal(),
                    r.getTotalLicencies(),
                    r.getHommes(),
                    r.getFemmes(),
                    r.getNbClubs(),
                    r.getNbEtablissements(),
                    r.getTotalStructures()
            });
        }

        // --- TRI DES RÉSULTATS ---
        int colonneTri = cbTri.getSelectedIndex(); // même ordre que les colonnes du tableau

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        sorter.toggleSortOrder(colonneTri); // tri croissant
    }
}
