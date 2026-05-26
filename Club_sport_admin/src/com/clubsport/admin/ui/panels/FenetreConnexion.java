package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class FenetreConnexion extends JDialog {

    public FenetreConnexion(test1 parent) {
        super(parent, "Connexion administrateur", true); // fenêtre modale
        setSize(350, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblEmail = new JLabel("Email :");
        JTextField txtEmail = new JTextField();

        JLabel lblMdp = new JLabel("Mot de passe :");
        JPasswordField txtMdp = new JPasswordField();

        JButton btnValider = new JButton("Valider");

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblMdp);
        panel.add(txtMdp);
        panel.add(new JLabel()); // vide pour l'espacement
        panel.add(btnValider);

        add(panel);

        // ACTION DU BOUTON VALIDER 
        btnValider.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String mdp = new String(txtMdp.getPassword()).trim();

            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur u = dao.verifierConnexion(email, mdp); // mdp en clair

            if (u != null) {
                parent.setUtilisateurConnecte(u);
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Accès refusé.");
            }
        });

    }}
