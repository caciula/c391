package main.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Backing servlet for the Logout system
 * 
 *  @author Gabriel Caciula
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     *  GET command
     *  
     *  Logs out a user by removing their username from the session
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		session.setAttribute("username", null);
		response.sendRedirect("/PhotoWebApp/Home");
	}
}