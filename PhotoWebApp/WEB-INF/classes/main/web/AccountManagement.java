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
import main.util.SQLQueries;

public class AccountManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		//ensures that there is a user logged in
		if (username == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Login.jsp");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
		} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				PreparedStatement query1 = connection.prepareStatement(SQLQueries.GET_PERSON_BY_USER_NAME);
				query1.setString(1, username);
				
				ResultSet resultSet1 = query1.executeQuery();
				
				if (resultSet1 != null && resultSet1.next()) {
					String firstname = resultSet1.getString("first_name");
					String lastname = resultSet1.getString("last_name");
					String address = resultSet1.getString("address");
					String email = resultSet1.getString("email");
					String phonenumber = resultSet1.getString("phone");
					
					request.setAttribute("username", username);
					request.setAttribute("firstname", firstname);
					request.setAttribute("lastname", lastname);
					request.setAttribute("address", address);
					request.setAttribute("email", email);
					request.setAttribute("phonenumber", phonenumber);
				}
				
				PreparedStatement query2 = connection.prepareStatement("select password from users where user_name = ?");
				query2.setString(1, username);
				
				ResultSet resultSet2 = query2.executeQuery();
				
				if (resultSet2 != null && resultSet2.next()) {
					String password = resultSet2.getString("password");
					
					request.setAttribute("password", password);
				}
				
				connection.close();
			} catch (Exception e) {
				System.out.println("An error occured while obtaining account information: " + e);
                request.setAttribute("errorMessage", "An error occured while obtaining account information.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/Home.jsp");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
	            return;
			}
			
			request.getRequestDispatcher("/AccountManagement.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) (request.getSession()).getAttribute("username");
		String inputPassword = (request.getParameter("password")).trim();
		String inputFirstname = (request.getParameter("firstname")).trim();
		String inputLastname = (request.getParameter("lastname")).trim();
		String inputAddress = (request.getParameter("address")).trim();
		String inputEmail = (request.getParameter("email")).trim();
		String inputPhonenumber = (request.getParameter("phonenumber")).trim();

		String errorMessage = "";
		
		if (inputPassword.isEmpty()||inputFirstname.isEmpty()||inputLastname.isEmpty()||inputAddress.isEmpty()||inputEmail.isEmpty()||inputPhonenumber.isEmpty()) {
			errorMessage = "One or more fields are empty.";
		} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				PreparedStatement query1 = connection.prepareStatement("select count(*) from persons where email = ? and user_name != ?");
	        	query1.setString(1, inputEmail);
	        	query1.setString(2, username);
				
	        	ResultSet resultSet1 = query1.executeQuery();
	        	
	        	if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
					    errorMessage = "Email already exists. Please try again.";
					} else {	
						//updates that user's personal information
						PreparedStatement query2 = connection.prepareStatement("update persons set first_name=?, last_name=?, address=?, email=?, phone=? where user_name=?");
						query2.setString(1, inputFirstname);
						query2.setString(2, inputLastname);
						query2.setString(3, inputAddress);
						query2.setString(4, inputEmail);
						query2.setString(5, inputPhonenumber);
						query2.setString(6, username);
						
						PreparedStatement query3 = connection.prepareStatement("update users set password=? where user_name=?");
						query3.setString(1, inputPassword);
						query3.setString(2, username);
						
						query2.executeUpdate();
						query3.executeUpdate();
						
						connection.commit();
					}
				}
				
	        	connection.close();
			} catch (Exception e) {
				errorMessage = "An error occurred while updating personal information. Please try again.";
			}
		}
		
		if (errorMessage.isEmpty()) {
			response.sendRedirect("/PhotoWebApp/ViewProfile?" + username);
		} else {
			request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/AccountManagement");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}
}
