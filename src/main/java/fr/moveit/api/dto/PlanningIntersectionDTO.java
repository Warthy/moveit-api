package fr.moveit.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanningIntersectionDTO {
	private Long userId;
	private Integer minimumDuration;
	private LocalDateTime from;
	private LocalDateTime to;
}
