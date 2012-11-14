<html>

<head>
    <title>Create Group</title>
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
        
        <p class="pageTitle">Create Group</p>
        
        <hr>
        
        <form name="CreateGroup" action="CreateGroup" method="post">
              <table>
                <tbody>
                <tr>
                    <th>Group Name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" size="24" name="groupname"></td>
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
    </div>
</body>

</html>
