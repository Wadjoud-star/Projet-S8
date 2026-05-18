# Restauration de la base `clubs_sportifs` depuis un dump SQL

Ce guide explique quoi faire **des la reception du fichier dump** jusqu'a une base operationnelle.

Le fichier exporte depuis MySQL Workbench avec `Include Create Schema` contient en general:
- `CREATE DATABASE ...`
- `USE clubs_sportifs`
- les `CREATE TABLE ...`
- les `INSERT INTO ...` (donnees)

Donc, oui: si vos camarades importent ce fichier, ils recuperent schema + donnees.

---

## 1) Avant de commencer (tout le monde)

1. Recuperer le fichier (exemple: `dump_clubs_sportifs.sql`).
2. Verifier l'acces MySQL:
   - hote: `127.0.0.1`
   - port: `3306`
   - utilisateur: `root`
   - mot de passe: `root` (a adapter si necessaire)
3. Fermer les applications qui utilisent la base pendant l'import (option recommandee).

---

## 2) Methode A - Import via MySQL Workbench

1. Ouvrir MySQL Workbench et se connecter au serveur.
2. Aller dans:
   - `Server` -> `Data Import` (ou `Administration` -> `Data Import/Restore` selon version).
3. Choisir:
   - `Import from Self-Contained File`
   - selectionner `dump_clubs_sportifs.sql`
4. Cible d'import:
   - si le dump contient `CREATE DATABASE` + `USE`, laisser MySQL executer tel quel.
   - sinon, choisir schema cible `clubs_sportifs`.
5. Cliquer `Start Import`.
6. A la fin, rafraichir le panneau `Schemas`.

Verification rapide dans Workbench:

```sql
USE clubs_sportifs;
SELECT COUNT(*) AS nb_lic_opendata FROM statistique_licencies_opendata;
SELECT COUNT(*) AS nb_clubs_opendata FROM statistique_clubs_opendata;
SELECT COUNT(*) AS nb_lic_agrege FROM statistique_licencies;
SELECT COUNT(*) AS nb_clubs_agrege FROM statistique_clubs;
```

---

## 3) Methode B - Import en ligne de commande

### 3.1 Import standard

```bash
mysql -h 127.0.0.1 -P 3306 -u root -proot < dump_clubs_sportifs.sql
```

Si le dump ne contient pas `CREATE DATABASE`/`USE`, cibler directement la base:

```bash
mysql -h 127.0.0.1 -P 3306 -u root -proot clubs_sportifs < dump_clubs_sportifs.sql
```

### 3.2 Verification en CLI

```bash
mysql -h 127.0.0.1 -P 3306 -u root -proot -e "USE clubs_sportifs; SHOW TABLES;"
mysql -h 127.0.0.1 -P 3306 -u root -proot -e "USE clubs_sportifs; SELECT COUNT(*) AS nb FROM statistique_licencies_opendata;"
```

---

## 4) Si vous utilisez Docker pour MySQL

Verifier que le conteneur MySQL tourne avant import:

```bash
docker ps --filter name=club_mysql
```

Si besoin:

```bash
cd Club_sport
docker compose up -d mysql
```

> Note: inutile de lancer le Tomcat Docker si vous utilisez Tomcat Eclipse local.

---

## 5) Erreurs frequentes et solution rapide

- `Unknown database clubs_sportifs`
  - le dump n'a pas cree la base, ou import dans la mauvaise instance.
  - solution: creer la base puis relancer, ou importer avec `Include Create Schema`.

- `Table ... doesn't exist`
  - import incomplet ou mauvais ordre de scripts.
  - solution: relancer l'import du dump complet.

- `Access denied for user`
  - identifiants MySQL incorrects.
  - solution: corriger user/password/hote/port.

- `Duplicate entry` pendant import
  - donnees deja presentes.
  - solution: repartir d'une base vide ou drop/recreate schema avant import.

---

## 6) Procedure recommandee pour le groupe

1. Recupere le fichier `dump_clubs_sportifs.sql`.
2. Importe-le (Workbench ou CLI).
3. Verifie les `COUNT(*)` sur tables opendata + agregees.
4. Lance l'application.
5. Si besoin, partage captures de verification (tables + compteurs) pour validation d'equipe.
