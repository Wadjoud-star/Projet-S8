package com.clubsport.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/clubs_sportifs";
    private static final String USER     = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    // Constructeur privé : personne ne peut faire new ConnexionDB()
    private ConnexionDB() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL introuvable : " + e.getMessage());
        }
    }

    public static void fermer(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur fermeture connexion : " + e.getMessage());
            }
        }
    }
}