package is.sio.jpa.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Domaine {

    @Id
    private int id;

    private String nom;

    @ManyToMany
    private Set<EcolePolytech> ecoles;

    public Domaine() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<EcolePolytech> getEcoles() {
        return ecoles;
    }

    public void setEcoles(Set<EcolePolytech> ecoles) {
        this.ecoles = ecoles;
    }

    public String toString() {
        return (
            this.getNom() +
            "\t:\t" +
            this.getEcoles()
                .stream()
                .reduce(
                    "",
                    (acc, curr) -> acc + curr.toString() + "\t",
                    String::concat
                )
        );
    }
}
