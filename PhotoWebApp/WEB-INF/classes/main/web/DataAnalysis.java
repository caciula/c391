package main.web;  

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import main.util.DBConnection;
import main.util.Filter;

public class DataAnalysis extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection myConn = null;
        try{
       	 	myConn = DBConnection.createConnection();
       	 	ArrayList<String> users = new ArrayList<String>();
       	 	ArrayList<String> subjects = new ArrayList<String>();
       	 	
       	 	String userQuery = "SELECT user_name FROM persons";
       	 	String subjectQuery = "SELECT DISTINCT subject FROM images";
       	 	
       	 	PreparedStatement findUsers = myConn.prepareStatement(userQuery);
       	 	PreparedStatement findSubjects = myConn.prepareStatement(subjectQuery);
       	 	
       	 	ResultSet userResults = findUsers.executeQuery();
       	 	ResultSet subjectResults = findSubjects.executeQuery();
       	 	
       	 	while (userResults.next()) {
       	 		users.add(userResults.getString("user_name"));
       	 	}
       	 	while (subjectResults.next()){
       	 		subjects.add(subjectResults.getString("subject"));
       	 	}
       	 	
       	 	request.setAttribute("users", users);
       	 	request.setAttribute("subjects", subjects);
            userResults.close();
            subjectResults.close();
        } catch(Exception ex) {
                System.err.println("Exception: " + ex.getMessage());
        } finally {
            try {
                myConn.close();
            } catch (Exception ex) {
           	 System.err.println("Exception: " + ex.getMessage());
            }
        }
        
		request.getRequestDispatcher("/DataAnalysis.jsp").forward(request, response);
	}
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	 String user = request.getParameter("user");
    	 String subject = request.getParameter("subject");
         String fromDate = request.getParameter("fromDate");
         String toDate = request.getParameter("toDate");
         
         String query = user +" "+ subject +" "+ fromDate +" "+ toDate;
         request.setAttribute("yourQuery", query);
         request.getRequestDispatcher("/DataAnalysisResults.jsp").forward(request, response);
    }   
}