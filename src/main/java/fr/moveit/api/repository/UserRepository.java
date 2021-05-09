package fr.moveit.api.repository;


import fr.moveit.api.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsernameAndDeletedAtIsNull(String string);

	List<User> findAllByDeletedAtIsNull();
}