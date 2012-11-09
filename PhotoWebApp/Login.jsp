<html>

<head>
  <title>Login</title>
</head>

<body>
  <form name="Login" action="Login" method="post">
    Username: <input type="text" maxlength="24" name="username"> <br>
    Password: <input type="password" maxlength="24" name="password"> <br>
    <input type="submit" value="Submit">
  </form>

  <br>

  <form method="link" action="Register">
    <input type="submit" value="Click here to register">
  </form>

  <br>

  ${output}

</body>

</html>
