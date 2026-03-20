package is.sio.jpa.entites;

import jakarta.persistence.Entity;

@Entity
public class SpecialiteApprentissage extends Specialite {

    private int tailleLimite;

    public SpecialiteApprentissage() {
        super();
    }

    public SpecialiteApprentissage(String nom, String acronyme) {
        super(nom, acronyme);
    }

    public SpecialiteApprentissage(
        String nom,
        String acronyme,
        int tailleLimite
    ) {
        super(nom, acronyme);
        this.setTailleLimite(tailleLimite);
    }

    public int getTailleLimite() {
        return tailleLimite;
    }

    public void setTailleLimite(int tailleLimite) {
        this.tailleLimite = tailleLimite;
    }
}
