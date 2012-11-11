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
			response.sendRedirect("/PhotoWebApp/Login");
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
        
        String output = "";
        
        if (inputGroupname.isEmpty()) {
        	output = "error: one or more empty fields";
        } else {
	        try {
	        	Connection connection = DBConnection.createConnection();
	        	
	        	PreparedStatement query1 = connection.prepareStatement("select count(*) from groups where user_name = ? and group_name = ?");
	        	query1.setString(1, username);
	        	query1.setString(2, inputGroupname);
	        	
	        	ResultSet resultSet1 = query1.executeQuery();
	        	
	        	if (resultSet1 != null && resultSet1.next()) {
					if (resultSet1.getInt(1) == 1) {
						output = "error: multiple groups with the same name cannot be created by the same user";
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
							
							output = "";
						}
					}
				}
	        	
	        	connection.close();
	        } catch (Exception e) {
	        	output = "error: couldn't complete request";
	        }
        }
        
        if (output.isEmpty()) {
        	response.sendRedirect("/PhotoWebApp/Home.jsp");
        } else {
        	request.setAttribute("output", output);
        	request.getRequestDispatcher("/CreateGroup.jsp").forward(request, response);
        }
	}
}