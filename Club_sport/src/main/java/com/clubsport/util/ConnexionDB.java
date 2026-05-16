package com.clubsport.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {

    /**
     * Eclipse + MySQL Docker sur le Mac : par défaut 127.0.0.1:3306.
     * Tomcat dans Docker (compose) : définir MYSQL_HOST=mysql.
     */
    private static final String JDBC_URL = buildJdbcUrl();
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private ConnexionDB() {
    }

    private static String buildJdbcUrl() {
        String host = System.getenv("MYSQL_HOST");
        if (host == null || host.isBlank()) {
            host = "127.0.0.1";
        }
        String port = System.getenv("MYSQL_PORT");
        if (port == null || port.isBlank()) {
            port = "3306";
        }
        return "jdbc:mysql://" + host + ":" + port + "/clubs_sportifs";
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
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
