package com.clubsport.admin.ui.panels;

import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ValidationInscription extends JFrame {

    private Utilisateur utilisateur; // l'utilisateur dont on affiche les infos et on stocke les infos 

    // --- Constructeur : on reçoit l'utilisateur sélectionné ---
    public ValidationInscription(Utilisateur utilisateur) {
        this.utilisateur = utilisateur; // on affecte à l'attribut de la classe 

        setTitle("Validation de l'identité");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TITRE EN HAUT ---
        JLabel titre = new JLabel("Validation de l'identité", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titre, BorderLayout.NORTH);

        // --- PANEL CENTRAL AVEC LES INFOS ---
        JPanel panelInfos = new JPanel(new GridBagLayout()); // panel flexible 
        GridBagConstraints gbc = new GridBagConstraints(); // pour configurer la position et le style des composants
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Texte explicatif
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // label a deux colonnes
        panelInfos.add(new JLabel("Informations du compte :"), gbc);

        gbc.gridwidth = 1; // composants suivants

        // --- Nom ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInfos.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getNom()), gbc);

        // --- Email ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInfos.add(new JLabel("Email :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getEmail()), gbc);

        // --- Rôle ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelInfos.add(new JLabel("Rôle :"), gbc);

        gbc.gridx = 1;
        panelInfos.add(new JLabel(utilisateur.getRole()), gbc);

        // --- Statut de vérification ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelInfos.add(new JLabel("Statut vérification :"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboStatut = new JComboBox<>(new String[]{
                "EN_ATTENTE", "VERIFIE", "REFUSE"
        });
        comboStatut.setSelectedItem(utilisateur.getStatutVerification());
        panelInfos.add(comboStatut, gbc);

        // --- Justificatif ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelInfos.add(new JLabel("Justificatif :"), gbc);

        gbc.gridx = 1;
        JButton btnVoirJustificatif = new JButton("Voir");
        panelInfos.add(btnVoirJustificatif, gbc);

        add(panelInfos, BorderLayout.CENTER);

        // --- BOUTON FERMER ---
        JButton btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnFermer);

        add(bottom, BorderLayout.SOUTH);

        // --- ACTION : Voir justificatif ---
        btnVoirJustificatif.addActionListener(e -> {

            // récupère le chemin de la photo stocké en base (ex: "uploads/12345_image.jpg")
            String chemin = utilisateur.getPhotoIdentite();

            if (chemin == null || chemin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun justificatif disponible.");
                return;
            }

            try {
                ImageIcon icon;

                // --- NOUVEAU : Correction du chemin relatif ---
                // Si le chemin commence par "uploads/", on le relie au dossier du projet Club_sport
                if (!chemin.startsWith("http") && chemin.startsWith("uploads/")) {

                    // Ici on suppose que Club_sport_admin et Club_sport sont côte à côte
                    chemin = "../Club_sport/" + chemin;
                }

                // Vérification que le fichier existe
                java.io.File file = new java.io.File(chemin);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "Le fichier n'existe pas :\n" + chemin,
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chargement de l'image
                icon = new ImageIcon(chemin);

                // Redimensionner proprement
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);

                JLabel labelImage = new JLabel(icon);

                JOptionPane.showMessageDialog(this, labelImage, "Justificatif", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de charger l'image.\nChemin : " + chemin,
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
