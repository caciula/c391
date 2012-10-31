package main.web;  

import main.util.DBConnectionUtil;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * Backing servlet for the Edit Image screen (editImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class EditImage extends HttpServlet {

    /**
     *  GET command for editImage.jsp
     *  Displays the image details.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        String picId  = request.getQueryString();
        String query = "select * from images where photo_id=" + picId;
	
        Connection conn = null;
        try {
            conn = DBConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next() ) {
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("description", rset.getString("description"));
                request.setAttribute("timing", rset.getDate("timing").toString());
            } 
            else {
                // TODO: handle no result
            }
        } catch( Exception ex ) {
            // TODO: handle error
        } finally {
            // Close the connection
            try {
                conn.close();
            } catch ( SQLException ex) {
                // TODO: handle error
            }
        }
		
        // Redirect to the editImage.jsp
        RequestDispatcher dispatcher =request.getRequestDispatcher("/editImage.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *  POST command for editImage.jsp
     *  Saves updates to the image details.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
        String picId  = request.getQueryString();
        String sql = "update images set subject=? , place=?, timing=?, description=? where photo_id=" + picId;

        Connection connection = null;
        try {
            connection = DBConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, request.getParameter("subject"));
            preparedStatement.setString(2, request.getParameter("place"));
            preparedStatement.setString(3, request.getParameter("timing"));
            preparedStatement.setString(4, request.getParameter("description"));
            preparedStatement.executeUpdate();
        } catch(Exception ex) {
            // TODO: handle error
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException ex) {
                // TODO: handle error
            }
        }
        
        // Redirect to the ViewImage servlet to view the updated image
        response.sendRedirect("/PhotoWebApp/ViewImage?" + picId);
    }
}
