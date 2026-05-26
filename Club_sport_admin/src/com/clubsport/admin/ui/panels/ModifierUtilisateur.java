package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.dao.AuditDAO; // ➕ ajout pour l’audit
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ModifierUtilisateur extends JFrame {

    // champs que l'admin peut modifier
    private JTextField txtNom; // champ de texte pour nom et mail
    private JTextField txtEmail;
    private JComboBox<String> comboRole; // champ pour la combobox
    private JComboBox<String> comboStatut; // statut de vérification
    private JTextField txtPhoto; // chemin photo identité

    private Utilisateur utilisateur; // utilisateur objet qui va etre modifié
    private Utilisateur adminConnecte; // ➕ admin connecté

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private AuditDAO auditDAO = new AuditDAO(); // ➕ DAO pour enregistrer l’action dans l’audit

    // ➕ constructeur modifié pour recevoir l’admin connecté
    public ModifierUtilisateur(Utilisateur utilisateur, Utilisateur adminConnecte) {
        this.utilisateur = utilisateur; // construction d'un utilisateur via notre model
        this.adminConnecte = adminConnecte; // ➕ on stocke l’admin connecté

        setTitle("Modifier un utilisateur");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fermer la page

        // Police moderne pour toute la fenêtre
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));

        // paneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // composants empilés verticalement
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // marges plus larges

        // Message d'introduction placé au centre
        JLabel lblIntro = new JLabel("Modifier les informations de l'utilisateur");
        lblIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIntro.setFont(new Font("Segoe UI", Font.BOLD, 16)); // police moderne
        panel.add(lblIntro);
        panel.add(Box.createVerticalStrut(20)); // espace vertical

        // Champ Nom
        panel.add(new JLabel("Nom :"));
        txtNom = new JTextField(utilisateur.getNom());
        txtNom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); // hauteur uniforme
        panel.add(txtNom);
        panel.add(Box.createVerticalStrut(12));

        // Pour le mail
        panel.add(new JLabel("Email :")); // Ajoute une ligne
        txtEmail = new JTextField(utilisateur.getEmail()); // pour récupérer le nouveau mail
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(12));

        // Pour le rôle
        panel.add(new JLabel("Rôle :")); // on ajoute une nouvelle ligne

        comboRole = new JComboBox<>(new String[]{
                "admin", "elu", "president", "entraineur", "sportif"
        });

        comboRole.setSelectedItem(utilisateur.getRole()); // permet à l'admin de saisir
        comboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(comboRole);
        panel.add(Box.createVerticalStrut(12));

        // Statut de vérification
        panel.add(new JLabel("Statut de vérification :"));

        comboStatut = new JComboBox<>(new String[]{
                "EN_ATTENTE", "VERIFIE", "REFUSE"
        });

        comboStatut.setSelectedItem(utilisateur.getStatutVerification());
        comboStatut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(comboStatut);
        panel.add(Box.createVerticalStrut(12));

        // Photo identité
        panel.add(new JLabel("Photo d'identité  :"));
        txtPhoto = new JTextField(utilisateur.getPhotoIdentite());
        txtPhoto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(txtPhoto);
        panel.add(Box.createVerticalStrut(10));

        // --- Bouton Voir l'image ---
        JButton btnVoirImage = new JButton("Voir l'image");
        btnVoirImage.setBackground(new Color(200, 200, 200)); // couleur du fond gris clair
        btnVoirImage.setFocusPainted(false);
        btnVoirImage.setPreferredSize(new Dimension(150, 30));

        btnVoirImage.addActionListener(e -> {
            String chemin = txtPhoto.getText().trim(); // recup le texte du champ txtPhoto

            if (chemin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune image à afficher.");
                return;
            }

            try {
                ImageIcon icon;

                // URL ou fichier local
                if (chemin.startsWith("http")) {
                    icon = new ImageIcon(new java.net.URL(chemin));
                } else {
                    icon = new ImageIcon(chemin);
                }

                // Redimensionner proprement
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);

                JLabel labelImage = new JLabel(icon);

                JOptionPane.showMessageDialog(this, labelImage, "Aperçu de l'image", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de charger l'image.\nChemin : " + chemin,
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnVoirImage);
        panel.add(Box.createVerticalStrut(25));

        // Boutons qui s'affichent sur notre page
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnValider = new JButton("Valider");
        JButton btnAnnuler = new JButton("Annuler");

        // Style moderne des boutons 
        btnValider.setBackground(new Color(0, 120, 215));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);
        btnValider.setPreferredSize(new Dimension(110, 35));

        btnAnnuler.setBackground(new Color(200, 200, 200));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setPreferredSize(new Dimension(110, 35));

        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        panel.add(panelBoutons);

     // --- Action bouton Valider ---
        btnValider.addActionListener(e -> {
            // Mise à jour de l'objet utilisateur
            utilisateur.setNom(txtNom.getText());
            utilisateur.setEmail(txtEmail.getText());
            utilisateur.setRole(comboRole.getSelectedItem().toString());
            utilisateur.setStatutVerification(comboStatut.getSelectedItem().toString());
            utilisateur.setPhotoIdentite(txtPhoto.getText());

            boolean ok = utilisateurDAO.modifierUtilisateur(utilisateur); // mise à jour en base

            if (ok) {

                // ➕ ENREGISTREMENT DANS L’AUDIT (sécurisé)
                int idAdmin = (adminConnecte != null) ? adminConnecte.getId() : 20;

                auditDAO.enregistrerAction(
                        idAdmin,
                        "Modification utilisateur",
                        "Modification de l’utilisateur : " + utilisateur.getNom()
                );

                JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
                dispose(); // fermeture de la fenêtre
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action bouton Annuler qui ferme la page
        btnAnnuler.addActionListener(e -> dispose());

        add(panel);
    }
}
