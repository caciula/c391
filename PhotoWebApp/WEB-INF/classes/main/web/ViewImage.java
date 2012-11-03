package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.DateFormat;
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
                request.setAttribute("description", rset.getString("description"));
                DateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
                request.setAttribute("date", formatter.format(rset.getDate("timing")));
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
            } catch (SQLException ex) {
                // TODO: handle error
            }
        }
		
        // Redirect to viewImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewImage.jsp");
        dispatcher.forward(request, response);
    }
}
