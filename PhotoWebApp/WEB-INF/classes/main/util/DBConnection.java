package main.util;

import java.sql.*;

public class DBConnection {
	private DBConnection() {};
	
	public static Connection createConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		Class<?> driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        DriverManager.registerDriver((Driver) driverClass.newInstance());
	
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS", "caciula", "c301rulez");
        connection.setAutoCommit(false);
        return connection;
	}
	
    public static ResultSet executeQuery(Connection connection, String query) throws SQLException {
         Statement statement = connection.createStatement();
         return statement.executeQuery(query);
    }
	
    public static PreparedStatement getPreparedStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}