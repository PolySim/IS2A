package fr.isia4;

import java.sql.*;


public class DbContext {

  String url = "jdbc:postgresql://localhost:5432/archilog";
  String user = "postgres";
  String passwd = "postgres";
  Connection connect;

  public DbContext() throws SQLException {
     this.connect = DriverManager.getConnection(url, user, passwd);
  }
}
