package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PageGestionComptes extends JFrame {

    private JComboBox<String> comboType;
    private JTable table;
    private DefaultTableModel model;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public PageGestionComptes() {
        setTitle("Gestion des comptes");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL HAUT ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Type : "));

        comboType = new JComboBox<>(new String[]{
                "Élus", "Présidents", "Entraîneurs", "Sportifs", "Administrateurs"
        });

        JButton btnChercher = new JButton("Chercher");
        btnChercher.setBackground(new Color(0, 120, 215));
        btnChercher.setForeground(Color.WHITE);
        btnChercher.setFocusPainted(false);
        btnChercher.setPreferredSize(new Dimension(110, 35));
        btnChercher.addActionListener(e -> chargerComptes());

        topPanel.add(comboType);
        topPanel.add(btnChercher);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLEAU ---
        String[] colonnes = {"Sélection", "Identifiant", "Nom", "Email", "Rôle", "Statut Vérification"};

        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // seule la checkbox est cliquable
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- PANEL BAS ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Color bleu = new Color(0, 120, 215);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBackground(bleu);
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setPreferredSize(new Dimension(110, 35));

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(bleu);
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setPreferredSize(new Dimension(110, 35));

        JButton btnCreer = new JButton("Créer");
        btnCreer.setBackground(bleu);
        btnCreer.setForeground(Color.WHITE);
        btnCreer.setPreferredSize(new Dimension(110, 35));

        JButton btnValider = new JButton("Valider");
        btnValider.setBackground(bleu);
        btnValider.setForeground(Color.WHITE);
        btnValider.setPreferredSize(new Dimension(110, 35));

        bottomPanel.add(btnModifier);
        bottomPanel.add(btnSupprimer);
        bottomPanel.add(btnCreer);
        bottomPanel.add(btnValider);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- ACTIONS ---
        btnModifier.addActionListener(e -> modifierSelection());
        btnSupprimer.addActionListener(e -> supprimerSelection());

        btnCreer.addActionListener(e -> {
            CreerUtilisateur fen = new CreerUtilisateur();
            fen.setVisible(true);

            fen.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    chargerComptes();
                }
            });
        });

        btnValider.addActionListener(e -> {
            List<Integer> ids = getSelectedIds();

            if (ids.size() != 1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un seul compte à valider.");
                return;
            }

            Utilisateur utilisateur = utilisateurDAO.getUtilisateurParId(ids.get(0));

            if (utilisateur == null) {
                JOptionPane.showMessageDialog(this, "Impossible de charger cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ValidationInscription fen = new ValidationInscription(utilisateur);
            fen.setVisible(true);
        });
    }

    // --- CHARGER LES COMPTES ---
    private void chargerComptes() {
        model.setRowCount(0);

        String type = (String) comboType.getSelectedItem();

        String roleBDD = switch (type) {
            case "Élus" -> "elu";
            case "Présidents" -> "president";
            case "Entraîneurs" -> "entraineur";
            case "Sportifs" -> "sportif";
            case "Administrateurs" -> "admin";
            default -> "";
        };

        List<Utilisateur> utilisateurs = utilisateurDAO.getUtilisateursParRole(roleBDD);

        for (Utilisateur u : utilisateurs) {
            model.addRow(new Object[]{
                    false,
                    u.getId(),
                    u.getNom(),
                    u.getEmail(),
                    u.getRole(),
                    u.getStatutVerification() // ✔ NOUVEAU
            });
        }
    }

    // --- RÉCUPÉRER LES IDS SÉLECTIONNÉS ---
    private List<Integer> getSelectedIds() {
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean selected = (boolean) model.getValueAt(i, 0);
            if (selected) {
                ids.add(Integer.parseInt(model.getValueAt(i, 1).toString()));
            }
        }
        return ids;
    }

    // --- MODIFIER ---
    private void modifierSelection() {
        List<Integer> ids = getSelectedIds();

        if (ids.size() == 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un compte.");
            return;
        }

        if (ids.size() > 1) {
            JOptionPane.showMessageDialog(this, "Vous ne pouvez modifier qu’un seul compte à la fois.");
            return;
        }

        int id = ids.get(0);
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurParId(id);

        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Impossible de charger cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModifierUtilisateur fenetre = new ModifierUtilisateur(utilisateur);
        fenetre.setVisible(true);

        fenetre.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                chargerComptes();
            }
        });
    }

    // --- SUPPRIMER ---
    private void supprimerSelection() {
        List<Integer> ids = getSelectedIds();

        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez au moins un compte.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer " + ids.size() + " compte(s) ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        for (int id : ids) {
            utilisateurDAO.supprimerUtilisateur(id);
        }

        JOptionPane.showMessageDialog(this, "Suppression validée.");
        chargerComptes();
    }

    // --- MAIN ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PageGestionComptes fen = new PageGestionComptes();
            fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fen.setVisible(true);
        });
    }
}
