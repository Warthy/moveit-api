package fr.moveit.api.repository;

import fr.moveit.api.entity.Planning;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlanningRepository extends CrudRepository<Planning, Long> {

	List<Planning> findAllByOwner_Id(Long id);
}
