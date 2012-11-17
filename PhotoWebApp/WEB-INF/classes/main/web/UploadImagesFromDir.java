package main.web;
import main.util.DBConnection;
import main.util.SQLQueries;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Backing servlet for the Upload Multiple Images screen (uploadImagesFromDir.jsp)
 * 
 *  @author Tim Phillips
 */
public class UploadImagesFromDir extends HttpServlet {
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
        
        // Obtain the groups to display in the permission drop down
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
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/UploadImagesFromDir.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     *  Called when the "Upload" button is clicked on the applet
     *  Uploads all images in the selected directory.
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
        String updateImageDetails = "false";
        String imageIdsString = null;
        ArrayList<Integer> imageIds = new ArrayList<Integer>();
        
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
                    } else if (item.getFieldName().equals("updateDetails")){
                        updateImageDetails = Streams.asString(stream);
                    } else if (item.getFieldName().equals("imageIds")){
                        imageIdsString = Streams.asString(stream);
                    }
                } else{
                    img = ImageIO.read(stream);
                    thumbNail = shrink(img, 10);
                    ResultSet rset1 = DBConnection.executeQuery(connection, "SELECT pic_id_sequence.nextval from dual");
                    rset1.next();
                    photoId = rset1.getInt(1);
                    DBConnection.executeQuery(connection, "INSERT INTO images VALUES(" + photoId +
                           ",'" + username + "',null,null,null,null,null,empty_blob(), empty_blob())");
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
                    imageIds.add(photoId);
                }

            }
            DBConnection.executeQuery(connection, "commit");
            connection.close();
        } catch(Exception e){
            System.out.println("An error occured while uploading a photo: " + e);
            request.setAttribute("errorMessage", "An unexpected error occured while uploading the images. Please ensure all fields have been entered correctly.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImage");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        }
        
        if (updateImageDetails.equals("true")) {

            Connection updateConnection = null;
            try{
                String[] imageIdsArray = imageIdsString.split("\\s");
                for(String imageId : imageIdsArray) {
                    updateConnection = DBConnection.createConnection();
                    PreparedStatement preparedStatement = updateConnection.prepareStatement(SQLQueries.UPDATE_IMG_DETAILS_BY_ID);
                    preparedStatement.setString(1, subject);
                    preparedStatement.setString(2, place);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:mm");
                    if (!date.equals("")) {
                        if (time.equals("")) {
                            time = "12:00";
                        }
                        String inputDateTime = date + " " + time;
                        Date dateTime = formatter.parse(inputDateTime);
                        preparedStatement.setTimestamp(3, new Timestamp(dateTime.getTime()));
                    } else {
                        preparedStatement.setTimestamp(3, null);
                    }
                    preparedStatement.setString(4, description);
                    preparedStatement.setString(5, access);
                    preparedStatement.setString(6, imageId);
                    preparedStatement.executeUpdate();
                    DBConnection.executeQuery(updateConnection, "commit");
                    updateConnection.close();
                }
            } catch (Exception e) {
                System.out.println("An error occured while uploading a photo: " + e);
                request.setAttribute("errorMessage", "An unexpected error occured while uploading the images. Please ensure all fields have been entered correctly.");
                request.setAttribute("errorBackLink", "/PhotoWebApp/UploadImage");
                request.getRequestDispatcher("/Error.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("/PhotoWebApp/ViewUserImages?" + username);
        } else {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            for(Integer imageId : imageIds) {
                out.println(imageId + " ");
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
