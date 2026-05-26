package com.clubsport.servlet;

import java.io.IOException;

import com.clubsport.dao.ClubDAO;
import com.clubsport.model.Club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/acteur/horaires")
public class GestionHorairesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ClubDAO clubDAO = new ClubDAO();

    private String escapeHtml(String input) {
        if (input == null) return "";

        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("UserId") == null) {
            response.sendRedirect(request.getContextPath() + "/authentification.jsp");
            return;
        }

        int userId = (int) session.getAttribute("UserId");

        Club club = clubDAO.findByUserId(userId);

        if (club == null) {
            response.sendRedirect(request.getContextPath() + "/acteur/creer-club");
            return;
        }

        request.setAttribute("club", club);

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/gerer-horaires.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("UserId") == null) {
            response.sendRedirect(request.getContextPath() + "/authentification.jsp");
            return;
        }

        int userId = (int) session.getAttribute("UserId");

        Club club = clubDAO.findByUserId(userId);

        if (club == null) {
            response.sendRedirect(request.getContextPath() + "/acteur/creer-club");
            return;
        }

        String horaires = escapeHtml(request.getParameter("horaires"));

        clubDAO.updateHoraires(club.getIdClub(), horaires);

        response.sendRedirect(request.getContextPath() + "/acteur/horaires");
    }
}
