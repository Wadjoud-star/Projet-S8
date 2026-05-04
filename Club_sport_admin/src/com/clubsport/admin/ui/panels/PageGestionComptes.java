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

        //  Bouton Chercher
        JButton btnChercher = new JButton("Chercher");
        btnChercher.setBackground(new Color(0, 120, 215));
        btnChercher.setForeground(Color.WHITE);
        btnChercher.setFocusPainted(false);
        btnChercher.setPreferredSize(new Dimension(110, 35));
        btnChercher.addActionListener(e -> chargerComptes());

        topPanel.add(comboType);
        topPanel.add(btnChercher);

        add(topPanel, BorderLayout.NORTH);

        // Le tableau avec les cases a selectionner 
        String[] colonnes = {"Sélection", "Identifiant", "Nom"};

        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {// definit le type de colonne du tableau
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

        // --- PANEL BAS : Boutons Modifier / Supprimer ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // --- Bouton Modifier harmonisé ---
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(0, 120, 215));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setPreferredSize(new Dimension(110, 35));

        // Bouton Supprimer 
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(200, 200, 200));
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setPreferredSize(new Dimension(110, 35));

        bottomPanel.add(btnModifier);
        bottomPanel.add(btnSupprimer);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- ACTION MODIFIER ---
        btnModifier.addActionListener(e -> modifierSelection());

        // --- ACTION SUPPRIMER ---
        btnSupprimer.addActionListener(e -> supprimerSelection());
    }

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

        // --- AJOUT AUTOMATIQUE D’UN UTILISATEUR FICTIF A CHAQUE FOIS ---
        Utilisateur fictif = new Utilisateur(
                999,                      // id fictif
                "Martin Élu",             // nom
                "martin.elu@example.com", // email
                "elu"                     // rôle
        );
        utilisateurs.add(fictif);

        for (Utilisateur u : utilisateurs) {
            model.addRow(new Object[]{
                    false, // checkbox
                    u.getId(),
                    u.getNom()
            });
        }
    }

    private List<Integer> getSelectedIds() {
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {// parcourt le slignes du tableau
            boolean selected = (boolean) model.getValueAt(i, 0);// recupere les infos de la premiere colonne 
            if (selected) {
                ids.add(Integer.parseInt(model.getValueAt(i, 1).toString()));// convertit la colonne 1 en texte puis en entier et on ajoute a la liste
            }
        }
        return ids;
    }

    private void modifierSelection() {
        List<Integer> ids = getSelectedIds();

        if (ids.size() == 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un compte.");
            return;
        }

        if (ids.size() > 1) {
            JOptionPane.showMessageDialog(this, "Vous ne pouvez modifier qu’un seul compte à la fois.");
            return;
        }// this désigne la fenetre actuelle 

        int id = ids.get(0);

        // Charger l'utilisateur complet
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurParId(id);

        // Si l'utilisateur n'existe pas en BDD (cas du fictif), on le crée manuellement
        if (utilisateur == null && id == 999) {
            utilisateur = new Utilisateur(
                    999,
                    "Martin Élu",
                    "martin.elu@example.com",
                    "elu"
            );
        }

        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Impossible de charger cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ouvrir la fenêtre de modification
        ModifierUtilisateur fenetre = new ModifierUtilisateur(utilisateur);
        fenetre.setVisible(true);

        // Quand la fenêtre se ferme remet  le tableau vide
        fenetre.addWindowListener(new java.awt.event.WindowAdapter() {// permet de savoir si on ferme la fenetre 
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                chargerComptes();
            }
        });
    }

    private void supprimerSelection() {
        List<Integer> ids = getSelectedIds();

        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sélectionnez au moins un compte.");
            return;// si on ne selectionne rien on demande a l'utilisateur de selectionner quelque chose
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer " + ids.size() + " compte(s) ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        for (int id : ids) {
            utilisateurDAO.supprimerUtilisateur(id);// si on clique sur oui on supprime les id selectionner de la BDD
        }

        JOptionPane.showMessageDialog(this, "Suppression validée.");
        chargerComptes();// on remet à jour la page
    }

    // --- MAIN POUR LANCER DIRECTEMENT LA PAGE ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PageGestionComptes fen = new PageGestionComptes();
            fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fen.setVisible(true);
        });
    }
}
