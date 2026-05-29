package fr.apgis4.tp506c.Repository;

import fr.apgis4.tp506c.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {}
