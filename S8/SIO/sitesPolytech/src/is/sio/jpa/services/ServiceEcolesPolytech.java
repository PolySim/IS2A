package is.sio.jpa.services;

import java.util.Collection;

import is.sio.jpa.entites.Domaine;
import is.sio.jpa.entites.EcolePolytech;
import is.sio.jpa.entites.Specialite;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ServiceEcolesPolytech implements IServiceEcolesPolytech {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ServiceEcolesPolytech() {
        this.emf = Persistence.createEntityManagerFactory("sitesPolytechUnit");
        this.em = emf.createEntityManager();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    @Override
    public EcolePolytech getEcole(String nom) throws EcoleInconnueException {
        EcolePolytech ecole = em.find(EcolePolytech.class, nom);
        if (ecole == null) {
            throw new EcoleInconnueException();
        }
        return ecole;
    }

    @Override
    public void creerEcolePolytech(
            String nom,
            String siteWeb,
            double latitude,
            double longitude) throws EcoleDejaCreeeException {
        try {
            this.getTransaction().begin();
            EcolePolytech ecole = new EcolePolytech(
                    nom,
                    siteWeb,
                    latitude,
                    longitude);
            em.persist(ecole);
            this.getTransaction().commit();
        } catch (EntityExistsException e) {
            throw new EcoleDejaCreeeException("L'école " + nom + " existe déjà");
        } finally {
            if (this.getTransaction().isActive())
                this.getTransaction().rollback();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<EcolePolytech> getEcolesPolytech() {
        return null;
    }

    @Override
    public void modifierSiteWeb(String nomEcole, String url)
            throws EcoleInconnueException {
    }

    @Override
    public Collection<Domaine> getDomaines() {
        return em.createQuery("SELECT d FROM Domaine d", Domaine.class).getResultList();
    }

    @Override
    public void attacherDomaine(int domaineId, String nomEcole)
            throws DomaineInconnuException, EcoleInconnueException {
        try {
            this.getTransaction().begin();
            Domaine domaine = this.getDomaine(domaineId);
            EcolePolytech ecole = this.getEcole(nomEcole);
            domaine.getEcoles().add(ecole);
            this.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (this.getTransaction().isActive())
                this.getTransaction().rollback();
        }
    }

    private Domaine getDomaine(int domaineId) throws DomaineInconnuException {
        Domaine domaine = em.find(Domaine.class, domaineId);
        if (domaine == null) {
            throw new DomaineInconnuException();
        }
        return domaine;
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
            TypeSpec type) throws EcoleInconnueException, SpecialiteExisteDejaException {
    }
}
