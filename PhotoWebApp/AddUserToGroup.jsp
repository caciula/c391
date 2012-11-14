<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
  <title>AddUserToGroup</title>
  <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />
    
        <div class="content">
        
	        <p class="pageTitle">Add User To Group</p>
	        
	        <hr>
	        
			<form name="AddUserToGroup" action="AddUserToGroup" method="post">
			    <table>
	                <tbody>
	                <tr>
	                    <th>Group name: <span class="requiredField">*</span></th>
	                    <td>
	                       <select name="groups">
                               <c:forEach items="${groups}" var="group">
                                  <option value="${group[0]}">${group[1]}</option>
                               </c:forEach>
                           </select>
                       </td>
	                </tr>
	                <tr>
                        <th>Username:</th>
                        <td>
                            <input type="text" maxlength="24" name="username">
                        </td>
                    </tr>
	                <tr>
	                    <th></th>
	                    <td>
	                        <input type="submit" value="Submit" >
	                        <input type="button" value="Cancel" onclick="location.href='ViewUserImages?${username}'">
	                    </td>
	                </tr>
	                </tbody>
               </table>
			</form>
			${output}
		</div>
</body>

</html>