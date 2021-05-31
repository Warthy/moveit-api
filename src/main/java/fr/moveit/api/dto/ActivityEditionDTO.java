package fr.moveit.api.dto;

import fr.moveit.api.entity.ActivityVisibility;
import lombok.Data;

import java.util.Date;


@Data
public class ActivityEditionDTO {
	private String name;
	private Date start;
	private Date end;

	private ActivityVisibility visibility;

	private String description;

	private String location;
	private Float price;
}
