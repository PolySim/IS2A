package is.sio.jpa.services;

import is.sio.jpa.entites.Domaine;
import is.sio.jpa.entites.EcolePolytech;
import is.sio.jpa.entites.Specialite;
import jakarta.persistence.EntityTransaction;
import java.util.Collection;

public interface IServiceEcolesPolytech {
    public void creerEcolePolytech(
        String nom,
        String siteWeb,
        double latitude,
        double longitude
    ) throws EcoleDejaCreeeException;
    public EcolePolytech getEcole(String nom) throws EcoleInconnueException;
    public Collection<Domaine> getDomaines();
    public void attacherDomaine(int domaineId, String nomEcole)
        throws DomaineInconnuException, EcoleInconnueException, EcoleDejaAttacheeException;
    public Collection<EcolePolytech> getEcolesPolytech();
    Collection<EcolePolytech> getEcolesPolytechAuNordDe(String nom)
        throws EcoleInconnueException;
    public Collection<EcolePolytech> getEcolesPolytechAuSudDe(String nom)
        throws EcoleInconnueException;

    public enum TypeSpec {
        INITIALE,
        APPRENTISSAGE,
        FC,
    }

    public void addSpecialite(
        String nomEcole,
        String nomSpecialite,
        String acronyme,
        TypeSpec type
    ) throws EcoleInconnueException, SpecialiteExisteDejaException;
    EntityTransaction getTransaction();
    void modifierSiteWeb(String nomEcole, String url)
        throws EcoleInconnueException;
    Collection<Specialite> getSpecialites(String nomEcole)
        throws EcoleInconnueException;
}
