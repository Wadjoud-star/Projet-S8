package com.clubsport.servlet;

import java.io.IOException;

import com.clubsport.dao.ClubDAO;
import com.clubsport.model.Club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/acteur/gerer-cotisation")
public class GestionCotisationServlet extends HttpServlet {

    private ClubDAO clubDAO = new ClubDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Club club = clubDAO.findById(2);
        request.setAttribute("club", club);

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/gerer-cotisation.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String cotisation = request.getParameter("cotisation");
        clubDAO.updateCotisation(2, cotisation);

        response.sendRedirect("/acteur/gerer-cotisation");
    }
}