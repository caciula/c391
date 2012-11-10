package main.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.DBConnection;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (session.getAttribute("username") == null) {
			response.sendRedirect("/PhotoWebApp/Login");
		} else {
			request.getRequestDispatcher("/Register.jsp").forward(request, response);
		}
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
        String currentDate = sqlDate.toString();
        
        String query1 = "select count(*) from users where user_name = '"+inputUsername+"'";
        String query2 = "select count(*) from persons where email = '"+inputEmail+"'";
        String query3 = "insert into users values('"+inputUsername+"', '"+inputPassword+"', TO_DATE('"+currentDate+"', 'YYYY-MM-DD'))";
        String query4 = "insert into persons values('"+inputUsername+"', '"+inputFirstname+"', '"+inputLastname+"', '"+inputAddress+"', '"+inputEmail+"', '"+inputPhonenumber+"')";

        String output = "";
        String redirection = "";
        
        if (inputUsername.isEmpty()||inputPassword.isEmpty()||inputFirstname.isEmpty()||inputLastname.isEmpty()||inputAddress.isEmpty()||inputEmail.isEmpty()||inputPhonenumber.isEmpty()) {
        	output = "error: one or more empty fields";
        	redirection = "/Register.jsp";
        } else {
	        try {
	        	Connection connection = DBConnection.createConnection();
				ResultSet resultSet1 = DBConnection.executeQuery(connection, query1);
				ResultSet resultSet2 = DBConnection.executeQuery(connection, query2);
				
				if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
						output = "error: user already exists";
						redirection = "/Register.jsp";
					} else {
						if (resultSet2 != null && resultSet2.next()) {
							if (resultSet2.getInt(1) == 1) {
								output = "error: email already exists";
								redirection = "/Register.jsp";
							} else {
								DBConnection.executeQuery(connection, query3);
								DBConnection.executeQuery(connection, query4);
								connection.commit();
								output = "";
								redirection = "/PhotoWebApp/Login";
							}
						}
					}
				}
				
				connection.close();
	        } catch (Exception e) {
	        	output = "error: couldn't complete request";
	        	redirection = "/Register.jsp";
	        }
        }
        
        if (output.isEmpty()) {
        	response.sendRedirect(redirection);
        } else {
        	request.setAttribute("output", output);
        	request.getRequestDispatcher(redirection).forward(request, response);
        }
	}
}
