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
	connection = DriverManager.getConnection(dbstring,"caciula","c391rulez");
	connection.setAutoCommit(false);
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}
	
//select the user table from the underlying db and validate the user name and password
Statement statement = null;
ResultSet resultSet = null;
String sql = "select password from users where user_name = '"+username+"'";

try {
	statement = connection.createStatement();
	resultSet = statement.executeQuery(sql);
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}

String truePassword = "";

while (resultSet != null && resultSet.next()) {
	truePassword = (resultSet.getString(1)).trim();
}

//display the result
if (password.equals(truePassword)) {
	out.println("Login successful");
} else {
	out.println("Invalid username/password");
}

try {
	connection.close();
} catch (Exception ex) {
	out.println("<hr>" + ex.getMessage() + "<hr>");
}

%>
</BODY>
</HTML>

