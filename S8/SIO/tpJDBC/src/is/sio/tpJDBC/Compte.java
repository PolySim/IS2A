package is.sio.tpJDBC;

import java.sql.*;

public class Compte {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public Compte(String url, String user, String passwd, String idClient)
        throws CompteInconnuException, SQLException {
        connect = DriverManager.getConnection(url, user, passwd);

        String SQLrequestSelect = "select * from compte where id_client = ?;";
        stmt = connect.prepareStatement(
            SQLrequestSelect,
            ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE
        );
        stmt.setString(1, idClient);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new CompteInconnuException();
        }
    }

    public Connection getConnection() {
        return connect;
    }

    public void startTransaction() throws SQLException {
        connect.setAutoCommit(false);
        connect.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
    }

    public void commit() throws SQLException {
        connect.commit();
    }

    public void rollback() throws SQLException {
        connect.rollback();
    }

    public double getSolde() throws SQLException {
        rs.refreshRow();
        return rs.getDouble("solde");
    }

    public void setSolde(double value) throws SQLException {
        rs.updateDouble("solde", value);
        rs.updateRow();
    }

    public String getIdClient() throws SQLException {
        rs.refreshRow();
        return rs.getString("id_client");
    }

    public void close() throws SQLException {
        connect.close();
    }

    public String toString() {
        try {
            return (
                "Compte " + getIdClient() + " avec un solde de " + getSolde()
            );
        } catch (SQLException e) {
            return "Compte [erreur: " + e.getMessage() + "]";
        }
    }
}
