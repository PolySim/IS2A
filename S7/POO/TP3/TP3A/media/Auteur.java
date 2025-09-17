package media;

import exceptions.NomTropCourtException;

public class Auteur {
    private String nom;

    public Auteur(String nom) throws NomTropCourtException {
        this.setNom(nom);
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String newNom) throws NomTropCourtException {
        this.controleNom(newNom);
        this.nom = newNom;
    }

    private void controleNom(String nom) throws NomTropCourtException {
        if (nom.length() < 3) {
            throw new NomTropCourtException();
        }
    }

    public String toString() {
        return "Auteur : " + this.getNom();
    }
}
