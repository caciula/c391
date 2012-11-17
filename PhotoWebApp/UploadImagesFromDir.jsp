<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title>Upload Images</title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/base/jquery-ui.css">
    <script type="text/javascript">
    $(function() {
        $(function(){
            $("#date").datepicker({dateFormat:"dd/mm/yy"});
        });
    });

    function updateImageDetails(var1) {
        document.getElementById('imageIds').value= var1;
        document.uploadImages.submit();
    }
    </script>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
	    <p class="pageTitle">Upload Multiple Images</p>
	    
	    <p>
        <a href="ViewUserImages?${username}">View My Profile</a> |
        <a href="UploadImage">Upload Single Image</a>
        </p>
	        
	    <hr>
	    <form name="uploadImages" id="uploadImages" method="POST" enctype="multipart/form-data" action="/PhotoWebApp/UploadImagesFromDir">
	        <table>
	            <tr>
	                <th>Subject: </th>
	                <td><input name="subject" size="50" maxlength="128" type="text"></td>
	            </tr>
	            <tr>
	                <th>Place: </th>
	                <td><input name="place" size="50" maxlength="128" type="text"></td>
	            </tr>
	            <tr>
	                <th>Date: </th>
	                <td>
	                <input name="date" id="date" class="date-picker" maxlength="10"/>
	                <span class="formHintText">(dd/MM/yyyy)</span>
	                </td>
	            </tr>
	            <tr>
	                <th>Time: </th>
	                <td>
	                <input name="time" id="time" maxlength="5"/>
	                <span class="formHintText">(hh:mm)</span>
	                </td>
	            </tr>
	            <tr>
	                <th>Description: </th>
	                <td><textarea name="description" rows="4" cols="57" maxlength="2048"></textarea></td>
	            </tr>
	            <tr>
	                <th>Access: <span class="requiredField">*</span></th>
	                <td>
		                <select name="access">
		                    <c:forEach items="${groups}" var="group">
		                        <option value="${group[1]}" <c:if test="${group[1]==2}">selected=true</c:if>>${group[0]}</option>
		                    </c:forEach>
		                </select>
	                </td>
	            </tr>
	            <input type="hidden" name="imageIds" id="imageIds" value="">
                <input type="hidden" name="updateDetails" value="true">
	            </tbody>
	         </table>
	    </form>
	    
        <applet code="applet-basic_files/wjhk.JUploadApplet" name="JUpload" archive="applet-basic_files/wjhk.jar" mayscript="true" height="300" width="640">
            <param name="CODE" value="wjhk.jupload2.JUploadApplet">
            <param name="ARCHIVE" value="wjhk.jupload.jar">
            <param name="postURL" value="http://ui19.cs.ualberta.ca:16090/PhotoWebApp/UploadImagesFromDir">
            <param name="afterUploadURL" value="javascript:updateImageDetails('%body%')">
            <param name="stringUploadSuccess" value="">
            <param name="nbFilesPerRequest" value="2">
            Java 1.4 or higher plugin required.
        </applet>
	    
    </div>
</body>
</html>
