package main.resource;

import main.util.DBConnection;
import main.util.Filter;
import main.util.SQLQueries;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/**
 *  Obtains the thumbnail-sized image associated with the provided id parameter.
 * 
 *  @author Tim Phillips
 */
public class GetThumbnailImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  Obtains the thumbnail-sized image associated with the provided id parameter.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        //  Obtain the image id from the query string
        String picId  = request.getQueryString();
        ServletOutputStream out = response.getOutputStream();
        
        // Obtain the current user from the session
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        
        // Ensure that the user has permission to view the image
        if (!Filter.isViewable(userName, picId)) {
            out.print("You do not have permission to view this image");
            return;
        }
        
        Connection connection = null;
        try {
            // Obtain the thumbnail image from the database
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_THUMBNAIL_ONLY_BY_ID);
            preparedStatement.setString(1, picId);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next()) {
                response.setContentType("image/jpg");
                InputStream input = rset.getBinaryStream(1);	    
                int imageByte;
                while((imageByte = input.read()) != -1) {
                    out.write(imageByte);
                }
                input.close();
            }
        } catch( Exception ex ) {
            System.out.println("An error occurred while obtaining a thumbnail for photo with id " + picId + ": " + ex);
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch ( SQLException ex) {
                System.out.println("An error occurred while obtaining a thumbnail for photo with id:" + picId + ": " + ex);
            }
        }
    }
}
