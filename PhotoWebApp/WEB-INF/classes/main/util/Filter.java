package main.util;

import main.util.DBConnection;
import main.util.SQLQueries;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class Filter{

	//Determine if the provided picture id is viewalble by the current user
	public static boolean isViewable(String currentUser, String picture_Id){
        Connection myConn = null;
        try { 
        	myConn = DBConnection.createConnection();
            PreparedStatement findImage = myConn.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
           	findImage.setString(1, picture_Id);
            ResultSet result = findImage.executeQuery();
            String Owner;
            int permission;
            if (result.next()) {
          		owner = result.getString("owner_name");
                permission =  result.getInt("permitted");
			}else{
				//No image found for id
				return false;
			}
			
			 // Determine if the user may view the image
            if (permission == 1 || owner.equals(currentUser) || currentUser.equals("admin")) {
                    return true;
            } else if (permission == 2) {
                    return false;
            } else {
                    PreparedStatement findMembers = myConn.prepareStatement(SQLQueries.GET_MEMBERS_BY_GROUP_ID);
                    findMembers.setInt(1, permission);
                    ResultSet membersResult = findMembers.executeQuery();
                    while(membersResult.next()) {
                        if (membersResult.getString("friend_id").equals(currentUser)) {
                            return true;
                        }
                    }
                    return false;
            }
           
        } catch(Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
        } finally {
        	try {
            myConn.close();
        	} catch (Exception ex) {
        		System.err.println("Exception: " + ex.getMessage());
        	}
        }        
      return false;
	}

}