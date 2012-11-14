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

public class AddUserToGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String[]> groups;
		
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Login.jsp");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        } else {
            try {
                groups = getListOfGroups(request);
                request.setAttribute("groups", groups);
            } catch (Exception e) {
                System.out.println("An error occured while obtaining all the groups: " + e);
                request.setAttribute("errorMessage", "An error occured while obtaining all the groups to display.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/Home.jsp");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
	            return;
            }
        }
		
        request.getRequestDispatcher("/AddUserToGroup.jsp").forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int inputGroupID = Integer.parseInt(request.getParameter("groups"));
		String inputUsername = request.getParameter("username");
		
		java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		String errorMessage = "";
		
		if (inputUsername.isEmpty()) {
			errorMessage = "A username must be entered.";
		} else {
			try {
				Connection connection = DBConnection.createConnection();
				
				PreparedStatement query1 = connection.prepareStatement("select count(*) from users where user_name = ? and user_name != ?");
				query1.setString(1, inputUsername);
				query1.setString(2, username);
				
				ResultSet resultSet1 = query1.executeQuery();
				
				if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
						PreparedStatement query2 = connection.prepareStatement("insert into group_lists values(?, ?, ?, ?)");
						query2.setInt(1, inputGroupID);
						query2.setString(2, inputUsername);
						query2.setDate(3, sqlDate);
						query2.setString(4, "");
						
						query2.execute();
						connection.commit();
					} else {
					    errorMessage = "An invalid user was entered.";
					}
				}
				
				connection.close();
			} catch (Exception e) {
	            System.out.println("An error occured while adding a user to a group: " + e);
	            errorMessage = "An error occured while adding a user to a group.";
			}
		}
		
		if (errorMessage.isEmpty()) {
			response.sendRedirect("/PhotoWebApp/ViewUserImages?" + username);
		} else {
			request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/AddUserToGroup");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}
	
	private ArrayList<String[]> getListOfGroups(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Connection connection = DBConnection.createConnection();
		
		PreparedStatement query = connection.prepareStatement("select group_id, group_name from groups where user_name = ?");
		query.setString(1, (String) session.getAttribute("username"));
		
		ResultSet resultSet = query.executeQuery();
		
		ArrayList<String[]> groups = new ArrayList<String[]>();		
		while (resultSet != null && resultSet.next()) {
			String[] group = new String[2];
			group[0] = Integer.toString(resultSet.getInt(1));
			group[1] = resultSet.getString(2);
			groups.add(group);
		}
		
		return groups;
	}
}
