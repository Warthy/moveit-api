package fr.moveit.api.dto;

import fr.moveit.api.entity.InterestType;
import lombok.Data;

@Data
public class InterestCreationDTO {

	private String name;
	private InterestType type;
}
