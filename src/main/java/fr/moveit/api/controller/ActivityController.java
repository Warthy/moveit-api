package fr.moveit.api.controller;

import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.dto.ActivityEditionDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {
	final ActivityService activityService;

	@GetMapping
	public Iterable<Activity> getAll(){
		return activityService.getAllUserActivities();
	}

	@PostMapping
	public Activity create(@RequestBody ActivityCreationDTO body) {
		return activityService.createActivity(body);
	}

	@PutMapping("/{id}")
	public Activity update(@PathVariable Long id, ActivityEditionDTO body) {
		return activityService.updateActivity(id, body);
	}

	@GetMapping("/{id}")
	public Activity get(@PathVariable Long id) {
		return activityService.getActivity(id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		activityService.deleteActivity(id);
	}

	@PostMapping("/{id}/join")
	public Activity join(@PathVariable Long id) {
		return activityService.joinActivity(id);
	}

	@PostMapping("/{id}/member")
	public Activity addMember(@PathVariable Long id, @RequestParam List<Long> ids) {
		return activityService.addParticipants(id, ids);
	}

	@DeleteMapping("/{id}/member")
	public Activity removeMember(@PathVariable Long id, @RequestParam List<Long> ids) {
		return activityService.removeParticipants(id, ids);
	}


}

