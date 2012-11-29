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

/**
 *  Backing servlet for the Account Management screen (AccountManagement.jsp)
 * 
 *  @author Gabriel Caciula
 */
public class AccountManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 *  GET command for AccountManagement.jsp
	 *  
	 *  Displays the user's personal information
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		//ensures that there is a user logged in
		if (username == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Login");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
		} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				//obtains account information of the user
				PreparedStatement query1 = connection.prepareStatement(SQLQueries.GET_PERSON_BY_USER_NAME);
				query1.setString(1, username);
				
				ResultSet resultSet1 = query1.executeQuery();
				
				if (resultSet1 != null && resultSet1.next()) {
					String firstname = resultSet1.getString("first_name");
					String lastname = resultSet1.getString("last_name");
					String address = resultSet1.getString("address");
					String email = resultSet1.getString("email");
					String phone = resultSet1.getString("phone");
					
					request.setAttribute("username", username);
					request.setAttribute("firstname", firstname);
					request.setAttribute("lastname", lastname);
					request.setAttribute("address", address);
					request.setAttribute("email", email);
					request.setAttribute("phone", phone);
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
                request.setAttribute("errorBackLink", "/PhotoWebApp/AccountManagement");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
			}
			
			request.getRequestDispatcher("/AccountManagement.jsp").forward(request, response);
		}
	}

	/**
	 *  POST command for AccountManagement.jsp
	 *  
	 *  Verifies that the user input is correct (email isn't in use by another user) before updating their personal information
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) (request.getSession()).getAttribute("username");
		String password = (request.getParameter("password")).trim();
		String firstname = (request.getParameter("firstname")).trim();
		String lastname = (request.getParameter("lastname")).trim();
		String address = (request.getParameter("address")).trim();
		String email = (request.getParameter("email")).trim();
		String phone = (request.getParameter("phone")).trim();

		String errorMessage = "";
		
		if (password.isEmpty()||firstname.isEmpty()||lastname.isEmpty()||address.isEmpty()||email.isEmpty()||phone.isEmpty()) {
			errorMessage = "One or more fields are empty.";
		} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				//checks to see if the specified email already belongs to another user
				PreparedStatement query1 = connection.prepareStatement("select count(*) from persons where email = ? and user_name != ?");
	        	query1.setString(1, email);
	        	query1.setString(2, username);
				
	        	ResultSet resultSet1 = query1.executeQuery();
	        	
	        	if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
					    errorMessage = "Email already exists. Please try again.";
					} else {	
						//updates that user's personal information
						PreparedStatement query2 = connection.prepareStatement("update persons set first_name = ?, last_name = ?, address = ?, email = ?, phone = ? where user_name = ?");
						query2.setString(1, firstname);
						query2.setString(2, lastname);
						query2.setString(3, address);
						query2.setString(4, email);
						query2.setString(5, phone);
						query2.setString(6, username);
						
						//update the user's password
						PreparedStatement query3 = connection.prepareStatement("update users set password = ? where user_name = ?");
						query3.setString(1, password);
						query3.setString(2, username);
						
						query2.executeUpdate();
						query3.executeUpdate();
						
						connection.commit();
					}
				}
				
	        	connection.close();
			} catch (Exception e) {
				System.out.println("An error occurred while updating personal information: " + e);
				errorMessage = "An error occurred while updating personal information.";
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
