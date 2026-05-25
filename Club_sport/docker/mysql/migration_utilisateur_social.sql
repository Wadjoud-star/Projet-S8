-- =============================================================================
-- Migration : espace utilisateur lambda + social (publications, abonnements, likes,
-- commentaires, notifications in-app).
--
-- Prérequis : MySQL du projet démarré (docker compose up mysql).
--
-- Exécution (depuis le dossier Club_sport/) :
--
--   docker exec -i club_mysql mysql -uroot -proot clubs_sportifs < docker/mysql/migration_utilisateur_social.sql
--
-- Vérification :
--
--   docker exec -it club_mysql mysql -uroot -proot -e "SHOW TABLES LIKE 'publication%'; SHOW TABLES LIKE 'abonnement';" clubs_sportifs
--
-- Idempotent : CREATE TABLE IF NOT EXISTS + colonnes utilisateur ajoutées une seule fois.
-- =============================================================================

USE clubs_sportifs;

-- -----------------------------------------------------------------------------
-- 1. Profil utilisateur (colonnes sur compte existant)
-- photo_identite = pièce d'identité (admin) | photo_profil = avatar réseau social
-- -----------------------------------------------------------------------------

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'photo_profil'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN photo_profil VARCHAR(255) NULL AFTER photo_identite',
    'SELECT ''utilisateur.photo_profil : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'telephone'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN telephone VARCHAR(20) NULL AFTER photo_profil',
    'SELECT ''utilisateur.telephone : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'bio'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN bio TEXT NULL AFTER telephone',
    'SELECT ''utilisateur.bio : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'clubs_sportifs' AND TABLE_NAME = 'utilisateur' AND COLUMN_NAME = 'date_inscription'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE utilisateur ADD COLUMN date_inscription DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER bio',
    'SELECT ''utilisateur.date_inscription : déjà là'' AS migration_note');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Rôle attendu côté appli pour l''utilisateur lambda : role = ''utilisateur''
-- Inscription lambda : statut_verification = ''VALIDE'' (pas EN_ATTENTE)

-- -----------------------------------------------------------------------------
-- 2. Préférences sportives (fédérations favorites)
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS utilisateur_federation (
    id_utilisateur INT NOT NULL,
    code_federation VARCHAR(10) NOT NULL,
    date_ajout TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utilisateur, code_federation),
    CONSTRAINT fk_uf_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_uf_federation FOREIGN KEY (code_federation) REFERENCES federation(code_federation)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 3. Publications (auteur = acteur sportif, utilisateur.id)
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_auteur INT NOT NULL COMMENT 'utilisateur avec role acteur',
    titre VARCHAR(200) NOT NULL,
    contenu TEXT NULL,
    image_url VARCHAR(255) NULL,
    date_publication DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_publication_auteur FOREIGN KEY (id_auteur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_publication_date (date_publication DESC),
    INDEX idx_publication_auteur (id_auteur)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Plusieurs activités par publication (besoin acteurs)
CREATE TABLE IF NOT EXISTS publication_activite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_publication INT NOT NULL,
    libelle VARCHAR(150) NOT NULL,
    type_activite VARCHAR(80) NULL,
    lieu VARCHAR(150) NULL,
    date_debut DATETIME NULL,
    description TEXT NULL,
    CONSTRAINT fk_pub_activite_publication FOREIGN KEY (id_publication) REFERENCES publication(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_pub_activite_publication (id_publication)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 4. Abonnement utilisateur lambda → acteur sportif
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS abonnement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL COMMENT 'utilisateur lambda',
    id_acteur INT NOT NULL COMMENT 'utilisateur acteur',
    date_abonnement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_abonnement_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_abonnement_acteur FOREIGN KEY (id_acteur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uq_abonnement (id_utilisateur, id_acteur),
    INDEX idx_abonnement_acteur (id_acteur)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 5. Likes et commentaires sur publications
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS publication_like (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_publication INT NOT NULL,
    id_utilisateur INT NOT NULL,
    date_like TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_publication FOREIGN KEY (id_publication) REFERENCES publication(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_like_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uq_publication_like (id_publication, id_utilisateur),
    INDEX idx_like_publication (id_publication)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS publication_commentaire (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_publication INT NOT NULL,
    id_utilisateur INT NOT NULL,
    texte TEXT NOT NULL,
    date_commentaire TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_publication FOREIGN KEY (id_publication) REFERENCES publication(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_comment_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_comment_publication (id_publication, date_commentaire)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 6. Notifications in-app (APK)
-- Types suggérés : NOUVELLE_PUBLICATION, NOUVEAU_LIKE, NOUVEAU_COMMENTAIRE
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    type_notification VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    id_publication INT NULL,
    lu BOOLEAN NOT NULL DEFAULT FALSE,
    date_notification TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_notification_publication FOREIGN KEY (id_publication) REFERENCES publication(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_notification_user_lu (id_utilisateur, lu, date_notification DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Fin migration
