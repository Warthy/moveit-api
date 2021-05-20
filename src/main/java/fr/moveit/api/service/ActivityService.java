package fr.moveit.api.service;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.NotFoundException;
import fr.moveit.api.exceptions.UnauthorizedException;
import fr.moveit.api.repository.ActivityRepository;
import fr.moveit.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final UserService studentService;
	private final ModelMapper mapper;
	private final ActivityRepository repository;

	public Activity createActivity(ActivityCreationDTO dto) {
		Activity activity = mapper.map(dto, Activity.class);

		activity.setCreatedAt(LocalDateTime.now());
		activity.setAuthor(studentService.loadUserByUsername(SecurityUtils.getCurrentUserLogin().getUsername()));

		dto.getParticipants().forEach(id -> activity.getParticipants().add(studentService.getUser(id)));

		return repository.save(activity);
	}

	public Activity getActivity(Long id) {
		Optional<Activity> activity = repository.findById(id);

		if (activity.isEmpty())
			throw new NotFoundException("activity not found");
		return activity.get();
	}


	public void deleteActivity(Long id) {
		Activity activity = getActivity(id);

		User current = studentService.loadUserByUsername(SecurityUtils.getCurrentUserLogin().getUsername());
		if (activity.getAuthor().equals(current) || SecurityUtils.hasCurrentUserThisAuthority(Roles.ADMIN)) {
			repository.delete(activity);
		}

		throw new UnauthorizedException("permission insuffisantes");
	}
}
