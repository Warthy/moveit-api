package fr.moveit.api.repository;

import fr.moveit.api.entity.Activity;
import fr.moveit.api.entity.Interest;
import fr.moveit.api.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {


	@Query("select a from Activity a where " +
			"a.visibility = 'PUBLIC' " +
			"or (a.visibility = 'PRIVATE' and ?1 member of a.participants) " +
			"or (a.visibility = 'INTERN' and ?1 member of a.author.friends) "
	)
	Iterable<Activity> findAllUserAccessibleActivity(User user);

	Iterable<Activity> findAllActivityByInterest(Interest interest);
}
