package is.sio.jpa.services;

import is.sio.jpa.entites.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Collection;

public class ServiceEcolesPolytech implements IServiceEcolesPolytech {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ServiceEcolesPolytech() {
        this.emf = Persistence.createEntityManagerFactory("XXXXXXX");
        this.em = emf.createEntityManager();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    @Override
    public EcolePolytech getEcole(String nom) throws EcoleInconnueException {
        return null;
    }

    @Override
    public void creerEcolePolytech(
        String nom,
        String siteWeb,
        double latitude,
        double longitude
    ) throws EcoleDejaCreeeException {}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<EcolePolytech> getEcolesPolytech() {
        return null;
    }

    @Override
    public void modifierSiteWeb(String nomEcole, String url)
        throws EcoleInconnueException {}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Domaine> getDomaines() {
        return null;
    }

    @Override
    public void attacherDomaine(int domaineId, String nomEcole)
        throws DomaineInconnuException, EcoleInconnueException {}

    private Domaine getDomaine(int domaineId) throws DomaineInconnuException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<EcolePolytech> getEcolesPolytechAuNordDe(String nom)
        throws EcoleInconnueException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<EcolePolytech> getEcolesPolytechAuSudDe(String nom)
        throws EcoleInconnueException {
        return null;
    }

    @Override
    public Collection<Specialite> getSpecialites(String nomEcole)
        throws EcoleInconnueException {
        return null;
    }

    @Override
    public void addSpecialite(
        String nomEcole,
        String nomSpecialite,
        String acronyme,
        TypeSpec type
    ) throws EcoleInconnueException, SpecialiteExisteDejaException {}
}
