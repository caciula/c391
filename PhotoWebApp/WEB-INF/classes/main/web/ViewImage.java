package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Backing servlet for the View Image screen (viewImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class ViewImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for viewImage.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Obtain the image id from the user's QueryString
        String picId  = request.getQueryString();
	
        Connection connection = null;
        try { 
            // Obtain the image from the database, based on the image's id
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next()) {
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("description", rset.getString("description"));
                // TODO: check that the user has permission to see the image
                PreparedStatement getGroupStatement = connection.prepareStatement(SQLQueries.GET_GROUP_BY_ID);
                getGroupStatement.setInt(1, rset.getInt("permitted"));
                ResultSet groupResult = getGroupStatement.executeQuery();
                if (groupResult.next()) {
                    request.setAttribute("access", groupResult.getString("group_name"));
                }
                if (rset.getDate("timing") != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:mm");
                    request.setAttribute("date", formatter.format(rset.getTimestamp("timing")));
                }
            }
            else {
                // Handle no result
                request.setAttribute("errorMessage", "No result found for the provided photo id.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/home.jsp");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            connection.close();
            
        } catch(Exception ex) {
            // Handle error
            System.out.println("An error occurred while obtaining a photo to view:" + ex);
            request.setAttribute("errorMessage", "An error occurred while obtaining the photo.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/home.jsp");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        
        // Redirect to viewImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewImage.jsp");
        dispatcher.forward(request, response);
    }
}
