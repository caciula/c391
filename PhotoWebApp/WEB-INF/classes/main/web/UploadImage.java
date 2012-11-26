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
import main.util.DBConnection;
import main.util.Filter;
import main.util.SQLQueries;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sql.*;
import oracle.jdbc.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Backing servlet for the Upload Image screen (UploadImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class UploadImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for UploadImage.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Ensure that the user is logged in to access this screen
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            request.setAttribute("errorMessage", "You must be logged in to view this screen.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Home");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        }
        
        // Obtain the groups to display in the permission drop down
        try {
            request.setAttribute("groups", Filter.getAllowedGroups((String)session.getAttribute("username")));
        } catch(Exception ex) {
            System.out.println("An error occurred while obtaining the groups a user is allowed to use:" + ex);
            request.setAttribute("errorMessage", "An error occurred while obtaining the groups a user is allowed to use.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Home");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        }
        
        // Redirect to uploadImage.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/UploadImage.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     *  Called when the "Upload" button is clicked on uploadImage.jsp
     *  Uploads an image and stores the provided image details in the database
     *  Upon completion, redirects to viewImage.jsp
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String)session.getAttribute("username");
        Integer photoId = null;
        String subject = null;
        String place = null;
        String description = null;
        String date = null;
        String time = null;
        String access = null;
        BufferedImage img = null;
        BufferedImage thumbNail = null;
        
        Connection connection = null;
        try {
            connection = DBConnection.createConnection();
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
                    } else if (item.getFieldName().equals("place")){
                        place = Streams.asString(stream);
                    } else if (item.getFieldName().equals("description")){
                        description = Streams.asString(stream);
                    } else if (item.getFieldName().equals("access")){
                        access = Streams.asString(stream);
                    } else if (item.getFieldName().equals("date")){
                        date = Streams.asString(stream);
                    } else if (item.getFieldName().equals("time")){
                        time = Streams.asString(stream);
                    }
                // Item is the uploaded image
                } else{
                  img = ImageIO.read(stream);
                  thumbNail = shrink(img, 10);
                }
                stream.close();
            }

            // First, generate a unique photo_id using the SQL sequence
            Statement statement = connection.createStatement();
            ResultSet rset1 = statement.executeQuery("SELECT pic_id_sequence.nextval from dual");
            rset1.next();
            photoId = rset1.getInt(1);
            
            // Create a new image record with the provided image details
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.UPLOAD_IMAGE_DETAILS);
            preparedStatement.setInt(1, photoId);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, access);
            preparedStatement.setString(4, subject);
            preparedStatement.setString(5, place);
            // If the date is entered, but the time is not, set the time to noon by default
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:mm");
            if (date != null && !date.equals("")) {
                if (time == null || time.equals("")) {
                    time = "12:00";
                }
                Date dateTime = formatter.parse(date + " " + time);
                preparedStatement.setTimestamp(6, new Timestamp(dateTime.getTime()));
            } else {
                preparedStatement.setTimestamp(6, null);
            }
            preparedStatement.setString(7, description);
            preparedStatement.execute();

            // Write both the full image and the thumbnail image
            PreparedStatement updateStatement = connection.prepareStatement(SQLQueries.SELECT_IMAGE_FOR_UPDATE);
            updateStatement.setInt(1, photoId);
            ResultSet rset = updateStatement.executeQuery();
            rset.next();
            BLOB fullBlob = ((OracleResultSet)rset).getBLOB(9);
            BLOB thumbNailBlob = ((OracleResultSet)rset).getBLOB(8);
            OutputStream fullOutstream = fullBlob.setBinaryStream(0);
            ImageIO.write(img, "jpg", fullOutstream);
            fullOutstream.close();

            OutputStream thumbnailOutstream = thumbNailBlob.setBinaryStream(0);
            ImageIO.write(thumbNail, "jpg", thumbnailOutstream);
            thumbnailOutstream.close();
  
            Statement commitStatement = connection.createStatement();
            commitStatement.executeQuery("commit");
            
            // Redirect to the ViewImage servlet to view the uploaded image
            response.sendRedirect("/PhotoWebApp/ViewImage?" + photoId);
        } catch(Exception ex) {
            try {
                connection.rollback();
            } catch (Exception rollbackEx) {
                System.out.println("An error occured while rolling back the transaction: " + rollbackEx);
            }
            System.out.println("An error occured while uploading a photo: " + ex);
            request.setAttribute("errorMessage", "An error occured while uploading the file. Please ensure a .jpg or .gif file is selected and " +
            		"all fields have been entered correctly.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImage");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return; 
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("An error occured while uploading a photo: " + ex);
                request.setAttribute("errorMessage", "An error occured while uploading the file. Please ensure a .jpg or .gif file is selected and " +
                        "all fields have been entered correctly.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImage");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
        }
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
