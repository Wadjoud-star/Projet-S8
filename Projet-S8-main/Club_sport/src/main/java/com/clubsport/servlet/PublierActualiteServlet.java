package com.clubsport.servlet;

import java.io.IOException;

import com.clubsport.dao.ClubDAO;
import com.clubsport.model.Club;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/acteur/publier-actualite")
public class PublierActualiteServlet extends HttpServlet {

    private ClubDAO clubDAO = new ClubDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Club club = clubDAO.findById(2);

        request.setAttribute("club", club);

        request.getRequestDispatcher("/WEB-INF/jsp/acteur/publier-actualite.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String actualite = request.getParameter("actualite");

        clubDAO.updateActualite(2, actualite);

        response.sendRedirect("/acteur/publier-actualite");
    }
}