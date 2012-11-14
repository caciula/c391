<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>View User (${userFirstName} ${userLastName} - ${userName})</title> 
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
	    <p class="pageTitle">My Profile</p>
        
        <hr>
	    
        <table>
            <tbody>
            <tr>
                <th>Username: </th>
                <td>${userName}</td>
            </tr>
            <tr>
                <th>Name: </th>
                <td>${userFirstName} ${userLastName}</td>
            </tr>
            <tr>
                <th>Phone number: </th>
                <td>${phone}</td>
            </tr>
            <tr>
                <th>Email: </th>
                <td>${email}</td>
            </tr>
            <tr>
                <th>Address: </th>
                <td>${address}</td>
            </tr>
            </tbody>
        </table>

        <p class="pageTitle">Groups:</p>
        
        <a href="/PhotoWebApp/CreateGroup">Create a new group</a> 
        <br>
        <a href="/PhotoWebApp/AddUserToGroup">Add a user to a group</a>
	    
	    <p>TODO: Display a table of the user's groups?</p>
	    
	    <p class="pageTitle">Images:</p>
        
        <p><a href="UploadImage">Upload New Image</a></p>
	    
		<table>
		    <tbody>
		    <tr>
		    <c:forEach items="${imageIds}" var="imageId">
		       <a href="/PhotoWebApp/ViewImage?${imageId}"><img src ="/PhotoWebApp/GetThumbnailImage?${imageId}"></a>
		    </c:forEach>
		    </tr>
		    </tbody>
		 </table>
	 </div>
</body>
</html>
