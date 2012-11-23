package main.util;

import java.sql.*;

/**
 *  Helper utility to obtain a database connection with the specified parameters.
 * 
 *  @author Tim Phillips, Gabriel Caciula
 */
public class DBConnection {
	private DBConnection() {};
	
	public static Connection createConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		Class<?> driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        DriverManager.registerDriver((Driver) driverClass.newInstance());
	
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS", "tdphilli", "CMPUT391");
        connection.setAutoCommit(false);
        return connection;
	}
	
}