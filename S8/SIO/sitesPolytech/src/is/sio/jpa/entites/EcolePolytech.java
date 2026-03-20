package is.sio.jpa.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class EcolePolytech {

    @Id
    private String nom;

    private String siteWeb;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "ecole")
    private Set<Specialite> specialites;

    public EcolePolytech() {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<Specialite> getSpecialites() {
        return specialites;
    }

    public void setSpecialites(Set<Specialite> specialites) {
        this.specialites = specialites;
    }
}
