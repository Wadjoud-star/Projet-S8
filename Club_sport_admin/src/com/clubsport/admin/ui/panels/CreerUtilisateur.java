package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class CreerUtilisateur extends JFrame {

    private JTextField txtNom;
    private JTextField txtEmail;
    private JComboBox<String> comboRole;
    private JTextField txtPhoto; // champ pour la photo

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public CreerUtilisateur() {

        // --- PARAMÈTRES DE LA FENÊTRE ---
        setTitle("Créer un utilisateur");
        setSize(500, 450);
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
        txtNom.setPreferredSize(new Dimension(250, 28));
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
        panel.add(new JLabel("Rôle :"), gbc);

        comboRole = new JComboBox<>(new String[]{
                "elu", "president", "entraineur", "sportif", "admin"
        });
        comboRole.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(comboRole, gbc);

        // --- PHOTO IDENTITÉ ---
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Photo identité :"), gbc);

        txtPhoto = new JTextField();
        txtPhoto.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(txtPhoto, gbc);

        // --- BOUTON CHOISIR FICHIER ---
        gbc.gridx = 1; gbc.gridy = 4;
        JButton btnChoisirFichier = new JButton("Choisir un fichier");
        btnChoisirFichier.setBackground(new Color(200, 200, 200));
        btnChoisirFichier.setFocusPainted(false);
        btnChoisirFichier.setPreferredSize(new Dimension(150, 30));

        btnChoisirFichier.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner une photo d'identité");

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String chemin = fileChooser.getSelectedFile().getAbsolutePath();
                txtPhoto.setText(chemin); // on remplit automatiquement le champ
            }
        });

        panel.add(btnChoisirFichier, gbc);

        add(panel, BorderLayout.CENTER);

        // --- PANEL BAS AVEC LES BOUTONS ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnCreer = new JButton("Créer");
        btnCreer.setBackground(new Color(0, 120, 215));
        btnCreer.setForeground(Color.WHITE);
        btnCreer.setPreferredSize(new Dimension(120, 35));

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBackground(new Color(0, 120, 215));
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setPreferredSize(new Dimension(120, 35));

        bottomPanel.add(btnCreer);
        bottomPanel.add(btnAnnuler);

        add(bottomPanel, BorderLayout.SOUTH);

        btnCreer.addActionListener(e -> creerUtilisateur());
        btnAnnuler.addActionListener(e -> dispose());
    }

    // On crée un utilisateur
    private void creerUtilisateur() {

        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();
        String role = (String) comboRole.getSelectedItem();
        String photo = txtPhoto.getText().trim();

        if (nom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Attention à remplir toutes les informations");
            return;
        }

        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setEmail(email);
        u.setRole(role);
        u.setPhotoIdentite(photo.isEmpty() ? null : photo);
        u.setStatutVerification("EN_ATTENTE");

        boolean ok = utilisateurDAO.ajouterUtilisateur(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
