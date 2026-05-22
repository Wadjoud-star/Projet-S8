-- Journal des actions admin (création / modification / suppression utilisateur, validation inscription).
-- À exécuter une fois sur clubs_sportifs :
--   mysql -h 127.0.0.1 -uroot -proot clubs_sportifs < docker/mysql/migration_audit_action.sql
-- Idempotent : CREATE TABLE IF NOT EXISTS.

USE clubs_sportifs;

CREATE TABLE IF NOT EXISTS audit_action (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Types d'action attendus (à respecter côté Java, pas de ENUM en BDD pour rester souple) :
--   Création utilisateur
--   Modification utilisateur
--   Suppression utilisateur
--   Validation inscription
