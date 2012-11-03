package main.util;

import java.sql.*;

public class DBConnection {
    private Connection connection = null;

    public DBConnection() throws Exception {
        Class<?> driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        DriverManager.registerDriver((Driver) driverClass.newInstance());
	
        this.connection = DriverManager.getConnection("jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS", "tdphilli", "********");
        this.connection.setAutoCommit(true);
    }
	
    public ResultSet executeQuery(String sqlStatement) throws SQLException {
         Statement statement = connection.createStatement();
         return statement.executeQuery(sqlStatement);
    }
	
    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.connection.prepareStatement(query);
    }
	
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}