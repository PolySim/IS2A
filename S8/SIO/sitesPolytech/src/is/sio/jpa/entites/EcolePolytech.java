package is.sio.jpa.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Table(name = "ecole")
@Entity
public class EcolePolytech {

    @Column(name = "nom", length = 80)
    @Id
    private String nom;

    @Column(name = "url", length = 100)
    private String siteWeb;

    @Column(name = "longit")
    private double latitude;

    @Column(name = "latit")
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
