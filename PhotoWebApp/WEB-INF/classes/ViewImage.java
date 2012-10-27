import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * TODO
 * 
 *  @author Tim Phillips
 *
 */
public class ViewImage extends HttpServlet implements SingleThreadModel {

    /**
     *  TODO
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	
		String picId  = request.getQueryString();
		String query;
		
		// Photo information
		String ownerName = null;
		String subject = null;
		String place = null;
		String timing = null;
		String description = null;
		
		query = "select * from images where photo_id=" + picId;
	
		Connection conn = null;
		try {
		    conn = getConnected();
		    Statement stmt = conn.createStatement();
		    ResultSet rset = stmt.executeQuery(query);
		    
		    if (rset.next() ) {
		    	ownerName = rset.getString("owner_name");
		    	subject = rset.getString("subject");
		    	place = rset.getString("place");
		    	timing = rset.getDate("timing").toString();
		    	description = rset.getString("description");
			} 
		    else {
		    	// TODO: handle no result
		    }
		} catch( Exception ex ) {
		    // TODO: handle error
		}
		// Close the connection
		finally {
		    try {
			    conn.close();
		    } catch ( SQLException ex) {
			    // TODO: handle error
		    }
		}
		
		// Add the image info to the request result
		request.setAttribute("picId", picId);
		request.setAttribute("ownerName", ownerName);
		request.setAttribute("subject", subject);
		request.setAttribute("place", place);
		request.setAttribute("timing", timing);
		request.setAttribute("description", description);
		
		// Redirect to the viewImage.jsp
		RequestDispatcher dispatcher =request.getRequestDispatcher("/viewImage.jsp");
		dispatcher.forward(request, response);
    }

    /*
     *   Connect to the specified database
     */
    private Connection getConnected() throws Exception {
		String username = "tdphilli";
		String password = "*****";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String dbstring ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	
		//  Connect to the database
		Class drvClass = Class.forName(driverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbstring,username,password) );
    }
}
