package fr.moveit.api.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class PlanningIntersectionDTO {
	private List<Long> users;
	private Integer minimumDuration;
	private Date from;
	private Date to;
}
