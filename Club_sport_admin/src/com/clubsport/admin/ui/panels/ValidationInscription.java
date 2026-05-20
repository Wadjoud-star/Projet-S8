package com.clubsport.admin.ui.panels;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.dao.UtilisateurDAO; // ← nécessaire pour la mise à jour BDD

import javax.swing.*;
import java.awt.*;

public class ValidationInscription extends JFrame {

    private Utilisateur utilisateur; // l'utilisateur dont on affiche les infos
    private Runnable parentRefreshCallback; // rafraîchir PageGestionComptes

    // --- Constructeur : on reçoit l'utilisateur + un callback pour rafraîchir la page parent ---
    public ValidationInscription(Utilisateur utilisateur, Runnable refreshCallback) {
        this.utilisateur = utilisateur;
        this.parentRefreshCallback = refreshCallback; // on stocke le callback

        setTitle("Validation de l'identité");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TITRE EN HAUT ---
        JLabel titre = new JLabel("Validation de l'identité", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titre, BorderLayout.NORTH);

        //  CENTRE AVEC LES INFOS ---
        JPanel panelInfos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Texte explicatif en haut 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelInfos.add(new JLabel("Informations du compte sélectionner  :"), gbc);

        gbc.gridwidth = 1;

        //Nom
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInfos.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getNom()), gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInfos.add(new JLabel("Email :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getEmail()), gbc);

        //  Rôle 
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelInfos.add(new JLabel("Rôle :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getRole()), gbc);

        //  Statut de vérification
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelInfos.add(new JLabel("Statut vérification :"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboStatut = new JComboBox<>(new String[]{
                "EN_ATTENTE", "VALIDE", "REFUSE" //  liste des mots dans la combobox
        });
        comboStatut.setSelectedItem(utilisateur.getStatutVerification());
        panelInfos.add(comboStatut, gbc);

        // pour  Justificatif
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelInfos.add(new JLabel("Justificatif :"), gbc);

        gbc.gridx = 1;
        JButton btnVoirJustificatif = new JButton("Voir");
        panelInfos.add(btnVoirJustificatif, gbc);

        add(panelInfos, BorderLayout.CENTER);

        // Bas de la page avec les boutons
        JPanel bottom = new JPanel();

        JButton btnValiderInscription = new JButton("Valider inscription"); 
        JButton btnFermer = new JButton("Fermer");

        bottom.add(btnValiderInscription);
        bottom.add(btnFermer);

        add(bottom, BorderLayout.SOUTH);

        // Fermer la fenetre
        btnFermer.addActionListener(e -> dispose());

        // pour  Voir justificatif  
        btnVoirJustificatif.addActionListener(e -> {// on ajoute un ActionListener au bouton btnVoirJustificatif


            String chemin = utilisateur.getPhotoIdentite();//récupère depuis l’objet utilisateur le chemin du fichier

            if (chemin == null || chemin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun justificatif disponible.");
                return;
            }

            try {
                ImageIcon icon;// déclare une variable qui contiendra l’image à afficher.

                // !chemin.startsWith("http") pour pointer sur  un fichier local commencant par https....
                if (!chemin.startsWith("http") && chemin.startsWith("uploads/")) {
                    chemin = "../Club_sport/" + chemin;// le chemin de l'image 
                }

                java.io.File file = new java.io.File(chemin);// java.io : bibliothèque de java
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "Le fichier n'existe pas :\n" + chemin,
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                icon = new ImageIcon(chemin);//On crée un objet ImageIcon avec chemin du fichier et la fenetre charge l’image en mémoire.

                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(400, -1, Image.SCALE_SMOOTH);// On redimensionne l’image 
                icon = new ImageIcon(scaled);//  recrée un nouvel ImageIcon avec l’image redimensionné.

                JLabel labelImage = new JLabel(icon);

                JOptionPane.showMessageDialog(this, labelImage, "Justificatif", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de charger l'image.\nChemin : " + chemin,
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // pour Valider l'inscription 
        btnValiderInscription.addActionListener(e -> {

            // on passe le statut en valide
            String nouveauStatut = "VALIDE";

            // Mise à jour dans la base
            boolean ok = UtilisateurDAO.updateStatutVerification(utilisateur.getId(), nouveauStatut);

            if (ok) {
                // On met aussi à jour l'objet utilisateur
                utilisateur.setStatutVerification(nouveauStatut);

                JOptionPane.showMessageDialog(this,
                        "L'inscription a été validée avec succès !");

                // Rafraîchir la page précédente
                if (parentRefreshCallback != null) {
                    parentRefreshCallback.run();
                }

                dispose(); // ferme la fenêtre
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
