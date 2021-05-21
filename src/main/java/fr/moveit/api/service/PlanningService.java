package fr.moveit.api.service;

import fr.moveit.api.dto.PlanningDTO;
import fr.moveit.api.entity.Planning;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.ForbiddenException;
import fr.moveit.api.exceptions.NotFoundException;
import fr.moveit.api.repository.PlanningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlanningService {
	PlanningRepository repository;

	UserService userService;

	public Planning getPlanning(Long id) {
		Optional<Planning> planning = repository.findById(id);

		if (planning.isEmpty())
			throw new NotFoundException("planning not found");
		return planning.get();
	}

	public Collection<Planning> getOwnPlanning(){
		return userService.getCurrentUser().getPlannings();
	}

	public Planning addPlanning(PlanningDTO dto){
		User user = userService.getCurrentUser();

		Planning planning = new Planning();
		planning.setUri(dto.getUri());
		planning.setOwner(user);


		return repository.save(planning);
	}

	public void removePlanning(Long id){
		Planning planning = getPlanning(id);

		if(!planning.getOwner().equals(userService.getCurrentUser()))
			throw new ForbiddenException("you're not allowed to remove this planning");

		repository.delete(planning);
	}
}
