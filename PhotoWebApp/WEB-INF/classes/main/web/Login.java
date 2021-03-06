package main.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.util.DBConnection;

/**
 *  Backing servlet for the Login screen (Login.jsp)
 * 
 *  @author Gabriel Caciula
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     *  GET command for Login.jsp
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Login.jsp").forward(request, response);
	}
	
    /**
     *  POST command for Login.jsp
     *  Logs the user in based on the input parameters
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String username = (request.getParameter("username")).trim();
        String password = (request.getParameter("password")).trim();
        
    	String errorMessage = "";
    	
    	if (username.isEmpty()||password.isEmpty()) {
    		errorMessage = "One or more fields are empty.";
    	} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				PreparedStatement query = connection.prepareStatement("select count(*) from users where user_name = ? and password = ?");
				query.setString(1, username);
				query.setString(2, password);
				
				ResultSet resultSet = query.executeQuery();
				
				if (resultSet != null && resultSet.next()) {
					if (resultSet.getInt(1) == 1) {
						HttpSession session = request.getSession();
						session.setAttribute("username", username);
					} else {
						errorMessage = "Invalid username or password.";
					}
				}
				
				connection.close();
			} catch (Exception e) {
			    errorMessage = "An error occurred while logging in. Please try again.";
			}
    	}
	
    	if (errorMessage.isEmpty()) {
    	    response.sendRedirect("ViewProfile?" + username);
    	} else {
	    	request.setAttribute("errorMessage", errorMessage);
	    	request.setAttribute("errorBackLink", "/PhotoWebApp/Login");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
    	}
	}
}

