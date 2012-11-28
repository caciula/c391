<html>

<head>
  <title>Account Management</title>
  <link type="text/css" rel="stylesheet" href="/PhotoWebApp/resources/style/style.css"/>
</head>

<body>

    <jsp:include page="resources/includes/header.jsp" />

    <div class="content">
    
        <p class="pageTitle">Account Management</p>
        
        <hr>
        
        <form name="AccountManagement" action="AccountManagement" method="post">
            <table>
                <tbody>
   
                <tr>
                    <th>Username: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="username" value="${username}" readonly="readonly"></td>
                </tr>
                <tr>
                    <th>Password: <span class="requiredField">*</span></th>
                    <td><input type="password" maxlength="24" name="password" value="${password}"></td>
                </tr>
                <tr>
                    <th>First name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="firstname" value="${firstname}"></td>
                </tr>
                <tr>
                    <th>Last name: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="24" name="lastname" value="${lastname}"></td>
                </tr>
                <tr>
                    <th>Address: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="128" name="address" value="${address}"></td>
                </tr>
                <tr>
                    <th>Email: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="128" name="email" value="${email}"></td>
                </tr>
                <tr>
                    <th>Phone number: <span class="requiredField">*</span></th>
                    <td><input type="text" maxlength="10" size="10" name="phonenumber" value="${phonenumber}"></td>
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
