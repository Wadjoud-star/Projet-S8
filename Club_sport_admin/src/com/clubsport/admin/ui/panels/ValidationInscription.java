package com.clubsport.admin.ui.panels;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.dao.UtilisateurDAO; // ← nécessaire pour la mise à jour BDD

import javax.swing.*;
import java.awt.*;

public class ValidationInscription extends JFrame {

    private Utilisateur utilisateur; // l'utilisateur dont on affiche les infos
    private Runnable parentRefreshCallback; // ← callback pour rafraîchir PageGestionComptes

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

        // --- PANEL CENTRAL AVEC LES INFOS ---
        JPanel panelInfos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Texte explicatif
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelInfos.add(new JLabel("Informations du compte :"), gbc);

        gbc.gridwidth = 1;

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
                "EN_ATTENTE", "VALIDE", "REFUSE" // ← on ajoute VALIDE dans la liste
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

        // --- PANEL BAS AVEC LES BOUTONS ---
        JPanel bottom = new JPanel();

        JButton btnValiderInscription = new JButton("Valider inscription"); // ← NOUVEAU BOUTON
        JButton btnFermer = new JButton("Fermer");

        bottom.add(btnValiderInscription);
        bottom.add(btnFermer);

        add(bottom, BorderLayout.SOUTH);

        // --- ACTION : Fermer ---
        btnFermer.addActionListener(e -> dispose());

        // --- ACTION : Voir justificatif ---
        btnVoirJustificatif.addActionListener(e -> {

            String chemin = utilisateur.getPhotoIdentite();

            if (chemin == null || chemin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun justificatif disponible.");
                return;
            }

            try {
                ImageIcon icon;

                // Correction du chemin relatif
                if (!chemin.startsWith("http") && chemin.startsWith("uploads/")) {
                    chemin = "../Club_sport/" + chemin;
                }

                java.io.File file = new java.io.File(chemin);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "Le fichier n'existe pas :\n" + chemin,
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                icon = new ImageIcon(chemin);

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

        // --- ACTION : Valider l'inscription ---
        btnValiderInscription.addActionListener(e -> {

            // On force le statut à VALIDE
            String nouveauStatut = "VALIDE";

            // Mise à jour dans la base
            boolean ok = UtilisateurDAO.updateStatutVerification(utilisateur.getId(), nouveauStatut);

            if (ok) {
                // On met aussi à jour l'objet en mémoire
                utilisateur.setStatutVerification(nouveauStatut);

                JOptionPane.showMessageDialog(this,
                        "L'inscription a été validée avec succès !");

                // Rafraîchir la page GestionDeCompte
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
