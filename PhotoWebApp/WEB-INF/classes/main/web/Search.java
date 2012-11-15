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

public class Search extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Search.jsp").forward(request, response);
	}
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	 String keywords = request.getParameter("keywords");
         String fromDate = request.getParameter("fromDate");
         String toDate = request.getParameter("toDate");
         String sort = request.getParameter("SortBy");

         String query = "SELECT photo_id FROM images";
      
         if(!keywords.isEmpty()){
        	 query += " WHERE CONTAINS(subject, " + keywords +", 1) > 0 OR CONTAINS(place, " + keywords +", 2) > 0 OR CONTAINS(description, " + keywords +", 3) > 0 ";
         }
         
         if(!fromDate.isEmpty()){
                 query += " AND when >=  " + fromDate +" ";
         }
         if(!toDate.isEmpty()){
                 query += " AND when <=  " + toDate + " ";
         }

         if(sort.equals("Rank")){
                         query += " ORDER BY ((6*SCORE(1))+(3*SCORE(2))+SCORE(3)) DESC";
         }else if(sort.equals("New")){
                         query += " ORDER BY timing DESC";
         }else if(sort.equals("Old")){
                         query += " ORDER BY timing ASC";
         }

         Connection myConn = null;
         try{
        	 	myConn = DBConnection.createConnection();
        	 	ArrayList<String> matchingIds = new ArrayList<String>();
        	 	PreparedStatement myQuery = myConn.prepareStatement(query);
        	 	ResultSet results = myQuery.executeQuery();
        	 	while (results.next()) {
        	 		matchingIds.add(Integer.toString(results.getInt(1)));
        	 	}
        	 	request.setAttribute("matchingIds", matchingIds);
                myQuery.close();
         } catch(Exception ex) {
                 System.err.println("Exception: " + ex.getMessage());
         } finally {
             try {
                 myConn.close();
             } catch (Exception ex) {
            	 System.err.println("Exception: " + ex.getMessage());
             }
         }
         
         request.getRequestDispatcher("/SearchResults.jsp").forward(request, response);
    }
}