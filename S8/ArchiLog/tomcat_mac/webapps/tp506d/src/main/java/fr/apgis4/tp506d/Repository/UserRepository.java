package fr.apgis4.tp506d.Repository;

import fr.apgis4.tp506d.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {}
