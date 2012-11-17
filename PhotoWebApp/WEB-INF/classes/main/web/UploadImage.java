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
import main.util.SQLQueries;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.ArrayList;

import oracle.sql.*;
import oracle.jdbc.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Backing servlet for the Upload Image screen (uploadImage.jsp)
 * 
 *  @author Tim Phillips
 */
public class UploadImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for uploadImage.jsp
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
 
        Connection connection = null;
        try {
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_ALL_GROUPS);
            ResultSet allGroups = preparedStatement.executeQuery();
            ArrayList<String[]> groups = new ArrayList<String[]>();
            
            while (allGroups.next()) {
                String[] group = new String[2];
                group[0] = allGroups.getString("group_name");
                group[1] = Integer.toString((allGroups.getInt("group_id")));
                groups.add(group);
            }
            
            request.setAttribute("groups", groups);
            connection.close();
        } catch(Exception ex) {
            System.out.println("An error occured while obtaining all the groups: " + ex);
            request.setAttribute("errorMessage", "An error occured while obtaining all the groups.");
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
            
            // Ensure that the date and the time have been entered correctly
            String dateTime = null;
            if (!date.equals("") && time.equals("")) {
                dateTime = "TO_DATE('" + date + "12:00"+ "', 'DD/MM/YYYY HH24:MI')";
            } else if (!date.equals("") && !time.equals("")) {
                dateTime = "TO_DATE('" + date + time + "', 'DD/MM/YYYY HH24:MI')";
            }

            // First, generate a unique photo_id using the SQL sequence
            ResultSet rset1 = DBConnection.executeQuery(connection, "SELECT pic_id_sequence.nextval from dual");
            rset1.next();
            photoId = rset1.getInt(1);
            
            // Create the image record (with empty blobs for the image and thumbnail)
            DBConnection.executeQuery(connection, "INSERT INTO images VALUES(" + photoId + ",'" + userName + "'," + access + ",'"
                    + subject +"','" + place + "'," + dateTime + ",'" + 
                    description + "',empty_blob(), empty_blob())");

            // Write both the full image and the thumbnail image
            String cmd = "SELECT * FROM images WHERE photo_id = " + photoId + " FOR UPDATE";
            ResultSet rset = DBConnection.executeQuery(connection, cmd);
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
  
            DBConnection.executeQuery(connection, "commit");
            // Redirect to the ViewImage servlet to view the uploaded image
            response.sendRedirect("/PhotoWebApp/ViewImage?" + photoId);
        } catch(Exception ex) {
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
    private static BufferedImage shrink(BufferedImage image, int n) {

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
