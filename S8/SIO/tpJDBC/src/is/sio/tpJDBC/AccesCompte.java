package is.sio.tpJDBC;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class AccesCompte {

    public static void main(String args[]) {
        try {
            String nomBase = args[0];
            String idClient = args[1];
            Properties props = new Properties();
            //lecture du fichier database.properties
            FileInputStream in = new FileInputStream("database.properties");
            props.load(in);
            String server = props.getProperty("jdbc.server");
            String username = props.getProperty("jdbc.user");
            String password = props.getProperty("jdbc.password");
            String protocoleURL = props.getProperty("jdbc.protocole");

            String url = protocoleURL + "://" + server + "/" + nomBase;

            // programme :
            Compte compte = new Compte(url, username, password, idClient);
            System.out.println(
                "compte : " +
                    compte.getIdClient() +
                    ", solde : " +
                    compte.getSolde()
            );
            compte.setSolde(compte.getSolde() + 20.0);
            System.out.println("ajout de 20.0 au compte");
            System.out.println(
                "compte : " +
                    compte.getIdClient() +
                    ", solde : " +
                    compte.getSolde()
            );
            compte.close();
        } catch (ArrayIndexOutOfBoundsException e0) {
            System.err.println(
                "erreur argument: java AccesCompte nomBase idClient"
            );
        } catch (IOException e1) {
            System.err.println("erreur fichier database.properties");
        } catch (SQLException e3) {
            System.err.println("erreur SQL : " + e3.getMessage());
        } catch (CompteInconnuException e4) {
            System.err.println("compte client inconnu");
        }
    }
}
