<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>Home</title> 
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
	    <p class="pageTitle">Home</p>
	    
	    <hr/>
	    
        <p>Welcome to PhotoWeb!</p>
        
        <ul> 
            <c:if test='${username != null && username != ""}'>
                <li>
                You are already logged in as ${username}.
                Click <a href="/PhotoWebApp/ViewUserImages?${username}">here</a> to view your profile, manage your groups, and upload images.
                </li>
            </c:if>
            <li><p><a href="/PhotoWebApp/Login">Log In</a></p></li>
            <li><p><a href="/PhotoWebApp/Register">Register</a></p></li>
            <li><p><a href="/PhotoWebApp/Search">Search</a></p></li>
        </ul> 
        
        <p>Also need to display the top 5 images here?</p>

    </div>
</body>
</html>
