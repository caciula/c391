package main.web;
import main.util.DBConnection;
import main.util.Filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import oracle.sql.*;
import oracle.jdbc.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Backing servlet for the Upload Multiple Images screen (uploadImagesFromDir.jsp)
 * 
 *  @author Tim Phillips
 */
public class UploadImagesFromDir extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for UploadImagesFromDir.jsp
     *  Ensures that the user can access the screen, and obtains the groups for the "Access" drop down.
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
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/UploadImagesFromDir.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     *  Called when the "Upload" button is clicked on the applet.
     *  Uploads all images in the selected directory, one at a time.
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        Integer photoId = null;
        BufferedImage img = null;
        BufferedImage thumbNail = null;
        String subject = null;
        String place = null;
        String description = null;
        String date = null;
        String time = null;
        String access = null;
        
        Connection connection = null;
        try{
            connection = DBConnection.createConnection();
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
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
                } else{
                    img = ImageIO.read(stream);
                    thumbNail = shrink(img, 10);
                }
            }
            
            // Ensure that the date and the time have been entered correctly
            String dateTime = null;
            if (!date.equals("") && time.equals("")) {
                dateTime = "TO_DATE('" + date + "12:00"+ "', 'DD/MM/YYYY HH24:MI')";
            } else if (!date.equals("") && !time.equals("")) {
                dateTime = "TO_DATE('" + date + time + "', 'DD/MM/YYYY HH24:MI')";
            }
            
            ResultSet rset1 = DBConnection.executeQuery(connection, "SELECT pic_id_sequence.nextval from dual");
            rset1.next();
            photoId = rset1.getInt(1);
            DBConnection.executeQuery(connection, "INSERT INTO images VALUES(" + photoId + ",'" + username + "'," + access + ",'"
                    + subject +"','" + place + "'," + dateTime + ",'" + 
                    description + "',empty_blob(), empty_blob())");
            String cmd = "SELECT * FROM images WHERE photo_id = " + photoId + " FOR UPDATE";
            ResultSet rset = DBConnection.executeQuery(connection, cmd);
            rset.next();
            BLOB fullBlob = ((OracleResultSet)rset).getBLOB(9);
            BLOB thumbNailBlob = ((OracleResultSet)rset).getBLOB(8);
            OutputStream fullOutstream = fullBlob.setBinaryStream(0);
            OutputStream thumbnailOutstream = thumbNailBlob.setBinaryStream(0);
            ImageIO.write(img, "jpg", fullOutstream);
            ImageIO.write(thumbNail, "jpg", thumbnailOutstream);
           
            fullOutstream.close();
            thumbnailOutstream.close();
            
            DBConnection.executeQuery(connection, "commit");
        } catch(Exception e){
            try {
                connection.rollback();
            } catch (Exception rollbackEx) {
                System.out.println("An error occured while rolling back the transaction: " + rollbackEx);
            }
            System.out.println("An error occured while uploading a photo: " + e);
            request.setAttribute("errorMessage", "An unexpected error occured while uploading the images. Please ensure all fields have been entered correctly.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImagesFromDir");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("An error occured while uploading photo: " + ex);
                request.setAttribute("errorMessage", "An unexpected error occured while uploading the images. Please ensure all fields have been entered correctly.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImage");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("SUCCESS");
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
