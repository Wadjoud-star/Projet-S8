package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class CreerUtilisateur extends JFrame {

    private JTextField txtNom;
    private JTextField txtEmail;
    private JComboBox<String> comboRole;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public CreerUtilisateur() {

        // --- PARAMÈTRES DE LA FENÊTRE ---
        setTitle("Créer un utilisateur");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TEXTE D'INTRODUCTION ---
        JLabel lblIntro = new JLabel(
                "<html><div style='text-align:center;'>Vous allez créer un utilisateur.<br/>" +
                "Veuillez remplir les champs suivants :</div></html>"
        );
        lblIntro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIntro.setHorizontalAlignment(SwingConstants.CENTER);
        lblIntro.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblIntro, BorderLayout.NORTH);

        // --- PANEL CENTRAL AVEC LES CHAMPS ---
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- CHAMP NOM ---
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom :"), gbc);

        txtNom = new JTextField();
        txtNom.setPreferredSize(new Dimension(250, 28)); // champ plus large
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtNom, gbc);

        // --- CHAMP EMAIL ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Email :"), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtEmail, gbc);

        // --- CHAMP ROLE ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Rôle :"), gbc);

        comboRole = new JComboBox<>(new String[]{
                "elu", "president", "entraineur", "sportif", "admin"
        });
        comboRole.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboRole, gbc);

        add(panel, BorderLayout.CENTER);

        // --- PANEL BAS AVEC LES BOUTONS ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // --- Bouton Créer ---
        JButton btnCreer = new JButton("Créer");
        btnCreer.setBackground(new Color(0, 120, 215)); // bleu harmonisé
        btnCreer.setForeground(Color.WHITE);
        btnCreer.setFocusPainted(false);
        btnCreer.setPreferredSize(new Dimension(120, 35));

        // --- Bouton Annuler ---
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBackground(new Color(0, 120, 215)); // même couleur que Créer
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setPreferredSize(new Dimension(120, 35));

        bottomPanel.add(btnCreer);
        bottomPanel.add(btnAnnuler);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- ACTION : CRÉER L'UTILISATEUR ---
        btnCreer.addActionListener(e -> creerUtilisateur());

        // --- ACTION : ANNULER ET FERMER LA FENÊTRE ---
        btnAnnuler.addActionListener(e -> dispose());
    }

    // --- MÉTHODE POUR CRÉER L'UTILISATEUR ---
    private void creerUtilisateur() {

        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();
        String role = (String) comboRole.getSelectedItem();

        // Vérification des champs
        if (nom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        // Création de l'objet utilisateur
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setEmail(email);
        u.setRole(role);

        // Insertion en base
        boolean ok = utilisateurDAO.ajouterUtilisateur(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");
            dispose(); // ferme la fenêtre
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
