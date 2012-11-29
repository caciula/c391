package main.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.DBConnection;

/**
 *  Backing servlet for the Register screen (Register.jsp)
 * 
 *  @author Gabriel Caciula
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     *  GET command for Register.jsp
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Register.jsp").forward(request, response);
	}

    /**
     *  POST command for Register.jsp
     *  
     *  Checks the input from the user and verifies that it is correct (username doesn't exist, email doesn't exist) before adding
     *  the user to the database
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (request.getParameter("username")).trim();
        String password = (request.getParameter("password")).trim();
        String firstname = (request.getParameter("firstname")).trim();
        String lastname = (request.getParameter("lastname")).trim();
        String address = (request.getParameter("address")).trim();
        String email = (request.getParameter("email")).trim();
        String phone = (request.getParameter("phone")).trim();
        
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        String errorMessage = "";
        
        if (username.isEmpty()||password.isEmpty()||firstname.isEmpty()||lastname.isEmpty()||address.isEmpty()||email.isEmpty()||phone.isEmpty()) {
            errorMessage = "One or more fields are empty.";
        } else {
	        try {
	        	Connection connection = DBConnection.createConnection();
	        	
	        	//checks to see if the username is already in use
	        	PreparedStatement query1 = connection.prepareStatement("select count(*) from users where user_name = ?");
	        	query1.setString(1, username);
	        	
				ResultSet resultSet1 = query1.executeQuery();
				
				if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
					    errorMessage = "User already exists. Please try again.";
					} else {
						//checks to see if the email is already in use
						PreparedStatement query2 = connection.prepareStatement("select count(*) from persons where email = ?");
						query2.setString(1, email);
						
						ResultSet resultSet2 = query2.executeQuery();
						
						if (resultSet2 != null && resultSet2.next()) {
							if (resultSet2.getInt(1) == 1) {
							    errorMessage = "Email already exists. Please try again.";
							} else {
								//creates a user
								PreparedStatement query3 = connection.prepareStatement("insert into users values(?, ?, ?)");
								query3.setString(1, username);
								query3.setString(2, password);
								query3.setDate(3, sqlDate);
								
								//updates that user's personal information
								PreparedStatement query4 = connection.prepareStatement("insert into persons values(?, ?, ?, ?, ?, ?)");
								query4.setString(1, username);
								query4.setString(2, firstname);
								query4.setString(3, lastname);
								query4.setString(4, address);
								query4.setString(5, email);
								query4.setString(6, phone);
								
								query3.executeUpdate();
								query4.executeUpdate();
								
								connection.commit();
							}
						}
					}
				}
				
				connection.close();
	        } catch (Exception e) {
	            errorMessage = "An error occurred while creating a new user. Please try again.";
	        }
        }
        
        if (errorMessage.isEmpty()) {
            response.sendRedirect("/PhotoWebApp/Login");
        } else {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/Register");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
        }
	}
}
