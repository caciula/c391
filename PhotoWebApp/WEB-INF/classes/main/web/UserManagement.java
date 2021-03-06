package main.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.util.DBConnection;
import main.util.SQLQueries;

/**
 *  Backing servlet for the User Management screen (UserManagement.jsp)
 * 
 *  @author Gabriel Caciula
 */
public class UserManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    /**
     *  GET command for UserManagement.jsp
     *  
     *  Displays a list of all groups the user created
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
        		
        		//obtain the list of all groups that the user has created
        		PreparedStatement query = connection.prepareStatement(SQLQueries.GET_USER_GROUPS);
        		query.setString(1, (String) session.getAttribute("username"));
        		
        		ResultSet resultSet = query.executeQuery();
        		
        		ArrayList<String[]> groups = new ArrayList<String[]>();		
        		while (resultSet != null && resultSet.next()) {
        			String[] group = new String[2];
        			group[0] = Integer.toString(resultSet.getInt("group_id"));
        			group[1] = resultSet.getString("group_name");
        			groups.add(group);
        		}
        		
        		connection.close();
        		
                request.setAttribute("groups", groups);
            } catch (Exception e) {
                System.out.println("An error occured while obtaining all the groups to display: " + e);
                request.setAttribute("errorMessage", "An error occured while obtaining all the groups to display.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/UserManagement");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
            }
        }
		
        request.getRequestDispatcher("/UserManagement.jsp").forward(request, response);
    }
    
    /**
     *  POST command for UserManagement.jsp
     *  
     *  Either adds or removes a user to/from a group, depending on which button is pressed
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String button = request.getParameter("submit");

		int groupID = Integer.parseInt(request.getParameter("groups"));
		String inputUsername = (request.getParameter("username")).trim();
		
		java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		
		HttpSession session = request.getSession();
		String sessionUsername = (String) session.getAttribute("username");
		
		String errorMessage = "";
		
		if (inputUsername.isEmpty()) {
			errorMessage = "A username must be entered.";
		} else {
			if (button.equals("Add")) {
				//the user is to be added to the selected group
				try {
					Connection connection = DBConnection.createConnection();
					
					//checks to see if the user is valid (is registered, is not the current user)
					PreparedStatement query11 = connection.prepareStatement("select count(*) from users where user_name = ? and user_name != ?");
					query11.setString(1, inputUsername);
					query11.setString(2, sessionUsername);
					
					ResultSet resultSet11 = query11.executeQuery();
					
					if (resultSet11 != null && resultSet11.next()) {
						if (resultSet11.getInt(1) == 1) {
							//checks to see if the user isn't already a part of the group
							PreparedStatement query21 = connection.prepareStatement("select count(*) from group_lists where group_id = ? and friend_id = ?");
							query21.setInt(1, groupID);
							query21.setString(2, inputUsername);
							
							ResultSet resultSet21 = query21.executeQuery();
							
							if (resultSet21 != null && resultSet21.next()) {
								if (resultSet21.getInt(1) == 1) {
									errorMessage = "The user is already a part of the group";
								} else {
									//adds the user to the group
									PreparedStatement query31 = connection.prepareStatement("insert into group_lists values(?, ?, ?, ?)");
									query31.setInt(1, groupID);
									query31.setString(2, inputUsername);
									query31.setDate(3, sqlDate);
									query31.setString(4, "");
									
									query31.execute();
									connection.commit();
								}
							}
						} else {
						    errorMessage = "An invalid user was entered.";
						}
					}
					
					connection.close();
				} catch (Exception e) {
		            System.out.println("An error occured while adding a user to a group: " + e);
		            errorMessage = "An error occured while adding a user to a group.";
				}
			} else {
				//the user is to be deleted from the selected group
				try {
					Connection connection = DBConnection.createConnection();
					
					//checks to see if the user is a member of the group
					PreparedStatement query12 = connection.prepareStatement("select count(*) from group_lists where group_id = ? and friend_id = ?");
					query12.setInt(1, groupID);
					query12.setString(2, inputUsername);
					
					ResultSet resultSet12 = query12.executeQuery();
					
					if (resultSet12 != null && resultSet12.next()) {
						if (resultSet12.getInt(1) == 1) {
							//removes the user from the group
							PreparedStatement query22 = connection.prepareStatement("delete from group_lists where group_id = ? and friend_id = ?");
							query22.setInt(1, groupID);
							query22.setString(2, inputUsername);
							
							query22.executeUpdate();
							connection.commit();
						} else {
							errorMessage = "An invalid user was entered";
						}
					}
				} catch (Exception e) {
					System.out.println("An error occured while removing a user from a group: " + e);
		            errorMessage = "An error occured while removing a user from a group.";
				}
			}
		}
		
		if (errorMessage.isEmpty()) {
			response.sendRedirect("/PhotoWebApp/ViewProfile?" + sessionUsername);
		} else {
			request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/UserManagement");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}
}
