package main.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.DBConnection;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Login.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String inputUsername = (request.getParameter("username")).trim();
        String inputPassword = (request.getParameter("password")).trim();
        String query = "select count(*) from users where user_name = '"+inputUsername+"' and password = '"+inputPassword+"'";
    	
    	String output = "";
    	
    	try {
			Connection connection = DBConnection.createConnection();
			ResultSet resultSet = DBConnection.executeQuery(connection, query);
			
			while(resultSet != null && resultSet.next()) {
				if (resultSet.getInt(1) == 1) {
					output = "Successfully logged in";
				} else {
					output = "Invalid username/password";
				}
			}
			
			DBConnection.closeConnection(connection);
		} catch (Exception e) {
			output = "error: couldn't complete request";
		}
        
		request.setAttribute("output", output);
		request.getRequestDispatcher("/Login.jsp").forward(request, response);
	}
}

