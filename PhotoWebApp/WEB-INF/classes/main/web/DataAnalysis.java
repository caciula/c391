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
import main.util.ReportRow;

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
         
         String query = "SELECT owner_name, subject, COUNT(*) FROM images";
         if(!user.equals("All")){
        	 query += " WHERE owner_name = ? ";
         }
         if(!subject.equals("All") && user.equals("All")){
        	 query += " WHERE subject = ? ";
         }else if(!subject.equals("All") && !user.equals("All")){
        	 query += " AND subject = ? ";
         }
         
         if(!fromDate.isEmpty() && subject.equals("All") && user.equals("All")){
        	 query += " WHERE timing >=  TO_DATE( ?, 'DD/MM/YYYY HH24:MI:SS')";
         }else if(!fromDate.isEmpty()){
        	 query += " AND timing >= TO_DATE( ?, 'DD/MM/YYYY HH24:MI:SS')";
         }
         if(!toDate.isEmpty() && fromDate.isEmpty() && subject.equals("All") && user.equals("All")){
        	 query += " WHERE timing <= ? ";
         }else if(!toDate.isEmpty()){
        	 query += " AND timing <=  ? ";
         }
         
         query += " GROUP BY owner_name, subject";
         Connection myConn = null;
         try{
        	 	myConn = DBConnection.createConnection();
        	 	
        	 	PreparedStatement getReport = myConn.prepareStatement(query);
        	 	int n = 1;
        	    if(!user.equals("All")){
        	    	getReport.setString(n, user);
        	    	n++;
                }
        	    if(!subject.equals("All")){
               	 	getReport.setString(n, subject);
               	 	n++;
                }
        	    if(!fromDate.isEmpty()){
               	 	getReport.setString(n, fromDate);
               	 	n++;
                }
        	    if (!toDate.isEmpty()){
        	    	getReport.setString(n, toDate);
        	    	n++;
        	    }
        	    
        	 	ResultSet report = getReport.executeQuery();
        	 	ArrayList<ReportRow> rows = new ArrayList<ReportRow>();
        	 	while (report.next()){
        	 		ReportRow row = new ReportRow();
        	 		row.setUser(report.getString(1));
        	 		row.setSubject(report.getString(2));
        	 		row.setTotal(Integer.toString(report.getInt(3)));
        	 		rows.add(row);
        	 	}
        	 	report.close();
        	 	request.setAttribute("reportRows", rows);
         } catch(Exception ex) {
                 System.err.println("Exception: " + ex.getMessage());
         } finally {
             try {
                 myConn.close();
             } catch (Exception ex) {
            	 System.err.println("Exception: " + ex.getMessage());
             }
         }
         
         request.getRequestDispatcher("/DataAnalysisResults.jsp").forward(request, response);
    }   
}