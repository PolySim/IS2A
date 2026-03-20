package is.sio.jpa.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public abstract class Specialite {

    @Id
    private String nom;

    private String acronyme;

    @JoinColumn(name = "ecole", nullable = false)
    @ManyToOne
    private EcolePolytech ecole;

    public Specialite() {}

    public Specialite(String nom, String acronyme) {
        this.setNom(nom);
        this.setAcronyme(acronyme);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    public EcolePolytech getEcole() {
        return ecole;
    }

    public void setEcole(EcolePolytech ecole) {
        this.ecole = ecole;
    }
}
