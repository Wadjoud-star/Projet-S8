CREATE DATABASE IF NOT EXISTS clubs_sportifs;
USE clubs_sportifs;

CREATE TABLE region (
    code_region VARCHAR(10) PRIMARY KEY,
    nom_region VARCHAR(100) NOT NULL
);

CREATE TABLE commune (
    code_commune VARCHAR(10) PRIMARY KEY,
    nom_commune VARCHAR(100) NOT NULL,
    population INT,
    code_region VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_region) REFERENCES region(code_region)
);

CREATE TABLE statistique_licencies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_licencies INT,
    licencies_femmes INT,
    licencies_hommes INT,
    code_commune VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_commune) REFERENCES commune(code_commune)
);

CREATE TABLE federation (
    code_federation VARCHAR(10) PRIMARY KEY,
    nom_federation VARCHAR(100) NOT NULL
);

CREATE TABLE club (
    id_club INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(100),
    code_postal VARCHAR(10),
    latitude DOUBLE,
    longitude DOUBLE,
    nb_licencies INT,
    nb_femmes INT,
    nb_hommes INT,
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
    role VARCHAR(50) NOT NULL
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

CREATE TABLE espace_club (
    id INT AUTO_INCREMENT PRIMARY KEY,
    actualites TEXT,
    horaires TEXT,
    cotisations DECIMAL(10,2),
    date_maj DATETIME,
    id_club INT NOT NULL,
    FOREIGN KEY (id_club) REFERENCES club(id_club)
);