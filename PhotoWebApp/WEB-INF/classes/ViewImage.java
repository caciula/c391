import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * Backing servlet for the View Image screen (viewImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class ViewImage extends HttpServlet {

    /**
     *  GET command for viewImage.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        String picId  = request.getQueryString();
        String query = "select * from images where photo_id=" + picId;
	
        Connection conn = null;
        try {
            conn = getConnected();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next() ) {
                // Add the image info to the request result
                request.setAttribute("picId", picId);
                request.setAttribute("ownerName", rset.getString("owner_name"));
                request.setAttribute("subject", rset.getString("subject"));
                request.setAttribute("place", rset.getString("place"));
                request.setAttribute("timing", rset.getDate("timing").toString());
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
                conn.close();
            } catch ( SQLException ex) {
                // TODO: handle error
            }
        }
		
        // Redirect to the viewImage.jsp
        RequestDispatcher dispatcher =request.getRequestDispatcher("/viewImage.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *   Connect to the specified database
     */
    private Connection getConnected() throws Exception {
        String username = "tdphilli";
        String password = "********";
        String driverName = "oracle.jdbc.driver.OracleDriver";
        String dbstring ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	
        // Connect to the database
        Class drvClass = Class.forName(driverName); 
        DriverManager.registerDriver((Driver) drvClass.newInstance());
        return( DriverManager.getConnection(dbstring,username,password) );
    }
}
