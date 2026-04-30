package com.clubsport.admin.ui.panels;

import com.clubsport.admin.dao.UtilisateurDAO;
import com.clubsport.admin.model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PageGestionComptes extends JFrame {

    private JComboBox<String> comboType;
    private JTable table;
    private DefaultTableModel model;

    // DAO réel
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public PageGestionComptes() {
        setTitle("Gestion des comptes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL HAUT : Type d'utilisateur ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Type : "));

        comboType = new JComboBox<>(new String[]{
                "Élus",
                "Présidents",
                "Entraîneurs",
                "Sportifs",
                "Administrateurs"
        });

        comboType.addActionListener(e -> chargerComptes());
        topPanel.add(comboType);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLEAU ---
        String[] colonnes = {"Identifiant", "Nom", "Prénom", "Modifier", "Supprimer"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3; // Modifier / Supprimer cliquables
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);

        ajouterBoutons();

        add(new JScrollPane(table), BorderLayout.CENTER);

        chargerComptes();
    }

    private void chargerComptes() {
        model.setRowCount(0);

        String type = (String) comboType.getSelectedItem();

        // Conversion du type en rôle MySQL
        String roleBDD = switch (type) {
            case "Élus" -> "elu";
            case "Présidents" -> "president";
            case "Entraîneurs" -> "entraineur";
            case "Sportifs" -> "sportif";
            case "Administrateurs" -> "admin";
            default -> "";
        };

        // Récupération depuis MySQL
        List<Utilisateur> utilisateurs = utilisateurDAO.getUtilisateursParRole(roleBDD);

        // Ajout dans le tableau
        for (Utilisateur u : utilisateurs) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getNom(),
                    u.getPrenom(),
                    "Modifier",
                    "Supprimer"
            });
        }
    }

    private void ajouterLigne(String id, String nom, String prenom) {
        model.addRow(new Object[]{id, nom, prenom, "Modifier", "Supprimer"});
    }

    private void ajouterBoutons() {
        table.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
        table.getColumn("Modifier").setCellEditor(new ButtonEditor(new JCheckBox(), "modifier"));

        table.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
        table.getColumn("Supprimer").setCellEditor(new ButtonEditor(new JCheckBox(), "supprimer"));
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String actionType;

        public ButtonEditor(JCheckBox checkBox, String actionType) {
            super(checkBox);
            this.actionType = actionType;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener((ActionEvent e) -> {
                int row = table.getSelectedRow();
                int id = (int) table.getValueAt(row, 0);

                if (actionType.equals("modifier")) {
                    JOptionPane.showMessageDialog(null, "Modifier le compte : " + id);
                } else if (actionType.equals("supprimer")) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Supprimer le compte " + id + " ?", "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (utilisateurDAO.supprimerUtilisateur(id)) {
                            JOptionPane.showMessageDialog(null, "Compte supprimé.");
                            chargerComptes();
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression.");
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            return button;
        }
    }
}
