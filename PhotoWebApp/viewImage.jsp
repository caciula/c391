<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <title>View Image</title> 
</head>

<body> 
    <p><b>View Image</b></p>
    
    <p><a href="/PhotoWebApp/EditImage?${picId}" >Edit Image</a> | <a href="uploadImage.html" >Upload New Image</a> </p>
    
    <hr>
    
	<table>
	    <tbody>
	    <tr>
	        <th>Owner: </th>
	        <td>TODO</td>
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
	        <th>Date: </th>
	        <td>TODO</td>
	    </tr>
	    <tr>
	        <th>Description: </th>
	        <td>${description}</textarea></td>
	    </tr>
	    </tbody>
	 </table>
	    
    <img src ="/PhotoWebApp/GetFullImage?${picId}">
    
</body>
</html>
