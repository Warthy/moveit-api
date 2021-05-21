package fr.moveit.api.controller;

import fr.moveit.api.dto.PlanningDTO;
import fr.moveit.api.entity.Planning;
import fr.moveit.api.service.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planning")
public class PlanningController {
	PlanningService service;

	public Collection<Planning> getPlannings(){
		return service.getOwnPlanning();
	}

	@PostMapping
	public Planning add(@RequestBody PlanningDTO dto){
		return service.addPlanning(dto);
	}

	@DeleteMapping("/{id}")
	public void remove(@PathVariable Long id){
		service.removePlanning(id);
	}
}
