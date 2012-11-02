package main.util;

import java.sql.*;

public class DBConnection {
	public Connection connection = null;
	public ResultSet resultSet = null;
	
	public DBConnection() throws Exception {
		Class<?> driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
		DriverManager.registerDriver((Driver) driverClass.newInstance());
	
		this.connection = DriverManager.getConnection("jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS", "caciula", "********");
		this.connection.setAutoCommit(false);
	}
	
	public void executeQuery(String sqlStatement) throws Exception {
			Statement statement = connection.createStatement();
			this.resultSet = statement.executeQuery(sqlStatement);
	}
	
	public void closeConnection() throws Exception {
		this.connection.close();
	}
}