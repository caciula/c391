<html>

<head>
  <title>Register</title>
</head>

<body>
  <form name="Register" action="Register" method="post">
    Username: <input type="text" maxlength="24" name="username" value=${username}> <br>
    Password: <input type="password" maxlength="24" name="password" value=${password}> <br>
    First name: <input type="text" maxlength="24" name="firstname" value=${firstname}> <br>
    Last name: <input type="text" maxlength="24" name="lastname" value=${lastname}> <br>
    Address: <input type="text" maxlength="128" name="address" value=${address}> <br>
    Email: <input type="text" maxlength="128" name="email" value=${email}> <br>
    Phone number: <input type="text" maxlength="10" name="phonenumber" value=${phonenumber}> <br>
    <input type="submit" value="Submit">
  </form>

  <br>

  ${output}

</body>

</html>
