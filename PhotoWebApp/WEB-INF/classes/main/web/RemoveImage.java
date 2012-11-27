package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * Removes an image.
 * 
 *  @author Tim Phillips
 */
public class RemoveImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  Removes the specified image from the database.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Obtain the logged-in user
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        // Obtain the image id from the user's QueryString
        String picId  = request.getQueryString();
	
        Connection connection = null;
        try {
            connection = DBConnection.createConnection();
            // Obtain the image and ensure that the user has permission to remove it
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next() ) {
                String owner = rset.getString("owner_name");
                if (!owner.equals(username)) {
                    request.setAttribute("errorMessage", "You must be the owner of the image to remove it.");
                    request.setAttribute("errorBackLink", "/PhotoWebApp/ViewImage?" + picId);
                    request.getRequestDispatcher("/Error.jsp").forward(request, response);
                    return;
                }
            }
            
            // Remove the image views first
            PreparedStatement deleteImageViews = connection.prepareStatement(SQLQueries.REMOVE_IMAGE_VIEWS_BY_ID);
            deleteImageViews.setString(1, picId);
            deleteImageViews.execute();
            // Remove the image
            PreparedStatement deleteImage = connection.prepareStatement(SQLQueries.REMOVE_IMAGE_BY_ID);
            deleteImage.setString(1, picId);
            deleteImage.execute();
            // Commit the changes
            Statement statement = connection.createStatement();
            statement.executeQuery("commit");
        } catch( Exception ex ) {
            // Handle error
            System.out.println("An error occurred while removing a photo:" + ex);
            request.setAttribute("errorMessage", "An error occurred while removing the photo.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/ViewImage?" + picId);
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch ( SQLException ex) {
                // Handle error
                System.out.println("An error occurred while obtaining a photo to edit:" + ex);
                request.setAttribute("errorMessage", "An error occurred while obtaining the photo to edit.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/ViewImage?" + picId);
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
        }
        
        response.sendRedirect("/PhotoWebApp/ViewProfile?" + username);
    }


}
