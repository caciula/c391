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
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        request.setAttribute("loggedInUser", userName);
        ArrayList<String[]> topImages = new ArrayList<String[]>();
        
        Connection connection = null;
        try {
            connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.GET_IMAGES_SORTED_BY_VIEWS);
            ResultSet results = preparedStatement.executeQuery();
            int count = 0;
            while (results.next() && count < 5) {
                if (Filter.isViewable(userName, results.getString(2))) {
                    String[] image = new String[2];
                    image[0] = Integer.toString(results.getInt(1));
                    image[1] = results.getString(2);
                    topImages.add(image);
                    count++;
                }
            }
            
        } catch (Exception e) {
            System.out.println("An error occurred while obtaining the Top Images: " + e);
        }
        
        request.setAttribute("topImages", topImages);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Home.jsp");
        dispatcher.forward(request, response);
    }
}
