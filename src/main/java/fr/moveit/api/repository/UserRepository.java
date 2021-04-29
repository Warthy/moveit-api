package fr.moveit.api.repository;


import fr.moveit.api.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}