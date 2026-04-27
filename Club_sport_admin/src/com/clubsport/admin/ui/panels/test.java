package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class test extends JFrame {

    public test() {
        setTitle("Interface Administrateur");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- PANEL PRINCIPAL ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Ajout d'espacement en haut
        mainPanel.add(Box.createVerticalStrut(40));

        // --- CREATION DES 3 CADRES ---
        mainPanel.add(createCenteredFrame("Gestion des comptes"));
        mainPanel.add(Box.createVerticalStrut(30)); // espace entre les cadres

        mainPanel.add(createCenteredFrame("Historique des connexions"));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createCenteredFrame("Recherche de clubs"));

        // Ajout d'espacement en bas
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCenteredFrame(String title) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 100));
        panel.setMaximumSize(new Dimension(400, 100));
        panel.setBorder(new LineBorder(Color.GRAY, 2, true));
        panel.setBackground(new Color(245, 245, 245));

        // Centrage horizontal
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new test().setVisible(true));
    }
}
