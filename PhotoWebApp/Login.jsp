<html>

<head>
  <title>Login</title>
  <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Login</p>
        
        <hr>
    
        <form name="Login" action="Login" method="post">
            <table>
                <tbody>
                <tr>
                    <th>Username: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="username"></td>
                </tr>
                <tr>
                    <th>Password: <span class="requiredField">*</span></th>
                    <td><input type="password" maxlength="24" name="password"></td>
                </tr>
                <tr>
                    <th></th>
                    <td><input type="submit" value="Submit"></td>
                </tr>
                <tr>
                    <th></th>
                    <td><br><input type="button" value="Click here to register" onclick="location.href='Register'"></td>
                </tr>
                </tbody>
            </table>
        </form>
        <br>
  ${output}
   </div>

</body>

</html>
