<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <title>View Album (${userFirstName} ${userLastName} - ${userName})</title> 
</head>

<body> 
    <p><b>View Album (${userFirstName} ${userLastName} - ${userName})</b></p>
    
    <p><a href="UploadImage">Upload New Image</a></p>
    
    <hr>
    
	<table>
	    <tbody>
	    <tr>
	    <c:forEach items="${imageIds}" var="imageId">
	       <a href="/PhotoWebApp/ViewImage?${imageId}"><img src ="/PhotoWebApp/GetThumbnailImage?${imageId}"></a>
	    </c:forEach>
	    </tr>
	    </tbody>
	 </table>
	 
</body>
</html>
