<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Register</title>
</head>
<body>
<h1>Register</h1>
<c:url var="registerUrl" value="/register"/>
<form action="${registerUrl}" method="post">
  <div>
    <label> Email:
      <input type="text" name="email">
    </label>
  </div>
  <div>
    <label> Password:
      <input type="password" name="password">
    </label>
  </div>
  <div>
    <input type="submit" value="Lets go!"/>
  </div>

</form>
</body>
</html>