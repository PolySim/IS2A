package is.sio.main;

import is.sio.jpa.entites.Domaine;
import is.sio.jpa.entites.EcolePolytech;
import is.sio.jpa.services.EcoleDejaCreeeException;
import is.sio.jpa.services.EcoleInconnueException;
import is.sio.jpa.services.IServiceEcolesPolytech;
import is.sio.jpa.services.ServiceEcolesPolytech;
import is.sio.jpa.services.SpecialiteExisteDejaException;
import java.util.Collection;

public class RunEcoles {

    public static void main(String[] args) {
        new RunEcoles().run();
    }

    public void run() {
        ServiceEcolesPolytech service = new ServiceEcolesPolytech();
        try {
            service.getTransaction().begin();
            service.creerEcolePolytech(
                "Lille",
                "https://polytech-lille.fr",
                48.70433003805475,
                2.1667098999023438
            );
            service.getTransaction().commit();
        } catch (EcoleDejaCreeeException e) {
            System.out.println(e.getMessage());
        } finally {
            if (service.getTransaction().isActive()) service
                .getTransaction()
                .rollback();
        }

        Collection<EcolePolytech> ecoles = service.getEcolesPolytech();
        ecoles.stream().forEach(ecole -> System.out.println(ecole));

        try {
            service.getTransaction().begin();
            EcolePolytech ecoleLille = service.getEcole("Lille");
            Collection<Domaine> domaines = service.getDomaines();
            domaines
                .stream()
                .forEach(domaine -> {
                    if (domaine.getId() != 3) domaine
                        .getEcoles()
                        .add(ecoleLille);
                    System.out.println(domaine);
                });
            service.getTransaction().commit();
        } catch (EcoleInconnueException e) {
            System.err.println("L'école Lille n'existe pas");
        } finally {
            if (service.getTransaction().isActive()) service
                .getTransaction()
                .rollback();
        }

        try {
            System.out.println("Ecole au nord de Lyon");
            service
                .getEcolesPolytechAuNordDe("Lyon")
                .stream()
                .forEach(ecole -> System.out.println(ecole));
            System.out.println("Ecole au sud de Lyon");
            service
                .getEcolesPolytechAuSudDe("Lyon")
                .stream()
                .forEach(ecole -> System.out.println(ecole));
        } catch (EcoleInconnueException e) {
            System.err.println("Ecole Lyon n'existe pas");
        }

        try {
            service.getTransaction().begin();
            service.getEcole("Annecy-Chambery").setSiteWeb("http://test");
            service.getTransaction().commit();
        } catch (EcoleInconnueException e) {
            System.err.println("Ecole Annecy-Chambery inconnu");
        } finally {
            if (service.getTransaction().isActive()) service
                .getTransaction()
                .rollback();
        }

        try {
            service.getTransaction().begin();
            service.addSpecialite(
                "Lille",
                "IS",
                "IS",
                IServiceEcolesPolytech.TypeSpec.INITIALE
            );
            service.addSpecialite(
                "Lille",
                "IS2A",
                "IS2A",
                IServiceEcolesPolytech.TypeSpec.APPRENTISSAGE
            );
            service.getTransaction().commit();
        } catch (EcoleInconnueException e) {
            System.err.println("Ecole Lille inconnu");
        } catch (SpecialiteExisteDejaException e) {
            System.err.println("La spécialité existe déjà");
        } finally {
            if (service.getTransaction().isActive()) service
                .getTransaction()
                .rollback();
        }
    }
}
