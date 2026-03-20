package is.sio.jpa.entites;

import jakarta.persistence.Entity;

@Entity
public class SpecialiteInitiale extends Specialite {

    public SpecialiteInitiale() {
        super();
    }

    public SpecialiteInitiale(String nom, String acronyme) {
        super(nom, acronyme);
    }
}
