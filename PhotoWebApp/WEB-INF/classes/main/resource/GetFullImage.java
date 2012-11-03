package main.resource;

import main.util.DBConnection;
import main.util.SQLQueries;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 *  Obtains the full-sized image associated with the provided id parameter.
 * 
 *  @author Li-Yan Yuan, Tim Phillips
 */
public class GetFullImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  Obtains the full-sized image associated with the provided id parameter.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        //  Obtain the image id from the query string
        String picId = request.getQueryString();
        ServletOutputStream out = response.getOutputStream();

        DBConnection connection = null;
        try {
         // Obtain the full image from the database
            connection = new DBConnection();
            PreparedStatement preparedStatement = connection.getPreparedStatement(SQLQueries.GET_PHOTO_ONLY_BY_ID);
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
            else {
                // TODO: handle exception
            }
        } catch( Exception ex ) {
            // TODO: handle exception
        } finally {
            // Close the connection
            try {
                connection.closeConnection();
            } catch ( SQLException ex) {
                // TODO: handle exception
            }
        }
    }
}
