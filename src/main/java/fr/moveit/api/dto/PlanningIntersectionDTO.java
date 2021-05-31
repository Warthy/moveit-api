package fr.moveit.api.dto;

import lombok.Data;

import java.util.Date;


@Data
public class PlanningIntersectionDTO {
	private Long userId;
	private Integer minimumDuration;
	private Date from;
	private Date to;
}
