package fr.moveit.api.repository;


import fr.moveit.api.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsernameAndDeletedAtIsNull(String string);

	List<User> findAllByDeletedAtIsNull();
}