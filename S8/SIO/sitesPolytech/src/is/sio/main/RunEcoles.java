
package is.sio.main;

import is.sio.jpa.services.EcoleDejaCreeeException;
import is.sio.jpa.services.ServiceEcolesPolytech;

public class RunEcoles {

    public static void main(String[] args) {
        new RunEcoles().run();
    }

    public void run() {
        ServiceEcolesPolytech service = new ServiceEcolesPolytech();
        try {
            service.creerEcolePolytech("Polytech Lille", "https://polytech-lille.fr", 48.70433003805475,
                    2.1667098999023438);
        } catch (EcoleDejaCreeeException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
