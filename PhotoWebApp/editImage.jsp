<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <title>Edit Image</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/base/jquery-ui.css">
	<script type="text/javascript">
	$(function() {
	    $('.date-picker').datepicker( {
	        changeMonth: true,
	        changeYear: true,
	    });
	});
	</script>
</head>

<body> 
    <p><b>Edit Image</b></p>
            
    <p><a href="ViewImage?${picId}">View Image</a> | <a href="UploadImage">Upload New Image</a></p>
    
    ${errorMessage}
    
    <hr>
    
    <form name="editImage" method="POST" action="/PhotoWebApp/EditImage?${picId}">
        <table>
            <tbody>
            <tr>
                <th>Subject:</th>
                <td><input name="subject" size="50" maxlength="128" type="text" value="${subject}"></td>
            </tr>
            <tr>
                <th>Place:</th>
                <td><input name="place" size="50" maxlength="128" type="text" value="${place}"></td>
            </tr>
            <tr>
                <th>Date:</th>
                <td><input name="date" id="date" class="date-picker" maxlength="10" value="${date}"/></td>
            </tr>
            <tr>
                <th>Description:</th>
                <td><textarea name="description" rows="4" cols="57" maxlength="2048">${description}</textarea></td>
            </tr>
            <tr>
                <th>Access:</th>
                <td>
	                <select name="access">
	                    <c:forEach items="${groups}" var="group">
	                        <option value="${group[1]}">${group[0]}</option>
	                    </c:forEach>
	                </select>
                </td>
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
