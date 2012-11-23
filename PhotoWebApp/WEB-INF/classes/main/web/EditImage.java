package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;
import main.util.Filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Backing servlet for the Edit Image screen (EditImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class EditImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for EditImage.jsp
     *  Obtains the image details for the user to edit.
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
            // Obtain the image from the database, based on the image's id
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next() ) {
                // Ensure that the logged-in user has permission to edit this image
                String owner = rset.getString("owner_name");
                if (!owner.equals(username)) {
                    request.setAttribute("errorMessage", "You must be the owner of the image to edit it.");
                    request.setAttribute("errorBackLink", "/PhotoWebApp/ViewImage?" + picId);
                    request.getRequestDispatcher("/Error.jsp").forward(request, response);
                    return;
                }
                
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("access", rset.getString("permitted"));
                if (rset.getDate("timing") != null) {
                    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat timeFormatter = new SimpleDateFormat("k:mm");
                    request.setAttribute("date", dateFormatter.format(rset.getTimestamp("timing")));
                    request.setAttribute("time", timeFormatter.format(rset.getTimestamp("timing")));
                }
                request.setAttribute("description", rset.getString("description"));
            } 
            else {
                // Handle no result
                request.setAttribute("errorMessage", "No result found for the provided photo id.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/Home.jsp");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("groups", Filter.getAllowedGroups(username));
            
        } catch( Exception ex ) {
            // Handle error
            System.out.println("An error occurred while obtaining a photo to edit:" + ex);
            request.setAttribute("errorMessage", "An error occurred while obtaining the photo to edit.");
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
		
        // Redirect to EditImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/EditImage.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *  POST command for EditImage.jsp
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:mm");
            if (!request.getParameter("date").equals("")) {
                String time = request.getParameter("time");
                if (time.equals("")) {
                    time = "12:00";
                }
                String inputDateTime = request.getParameter("date") + " " + time;
                Date date = formatter.parse(inputDateTime);
                preparedStatement.setTimestamp(3, new Timestamp(date.getTime()));
            } else {
                preparedStatement.setTimestamp(3, null);
            }
            preparedStatement.setString(4, request.getParameter("description"));
            preparedStatement.setString(5, request.getParameter("access"));
            preparedStatement.setString(6, picId);
            preparedStatement.executeUpdate();
            
            Statement statement = connection.createStatement();
            statement.executeQuery("commit");
        } catch (Exception ex) {
            // Handle error
            System.out.println("An error occurred while updating a photo: " + ex);
            request.setAttribute("errorMessage", "An error occurred while updating the photo. Please click back and try again.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/EditImage?" + picId);
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException ex) {
                // Handle error
                System.out.println("An error occurred while updating a photo: " + ex);
                request.setAttribute("errorMessage", "An error occurred while updating the photo. Please click back and try again.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/EditImage?" + picId);
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
        }
        
        // Redirect to the ViewImage servlet to view the updated image
        response.sendRedirect("/PhotoWebApp/ViewImage?" + picId);
    }
}
