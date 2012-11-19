package main.web;  

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import main.util.DBConnection;
import main.util.SQLQueries;
import main.util.Filter;

/**
 * Backing servlet for the Home screen (Home.jsp)
 * 
 *  @author Tim Phillips
 */
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *  GET command for Home.jsp
     *  Gets the top images to display on the screen.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        ArrayList<String[]> topImages = new ArrayList<String[]>();
        
        Connection connection = null;
        try {
            // Obtain the top images
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGES_SORTED_BY_VIEWS);
            ResultSet results = preparedStatement.executeQuery();
            int count = 0;
            int lastViewCount = 0;
            // Display the top 5 images. In case of a tie, display all tied images.
            while (results.next() && (count < 5 || results.getInt(1) == lastViewCount)) {
                if (Filter.isViewable(userName, results.getString(2))) {
                    String[] image = new String[2];
                    image[0] = Integer.toString(results.getInt(1));
                    image[1] = results.getString(2);
                    topImages.add(image);
                    lastViewCount = results.getInt(1);
                    count++;
                }
            }
            connection.close();
            
        } catch (Exception e) {
            System.out.println("An error occurred while obtaining the Top Images: " + e);
            request.setAttribute("errorMessage", "An error occurred while obtaining the Top Images.");
            request.setAttribute("errorBackLink", "/PhotoWebApp/Home");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("topImages", topImages);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Home.jsp");
        dispatcher.forward(request, response);
    }
}
