package fr.moveit.api.repository;


import fr.moveit.api.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByRole(String name);
}