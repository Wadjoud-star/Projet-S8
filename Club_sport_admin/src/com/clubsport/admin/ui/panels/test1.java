package com.clubsport.admin.ui.panels;

import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class test1 extends JFrame {

    private Utilisateur utilisateurConnecte = null; // stocke l'utilisateur connecté

    public test1() {
        setTitle("Interface Administrateur");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // pour fermer la fenetre

        JPanel mainPanel = new JPanel(); // creation du pannel principal
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(Box.createVerticalStrut(20)); // espace en haut

        // --- BARRE DE CONNEXION ---
        JPanel barreConnexion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        JButton btnConnexion = new JButton("Connexion");
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setEnabled(false); // désactivé tant qu'on n'est pas connecté

        barreConnexion.add(btnConnexion);
        barreConnexion.add(btnDeconnexion);

        mainPanel.add(barreConnexion);
        mainPanel.add(Box.createVerticalStrut(20));

        // --- 4 BLOCS CLIQUABLES ---
        mainPanel.add(createClickableBlock("Gestion des comptes", () -> {
            if (verifierAcces()) new PageGestionComptes().setVisible(true); // les faire apparaitre
        }));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createClickableBlock("Historique des connexions", () -> {
            if (verifierAcces()) new PageHistorique().setVisible(true);
        }));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createClickableBlock("Recherche de clubs", () -> {
            if (verifierAcces()) new PageRechercheClubs().setVisible(true);
        }));
        mainPanel.add(Box.createVerticalStrut(30));

        // --- NOUVEAU : GESTION ADMINISTRATEUR ---
        mainPanel.add(createClickableBlock("Gestion administrateur", () -> {
            if (verifierAcces()) new PageAuditAdministrateur().setVisible(true);
        }));

        mainPanel.add(Box.createVerticalGlue()); // un espace en bas
        add(mainPanel, BorderLayout.CENTER); // ajoute le panel a la fenetre

        // --- ACTIONS DES BOUTONS ---
        btnConnexion.addActionListener(e -> {
            FenetreConnexion fen = new FenetreConnexion(this);
            fen.setVisible(true);

            // si un admin est connecté
            if (utilisateurConnecte != null) {
                btnConnexion.setEnabled(false);
                btnDeconnexion.setEnabled(true);
            }
        });

        btnDeconnexion.addActionListener(e -> {
            utilisateurConnecte = null;
            btnConnexion.setEnabled(true);
            btnDeconnexion.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Déconnecté.");
        });
    }

    // creation de la fonction pour pouvoir cliquer . Définie par un titre et une action
    private JPanel createClickableBlock(String title, Runnable action) {
        JPanel panel = new JPanel();
        // Pannel de taille fixe
        panel.setPreferredSize(new Dimension(400, 100));
        panel.setMaximumSize(new Dimension(400, 100));
        // couleur du fond
        panel.setBorder(new LineBorder(Color.GRAY, 2, true));
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label);

        // Effet clic avec un effet de souris en forme de doigt
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run(); // ouvre nouvelle page
            }

            // quand la souris entre sur le bloc cliquable change de couleur
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(220, 220, 220));
            }

            // quand la souris sort du cadre redevient couleur normale
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }
        });

        return panel;
    }

    // Vérifie si un admin est connecté
    private boolean verifierAcces() {
        if (utilisateurConnecte == null) {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter en tant qu'administrateur.");
            return false;
        }
        return true;
    }

    // Permet à la fenêtre de connexion de définir l'utilisateur connecté
    public void setUtilisateurConnecte(Utilisateur u) {
        this.utilisateurConnecte = u;
    }

    // Lance l’interface affiche la fenêtre
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new test1().setVisible(true));
    }
}
