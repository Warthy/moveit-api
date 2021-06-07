package fr.moveit.api.repository;

import fr.moveit.api.entity.Interest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends CrudRepository<Interest, Long> {

	@Query("select i from Interest i where i.name = 'unknown'")
	Interest getUnknownInterest();
}
