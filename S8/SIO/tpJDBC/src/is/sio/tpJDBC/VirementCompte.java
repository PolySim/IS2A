package is.sio.tpJDBC;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class VirementCompte {

    public static void main(String[] args) {
        try {
            String nomBase_1 = args[0];
            String idClient_1 = args[1];
            String nomBase_2 = args[2];
            String idClient_2 = args[3];
            double montant = Double.parseDouble(args[4]);

            Properties props = new Properties();
            FileInputStream in = new FileInputStream("database.properties");
            props.load(in);
            String server = props.getProperty("jdbc.server");
            String username = props.getProperty("jdbc.user");
            String password = props.getProperty("jdbc.password");
            String protocoleURL = props.getProperty("jdbc.protocole");

            String url = protocoleURL + "://" + server + "/";

            Compte compte1 = new Compte(
                url + nomBase_1,
                username,
                password,
                idClient_1
            );
            Compte compte2 = new Compte(
                url + nomBase_2,
                username,
                password,
                idClient_2
            );

            System.out.println(compte1);
            System.out.println(compte2);

            compte1.startTransaction();
            compte2.startTransaction();

            try {
                compte1.setSolde(compte1.getSolde() - montant);
                compte2.setSolde(compte2.getSolde() + montant);
                if (compte1.getSolde() < 0) {
                    throw new CompteNegatifException("Solde négatif");
                }
            } catch (Exception e) {
                compte1.rollback();
                compte2.rollback();
                throw e;
            }

            System.out.println(
                "virement de " +
                    montant +
                    " euros de " +
                    compte1.getIdClient() +
                    " vers " +
                    compte2.getIdClient()
            );

            System.out.println(compte1);
            System.out.println(compte2);

            compte1.commit();
            compte2.commit();
            compte1.close();
            compte2.close();
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
        } catch (CompteNegatifException e5) {
            System.err.println("solde négatif : " + e5.getMessage());
        }
    }
}
