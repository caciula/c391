/***
 *  A sample program to demonstrate how to use servlet to 
 *  load an image file from the client disk via a web browser
 *  and insert the image into a table in Oracle DB.
 *  
 *  Copyright 2007 COMPUT 391 Team, CS, UofA                             
 *  Author:  Fan Deng, Tim Phillips
 *                                                                  
 *  Licensed under the Apache License, Version 2.0 (the "License");         
 *  you may not use this file except in compliance with the License.        
 *  You may obtain a copy of the License at                                 
 *      http://www.apache.org/licenses/LICENSE-2.0                          
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 *  Shrink function from
 *  http://www.java-tips.org/java-se-tips/java.awt.image/shrinking-an-image-by-skipping-pixels.html
 *
 ***/

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import oracle.sql.*;
import oracle.jdbc.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

public class UploadImage extends HttpServlet {
	
    /**
     *  TO DO
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response)
	    throws ServletException, IOException {
		Integer photoId = null;
	
		try {
		    // Parse the HTTP request to get the image stream
		    DiskFileUpload fu = new DiskFileUpload();
		    List FileItems = fu.parseRequest(request);
		        
		    // Process the uploaded items, assuming only 1 image file uploaded
		    Iterator i = FileItems.iterator();
		    FileItem item = (FileItem) i.next();
		    while (i.hasNext() && item.isFormField()) {
			    item = (FileItem) i.next();
		    }
	
		    // Get the image stream
		    InputStream instream = item.getInputStream();
	
		    BufferedImage img = ImageIO.read(instream);
		    BufferedImage thumbNail = shrink(img, 10);
	
	        // Connect to the database and create a statement
	        Connection conn = getConnected();
		    Statement stmt = conn.createStatement();
		    
		    // First, to generate a unique photo_id using an SQL sequence
		    ResultSet rset1 = stmt.executeQuery("SELECT pic_id_sequence.nextval from dual");
		    rset1.next();
		    photoId = rset1.getInt(1);
	
		    // Create the image record (with empty blobs for the images)
		    stmt.execute("INSERT INTO images VALUES(" + photoId + "," + null +
		    		",'1','Subject','Place',TO_DATE('12.03.2006', 'DD.MM.YYYY')," +
		    		"'Description',empty_blob(), empty_blob())");
	 
		    String cmd = "SELECT * FROM images WHERE photo_id = " + photoId + " FOR UPDATE";
		    ResultSet rset = stmt.executeQuery(cmd);
		    rset.next();
		    // Write both the full image and the thumbnail image
		    BLOB fullBlob = ((OracleResultSet)rset).getBLOB(9);
		    BLOB thumbNailBlob = ((OracleResultSet)rset).getBLOB(8);
		    OutputStream fullOutstream = fullBlob.getBinaryOutputStream();
		    OutputStream thumbnailOutstream = thumbNailBlob.getBinaryOutputStream();
		    ImageIO.write(img, "jpg", fullOutstream);
		    ImageIO.write(thumbNail, "jpg", thumbnailOutstream);
		    
		    instream.close();
		    fullOutstream.close();
		    thumbnailOutstream.close();

	        stmt.executeUpdate("commit");
	        conn.close();
		} catch( Exception ex ) {
		    // TODO: handle error
		}
	
		// Redirect to the ViewImage servlet to view the uploaded image
		response.sendRedirect("/PhotoWebApp/ViewImage?" + photoId);
    }

    /* To connect to the specified database */
    private static Connection getConnected() throws Exception {
		String username = "tdphilli";
		String password = "*****";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String dbstring ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	
		// Connect to the database
		Class drvClass = Class.forName(driverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return(DriverManager.getConnection(dbstring,username,password));
    }

    /* Shrink image by a factor of n, and return the shrinked image */
    public static BufferedImage shrink(BufferedImage image, int n) {

        int w = image.getWidth() / n;
        int h = image.getHeight() / n;

        BufferedImage shrunkImage = new BufferedImage(w, h, image.getType());

        for (int y=0; y < h; ++y) {
            for (int x=0; x < w; ++x) {
                shrunkImage.setRGB(x, y, image.getRGB(x*n, y*n));
            }
        }
        return shrunkImage;
    }
}
