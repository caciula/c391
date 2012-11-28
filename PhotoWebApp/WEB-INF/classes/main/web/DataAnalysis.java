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
            
            // Check to make sure that only "admin" can access this screen.
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            if (!username.equals("admin")) {
                request.setAttribute("errorMessage", "You must be an administrator to view this screen.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/Home");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
            
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
         String drillDown = request.getParameter("drillDown");
         if(drillDown == null){
        	 drillDown = "None";
         }
                  
         String select = "SELECT ";
         String from = "FROM images i";
         String where = "";
         String group = "";
         
         if(!user.equals("All")){
        	 select += "i.subject,";
        	 where += " WHERE i.owner_name = ? ";
        	 if(!subject.equals("All")){
        		 where += " AND i.subject = ? ";
        	 }
        	 group += " GROUP BY i.subject";
         }else if(!subject.equals("All")){
        	 select += "i.owner_name,";
        	 where += " WHERE i.subject = ? ";
        	 group += " GROUP BY i.owner_name";
         }			
         
         if(drillDown.equals("Yearly")){
        	 select += " t.year, COUNT(*) ";
        	 from += ", time_dim t ";
        	 where += " AND i.timing = t.time ";
        	 group += ", t.year";
    	 }else if(drillDown.equals("Monthly")){
    		 select += " t.month, COUNT(*) ";
    		 from += ", time_dim t ";
    		 where += " AND i.timing = t.time ";
    		 group += ", t.month";
    	 }else if(drillDown.equals("Weekly")){
    		 select += " t.week, COUNT(*) ";
    		 from += ", time_dim t ";
    		 where += " AND i.timing = t.time ";
    		 group += ", t.week";
    	 }else{
    		 select += " COUNT(*) ";
    	 }
         
         if(!fromDate.isEmpty()){
        	 where += " AND i.timing >=  TO_DATE( ?, 'DD/MM/YYYY HH24:MI:SS') ";
         }
         if(!toDate.isEmpty()){
        	 where += " AND i.timing <= TO_DATE( ?, 'DD/MM/YYYY HH24:MI:SS') ";
         }
         
         String query = select + from + where + group;
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
        	    	if(subject.isEmpty()){
        	    		getReport.setString(n, null);
        	    	 }else{
        	    		 getReport.setString(n, subject);
        	    	 }
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
        	 		if(drillDown.equals("None")){
        	 			row.setCol3(report.getString(1));
        	 			row.setCol4(Integer.toString(report.getInt(2)));
        	 		}else{
        	 			row.setCol2(report.getString(2));
        	 			row.setCol3(report.getString(1));
        	 			row.setCol4(Integer.toString(report.getInt(3)));
        	 		}
        	 		rows.add(row);
        	 	}
        	 	report.close();
        	 	if(!user.equals("All") && drillDown.equals("None")){
        	 		request.setAttribute("head1", "User");
               	 	request.setAttribute("head3", "Subject");
               	 	rows.get(0).setCol1(user);
    	 		}else if(!subject.equals("All")) {
    	 			request.setAttribute("head1", "Subject");
               	 	request.setAttribute("head3", "User");
               	 	rows.get(0).setCol1(subject);
    	 		}
    	 		request.setAttribute("head2", "Time");
           	 	request.setAttribute("head4", "Total");
    	 		
         		if(drillDown.equals("None")){
         			rows.get(0).setCol2(fromDate + " ~ " + toDate);
    	 		}
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