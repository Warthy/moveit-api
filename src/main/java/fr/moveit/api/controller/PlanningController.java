package fr.moveit.api.controller;

import fr.moveit.api.dto.PlanningDTO;
import fr.moveit.api.dto.PlanningIntersectionDTO;
import fr.moveit.api.entity.Planning;
import fr.moveit.api.service.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planning")
public class PlanningController {
	private final PlanningService planningService;

	@GetMapping
	public Collection<Planning> getPlannings(){
		return planningService.getOwnPlanning();
	}

	@PostMapping
	public Planning add(@RequestBody PlanningDTO dto){
		return planningService.addPlanning(dto);
	}

	@DeleteMapping("/{id}")
	public void remove(@PathVariable Long id){
		planningService.removePlanning(id);
	}

	@GetMapping("/intersection")
	public String getIntersection(PlanningIntersectionDTO dto){
		return planningService.intersection(dto);
	}
}
