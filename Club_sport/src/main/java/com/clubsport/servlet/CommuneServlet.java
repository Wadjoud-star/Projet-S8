package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.clubsport.dao.CommuneDAO;
import com.clubsport.util.JsonUtil;

/**
 * Servlet implementation class CommuneServlet
 */
@WebServlet("/api/communes")
public class CommuneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommuneServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String recherche = request.getParameter("recherche");
        CommuneDAO cDAO = new CommuneDAO();
        try {
            response.getWriter().print(JsonUtil.toJson(cDAO.rechercheParNom(recherche)));
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().print("{\"erreur\":\"Erreur base de données\"}");
        }
	}

}
