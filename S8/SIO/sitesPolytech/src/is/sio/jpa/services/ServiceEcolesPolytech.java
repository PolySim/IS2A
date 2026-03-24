package is.sio.jpa.services;

import is.sio.jpa.entites.Domaine;
import is.sio.jpa.entites.EcolePolytech;
import is.sio.jpa.entites.Specialite;
import is.sio.jpa.entites.SpecialiteApprentissage;
import is.sio.jpa.entites.SpecialiteFC;
import is.sio.jpa.entites.SpecialiteInitiale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.Collection;

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
        if (ecole == null) throw new EcoleInconnueException();
        return ecole;
    }

    @Override
    public void creerEcolePolytech(
        String nom,
        String siteWeb,
        double latitude,
        double longitude
    ) throws EcoleDejaCreeeException {
        try {
            this.getEcole(nom);
            throw new EcoleDejaCreeeException(
                "L'école " + nom + " existe déjà"
            );
        } catch (EcoleInconnueException e) {
            EcolePolytech ecole = new EcolePolytech(
                nom,
                siteWeb,
                latitude,
                longitude
            );
            em.persist(ecole);
        }
    }

    @Override
    public Collection<EcolePolytech> getEcolesPolytech() {
        String query = "SELECT ecole from EcolePolytech ecole";
        return em.createQuery(query, EcolePolytech.class).getResultList();
    }

    @Override
    public void modifierSiteWeb(String nomEcole, String url)
        throws EcoleInconnueException {}

    @Override
    public Collection<Domaine> getDomaines() {
        return em
            .createQuery("SELECT d FROM Domaine d", Domaine.class)
            .getResultList();
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
            if (
                this.getTransaction().isActive()
            ) this.getTransaction().rollback();
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
        EcolePolytech ecole = this.getEcole(nom);
        String query =
            "SELECT ecole FROM EcolePolytech ecole WHERE ecole.latitude > :lat";
        Query q = em.createQuery(query, EcolePolytech.class);
        q.setParameter("lat", ecole.getLatitude());
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<EcolePolytech> getEcolesPolytechAuSudDe(String nom)
        throws EcoleInconnueException {
        EcolePolytech ecole = this.getEcole(nom);
        String query =
            "SELECT ecole FROM EcolePolytech ecole WHERE ecole.latitude < :lat";
        Query q = em.createQuery(query, EcolePolytech.class);
        q.setParameter("lat", ecole.getLatitude());
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Specialite> getSpecialites(String nomEcole)
        throws EcoleInconnueException {
        EcolePolytech ecole = this.getEcole(nomEcole);
        String query =
            "SELECT s FROM Specialite s JOIN s.ecole e WHERE e.nom = :nom";
        Query q = em.createQuery(query, Specialite.class);
        q.setParameter("nom", ecole.getNom());
        return q.getResultList();
    }

    @Override
    public void addSpecialite(
        String nomEcole,
        String nomSpecialite,
        String acronyme,
        TypeSpec type
    ) throws EcoleInconnueException, SpecialiteExisteDejaException {
        EcolePolytech ecole = this.getEcole(nomEcole);
        Specialite new_specialite = em.find(Specialite.class, nomSpecialite);
        if (new_specialite != null) throw new SpecialiteExisteDejaException();
        switch (type) {
            case IServiceEcolesPolytech.TypeSpec.APPRENTISSAGE:
                new_specialite = new SpecialiteApprentissage(
                    nomSpecialite,
                    acronyme
                );
                break;
            case IServiceEcolesPolytech.TypeSpec.INITIALE:
                new_specialite = new SpecialiteInitiale(
                    nomSpecialite,
                    acronyme
                );
                break;
            case IServiceEcolesPolytech.TypeSpec.FC:
                new_specialite = new SpecialiteFC(nomSpecialite, acronyme);
                break;
        }
        new_specialite.setEcole(ecole);
        em.persist(new_specialite);
    }
}
