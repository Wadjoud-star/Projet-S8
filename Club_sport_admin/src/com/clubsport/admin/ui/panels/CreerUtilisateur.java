package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class CreerUtilisateur extends JFrame {

    private JTextField txtNom;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse; // champ mot de passe
    private JComboBox<String> comboRole;
    private JTextField txtPhoto; // champ pour la photo

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public CreerUtilisateur() {

        // --- PARAMÈTRES DE LA FENÊTRE ---
        setTitle("Créer un utilisateur");
        setSize(500, 500);
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();// gestionnaire de placements en grille
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- CHAMP NOM ---
        gbc.gridx = 0; gbc.gridy = 0;// texte colonne 0 ligne 0
        panel.add(new JLabel("Nom :"), gbc);

        txtNom = new JTextField();
        txtNom.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;// texte dans la colonne 1 
        gbc.weightx = 1.0;// permet au champ de s'élargir si la fenetre s'agrandie (0: non ,1 oui )
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

        // --- CHAMP MOT DE PASSE ---
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe :"), gbc);

        txtMotDePasse = new JPasswordField();
        txtMotDePasse.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(txtMotDePasse, gbc);

        // --- CHAMP ROLE ---
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Rôle :"), gbc);

        comboRole = new JComboBox<>(new String[]{
                "elu", "president", "entraineur", "sportif", "admin"
        });
        comboRole.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(comboRole, gbc);

        // --- PHOTO IDENTITÉ ---
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Photo identité :"), gbc);

        txtPhoto = new JTextField();
        txtPhoto.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(txtPhoto, gbc);

        // --- BOUTON CHOISIR FICHIER ---
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnChoisirFichier = new JButton("Choisir un fichier");
        btnChoisirFichier.setBackground(new Color(200, 200, 200));// couleur gris clair
        btnChoisirFichier.setFocusPainted(false);// désactives le contour bleu
        btnChoisirFichier.setPreferredSize(new Dimension(150, 30));// taille du bouton 

        btnChoisirFichier.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner une photo d'identité");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String chemin = fileChooser.getSelectedFile().getAbsolutePath();
                txtPhoto.setText(chemin); // on remplit automatiquement le champ
            }
        });

        panel.add(btnChoisirFichier, gbc);

        add(panel, BorderLayout.CENTER);

        //  BAS de la page avec les boutons 
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

    // --- HASHAGE SHA-256 ---
    private String hashSHA256(String motDePasse) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // On crée un utilisateur
    private void creerUtilisateur() {

        String nom = txtNom.getText().trim();// trim sert a enlever les espaces
        String email = txtEmail.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword()).trim();
        String role = (String) comboRole.getSelectedItem();
        String photo = txtPhoto.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Attention à remplir toutes les informations");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email invalide.");
            return;
        }

        if (!photo.isEmpty() && !new File(photo).exists()) {
            JOptionPane.showMessageDialog(this, "Le fichier photo n'existe pas.");
            return;
        }

        // Hashage du mot de passe
        String hash = hashSHA256(motDePasse);

        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setEmail(email);
        u.setRole(role);
        u.setPhotoIdentite(photo.isEmpty() ? null : photo);
        u.setStatutVerification("EN_ATTENTE");
        u.setMotDePasseHash(hash);

        boolean ok = utilisateurDAO.ajouterUtilisateur(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
