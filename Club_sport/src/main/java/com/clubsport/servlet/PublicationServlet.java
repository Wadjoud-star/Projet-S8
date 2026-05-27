package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.clubsport.dao.PublicationDAO;
import com.clubsport.model.Publication;

/**
 * Servlet implementation class PublicationServlet
 */
@WebServlet("/publications")
public class PublicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublicationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            PublicationDAO publicationDAO = new PublicationDAO();
			List<Publication> publications = publicationDAO .findAll();
            request.setAttribute("publications", publications);
            request.getRequestDispatcher("/actualite.jsp")
                   .forward(request, response);
 
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/index.hmtl");
        }
	}

}
