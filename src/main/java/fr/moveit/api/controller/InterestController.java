package fr.moveit.api.controller;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.ActivityCreationDTO;
import fr.moveit.api.dto.ActivityEditionDTO;
import fr.moveit.api.dto.InterestCreationDTO;
import fr.moveit.api.entity.Activity;
import fr.moveit.api.entity.Interest;
import fr.moveit.api.service.ActivityService;
import fr.moveit.api.service.InterestService;
import fr.moveit.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {
	final private InterestService interestService;
	final private UserService userService;

	@GetMapping
	public Iterable<Interest> getAll(){
		return interestService.getAll();
	}

	@GetMapping
	public Iterable<Interest> getUserInterests(){
		return interestService.getUserInterests(userService.getCurrentUser());
	}

	@PostMapping
	@Secured(Roles.ADMIN)
	public Interest create(@RequestBody InterestCreationDTO body) {
		return interestService.createInterest(body);
	}

	@DeleteMapping("/{id}")
	@Secured(Roles.ADMIN)
	public void delete(@PathVariable Long id) {
		interestService.deleteInterest(id);
	}

	@GetMapping("/{id}")
	public Interest get(@PathVariable Long id) {
		return interestService.getInterest(id);
	}
}

