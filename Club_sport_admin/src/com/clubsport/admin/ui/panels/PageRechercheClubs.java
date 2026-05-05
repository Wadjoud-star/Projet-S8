package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.clubsport.admin.dao.ClubDAO;
import com.clubsport.admin.model.Club;

public class PageRechercheClubs extends JFrame {

    private JRadioButton rbFederation, rbAdresse, rbCodePostal, rbLicencies, rbHommes, rbFemmes;// les boutons 
    private JTextField txtRecherche;// champ de recherhe de saisi
    private JTable table;// tableau qui affiche les résultats 
    private DefaultTableModel model;// modele du tableau

    private ClubDAO clubDAO = new ClubDAO();// objet de la base de données

    public PageRechercheClubs() {
        setTitle("Recherche de clubs");// titre de la fenetre
        setSize(750, 550);// taille de la fenetre 
        setLocationRelativeTo(null);// position de la page 
        setLayout(new BorderLayout());

        // --- TITRE ---
        JLabel titre = new JLabel("Recherche de clubs");
        titre.setFont(new Font("Arial", Font.BOLD, 20));// police du texte 
        titre.setHorizontalAlignment(SwingConstants.CENTER);// centré horizontalement 
        titre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titre, BorderLayout.NORTH);// titre placé en haut de la fenêtre

        // --- PANEL GLOBAL HAUT ---
        JPanel panelHaut = new JPanel();// Panel qui contient les boutons et la zone chercher
        panelHaut.setLayout(new BoxLayout(panelHaut, BoxLayout.Y_AXIS));// les éléments se supperposent 

        // --- BOUTONS CRITÈRES ---
        JPanel panelCriteres = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));// boutons centrés 
// attribue un nom à chaque bouton 
        rbFederation = new JRadioButton("Federation");
        rbAdresse = new JRadioButton("Adresse");
        rbCodePostal = new JRadioButton("Code postal");
        rbLicencies = new JRadioButton("Licenciés total");
        rbHommes = new JRadioButton("Licenciés hommes");
        rbFemmes = new JRadioButton("Licenciées femmes");

        ButtonGroup group = new ButtonGroup();
        group.add(rbFederation);
        group.add(rbAdresse);
        group.add(rbCodePostal);
        group.add(rbLicencies);
        group.add(rbHommes);
        group.add(rbFemmes);

        rbFederation.setSelected(true);// par défaut le bouton nom est sélectionner 

        panelCriteres.add(rbFederation);
        panelCriteres.add(rbAdresse);
        panelCriteres.add(rbCodePostal);
        panelCriteres.add(rbLicencies);
        panelCriteres.add(rbHommes);
        panelCriteres.add(rbFemmes);

        panelHaut.add(panelCriteres);

        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        txtRecherche = new JTextField(15);
        JButton btnChercher = new JButton("Chercher");
        btnChercher.addActionListener(e -> rechercherClubs());// si clique sur bouton cela fait appel a la fonction de recherche

        panelRecherche.add(txtRecherche);
        panelRecherche.add(btnChercher);

        panelHaut.add(panelRecherche);

        add(panelHaut, BorderLayout.NORTH);

// le tableau:
        
        // colonne du tableau
        String[] colonnes = {"Nom", "Adresse", "Code postal", "Licenciés", "Licenciés hommes", "Licenciées femmes"};

        model = new DefaultTableModel(colonnes, 1);
        table = new JTable(model);
        table.setRowHeight(28);// grandeur des lignes 

        add(new JScrollPane(table), BorderLayout.CENTER);// permet de dérouler le tableau qui est centré
    }

    private void rechercherClubs() {
        model.setRowCount(1);// on vide le tableau 

        String valeur = txtRecherche.getText().trim();
        if (valeur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur.");
            return;// recupere le texte entre et met un mess d'erreur si rien saisi
        }
        String colonne = "nom_federation";

        if (rbFederation.isSelected()) colonne = "nom_federation";
        if (rbAdresse.isSelected()) colonne = "adresse";
        if (rbCodePostal.isSelected()) colonne = "code_postal";
        if (rbLicencies.isSelected()) colonne = "nb_licencies";
        if (rbHommes.isSelected()) colonne = "nb_hommes";
        if (rbFemmes.isSelected()) colonne = "nb_femmes";

        // Récupération MySQL
        List<Club> clubs = clubDAO.rechercherPar(colonne, valeur);

        for (Club c : clubs) {
            model.addRow(new Object[]{
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
