package main.web;  

import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Backing servlet for the View Image screen (ViewImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class ViewImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for ViewImage.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        
        // Obtain the image id from the user's QueryString
        String picId  = request.getQueryString();
        
        String errorMessage = "";
	
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
                String ownerName = rset.getString("owner_name");
                request.setAttribute("ownerName", ownerName);
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("description", rset.getString("description"));

                int permission =  rset.getInt("permitted");
                PreparedStatement getGroupStatement = connection.prepareStatement(SQLQueries.GET_GROUP_BY_ID);
                getGroupStatement.setInt(1, permission);
                ResultSet groupResult = getGroupStatement.executeQuery();
                if (groupResult.next()){
                    request.setAttribute("access", groupResult.getString("group_name"));
                }
                
                // Ensure that the user is allowed to view this image.
                if (permission == 1 || ownerName.equals(userName) || userName.equals("admin")) {
                    // The image is public - the user has permission to view it.
                } else if (permission == 2) {
                    // The image is private - the user cannot view it if they're not the owner.
                    errorMessage = "You do not have permission to view this image.";
                } else {
                    PreparedStatement getMembersStatement = connection.prepareStatement(SQLQueries.GET_MEMBERS_BY_GROUP_ID);
                    getMembersStatement.setInt(1, permission);
                    ResultSet membersResult = getMembersStatement.executeQuery();
                    boolean allowedToSeeImage = false;
                    while(membersResult.next()) {
                        if (membersResult.getString("friend_id").equals(userName)) {
                            allowedToSeeImage = true;
                            break;
                        }
                    }
                    if (allowedToSeeImage == false) {
                        errorMessage = "You do not have permission to view this image.";
                    }
                }
                
                if (rset.getDate("timing") != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:mm");
                    request.setAttribute("date", formatter.format(rset.getTimestamp("timing")));
                }
            } else {
                // Handle no result
                errorMessage = "No result found for the provided photo id.";
            }
            
            if (errorMessage.isEmpty() && userName != null) {
                // Check if the user has viewed this image before
                PreparedStatement getImageViewedByUser = connection.prepareStatement(SQLQueries.GET_IMAGE_VIEWED_BY_USER);
                getImageViewedByUser.setString(1, userName);
                getImageViewedByUser.setString(2, picId);
                ResultSet userHasViewedImage = getImageViewedByUser.executeQuery();
                // if the user has not already viewed the image, add an entry to the image views
                if (!userHasViewedImage.next()) {
                    PreparedStatement insertImageView = connection.prepareStatement(SQLQueries.INSERT_IMAGE_VIEW);
                    insertImageView.setString(1, picId);
                    insertImageView.setString(2, userName);
                    insertImageView.execute();
                    Statement commitStatement = connection.createStatement();
                    commitStatement.executeQuery("commit");
                }
            }
            connection.close();
            
        } catch(Exception ex) {
            // Handle error
            System.out.println("An error occurred while obtaining a photo to view:" + ex);
            errorMessage = "An error occurred while obtaining the photo.";
        }
        
        // If an error occurred: redirect to the error page
        if (!errorMessage.isEmpty()) {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorBackLink", "/PhotoWebApp/Home");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        }
        
        // Redirect to viewImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/ViewImage.jsp");
        dispatcher.forward(request, response);
    }
}
