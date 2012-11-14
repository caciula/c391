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

public class CreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("username") == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Login.jsp");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/CreateGroup.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inputGroupname = (request.getParameter("groupname")).trim();
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        String errorMessage = "";
        
        if (inputGroupname.isEmpty()) {
            errorMessage = "The Group Name field cannot be empty.";
        } else {
	        try {
	        	Connection connection = DBConnection.createConnection();
	        	
	        	PreparedStatement query1 = connection.prepareStatement("select count(*) from groups where user_name = ? and group_name = ?");
	        	query1.setString(1, username);
	        	query1.setString(2, inputGroupname);
	        	
	        	ResultSet resultSet1 = query1.executeQuery();
	        	
	        	if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
					    errorMessage = "Multiple groups with the same name cannot be created by the same user.";
					} else {
						PreparedStatement query2 = connection.prepareStatement("select max(group_id) from groups");
						
						ResultSet resultSet2 = query2.executeQuery();
						
						if (resultSet2 != null && resultSet2.next()) {
							int groupID = resultSet2.getInt(1);
							groupID++;
							
							PreparedStatement query3 = connection.prepareStatement("insert into groups values (?, ?, ?, ?)");
							query3.setInt(1, groupID);
							query3.setString(2, username);
							query3.setString(3, inputGroupname);
							query3.setDate(4, sqlDate);
							
							query3.executeUpdate();
							connection.commit();
						}
					}
				}
	        	
	        	connection.close();
	        } catch (Exception e) {
                System.out.println("An error occured while creating a group: " + e);
	            errorMessage = "An error occurred while creating the group.";
	        }
        }
        
        if (errorMessage.isEmpty()) {
        	response.sendRedirect("/PhotoWebApp/ViewUserImages?" + username);
        } else {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/CreateGroup");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
        }
	}
}