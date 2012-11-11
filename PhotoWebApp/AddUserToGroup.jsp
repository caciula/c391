<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
  <title>AddUserToGroup</title>
</head>

<body>
  <form name="AddUserToGroup" action="AddUserToGroup" method="post">
    Group name: <select name="groups">
      <c:forEach items="${groups}" var="group">
        <option value="${group[0]}">${group[1]}</option>
      </c:forEach>
    </select> <br>
    Username: <input type="text" maxlength="24" name="username"> <br>
    <input type="submit" value="Submit">
  </form>

  <br>

  ${output}

</body>

</html>
