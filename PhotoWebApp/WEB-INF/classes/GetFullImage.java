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

    /**
     *  Obtains the full-sized image associated with the provided id parameter.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
	
        //  Construct the query from the client's QueryString
        String picId  = request.getQueryString();
        String query = "select photo from images where photo_id=" + picId;
        ServletOutputStream out = response.getOutputStream();

        Connection conn = null;
        try {
            // Execute the query
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
                // TODO: handle exception
            }
        } catch( Exception ex ) {
            // TODO: handle exception
        } finally {
            // Close the connection
            try {
                conn.close();
            } catch ( SQLException ex) {
                // TODO: handle exception
            }
        }
    }

    /*
     *  Connect to the specified database
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
