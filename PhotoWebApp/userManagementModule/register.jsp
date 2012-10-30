<HTML>

<HEAD>
<TITLE>Your Login Result</TITLE>
</HEAD>

<BODY>
<%@ page import="java.sql.*" %>
<% 

//get the user input from the login page
String username = (request.getParameter("username")).trim();
String password = (request.getParameter("password")).trim();
String firstname = (request.getParameter("firstname")).trim();
String lastname = (request.getParameter("lastname")).trim();
String address = (request.getParameter("address")).trim();
String email = (request.getParameter("email")).trim();
String phonenumber = (request.getParameter("phonenumber")).trim();

//establish the connection to the underlying database
Connection connection = null;

String driverName = "oracle.jdbc.driver.OracleDriver";
String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	
try {
	//load and register the driver
	Class driverClass = Class.forName(driverName); 
	DriverManager.registerDriver((Driver) driverClass.newInstance());
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}

try {
	//establish the connection 
	connection = DriverManager.getConnection(dbstring,"caciula","PASSWORD");
	connection.setAutoCommit(false);
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}
	
Statement statement = null;
ResultSet resultSet = null;
String sql1 = "insert into users values('"+username+"', '"+password+"', CURRENT_TIMESTAMP)";
String sql2 = "insert into persons values('"+username+"', '"+firstname+"', '"+lastname+"', '"+address+"', '"+email+"', '"+phonenumber+"')";

try {
	statement = connection.createStatement();
	statement.executeQuery(sql1);
	statement.executeQuery(sql2);
	connection.commit();
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
} catch (SQLException e) {
	if (connection != null) {
		connection.rollback();
		out.println("<hr>Connection rollback...<hr>");
	}
}

try {
	connection.close();
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}

%>
</BODY>
</HTML>

