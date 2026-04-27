package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class test1 extends JFrame {

    public test1() {
        setTitle("Interface Administrateur");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(Box.createVerticalStrut(40));

        // --- 3 BLOCS CLIQUABLES ---
        mainPanel.add(createClickableBlock("Gestion des comptes", () -> {
            new PageGestionComptes().setVisible(true);
        }));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createClickableBlock("Historique des connexions", () -> {
            new PageHistorique().setVisible(true);
        }));
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(createClickableBlock("Recherche de clubs", () -> {
            new PageRechercheClubs().setVisible(true);
        }));

        mainPanel.add(Box.createVerticalGlue());
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createClickableBlock(String title, Runnable action) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 100));
        panel.setMaximumSize(new Dimension(400, 100));
        panel.setBorder(new LineBorder(Color.GRAY, 2, true));
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label);

        // Effet visuel + clic
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run(); // ouvre la nouvelle page
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new test1().setVisible(true));
    }
}
