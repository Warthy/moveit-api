package fr.moveit.api.service;

import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final UserService studentService;
	private final ActivityRepository repository;

	public Activity createActivity(ActivityCreationDTO dto) {
		Activity activity = new Activity();

		activity.setName(dto.getName());
		activity.setDescription(dto.getDescription());
		activity.setLocation(dto.getLocation());
		activity.setPrice(dto.getPrice());

		activity.setStart(dto.getStart());
		activity.setEnd(dto.getEnd());

		dto.getParticipants().forEach(id -> {
			activity.getParticipants().add(studentService.getUser(id));
		});

		return repository.save(activity);
	}

	public Activity getActivity(Long id) {
		Optional<Activity> activity = repository.findById(id);

		if (activity.isEmpty())
			throw new IllegalArgumentException("activity not found");
		return activity.get();
	}


	public void deleteActivity(Long id){
		Activity activity = getActivity(id);

		//TODO: check permissions

		repository.delete(activity);
	}
}
