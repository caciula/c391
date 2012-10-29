package main.util;

import java.sql.*;

/**
 *  Utility class to connect to the specified database.
 * 
 *  @author Tim Phillips
 */
public class DBConnectionUtil  {

    /**
     *  Connects to the specified database
     *  @return Connection
     */
    public static Connection getConnection() throws Exception {
        String username = "tdphilli";
        String password = "********";
        String driverName = "oracle.jdbc.driver.OracleDriver";
        String dbstring ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";

        // Connect to the database
        Class drvClass = Class.forName(driverName); 
        DriverManager.registerDriver((Driver) drvClass.newInstance());
        return(DriverManager.getConnection(dbstring,username,password));
    }
}
