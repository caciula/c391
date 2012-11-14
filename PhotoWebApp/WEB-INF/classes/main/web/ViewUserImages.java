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
import main.util.SQLQueries;

/**
 * Backing servlet for the View User Images screen (viewUserImages.jsp)
 * 
 *  @author Tim Phillips
 */
public class ViewUserImages extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for ViewUserImages.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Obtain the user name from the QueryString
        String userName  = request.getQueryString();
                
        Connection connection = null;
        try {
            // Obtain the images from the database, based on the user
            connection = DBConnection.createConnection();
            ArrayList<String> imageIds = new ArrayList<String>();
            PreparedStatement getUserImagesQuery = connection.prepareStatement(SQLQueries.GET_IMAGES_BY_USER_ID);
            getUserImagesQuery.setString(1, userName);
            ResultSet rset = getUserImagesQuery.executeQuery();
            while (rset.next()) {
                imageIds.add(Integer.toString(rset.getInt("photo_id")));
            }
            request.setAttribute("imageIds", imageIds);
            
            // Obtain the user's name from the database
            PreparedStatement getUserQuery = connection.prepareStatement(SQLQueries.GET_PERSON_BY_USER_NAME);
            getUserQuery.setString(1, userName);
            ResultSet userResult = getUserQuery.executeQuery();
            if (userResult.next()) {
                request.setAttribute("userLastName", userResult.getString("last_name"));
                request.setAttribute("userFirstName", userResult.getString("first_name"));
                request.setAttribute("email", userResult.getString("email"));
                request.setAttribute("phone", userResult.getString("phone"));
                request.setAttribute("address", userResult.getString("address"));
                request.setAttribute("address", userResult.getString("address"));
            }
            
        } catch( Exception ex ) {
            System.out.println("An error occured while obtaining a user's (username:" + userName + ") images: " + ex);
            request.setAttribute("errorMessage", "An error occured while obtaining the user's images.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Home.jsp");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("An error occured while obtaining a user's (username:" + userName + ") images: " + ex);
                request.setAttribute("errorMessage", "An error occured while obtaining the user's images.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/Home.jsp");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
        }
		
        // Redirect to ViewUserImages.jsp
        request.setAttribute("userName", userName);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/ViewUserImages.jsp");
        dispatcher.forward(request, response);
    }
}
