import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 *  TO DO
 * 
 *  @author Li-Yan Yuan, Tim Phillips
 *
 */
public class GetFullImage extends HttpServlet implements SingleThreadModel {

    /**
     *  TO DO
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	
		//  Construct the query from the client's QueryString
		String picId  = request.getQueryString();
		String query = "select photo from images where photo_id=" + picId;
		ServletOutputStream out = response.getOutputStream();

		// Execute the query
		Connection conn = null;
		try {
		    conn = getConnected();
		    Statement stmt = conn.createStatement();
		    ResultSet rset = stmt.executeQuery(query);
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
			    out.println("No result");
		    }
		} catch( Exception ex ) {
		    out.println(ex.getMessage());
		}
		
		// Close the connection
		finally {
		    try {
			    conn.close();
		    } catch ( SQLException ex) {
		        out.println(ex.getMessage());
		    }
	    }
    }

    /*
     *  Connect to the specified database
     */
    private Connection getConnected() throws Exception {
		String username = "tdphilli";
		String password = "*****";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String dbstring ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	
		// Connect to the database
		Class drvClass = Class.forName(driverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbstring,username,password) );
    }
}
