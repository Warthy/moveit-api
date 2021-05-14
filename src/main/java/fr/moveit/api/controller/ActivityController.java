package fr.moveit.api.controller;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.service.ActivityService;
import fr.moveit.api.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {
	final ActivityService activityService;


	@PostMapping
	public Activity create(ActivityCreationDTO body) {
		return activityService.createActivity(body);
	}

	@GetMapping("/{id}")
	public Activity getOne(@PathVariable Long id) {
		SecurityService.getLoggedId();
		return activityService.getActivity(id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		activityService.deleteActivity(id);
	}

}

