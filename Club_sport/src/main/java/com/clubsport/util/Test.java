package com.clubsport.util;

import java.sql.Connection;


public class Test {
    public static void main(String[] args) {
        try {
            Connection conn = ConnexionDB.getConnection();
            System.out.println("Connexion réussie !");
            ConnexionDB.fermer(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}