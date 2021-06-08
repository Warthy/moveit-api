package fr.moveit.api.service;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.dto.ActivityEditionDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.entity.ActivityVisibility;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.BadRequestException;
import fr.moveit.api.exceptions.ForbiddenException;
import fr.moveit.api.exceptions.NotFoundException;
import fr.moveit.api.exceptions.UnauthorizedException;
import fr.moveit.api.repository.ActivityRepository;
import fr.moveit.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

	private final UserService userService;
	private final InterestService interestService;
	private final ModelMapper mapper;
	private final ActivityRepository repository;


	private Activity getActivityIfHasPermissions(Long id) {
		Activity activity = getActivity(id);
		User current = userService.getCurrentUser();

		if (!activity.getAuthor().equals(current) || !SecurityUtils.hasCurrentUserThisAuthority(Roles.ADMIN))
			throw new UnauthorizedException("permissions insuffisantes");

		return activity;
	}

	public Iterable<Activity> getAllUserActivities(){
		return repository.findAllUserAccessibleActivity(userService.getCurrentUser());
	}

	public Activity createActivity(ActivityCreationDTO dto) {
		Activity activity = mapper.map(dto, Activity.class);

		activity.setParticipants(new HashSet<>());
		activity.setCreatedAt(new Date());

		activity.setInterest(dto.getInterest() != null ? interestService.getInterest(dto.getInterest()): interestService.getUnknownInterest());
		activity.setAuthor(userService.getCurrentUser());

		dto.getParticipants().forEach(id -> activity.getParticipants().add(userService.getUser(id)));

		return repository.save(activity);
	}

	public Activity getActivity(Long id) {
		Optional<Activity> activity = repository.findById(id);

		if (activity.isEmpty())
			throw new NotFoundException("activity not found");
		return activity.get();
	}


	public Activity updateActivity(Long id, ActivityEditionDTO dto) {
		Activity activity = getActivityIfHasPermissions(id);

		mapper.map(dto, activity);
		if(dto.getInterest() != null)
			activity.setInterest(interestService.getInterest(dto.getInterest()));

		return repository.save(activity);
	}


	public void deleteActivity(Long id) {
		Activity activity = getActivityIfHasPermissions(id);

		repository.delete(activity);
	}

	public Activity joinActivity(Long id) {
		Activity activity = getActivity(id);
		User user = userService.getCurrentUser();

		if (
			(activity.getVisibility() == ActivityVisibility.PRIVATE) ||
		    (activity.getVisibility() == ActivityVisibility.INTERN && !activity.getAuthor().getFriends().contains(user))
		) throw new ForbiddenException("you can't join this activity");

		if (activity.getParticipants().contains(user))
			throw new BadRequestException("You are already member of this activity");


		activity.getParticipants().add(user);
		return repository.save(activity);
	}

	public Activity addParticipants(Long id, List<Long> ids) {
		Activity activity = getActivityIfHasPermissions(id);
		if (activity.getVisibility() != ActivityVisibility.PRIVATE)
			throw new BadRequestException("you can only add participants to an activity that is set as private");

		ids.forEach(userId ->
				activity.getParticipants().add(userService.getUser(id))
		);

		return repository.save(activity);
	}

	public Activity removeParticipants(Long id, List<Long> ids) {
		Activity activity = getActivityIfHasPermissions(id);
		if (activity.getVisibility() != ActivityVisibility.PRIVATE)
			throw new BadRequestException("you can only remove participants to an activity that is set as private");

		ids.forEach(userId ->
				activity.getParticipants().remove(userService.getUser(id))
		);

		return repository.save(activity);
	}

}
