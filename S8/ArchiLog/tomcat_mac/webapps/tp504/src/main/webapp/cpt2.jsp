<%!
class Cpt {
  private int value = 0;

  public void increment() {
    value++;
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    return "" + value;
  }
}

Cpt totalCount = new Cpt();
%>
<%
Integer userCount = (Integer) session.getAttribute("userCount");
if (userCount == null) {
  userCount = 0;
}

userCount++;
session.setAttribute("userCount", userCount);

totalCount.increment();
%>
<html>
<body>
<h2>Vous avez accede <%= userCount %> fois a cette page sur les <%= totalCount %> au total</h2>
</body>
</html>
