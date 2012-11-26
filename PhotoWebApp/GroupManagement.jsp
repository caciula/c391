<html>

<head>
    <title>Group Management</title>
    <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />
    
    <div class="content">
        
        <p class="pageTitle">Group Management</p>
        
        <hr>
        
        <form name="GroupManagement" action="GroupManagement" method="post">
              <table>
                <tbody>
                <tr>
                    <th>Group Name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" size="24" name="groupname"></td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <input type="submit" name="submit" value="Create">
                        <input type="submit" name="submit" value="Delete">

                        <input type="button" value="Cancel" onclick="location.href='ViewProfile?${username}'">
                    </td>
                </tr>
                </tbody>
             </table>
        </form>
    </div>
</body>

</html>
