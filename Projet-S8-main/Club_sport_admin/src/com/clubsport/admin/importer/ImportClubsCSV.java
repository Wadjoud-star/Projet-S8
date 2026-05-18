package com.clubsport.admin.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportClubsCSV {

    // Paramètres de connexion à la base MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/clubs_sportifs";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final Map<String, String> REGION_CODES = buildRegionCodes();

    public static void main(String[] args) {
        String cheminFichier = "clubs-data-2019.csv";
        Connection conn = null;

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base réussie !");
            conn.setAutoCommit(false);

            try (PreparedStatement psRegion = conn.prepareStatement(
                    "INSERT IGNORE INTO region (code_region, nom_region) VALUES (?, ?)");
                 PreparedStatement psCommune = conn.prepareStatement(
                    "INSERT IGNORE INTO commune (code_commune, nom_commune, population, code_region) VALUES (?, ?, NULL, ?)");
                 PreparedStatement psFederation = conn.prepareStatement(
                    "INSERT IGNORE INTO federation (code_federation, nom_federation) VALUES (?, ?)");
                 PreparedStatement psStats = conn.prepareStatement(
                    "INSERT INTO statistique_clubs (nb_clubs, nb_etablissements_professionnels, total_structures, code_commune, code_federation) VALUES (?, ?, ?, ?, ?)")) {

                String headerLine = br.readLine();
                if (headerLine == null) {
                    throw new IOException("Fichier vide : " + cheminFichier);
                }
                String[] headers = splitCsvLine(headerLine);
                Map<String, Integer> idx = buildHeaderIndex(headers);

                int iCodeCommune = requiredIndex(idx, "code_commune");
                int iCommune = requiredIndex(idx, "commune");
                int iRegion = requiredIndex(idx, "region");
                int iCodeFederation = requiredIndex(idx, "code", "code_federation", "fed_2019");
                int iNomFederation = requiredIndex(idx, "federation", "nom_federation", "nom_fed");
                int iClubs = requiredIndex(idx, "clubs", "clubs_sportifs_2019", "clubs_actifs");
                int iEpa = requiredIndex(idx, "epa", "etablissements_prof_2019", "epa_actifs");
                int iTotal = requiredIndex(idx, "total", "total_clubs_2019", "total_actifs");

                String ligne;
                int compteur = 0;
                int ignores = 0;
                int erreurs = 0;

                while ((ligne = br.readLine()) != null) {
                    String[] col = splitCsvLine(ligne);

                    try {
                        String codeCommune = getValue(col, iCodeCommune);
                        String nomCommune = getValue(col, iCommune);
                        String nomRegion = getValue(col, iRegion);
                        String codeFederation = getValue(col, iCodeFederation);
                        String nomFederation = getValue(col, iNomFederation);

                        if (codeCommune.isBlank() || isNonRepartiRegion(nomRegion)) {
                            ignores++;
                            continue;
                        }

                        int clubs = parseIntSafe(getValue(col, iClubs));
                        int ep = parseIntSafe(getValue(col, iEpa));
                        int total = parseIntSafe(getValue(col, iTotal));
                        String codeRegion = getRegionCode(nomRegion);

                        psRegion.setString(1, codeRegion);
                        psRegion.setString(2, nomRegion);
                        psRegion.executeUpdate();

                        psCommune.setString(1, codeCommune);
                        psCommune.setString(2, nomCommune);
                        psCommune.setString(3, codeRegion);
                        psCommune.executeUpdate();

                        psFederation.setString(1, codeFederation);
                        psFederation.setString(2, nomFederation);
                        psFederation.executeUpdate();

                        psStats.setInt(1, clubs);
                        psStats.setInt(2, ep);
                        psStats.setInt(3, total);
                        psStats.setString(4, codeCommune);
                        psStats.setString(5, codeFederation);
                        psStats.executeUpdate();

                        compteur++;
                        if (compteur % 1000 == 0) {
                            System.out.println(compteur + " lignes insérées...");
                        }
                    } catch (Exception e) {
                        erreurs++;
                    }
                }

                conn.commit();
                System.out.println("Import clubs terminé !");
                System.out.println("Lignes importées : " + compteur);
                System.out.println("Lignes ignorées : " + ignores);
                System.out.println("Lignes en erreur : " + erreurs);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static String[] splitCsvLine(String line) {
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        java.util.List<String> cols = new java.util.ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                cols.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        cols.add(sb.toString());
        return cols.toArray(new String[0]);
    }

    private static Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            idx.put(normalize(headers[i]), i);
        }
        return idx;
    }

    private static int requiredIndex(Map<String, Integer> idx, String... alternatives) {
        for (String alt : alternatives) {
            Integer found = idx.get(normalize(alt));
            if (found != null) {
                return found;
            }
        }
        throw new IllegalArgumentException("Colonne obligatoire introuvable: " + String.join(" / ", alternatives));
    }

    private static String getValue(String[] cols, int index) {
        if (index < 0 || index >= cols.length) {
            return "";
        }
        return cols[index].replace("\"", "").trim();
    }

    private static int parseIntSafe(String raw) {
        if (raw == null) {
            return 0;
        }
        String cleaned = raw.replace("\"", "").replace(" ", "").trim();
        if (cleaned.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(cleaned);
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String noAccent = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return noAccent.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }

    private static String getRegionCode(String nomRegion) {
        String normalized = normalize(nomRegion);
        String code = REGION_CODES.get(normalized);
        if (code != null) {
            return code;
        }
        String fallback = normalized.replace("_", "");
        return fallback.length() <= 10 ? fallback : fallback.substring(0, 10);
    }

    private static Map<String, String> buildRegionCodes() {
        Map<String, String> map = new HashMap<>();
        map.put("auvergne_rhone_alpes", "84");
        map.put("bourgogne_franche_comte", "27");
        map.put("bretagne", "53");
        map.put("centre_val_de_loire", "24");
        map.put("corse", "94");
        map.put("grand_est", "44");
        map.put("hauts_de_france", "32");
        map.put("ile_de_france", "11");
        map.put("normandie", "28");
        map.put("nouvelle_aquitaine", "75");
        map.put("occitanie", "76");
        map.put("pays_de_la_loire", "52");
        map.put("provence_alpes_cote_d_azur", "93");
        map.put("guadeloupe", "01");
        map.put("martinique", "02");
        map.put("guyane", "03");
        map.put("la_reunion", "04");
        map.put("mayotte", "06");
        map.put("com", "COM");
        map.put("drom", "DROM");
        map.put("etranger", "ETR");
        map.put("monaco", "MCO");
        return map;
    }

    private static boolean isNonRepartiRegion(String nomRegion) {
        String normalized = normalize(nomRegion);
        return normalized.equals("nr_non_reparti") || normalized.equals("nr");
    }
}