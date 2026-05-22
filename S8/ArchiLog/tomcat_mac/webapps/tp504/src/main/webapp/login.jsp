<%@ page isELIgnored="false" %>

<html><body>
<form action="${pageContext.request.contextPath}/verif">
  ${msg}<br>
    <input type="text" name="username" placeholder="Username"><br>
    <input type="password" name="password" placeholder="Password"><br>
    <input type="submit" value="Login">
</form>
</body>
</html>
