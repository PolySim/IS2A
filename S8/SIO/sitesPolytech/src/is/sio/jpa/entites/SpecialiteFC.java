package is.sio.jpa.entites;

import jakarta.persistence.Entity;

@Entity
public class SpecialiteFC extends Specialite {

    public SpecialiteFC() {
        super();
    }

    public SpecialiteFC(String nom, String acronyme) {
        super(nom, acronyme);
    }
}
