<%@ page import="src.AtomicInteger" %>
<%! AtomicInteger totalCount = new AtomicInteger(); %>
<%
Integer userCount = (Integer) session.getAttribute("userCount");
if (userCount == null) {
  userCount = 0;
}

userCount++;
session.setAttribute("userCount", userCount);

int total = totalCount.incrementAndGet();
%>
<html>
<body>
<h2>Vous avez accede <%= userCount %> fois a cette page sur les <%= total %> au total</h2>
</body>
</html>
