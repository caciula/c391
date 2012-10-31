package main.web;

/***
 *  Copyright 2007 COMPUT 391 Team, CS, UofA                             
 *  Author:  Fan Deng
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
 *  Modified by Tim Phillips, 2012
 *
 ***/
import main.util.DBConnectionUtil;

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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Backing servlet for the Upload Image screen (uploadImage.html)
 * 
 *  @author Tim Phillips
 */
public class UploadImage extends HttpServlet {

    /**
     *  Called when the "Upload" button is clicked on uploadImage.html
     *  Uploads an image and stores the provided image details in the database
     *  Upon completion, redirects to viewImage.jsp
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response)
        throws ServletException, IOException {
        
        Integer photoId = null;
        String subject = null;
        String place = null;
        String description = null;
        BufferedImage img = null;
        BufferedImage thumbNail = null;
        
        Connection conn = null;
        try {
            conn = DBConnectionUtil.getConnection();
            // Obtain the form info from the request
            ServletFileUpload upload = new ServletFileUpload();
            response.setContentType("text/plain"); 
            FileItemIterator iterator = upload.getItemIterator(request);

            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();
                // Item is a text input field
                if (item.isFormField()) {
                    if (item.getFieldName().equals("subject")){
                        subject = Streams.asString(stream);
                    } else if (item.getFieldName().equals("description")){
                        description = Streams.asString(stream);
                    } else if (item.getFieldName().equals("place")){
                        place = Streams.asString(stream);
                    }
                // Item is the uploaded image
                } else{
                  img = ImageIO.read(stream);
                  thumbNail = shrink(img, 10);
                }
                stream.close();
            }
            
            // Connect to the database
            Statement stmt = conn.createStatement();
        
            // First, generate a unique photo_id using the SQL sequence
            ResultSet rset1 = stmt.executeQuery("SELECT pic_id_sequence.nextval from dual");
            rset1.next();
            photoId = rset1.getInt(1);
          
            // Create the image record (with empty blobs for the image and thumbnail)
            stmt.execute("INSERT INTO images VALUES(" + photoId + "," + null +
                ",'1','" + subject +"','" + place + "',TO_DATE('12.03.2006', 'DD.MM.YYYY'),'" +
                description + "',empty_blob(), empty_blob())");

            // Write both the full image and the thumbnail image
            String cmd = "SELECT * FROM images WHERE photo_id = " + photoId + " FOR UPDATE";
            ResultSet rset = stmt.executeQuery(cmd);
            rset.next();
            BLOB fullBlob = ((OracleResultSet)rset).getBLOB(9);
            BLOB thumbNailBlob = ((OracleResultSet)rset).getBLOB(8);
            @SuppressWarnings("deprecation")
            OutputStream fullOutstream = fullBlob.getBinaryOutputStream();
            @SuppressWarnings("deprecation")
            OutputStream thumbnailOutstream = thumbNailBlob.getBinaryOutputStream();
            ImageIO.write(img, "jpg", fullOutstream);
            ImageIO.write(thumbNail, "jpg", thumbnailOutstream);
            
            fullOutstream.close();
            thumbnailOutstream.close();
  
            stmt.executeUpdate("commit");
            conn.close();
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
	
        // Redirect to the ViewImage servlet to view the uploaded image
        response.sendRedirect("/PhotoWebApp/ViewImage?" + photoId);
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
