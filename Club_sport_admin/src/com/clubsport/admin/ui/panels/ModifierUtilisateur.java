package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ModifierUtilisateur extends JFrame {

    private JTextField txtNom;
    private JTextField txtEmail;
    private JComboBox<String> comboRole;

    private Utilisateur utilisateur;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public ModifierUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Modifier un utilisateur");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Message d'introduction
        JLabel lblIntro = new JLabel("Vous allez modifier les informations de cet utilisateur");
        lblIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIntro.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblIntro);
        panel.add(Box.createVerticalStrut(15));

        // Champ Nom
        panel.add(new JLabel("Nom :"));
        txtNom = new JTextField(utilisateur.getNom());
        panel.add(txtNom);
        panel.add(Box.createVerticalStrut(10));

       

        // Pour le mail
        panel.add(new JLabel("Email :"));//Ajoute une ligne 
        txtEmail = new JTextField(utilisateur.getEmail());// pour recuperer le nouveau mail
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(10));

        //Pour le role: 
        panel.add(new JLabel("Rôle :"));// on ajoute une nouvelle ligne
        comboRole = new JComboBox<>(new String[]{"admin", "Elus", "utilisateur"});
        comboRole.setSelectedItem(utilisateur.getRole());// permet a l'admn de saisir
        panel.add(comboRole);// on ajoute la combobox au panel
        panel.add(Box.createVerticalStrut(20));

        // Boutons qui s'affiche sur notre page 
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnValider = new JButton("Valider");
        JButton btnAnnuler = new JButton("Annuler");

        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        panel.add(panelBoutons);

  // Action bouton Valider qui va enregistrer les informations dans la base 
        btnValider.addActionListener(e -> {
            utilisateur.setNom(txtNom.getText());
            utilisateur.setEmail(txtEmail.getText());
            utilisateur.setRole(comboRole.getSelectedItem().toString());

            boolean ok = utilisateurDAO.modifierUtilisateur(utilisateur);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action bouton Annuler qui ferme la page 
        btnAnnuler.addActionListener(e -> dispose());

        add(panel);
    }
    public static void main(String[] args) {
        // Création d'un utilisateur fictif pour tester l'interface
        Utilisateur u = new Utilisateur(
                1,                      // id fictif
                "Dupont",               // nom
                "dupont@example.com",   // email
                "hash",                 // mot de passe hash fictif
                "admin"                 // rôle
        );

        new ModifierUtilisateur(u).setVisible(true);
    }

}
