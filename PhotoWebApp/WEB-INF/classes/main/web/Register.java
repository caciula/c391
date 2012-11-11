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

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("username", "");
		request.setAttribute("password", "");
		request.setAttribute("firstname", "");
		request.setAttribute("lastname", "");
		request.setAttribute("address", "");
		request.setAttribute("email", "");
		request.setAttribute("phonenumber", "");
		request.getRequestDispatcher("/Register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inputUsername = (request.getParameter("username")).trim();
        String inputPassword = (request.getParameter("password")).trim();
        String inputFirstname = (request.getParameter("firstname")).trim();
        String inputLastname = (request.getParameter("lastname")).trim();
        String inputAddress = (request.getParameter("address")).trim();
        String inputEmail = (request.getParameter("email")).trim();
        String inputPhonenumber = (request.getParameter("phonenumber")).trim();
        
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        String output = "";
        
        if (inputUsername.isEmpty()||inputPassword.isEmpty()||inputFirstname.isEmpty()||inputLastname.isEmpty()||inputAddress.isEmpty()||inputEmail.isEmpty()||inputPhonenumber.isEmpty()) {
        	output = "error: one or more empty fields";
        } else {
	        try {
	        	Connection connection = DBConnection.createConnection();
	        	
	        	PreparedStatement query1 = connection.prepareStatement("select count(*) from users where user_name = ?");
	        	query1.setString(1, inputUsername);
	        	
	        	PreparedStatement query2 = connection.prepareStatement("select count(*) from persons where email = ?");
	        	query2.setString(1, inputEmail);
	        	
				ResultSet resultSet1 = query1.executeQuery();
				ResultSet resultSet2 = query2.executeQuery();
				
				if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
						output = "error: user already exists";
					} else {
						if (resultSet2 != null && resultSet2.next()) {
							if (resultSet2.getInt(1) == 1) {
								output = "error: email already exists";
							} else {
								PreparedStatement query3 = connection.prepareStatement("insert into users values(?, ?, ?)");
								query3.setString(1, inputUsername);
								query3.setString(2, inputPassword);
								query3.setDate(3, sqlDate);
								
								PreparedStatement query4 = connection.prepareStatement("insert into persons values(?, ?, ?, ?, ?, ?)");
								query4.setString(1, inputUsername);
								query4.setString(2, inputFirstname);
								query4.setString(3, inputLastname);
								query4.setString(4, inputAddress);
								query4.setString(5, inputEmail);
								query4.setString(6, inputPhonenumber);
								
								query3.executeUpdate();
								query4.executeUpdate();
								
								connection.commit();
								
								output = "";
							}
						}
					}
				}
				
				connection.close();
	        } catch (Exception e) {
	        	output = "error: couldn't complete request";
	        }
        }
        
        if (output.isEmpty()) {
        	response.sendRedirect("/PhotoWebApp/Login");
        } else {
        	request.setAttribute("username", inputUsername);
    		request.setAttribute("password", inputPassword);
    		request.setAttribute("firstname", inputFirstname);
    		request.setAttribute("lastname", inputLastname);
    		request.setAttribute("address", inputAddress);
    		request.setAttribute("email", inputEmail);
    		request.setAttribute("phonenumber", inputPhonenumber);
        	request.setAttribute("output", output);
        	request.getRequestDispatcher("/Register.jsp").forward(request, response);
        }
	}
}
