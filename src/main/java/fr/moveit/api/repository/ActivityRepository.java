package fr.moveit.api.repository;

import fr.moveit.api.entity.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

}