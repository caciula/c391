<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Search</title> 
</head>

<body> 

<jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Search</p>
        
        <hr/>

<form name="DataAnalysis" method="POST" action="DataAnalysis">
<table>
	<tr>
		<th>Please specify your parameters</th>
	</tr>
  <tr>
    <th>User: </th>
    <td>
    	<select name="user">
    	<option value="All">All</option>
		<c:forEach items="${users}" var="foundUser">
			  <option value="${foundUser}">${foundUser}</option>
		</c:forEach> 
    	</select>
    
    </td>
  </tr>
    <tr>
    <th>Subject: </th>
    <td>
    	<select name="subject">
    	<option value="All">All</option>
		<c:forEach items="${subjects}" var="foundSubject">
			  <option value="${foundSubject}">${foundSubject}</option>
		</c:forEach> 
    	</select>
    
    </td>
  </tr>
  
  <tr>
    <th>From Date: </th>
    <td><input name="fromDate" type="textfield" size="30" ></input></td>
  </tr>
  <tr>
    <th>To Date: </th>
    <td><input name="toDate" type="textfield" size="30" ></input></td>
  </tr>
  
  <tr>
    <td ALIGN=CENTER COLSPAN="2"><input type="submit" name=".submit" value="Generate Report"></td>
  </tr>
</table>
    </div>
</form>
</body> 
</html>
