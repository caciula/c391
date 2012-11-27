<!DOCTYPE html PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title>Edit Image</title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"> 
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/base/jquery-ui.css">
	<script type="text/javascript">
	     $(function(){
	          $("#date").datepicker({dateFormat:"dd/mm/yy"});
	     });
	</script>
</head>

<body> 

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">

	    <p class="pageTitle">Edit Image</p>
	            
	    <p>
	       <a href="ViewProfile?${ownerName}">View My Profile</a> |
	       <a href="ViewImage?${picId}">View Image</a> |
	       <a href="RemoveImage?${picId}">Remove Image</a>
	    </p>
	    
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
	                <td><input name="date" id="date" class="date-picker" maxlength="10" value="${date}"/> 
	                <span class="formHintText">(dd/MM/yyyy)</span>
	                </td>
	            </tr>
	            <tr>
	                <th>Time:</th>
	                <td><input name="time" maxlength="5" value="${time}"/>
	                <span class="formHintText">(hh:mm)</span>
	                </td>
	            </tr>
	            <tr>
	                <th>Description:</th>
	                <td><textarea name="description" rows="4" cols="57" maxlength="2048">${description}</textarea></td>
	            </tr>
	            <tr>
	                <th>Access: <span class="requiredField">*</span></th>
	                <td>
		                <select name="access">
		                    <c:forEach items="${groups}" var="group">
	                             <c:choose>
	                                  <c:when test="${access == group[1]}">
	                                      <option value="${group[1]}" selected=true>${group[0]}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                      <option value="${group[1]}">${group[0]}</option>
	                                  </c:otherwise>
	                             </c:choose>
	                        </c:forEach>
		                </select>
	                </td>
	            </tr>
	            <tr>
	                <th></th>
	                <td>
	                    <br><input name=".submit" value="Save" type="submit">
	                    <input value="Cancel" type="button" onclick="location.href='ViewImage?${picId}'">
	                </td>
	            </tr>
	            </tbody>
	         </table>
	    </form>
	    
	    <p><a href="/PhotoWebApp/GetFullImage?${picId}" target="_blank"><img src ="/PhotoWebApp/GetFullImage?${picId}" ></a></p>
    </div>
</body>
</html>
