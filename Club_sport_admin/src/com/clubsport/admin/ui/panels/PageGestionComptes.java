package com.clubsport.admin.ui.panels;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.dao.UtilisateurDAO; // ← nécessaire pour la mise à jour BDD
import com.clubsport.admin.dao.AuditDAO; //

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PageGestionComptes extends JFrame {

    private JComboBox<String> comboType;
    private JTable table;
    private DefaultTableModel model;

    // --- BOUTONS DE TRI ---
    private JRadioButton triNomAZ;
    private JRadioButton triNomZA;
    private JRadioButton triStatut;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private AuditDAO auditDAO = new AuditDAO(); // ➕ audit

    private Utilisateur adminConnecte; // ➕ admin connecté (peut être null maintenant)

    // --- Constructeur modifié pour permettre un accès libre ---
    public PageGestionComptes(Utilisateur adminConnecte) {
        this.adminConnecte = adminConnecte; // peut être null → accès libre

        setTitle("Gestion des comptes");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PANEL HAUT ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // empile verticalement

        // --- LIGNE 1 : TYPE + CHERCHER ---
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        ligne1.add(new JLabel("Type : "));

        comboType = new JComboBox<>(new String[]{
                "Élus", "Présidents", "Entraîneurs", "Sportifs", "Administrateurs"
        });

        JButton btnChercher = new JButton("Chercher");
        btnChercher.setBackground(new Color(0, 120, 215));
        btnChercher.setForeground(Color.WHITE);
        btnChercher.setFocusPainted(false);
        btnChercher.setPreferredSize(new Dimension(110, 35));
        btnChercher.addActionListener(e -> chargerComptes());

        ligne1.add(comboType);
        ligne1.add(btnChercher);

        topPanel.add(ligne1);

        // --- LIGNE 2 : TRI ---
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ligne2.add(new JLabel("Trier par : "));

        triNomAZ = new JRadioButton("Nom A→Z");
        triNomZA = new JRadioButton("Nom Z→A");
        triStatut = new JRadioButton("Statut");

        ButtonGroup groupTri = new ButtonGroup();
        groupTri.add(triNomAZ);
        groupTri.add(triNomZA);
        groupTri.add(triStatut);

        triNomAZ.setSelected(true); // tri par défaut

        ligne2.add(triNomAZ);
        ligne2.add(triNomZA);
        ligne2.add(triStatut);

        //Pour raffraichir la page 
        triNomAZ.addActionListener(e -> chargerComptes());
        triNomZA.addActionListener(e -> chargerComptes());
        triStatut.addActionListener(e -> chargerComptes());

        topPanel.add(ligne2);

        add(topPanel, BorderLayout.NORTH);

        // Le tableau
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

        // Bas de la page 
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

        //quand on clique sur un bouton 
        btnModifier.addActionListener(e -> modifierSelection());
        btnSupprimer.addActionListener(e -> supprimerSelection());

        btnCreer.addActionListener(e -> {
            // ➕ adminConnecte peut être null → OK
            CreerUtilisateur fen = new CreerUtilisateur(adminConnecte);
            fen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

            // ➕ adminConnecte peut être null → OK
            ValidationInscription fen = new ValidationInscription(utilisateur, () -> {
                chargerComptes(); // ← rafraîchire le tableau
            }, adminConnecte);

            fen.setVisible(true);
        });
    }

    //Fonction pour charger les comptes 
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

        // Foction pour tirier les role 
        if (triNomAZ.isSelected()) {
            utilisateurs.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        }
        else if (triNomZA.isSelected()) {
            utilisateurs.sort((a, b) -> b.getNom().compareToIgnoreCase(a.getNom()));
        }
        else if (triStatut.isSelected()) {

            utilisateurs.sort((a, b) -> {
                String sa = a.getStatutVerification();
                String sb = b.getStatutVerification();

                sa = (sa == null) ? "" : sa.toUpperCase();
                sb = (sb == null) ? "" : sb.toUpperCase();

                int pa = getPrioriteStatut(sa);
                int pb = getPrioriteStatut(sb);

                return Integer.compare(pa, pb);
            });
        }

        for (Utilisateur u : utilisateurs) {
            model.addRow(new Object[]{
                    false,
                    u.getId(),
                    u.getNom(),
                    u.getEmail(),
                    u.getRole(),
                    u.getStatutVerification()
            });
        }
    }

    // --- PRIORITÉ DES STATUTS ---
    private int getPrioriteStatut(String statut) {
        if (statut.contains("ATTENTE")) return 0; // EN_ATTENTE
        if (statut.startsWith("VALIDE")) return 1; // VALIDE
        if (statut.startsWith("REFUS")) return 2; // REFUSE
        return 3;
    }

    //récupérer les identifiants des clients sélectionner 
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

    //MODIFIER
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

        // ➕ adminConnecte peut être null → OK
        ModifierUtilisateur fenetre = new ModifierUtilisateur(utilisateur, adminConnecte);
        fenetre.setVisible(true);

        fenetre.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                chargerComptes();
            }
        });
    }

    // SUPPRIMER 
    private void supprimerSelection() {
        //récupérer les identifiants des clients sélectionner 
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

        //  SUPPRESSION utilisateur de la base
        for (int id : ids) {
            utilisateurDAO.supprimerUtilisateur(id);
        }

        //Enregistrement des actions de l'admin 
        int idAdmin = (adminConnecte != null) ? adminConnecte.getId() :20;

        auditDAO.enregistrerAction(
                idAdmin, // ➕ sécurisé
                "Suppression utilisateur",
                "Suppression de " + ids.size() + " compte(s) : " + ids
        );

        JOptionPane.showMessageDialog(this, "Suppression validée.");
        chargerComptes(); // rafraîchir le tableau
    }
}
