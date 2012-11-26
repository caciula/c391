<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <title>View User (${userFirstName} ${userLastName} - ${username})</title> 
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
                <td>${username}</td>
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
        
        <a href="/PhotoWebApp/GroupManagement">Create/delete groups</a> | <a href="/PhotoWebApp/UserManagement">Add/remove users to/from groups</a>
	    
	    <p>
	    <table>
            <tbody>
                <tr>
                    <th>Group Name</th>
                    <th>Members</th>
                </tr>
                <c:forEach items="${groups}" var="group" varStatus="groupLoop">
                    <tr>
                        <td>${group}</td>
                        <td>
                            <c:forEach items="${groupMembers[groupLoop.index]}" var="member">
                            ${member}  &nbsp;
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </p>
	    
	    
	    <p class="pageTitle">Images:</p>
        
        <a href="UploadImage">Upload Single Image</a> |
        <a href="UploadImagesFromDir">Upload Multiple Images</a>
	    <br><br>
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
