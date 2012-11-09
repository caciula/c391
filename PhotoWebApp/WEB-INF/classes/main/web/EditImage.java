package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
        Connection connection = null;
        try {
            // Obtain the image from the database, based on the image's id
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next() ) {
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("access", rset.getString("permitted"));
                if (rset.getDate("timing") != null) {
                    DateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
                    request.setAttribute("date", formatter.format(rset.getDate("timing")));
                }
                request.setAttribute("description", rset.getString("description"));
                // TODO: check that the user has permission to edit this image
            } 
            else {
                // Handle no result
                request.setAttribute("errorMessage", "No result found for the provided photo id.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/temp.jsp");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            // Get the groups for the 'Access' drop down
            ArrayList<String[]> groups = new ArrayList<String[]>();
            PreparedStatement getAllGroups = connection.prepareStatement(SQLQueries.GET_ALL_GROUPS);
            ResultSet allGroups = getAllGroups.executeQuery();
            while (allGroups.next()) {
                String[] group = new String[2];
                group[0] = allGroups.getString("group_name");
                group[1] = Integer.toString((allGroups.getInt("group_id")));
                groups.add(group);
            }
            request.setAttribute("groups", groups);
            
        } catch( Exception ex ) {
            // Handle error
            System.out.println("An error occurred while obtaining a photo to edit:" + ex);
            request.setAttribute("errorMessage", "An error occurred while obtaining the photo to edit.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/ViewImage?" + picId);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
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
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
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

        Connection connection = null;
        try {
            // Update the image based on the user's input
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.UPDATE_IMG_DETAILS_BY_ID);
            preparedStatement.setString(1, request.getParameter("subject"));
            preparedStatement.setString(2, request.getParameter("place"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if (!request.getParameter("date").equals("")) {
                Date date = formatter.parse(request.getParameter("date")); 
                preparedStatement.setTimestamp(3, new Timestamp(date.getTime()));
            } else {
                preparedStatement.setTimestamp(3, null);
            }
            preparedStatement.setString(4, request.getParameter("description"));
            preparedStatement.setString(5, request.getParameter("access"));
            preparedStatement.setString(6, picId);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            // Handle error
            System.out.println("An error occurred while updating a photo: " + ex);
            request.setAttribute("errorMessage", "An error occurred while updating the photo.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/EditImage?" + picId);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException ex) {
                // Handle error
                System.out.println("An error occurred while updating a photo: " + ex);
                request.setAttribute("errorMessage", "An error occurred while updating the photo.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/EditImage?" + picId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
        }
        
        // Redirect to the ViewImage servlet to view the updated image
        response.sendRedirect("/PhotoWebApp/ViewImage?" + picId);
    }
}
