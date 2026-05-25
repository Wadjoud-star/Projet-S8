package com.clubsport.servlet;

import jakarta.servlet.http.HttpSession;

final class UtilisateurServletHelper {

	private UtilisateurServletHelper() {
	}

	static boolean estUtilisateur(HttpSession session) {
		if (session == null) {
			return false;
		}
		Object role = session.getAttribute("Role");
		return role != null && "utilisateur".equalsIgnoreCase(String.valueOf(role));
	}

	static Integer userIdFromSession(HttpSession session) {
		if (session == null) {
			return null;
		}
		Object id = session.getAttribute("UserId");
		if (id instanceof Integer) {
			return (Integer) id;
		}
		if (id instanceof Number) {
			return ((Number) id).intValue();
		}
		return null;
	}
}
