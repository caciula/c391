<html>

<head>
  <title>Register</title>
  <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Register</p>
        
        <hr>
        
        <form name="Register" action="Register" method="post">
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
                    <th>First name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="firstname"></td>
                </tr>
                <tr>
                    <th>Last name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="lastname"></td>
                </tr>
                <tr>
                    <th>Address: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="128" name="address"></td>
                </tr>
                <tr>
                    <th>Email: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="128" name="email"></td>
                </tr>
                <tr>
                    <th>Phone number: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="10" size="10" name="phonenumber"></td>
                </tr>           
                <tr>
                    <th></th>
                    <td><input type="submit" value="Submit"></td>
                </tr>
                </tbody>
             </table>
         </form>

    <br>

    ${output}
    </div>

</body>

</html>
