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

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Login.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String inputUsername = (request.getParameter("username")).trim();
        String inputPassword = (request.getParameter("password")).trim();
        
    	String errorMessage = "";
    	
    	if (inputUsername.isEmpty()||inputPassword.isEmpty()) {
    		errorMessage = "One or more fields are empty.";
    	} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				PreparedStatement query = connection.prepareStatement("select count(*) from users where user_name = ? and password = ?");
				query.setString(1, inputUsername);
				query.setString(2, inputPassword);
				
				ResultSet resultSet = query.executeQuery();
				
				if (resultSet != null && resultSet.next()) {
					if (resultSet.getInt(1) == 1) {
						HttpSession session = request.getSession();
						session.setAttribute("username", inputUsername);
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
    	    response.sendRedirect("Home.jsp");
    	} else {
	    	request.setAttribute("errorMessage", errorMessage);
	    	request.setAttribute("errorBackLink", "/PhotoWebApp/Login");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
    	}
	}
}

