<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <title>Edit Image</title> 
</head>

<body> 
    <p><b>Edit Image</b></p>
            
    <p><a href="ViewImage?${picId}">View Image</a> | <a href="uploadImage.html" >Upload New Image</a></p>
    
    <hr>
    
    <form name="editImage" method="POST" action="/PhotoWebApp/EditImage?${picId}">
        <table>
            <tbody>
            <tr>
                <th>Subject: </th>
                <td><input name="subject" size="50" maxlength="128" type="text" value="${subject}"></td>
            </tr>
            <tr>
                <th>Place: </th>
                <td><input name="place" size="50" maxlength="128" type="text" value="${place}"></td>
            </tr>
            <tr>
                <th>Date: </th>
                <td>TODO</td>
            </tr>
            <tr>
                <th>Description: </th>
                <td><textarea name="description" rows="4" cols="57" maxlength="2048">${description}</textarea></td>
            </tr>
            <tr>
                <td colspan="2" align="CENTER"><input name=".submit" value="Save" type="submit"></td>
            </tr>
            </tbody>
         </table>
    </form>
    
    <p><img src ="/PhotoWebApp/GetFullImage?${picId}"></p>
    
</body>
</html>
