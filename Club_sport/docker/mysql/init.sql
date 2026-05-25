-- Schéma clubs_sportifs
-- Règles retenues (sans casser l'existant) :
--   - statistique_clubs / statistique_licencies : inchangés = agrégat simple utilisé par l'app (SUM par commune+fédération OK).
--   - statistique_*_opendata : grain fichier = commune + fédération + QPV + statut géo.
--     Pas de ligne "total" séparée si vous importez déjà le détail par QPV : agréger avec SUM vers les tables simples.
--     code_qpv / statut_geo vides ('') = pas de QPV / non renseigné (évite les doublons UNIQUE avec NULL).
--   - details_json : tranches d'âge, population, colonnes variables selon millésime (2019 sujet ou export plus récent).

CREATE DATABASE IF NOT EXISTS clubs_sportifs;
USE clubs_sportifs;

CREATE TABLE region (
    code_region VARCHAR(10) PRIMARY KEY,
    nom_region VARCHAR(100) NOT NULL
);

CREATE TABLE departement (
    code_departement VARCHAR(10) PRIMARY KEY,
    nom_departement VARCHAR(100) NULL,
    code_region VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_region) REFERENCES region(code_region)
);

CREATE TABLE commune (
    code_commune VARCHAR(10) PRIMARY KEY,
    nom_commune VARCHAR(100) NOT NULL,
    population INT NULL,
    code_region VARCHAR(10) NOT NULL,
    code_departement VARCHAR(10) NULL,
    FOREIGN KEY (code_region) REFERENCES region(code_region),
    FOREIGN KEY (code_departement) REFERENCES departement(code_departement)
);

CREATE TABLE federation (
    code_federation VARCHAR(10) PRIMARY KEY,
    nom_federation VARCHAR(150) NOT NULL
);

CREATE TABLE statistique_clubs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nb_clubs INT NULL,
    nb_etablissements_professionnels INT NULL,
    total_structures INT NULL,
    code_commune VARCHAR(10) NOT NULL,
    code_federation VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune),
    FOREIGN KEY (code_federation) REFERENCES federation(code_federation)
);

CREATE TABLE statistique_licencies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_licencies INT NULL,
    licencies_femmes INT NULL,
    licencies_hommes INT NULL,
    code_commune VARCHAR(10) NOT NULL,
    code_federation VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune),
    FOREIGN KEY (code_federation) REFERENCES federation(code_federation)
);

-- Détail open data (fichier clubs : QPV, statut géo, clubs / EPA / total)
CREATE TABLE statistique_clubs_opendata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code_commune VARCHAR(10) NOT NULL,
    code_federation VARCHAR(10) NOT NULL,
    code_qpv VARCHAR(64) NOT NULL DEFAULT '',
    nom_qpv VARCHAR(200) NULL,
    statut_geo VARCHAR(64) NOT NULL DEFAULT '',
    nb_clubs INT NULL,
    nb_epa INT NULL,
    total_structures INT NULL,
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune),
    FOREIGN KEY (code_federation) REFERENCES federation(code_federation),
    UNIQUE KEY uq_clubs_open (code_commune, code_federation, code_qpv, statut_geo)
);

-- Détail open data (fichier licences : QPV, statut géo, totaux + JSON pour tranches d'âge / population / colonnes millésime)
CREATE TABLE statistique_licencies_opendata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code_commune VARCHAR(10) NOT NULL,
    code_federation VARCHAR(10) NOT NULL,
    code_qpv VARCHAR(64) NOT NULL DEFAULT '',
    nom_qpv VARCHAR(200) NULL,
    statut_geo VARCHAR(64) NOT NULL DEFAULT '',
    total_licencies INT NULL,
    licencies_femmes INT NULL,
    licencies_hommes INT NULL,
    details_json JSON NULL COMMENT 'Toutes colonnes fines (âges H/F, pop, QP, etc.)',
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune),
    FOREIGN KEY (code_federation) REFERENCES federation(code_federation),
    UNIQUE KEY uq_lic_open (code_commune, code_federation, code_qpv, statut_geo)
);

CREATE TABLE club (
    id_club INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(150) NULL,
    code_postal VARCHAR(10) NULL,
    latitude DOUBLE NULL,
    longitude DOUBLE NULL,
    nb_licencies INT NULL,
    nb_femmes INT NULL,
    nb_hommes INT NULL,
    code_federation VARCHAR(10) NOT NULL,
    code_commune VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_federation) REFERENCES federation(code_federation),
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune)
);

CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    photo_identite VARCHAR(255) NULL,
    statut_verification VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE'
);

CREATE TABLE historique_connexion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_heure DATETIME NOT NULL,
    adresse_ip VARCHAR(45) NOT NULL,
    login VARCHAR(100) NOT NULL,
    succes BOOLEAN NOT NULL,
    utilisateur_id INT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
);

CREATE TABLE audit_action (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_admin INT NOT NULL,
    action VARCHAR(255) NOT NULL,
    details TEXT NULL,
    date_action TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_action_admin
        FOREIGN KEY (id_admin) REFERENCES utilisateur(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    INDEX idx_audit_action_date (date_action DESC),
    INDEX idx_audit_action_admin (id_admin)
);

CREATE TABLE espace_club (
    id INT AUTO_INCREMENT PRIMARY KEY,
    actualites TEXT NULL,
    horaires TEXT NULL,
    cotisations DECIMAL(10,2) NULL,
    date_maj DATETIME NULL,
    id_club INT NOT NULL,
    FOREIGN KEY (id_club) REFERENCES club(id_club)
);
