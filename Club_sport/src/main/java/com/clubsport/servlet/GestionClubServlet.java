package com.clubsport.servlet;

import java.io.IOException;

import com.clubsport.dao.ClubDAO;
import com.clubsport.model.Club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/acteur/gestion-club")
public class GestionClubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ClubDAO clubDAO = new ClubDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Club club = clubDAO.findById(2);

        request.setAttribute("club", club);

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/gestion-club.jsp")
               .forward(request, response);
    }
    	@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    Club club = new Club();
    club.setIdClub(2);
    club.setNom(request.getParameter("nom"));
    club.setAdresse(request.getParameter("adresse"));
    club.setCodePostal(request.getParameter("codePostal"));
    club.setNbLicencies(Integer.parseInt(request.getParameter("nbLicencies")));
    club.setNbFemmes(Integer.parseInt(request.getParameter("nbFemmes")));
    club.setNbHommes(Integer.parseInt(request.getParameter("nbHommes")));

    clubDAO.update(club);

    response.sendRedirect("/acteur/gestion-club");
}
}