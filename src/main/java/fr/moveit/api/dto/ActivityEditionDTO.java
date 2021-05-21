package fr.moveit.api.dto;

import fr.moveit.api.entity.ActivityVisibility;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityEditionDTO {
	private String name;
	private LocalDateTime start;
	private LocalDateTime end;

	private ActivityVisibility visibility;

	private String description;

	private String location;
	private Float price;
}
