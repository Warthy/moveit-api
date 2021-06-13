package fr.moveit.api.controller;

import fr.moveit.api.dto.PlanningDTO;
import fr.moveit.api.dto.PlanningIntersectionDTO;
import fr.moveit.api.entity.Planning;
import fr.moveit.api.service.PlanningService;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.model.Date;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

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

	@PostMapping("/intersection")
	public List<Date[]> getIntersection(@RequestBody PlanningIntersectionDTO dto) throws ParseException {
		return planningService.intersection(dto);
	}
}
