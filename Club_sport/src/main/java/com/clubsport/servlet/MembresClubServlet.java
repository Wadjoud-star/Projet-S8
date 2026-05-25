package com.clubsport.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clubsport.util.ConnexionDB;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/acteur/membres-club")
public class MembresClubServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //列表加键值对
        List<Map<String, String>> membres = new ArrayList<>();

        try {
            Connection conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, nom, email, role, statut_verification FROM utilisateur WHERE id_club = ?"
            );
            //这里要改成本club的而不是固定id_club=2
            ps.setInt(1, 2);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> m = new HashMap<>();
                m.put("id", rs.getString("id"));
                m.put("nom", rs.getString("nom"));
                m.put("email", rs.getString("email"));
                m.put("role", rs.getString("role"));
                m.put("statut", rs.getString("statut_verification"));
                membres.add(m);
            }

            ConnexionDB.fermer(conn);

        } catch (Exception e) {
            //打印错误
            e.printStackTrace();
        }

        request.setAttribute("membres", membres);
        request.getRequestDispatcher("/WEB-INF/jsp/acteur/membres-club.jsp")
                .forward(request, response);
    }
}