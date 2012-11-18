<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>View Image</title> 
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />

    <div class="content">
	    <p class="pageTitle">View Image</p>

	    <p>
	        <c:if test='${ownerName==username}'>
                <a href="ViewUserImages?${ownerName}">View My Profile</a> |
                <a href="EditImage?${picId}">Edit Image</a>
	        </c:if>
	    </p>
	    
	    <hr>
	    
		<table>
		    <tbody>
		    <tr>
		        <th>Owner: </th>
		        <td>${ownerName}</td>
		    </tr>
		    <tr>
		        <th>Subject: </th>
		        <td>${subject}</td>
		    </tr>
		    <tr>
		        <th>Place: </th>
		        <td>${place}</td>
		    </tr>
		    <tr>
		        <th>Date / Time: </th>
		        <td>${date}</td>
		    </tr>
		    <tr>
		        <th>Description: </th>
		        <td>${description}</td>
		    </tr>
		    <tr>
	            <th>Access: </th>
	            <td>${access}</td>
	        </tr>
		    </tbody>
		 </table>
		    
	    <a href="/PhotoWebApp/GetFullImage?${picId}" target="_blank"><img src ="/PhotoWebApp/GetFullImage?${picId}" ></a>
    </div>
</body>
</html>
