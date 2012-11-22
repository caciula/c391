package main.util;

import main.util.DBConnection;
import main.util.SQLQueries;
import java.sql.*;
import java.util.ArrayList;

/**
 *  Helper utility to perform common filtering operations.
 * 
 *  @author Tim Phillips, Evan Fauser
 */
public class Filter{

    /**
     *  Determines if the provided picture id is viewable by the current user
     *  @param currentUser
     *  @param picture_id
     *  @return true if the image is viewable, false otherwise
     */
	public static boolean isViewable(String currentUser, String picture_Id){
        Connection myConn = null;
        try { 
        	myConn = DBConnection.createConnection();
            PreparedStatement findImage = myConn.prepareStatement(SQLQueries.GET_IMAGE_BY_ID);
           	findImage.setString(1, picture_Id);
            ResultSet result = findImage.executeQuery();
            String owner;
            int permission;
            if (result.next()) {
          		owner = result.getString("owner_name");
                permission =  result.getInt("permitted");
			} else{
				// No image found for id
				return false;
			}
            
            if (currentUser == null) {
                if (permission == 1) {
                    return true;
                } else {
                    return false;
                }
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
            System.err.println("Exception occured while determining if a photo with id " + picture_Id + " is viewable: " + ex.getMessage());
        } finally {
        	try {
        	    myConn.close();
        	} catch (Exception ex) {
                System.err.println("Exception occured while determining if a photo with id " + picture_Id + " is viewable: " + ex.getMessage());
        	}
        }        
      return false;
	}
	
    /**
     *  Obtains the groups that the current user has access to:
     *      a) Groups that the user has created
     *      b) Groups the user is a member of
     *      c) Public and Private groups 
     *  @param username
     *  @return ArrayList<String[]> - an array containing elements of [group_name,group_id]
     */
	public static ArrayList<String[]> getAllowedGroups(String username) throws Exception {
	    
	    ArrayList<String[]> groups = new ArrayList<String[]>();
	    Connection connection = null;
	    try {
	        connection = DBConnection.createConnection();
	        // Get the groups the user is a member of
	        ArrayList<Integer> userGroups = new ArrayList<Integer>();
	        PreparedStatement getUsersGroups = connection.prepareStatement(SQLQueries.GET_GROUPS_BY_USER_ID);
	        getUsersGroups.setString(1, username);
	        ResultSet allUsersGroups = getUsersGroups.executeQuery();
	        while (allUsersGroups.next()) {
	            userGroups.add(allUsersGroups.getInt("group_id"));
	        }

	        // Get the groups the user has created
	        ArrayList<Integer> groupsUserCreated = new ArrayList<Integer>();
	        PreparedStatement getUserCreatedGroups = connection.prepareStatement(SQLQueries.GET_USER_GROUPS);
	        getUserCreatedGroups.setString(1, username);
	        ResultSet groupsUserCreatedResults = getUserCreatedGroups.executeQuery();
	        while (groupsUserCreatedResults.next()) {
	            groupsUserCreated.add(groupsUserCreatedResults.getInt("group_id"));
	        }
	        
	        // Get all groups
	        PreparedStatement getAllGroups = connection.prepareStatement(SQLQueries.GET_ALL_GROUPS);
	        ResultSet allGroups = getAllGroups.executeQuery();
	        // Filter out all groups that the user did not create, is not a member of, and isn't public/private
	        while (allGroups.next()) {
	            int groupId = allGroups.getInt("group_id");
	            if (userGroups.contains(groupId) || groupsUserCreated.contains(groupId) || groupId == 1 || groupId == 2) {
	                String[] group = new String[2];
	                group[0] = allGroups.getString("group_name");
	                group[1] = Integer.toString((groupId));
	                groups.add(group);
	        }
	    }

        } catch( Exception ex ) {
            // Rethrow any exception to be recaught and handled by the calling method.
            throw ex;
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch ( SQLException ex) {
                // Rethrow any exception to be recaught and handled by the calling method.
                throw ex;
            }
        }
        
	    return groups;
	}
}