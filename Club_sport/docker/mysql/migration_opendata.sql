-- Migration : aligner une base existante (ancien init.sql) sur le schéma avec *_opendata + département.
-- À exécuter une fois sur clubs_sportifs (Workbench ou : mysql -uroot -proot clubs_sportifs < migration_opendata.sql)
-- Idempotent : peut être relancé sans tout casser.

USE clubs_sportifs;

CREATE TABLE IF NOT EXISTS departement (
    code_departement VARCHAR(10) PRIMARY KEY,
    nom_departement VARCHAR(100) NULL,
    code_region VARCHAR(10) NOT NULL,
    FOREIGN KEY (code_region) REFERENCES region(code_region)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'commune' AND COLUMN_NAME = 'code_departement'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE commune ADD COLUMN code_departement VARCHAR(10) NULL AFTER code_region',
    'SELECT ''commune.code_departement : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Rendre les lignes référençables avant la FK : la table departement était vide alors que commune.code_departement était déjà rempli.
UPDATE commune SET code_departement = NULL WHERE code_departement = '';

INSERT IGNORE INTO departement (code_departement, nom_departement, code_region)
SELECT DISTINCT TRIM(c.code_departement), NULL, c.code_region
FROM commune c
WHERE c.code_departement IS NOT NULL
  AND TRIM(c.code_departement) <> '';

UPDATE commune c
LEFT JOIN departement d ON d.code_departement = c.code_departement
SET c.code_departement = NULL
WHERE c.code_departement IS NOT NULL AND d.code_departement IS NULL;

SET @fk_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = 'clubs_sportifs'
      AND TABLE_NAME = 'commune'
      AND CONSTRAINT_NAME = 'fk_commune_departement'
);
SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE commune ADD CONSTRAINT fk_commune_departement FOREIGN KEY (code_departement) REFERENCES departement(code_departement)',
    'SELECT ''fk_commune_departement : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS statistique_clubs_opendata (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS statistique_licencies_opendata (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Colonnes utilisateur (vérif inscription / pièce d’identité) — optionnel si déjà fait à la main
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'photo_identite'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN photo_identite VARCHAR(255) NULL AFTER role',
    'SELECT ''utilisateur.photo_identite : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'statut_verification'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN statut_verification VARCHAR(20) NOT NULL DEFAULT ''EN_ATTENTE'' AFTER photo_identite',
    'SELECT ''utilisateur.statut_verification : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT 'migration_opendata.sql terminé' AS ok;
