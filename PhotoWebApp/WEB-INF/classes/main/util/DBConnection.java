package main.util;

import java.sql.*;
import java.util.Properties;

/**
 *  Helper utility to obtain a database connection with the specified parameters.
 * 
 *  @author Tim Phillips, Gabriel Caciula
 */
public class DBConnection {
	private DBConnection() {};
	
	public static Connection createConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
	    
	    Connection connection = null;
	    try {
	        // Load the db information from a config file
	        Properties prop = new Properties();
	        prop.load(DBConnection.class.getClassLoader().getResourceAsStream(("main/config/config.properties")));
	        String username = prop.getProperty("username");
	        String password = prop.getProperty("password");
            String dbString = prop.getProperty("dbString");
            String dbDriver = prop.getProperty("dbDriver");	        

	        Class<?> driverClass = Class.forName(dbDriver);
	        DriverManager.registerDriver((Driver) driverClass.newInstance());
	    
	        connection = DriverManager.getConnection(dbString, username, password);
	        connection.setAutoCommit(false);
	    } catch (Exception ex) {
	        System.out.println("An error occurred while obtaining a connection to the database. Please ensure connection parameters have been set up correctly.");
	    }

        return connection;
	}
}