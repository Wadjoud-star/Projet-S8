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

@WebServlet("/acteur/creer-club")
public class CreerClubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ClubDAO clubDAO = new ClubDAO();

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

        if (clubDAO.userHasClub(userId)) {
            request.setAttribute("message", "Vous avez déjà créé un club.");
            request.getRequestDispatcher("/WEB-INF/jsp/acteur/club-deja-cree.jsp")
                    .forward(request, response);
            return;
        }

        request.setAttribute("regions", clubDAO.listRegions());
        request.setAttribute("communes", clubDAO.listCommunes());
        request.setAttribute("federations", clubDAO.listFederations());

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/creer-club.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        if (clubDAO.userHasClub(userId)) {
            request.setAttribute("message", "Vous avez déjà créé un club.");
            request.getRequestDispatcher("/WEB-INF/jsp/acteur/club-deja-cree.jsp")
                    .forward(request, response);
            return;
        }

        Club club = new Club();

        club.setNom(request.getParameter("nom"));
        club.setAdresse(request.getParameter("adresse"));
        club.setCodePostal(request.getParameter("codePostal"));
        club.setLatitude(0.0);
        club.setLongitude(0.0);
        club.setNbLicencies(Integer.parseInt(request.getParameter("nbLicencies")));
        club.setNbFemmes(Integer.parseInt(request.getParameter("nbFemmes")));
        club.setNbHommes(Integer.parseInt(request.getParameter("nbHommes")));

        club.setCodeCommune(request.getParameter("codeCommune"));
        club.setCodeFederation(request.getParameter("codeFederation"));

        int idClub = clubDAO.createClub(club, userId);

        if (idClub > 0) {
            response.sendRedirect(request.getContextPath() + "/acteur/gestion-club");
        } else {
            request.setAttribute("message", "Erreur lors de la création du club.");
            request.setAttribute("regions", clubDAO.listRegions());
            request.setAttribute("communes", clubDAO.listCommunes());
            request.setAttribute("federations", clubDAO.listFederations());

            request.getRequestDispatcher("/WEB-INF/jsp/acteur/creer-club.jsp")
                    .forward(request, response);
        }
    }
}
