package com.clubsport.admin.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import com.clubsport.admin.dao.HistoriqueConnexionDAO;
import com.clubsport.admin.model.HistoriqueConnexion;

public class PageHistorique extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtDate; // champ pour saisir la date
    private JButton btnFiltrer; // bouton filtrer

    private JLabel lblCompteur; // compteur dynamique

    // Pagination
    private int page = 0;
    private final int LIGNES_PAR_PAGE = 20;
    private List<HistoriqueConnexion> cacheAffichage = new ArrayList<>();

    // connexion au DAO Historique de connexions 
    private HistoriqueConnexionDAO historiqueDAO = new HistoriqueConnexionDAO();

    public PageHistorique() {
        setTitle("Historique des connexions");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //BLoc du haut 
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titre = new JLabel("Voici les dernières connexions au site :");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT); // centrer dans le panel
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(titre);

        // --- Ligne filtres radio ---
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        typePanel.add(new JLabel("Voulez-vous : "));

        JRadioButton rbTous = new JRadioButton("Toutes les connexions");
        JRadioButton rbEchecs = new JRadioButton("Échecs");
        JRadioButton rbReussis = new JRadioButton("Réussies");

        rbTous.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(rbTous);
        group.add(rbEchecs);
        group.add(rbReussis);

        typePanel.add(rbTous);
        typePanel.add(rbEchecs);
        typePanel.add(rbReussis);

        topPanel.add(typePanel);

        // Pannel permettant de filtrer par date les connexions 
        JPanel filtrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        filtrePanel.add(new JLabel("Entrez une date (AAAA-MM-JJ) :"));
        txtDate = new JTextField(10);
        filtrePanel.add(txtDate);

        btnFiltrer = new JButton("Sélectionner");
        btnFiltrer.setBackground(new Color(0, 120, 215));
        btnFiltrer.setForeground(Color.WHITE);
        btnFiltrer.setFocusPainted(false);
        btnFiltrer.setPreferredSize(new Dimension(110, 30));

        filtrePanel.add(btnFiltrer);// ajout a le panel du bouton filtre 

        // --- Bouton Recharger ---
        JButton btnRecharger = new JButton("Recharger");
        btnRecharger.setBackground(new Color(120, 120, 120));
        btnRecharger.setForeground(Color.WHITE);
        btnRecharger.setFocusPainted(false);
        btnRecharger.setPreferredSize(new Dimension(110, 30));
        filtrePanel.add(btnRecharger);

        topPanel.add(filtrePanel);// ajout dans le panel du haut 

        add(topPanel, BorderLayout.NORTH);

        // --- TABLEAU ---
        String[] colonnes = {"Utilisateur", "Adresse IP", "Date", "Succès"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // --- Compteur au-dessus du tableau ---
        lblCompteur = new JLabel("0 connexions affichées");
        lblCompteur.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(lblCompteur, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // --- Pagination ---
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnPrev = new JButton("Précédent");
        JButton btnNext = new JButton("Suivant");
        paginationPanel.add(btnPrev);
        paginationPanel.add(btnNext);
        add(paginationPanel, BorderLayout.SOUTH);

        // Charger les données BDD
        chargerHistorique();

        // Action du bouton Filtrer
        btnFiltrer.addActionListener(e -> filtrerParDate());

        // Action du bouton Recharger
        btnRecharger.addActionListener(e -> {
            txtDate.setText("");
            rbTous.setSelected(true);
            chargerHistorique();
        });

        // --- Actions des boutons radio ---
        rbTous.addActionListener(e -> {
            cacheAffichage = historiqueDAO.getHistorique();
            page = 0;
            afficherPage();
        });

        rbEchecs.addActionListener(e -> {
            List<HistoriqueConnexion> all = historiqueDAO.getHistorique();
            cacheAffichage = all.stream().filter(h -> !h.isSucces()).toList();
            page = 0;
            afficherPage();
        });

        rbReussis.addActionListener(e -> {
            List<HistoriqueConnexion> all = historiqueDAO.getHistorique();
            cacheAffichage = all.stream().filter(h -> h.isSucces()).toList();
            page = 0;
            afficherPage();
        });

        // --- Pagination actions ---
        btnPrev.addActionListener(e -> {
            if (page > 0) {
                page--;
                afficherPage();
            }
        });

        btnNext.addActionListener(e -> {
            if ((page + 1) * LIGNES_PAR_PAGE < cacheAffichage.size()) {
                page++;
                afficherPage();
            }
        });
    }

    private void chargerHistorique() {
        cacheAffichage = historiqueDAO.getHistorique();
        page = 0;
        afficherPage();
    }

    private void afficherPage() {
        model.setRowCount(0);

        int start = page * LIGNES_PAR_PAGE;
        int end = Math.min(start + LIGNES_PAR_PAGE, cacheAffichage.size());

        for (int i = start; i < end; i++) {
            HistoriqueConnexion h = cacheAffichage.get(i);
            model.addRow(new Object[]{
                    h.getUtilisateur().getNom(),
                    h.getAdresseIP(),
                    h.getDateHeure().toString(),
                    h.isSucces() ? "✔" : "✘"
            });
        }

        lblCompteur.setText(
            "Page " + (page + 1) + " — " + model.getRowCount() +
            " lignes affichées sur " + cacheAffichage.size()
        );
    }

    private void filtrerParDate() {
        String date = txtDate.getText().trim();// on recupere les infos rentrées par l'utilisateur et on enleve les espaces 

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une date.");
            return;
        }

        List<HistoriqueConnexion> logsBDD = historiqueDAO.getHistoriqueParDate(date);

        if (logsBDD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune connexion trouvée pour cette date.");
            return;
        }

        cacheAffichage = logsBDD;
        page = 0;
        afficherPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PageHistorique page = new PageHistorique();
            page.setVisible(true);
        });
    }
}
