package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Backing servlet for the Edit Image screen (editImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class EditImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for editImage.jsp
     *  Displays the image details.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        // Obtain the image id from the user's QueryString
        String picId  = request.getQueryString();
	
        DBConnection connection = null;
        try {
            // Obtain the image from the database, based on the image's id
            connection = new DBConnection();
            PreparedStatement preparedStatement = connection.getPreparedStatement(SQLQueries.GET_IMAGE_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next() ) {
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                DateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
                request.setAttribute("date", formatter.format(rset.getDate("timing")));
                request.setAttribute("description", rset.getString("description"));
            } 
            else {
                // TODO: handle no result
            }
        } catch( Exception ex ) {
            // TODO: handle error
        } finally {
            // Close the connection
            try {
                connection.closeConnection();
            } catch ( SQLException ex) {
                // TODO: handle error
            }
        }
		
        // Redirect to editImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/editImage.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *  POST command for editImage.jsp
     *  Saves updates to the image details.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
        String picId  = request.getQueryString();

        DBConnection connection = null;
        try {
            // Update the image based on the user's input
            connection = new DBConnection();
            PreparedStatement preparedStatement = connection.getPreparedStatement(SQLQueries.UPDATE_IMG_DETAILS_BY_ID);
            preparedStatement.setString(1, request.getParameter("subject"));
            preparedStatement.setString(2, request.getParameter("place"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(request.getParameter("date")); 
            preparedStatement.setTimestamp(3, new Timestamp(date.getTime()));
            preparedStatement.setString(4, request.getParameter("description"));
            preparedStatement.setString(5, picId);
            preparedStatement.executeUpdate();
        } catch(Exception ex) {
            // TODO: handle error
        } finally {
            // Close the connection
            try {
                connection.closeConnection();
            } catch (SQLException ex) {
                // TODO: handle error
            }
        }
        
        // Redirect to the ViewImage servlet to view the updated image
        response.sendRedirect("/PhotoWebApp/ViewImage?" + picId);
    }
}
