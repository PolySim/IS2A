package is.sio.tpJDBC ;
import java.sql.* ;

public class Compte {

  private Connection connect ;
  private PreparedStatement stmt ;
  private ResultSet rs ;

  public Compte(String url,String user, String passwd,String idClient) 
    throws CompteInconnuException, SQLException {

  }

  public Connection getConnection() { return null ; }

  public double getSolde() throws SQLException {
    return 0.0 ;
  }

  public void setSolde(double value) throws SQLException {
    
  }

  public  String getIdClient() throws SQLException {
    return null ;
  }
  public void close() throws SQLException {
    
  }
}
