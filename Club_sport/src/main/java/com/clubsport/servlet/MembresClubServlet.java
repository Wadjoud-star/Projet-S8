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
import jakarta.servlet.http.HttpSession;

@WebServlet("/acteur/membres-club")
public class MembresClubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, String>> membres = new ArrayList<>();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("UserId") == null) {
            response.sendRedirect(request.getContextPath() + "/authentification.jsp");
            return;
        }

        int userId = (int) session.getAttribute("UserId");

        String sql = """
            SELECT u.id, u.nom, u.email, u.role, u.statut_verification
            FROM utilisateur u
            JOIN espace_club e ON u.id = e.id
            WHERE e.id = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("membres", membres);

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/membres-club.jsp")
                .forward(request, response);
    }
}
