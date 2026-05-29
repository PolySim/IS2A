package fr.apgis4.tp506d.Repository;

import fr.apgis4.tp506d.Model.Livre;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface LivreRepository extends CrudRepository<Livre, String> {
  List<Livre> findByTitreLikeOrderByLnoDesc(String nom);
}
