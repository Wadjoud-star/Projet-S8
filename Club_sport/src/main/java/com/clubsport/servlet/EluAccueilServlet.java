package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.clubsport.dao.UserDAO;
import com.clubsport.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur : page d'accueil de l'espace élus (vue JSP).
 */
@WebServlet("/elu")
public class EluAccueilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String prenom = "élu";
        HttpSession session = req.getSession(false);
        if (session != null) {
            String nom = (String) session.getAttribute("Nom");
            if (nom == null || nom.isBlank()) {
                String email = (String) session.getAttribute("Email");
                if (email != null && !email.isBlank()) {
                    try {
                        User u = new UserDAO().getUserbymail(email);
                        if (u != null && u.getNom() != null) {
                            nom = u.getNom();
                            session.setAttribute("Nom", nom);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (nom != null && !nom.isBlank()) {
                prenom = extrairePrenom(nom);
            }
        }
        req.setAttribute("prenom", prenom);
        req.getRequestDispatcher("/WEB-INF/jsp/elus/accueil.jsp").forward(req, resp);
    }

    /** Premier mot du nom complet (ex. "Bill Dupont" → "Bill"). */
    static String extrairePrenom(String nomComplet) {
        String n = nomComplet.trim();
        if (n.isEmpty()) {
            return "élu";
        }
        String prenom = n.split("\\s+")[0];
        if (prenom.length() == 1) {
            return prenom.toUpperCase();
        }
        if (prenom.length() <= 1) {
            return prenom.toUpperCase();
        }
        return prenom.substring(0, 1).toUpperCase() + prenom.substring(1);
    }
}
