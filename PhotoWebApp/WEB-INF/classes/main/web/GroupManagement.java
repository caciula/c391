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
 *  Backing servlet for the Group Management screen (GroupManagement.jsp)
 * 
 *  @author Gabriel Caciula
 */
public class GroupManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    /**
     *  GET command for GroupManagement.jsp
     */ 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		//ensures that there is a user logged in
		if (session.getAttribute("username") == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Login.jsp");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/GroupManagement.jsp").forward(request, response);
		}
	}

    /**
     *  POST command for GroupManagement.jsp
     *  
     *  Either creates or deletes the specified group, depending on which button is pressed. If a group is deleted, all users belonging
     *  to that group are also removed from it
     */ 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String button = request.getParameter("submit");
		
		String inputGroupname = (request.getParameter("groupname")).trim();
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
        String errorMessage = "";
        
        if (inputGroupname.isEmpty()) {
            errorMessage = "The Group Name field cannot be empty.";
        } else {
        	if (button.equals("Create")) {
        		//the group should be created
        		try {
		        	Connection connection = DBConnection.createConnection();
		        	
		        	//checks to see if the current user has already created a group with the same name
		        	PreparedStatement query11 = connection.prepareStatement("select count(*) from groups where user_name = ? and group_name = ?");
		        	query11.setString(1, username);
		        	query11.setString(2, inputGroupname);
		        	
		        	ResultSet resultSet11 = query11.executeQuery();
		        	
		        	if (resultSet11 != null && resultSet11.next()) {
						if (resultSet11.getInt(1) == 1) {
						    errorMessage = "Multiple groups with the same name cannot be created by the same user.";
						} else {
							//we want to find the largest group id, so we can create a new group with a larger id (by 1)
							PreparedStatement query21 = connection.prepareStatement("select max(group_id) from groups");
							
							ResultSet resultSet21 = query21.executeQuery();
							
							if (resultSet21 != null && resultSet21.next()) {
								int groupID = resultSet21.getInt(1);
								groupID++;
								
								//creates the group
								PreparedStatement query31 = connection.prepareStatement("insert into groups values (?, ?, ?, ?)");
								query31.setInt(1, groupID);
								query31.setString(2, username);
								query31.setString(3, inputGroupname);
								query31.setDate(4, sqlDate);
								
								query31.executeUpdate();
								connection.commit();
							}
						}
					}
		        	
		        	connection.close();
		        } catch (Exception e) {
	                System.out.println("An error occured while creating a group: " + e);
		            errorMessage = "An error occurred while creating the group.";
		        }
        	} else {
        		//the group should be deleted
        		try {
	        		Connection connection = DBConnection.createConnection();
	        		
	        		//checks to see if the current user is the owner of the group being deleted
	        		PreparedStatement query12 = connection.prepareStatement("select count(*) from groups where user_name = ? and group_name = ?");
	        		query12.setString(1, username);
		        	query12.setString(2, inputGroupname);
	        		
		        	ResultSet resultSet12 = query12.executeQuery();
		        	
		        	if (resultSet12 != null && resultSet12.next()) {
		        		if (resultSet12.getInt(1) == 1) {
		        			//finds the group id of the group being deleted
		        			PreparedStatement query22 = connection.prepareStatement("select group_id from groups where user_name = ? and group_name = ?");
		        			query22.setString(1, username);
				        	query22.setString(2, inputGroupname);
			        		
				        	ResultSet resultSet22 = query22.executeQuery();
				        	
		        			if (resultSet22 != null && resultSet22.next()) {
		        				int groupID = resultSet22.getInt(1);
		        				
		        				//deletes all users from the group
		        				PreparedStatement query32 = connection.prepareStatement("delete from group_lists where group_id = ?");
		        				query32.setInt(1, groupID);
		        				query32.executeUpdate();
		        				
		        				//finally deletes the group itself
		        				PreparedStatement query42 = connection.prepareStatement("delete from groups where group_id = ?");
		        				query42.setInt(1, groupID);
		        				query42.executeUpdate();
		        				
		        				connection.commit();
		        			}
		        		} else {
		        			errorMessage = "Group doesn't exist or you aren't the owner of it";
		        		}
		        	}
		        	
		        	connection.close();
        		} catch (Exception e) {
        			System.out.println("An error occured while deleting a group: " + e);
		            errorMessage = "An error occurred while deleting the group.";
        		}
        	}
        }
        
        if (errorMessage.isEmpty()) {
        	response.sendRedirect("/PhotoWebApp/ViewProfile?" + username);
        } else {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/GroupManagement");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
        }
	}
}