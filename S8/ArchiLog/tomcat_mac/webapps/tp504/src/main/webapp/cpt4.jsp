<%@ page isELIgnored="false" %>
<jsp:useBean id="totalCount" class="src.Cpt" scope="application" />
<jsp:useBean id="userCount" class="src.Cpt" scope="session" />
<%
userCount.incrementAndGet();
totalCount.incrementAndGet();
%>
<html>
<body>
<h2>Vous avez accede ${sessionScope.userCount.value} fois a cette page sur les ${applicationScope.totalCount.value} au total</h2>
</body>
</html>
