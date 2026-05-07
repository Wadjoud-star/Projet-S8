package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ModifierUtilisateur extends JFrame {

    private JTextField txtNom; // champ de texte pour nom et mail
    private JTextField txtEmail;
    private JComboBox<String> comboRole; // champ pour la combobox

    private Utilisateur utilisateur; // utilisateur objet qui va etre modifié
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public ModifierUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur; // construction d'un utilisateur via notre model

        setTitle("Modifier un utilisateur");
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fermer la page

        // Police moderne pour toute la fenêtre
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));

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
        comboRole = new JComboBox<>(new String[]{"admin", "Elus", "utilisateur"});
        comboRole.setSelectedItem(utilisateur.getRole()); // permet à l'admin de saisir
        comboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(comboRole);
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

        // Action bouton Valider qui va enregistrer les informations dans la base
        btnValider.addActionListener(e -> { // addActionListener : code exécuté quand on clique sur “Valider”.
            utilisateur.setNom(txtNom.getText());
            utilisateur.setEmail(txtEmail.getText());
            utilisateur.setRole(comboRole.getSelectedItem().toString());

            boolean ok = utilisateurDAO.modifierUtilisateur(utilisateur); // mise à jour en base
            if (ok) {
                JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
                dispose(); // si true alors message de succès avec fermeture de la fenêtre
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action bouton Annuler qui ferme la page
        btnAnnuler.addActionListener(e -> dispose());

        add(panel);
    }
}
