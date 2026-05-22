package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.dao.AuditDAO; // ➕ ajout pour l’audit
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

// --- IMPORT BCRYPT ---
import org.mindrot.jbcrypt.BCrypt;

public class CreerUtilisateur extends JFrame {

    private JTextField txtNom;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse; // champ mot de passe
    private JComboBox<String> comboRole;
    private JTextField txtPhoto; // champ pour la photo

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private AuditDAO auditDAO = new AuditDAO(); // ➕ DAO pour enregistrer l’action dans l’audit

    public CreerUtilisateur() {

        // Modele de la fenetre 
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

        // Champ pour la photo
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Photo identité :"), gbc);

        txtPhoto = new JTextField();
        txtPhoto.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        panel.add(txtPhoto, gbc);

        // Bouton pour selectionner un fichier 
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnChoisirFichier = new JButton("Choisir un fichier");
        btnChoisirFichier.setBackground(new Color(200, 200, 200));// couleur gris clair
        btnChoisirFichier.setFocusPainted(false);// désactives le contour bleu
        btnChoisirFichier.setPreferredSize(new Dimension(150, 30));// taille du bouton 

        btnChoisirFichier.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner une photo d'identité");

            //Pour les pdf et autre fichier du style 
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images ou PDF", "jpg", "jpeg", "png", "pdf"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String chemin = fileChooser.getSelectedFile().getAbsolutePath();
                txtPhoto.setText(chemin); // on remplit automatiquement le champ
            }
        });

        panel.add(btnChoisirFichier, gbc);

        add(panel, BorderLayout.CENTER);

        //Le bas de la page avec tous les boutons 
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

    // --- CRÉATION UTILISATEUR ---
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

        // Methode pour hacher le mot de passe sur le meme modele que sur le site web
        String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());

        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setEmail(email);
        u.setRole(role);
        u.setPhotoIdentite(photo.isEmpty() ? null : photo);
        u.setStatutVerification("EN_ATTENTE");
        u.setMotDePasseHash(hash); // on enregistre le hash BCrypt

        boolean ok = utilisateurDAO.ajouterUtilisateur(u);

        if (ok) {

            // ➕ ENREGISTREMENT DANS L’AUDIT
            // ⚠️ Remplacer 1 par l’ID réel de l’admin connecté
            int idAdmin = 1;
            String details = "Création de l’utilisateur : " + nom;
            auditDAO.enregistrerAction(idAdmin, "Création utilisateur", details);

            JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
