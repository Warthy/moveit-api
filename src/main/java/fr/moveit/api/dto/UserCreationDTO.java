package fr.moveit.api.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserCreationDTO {
	private String username;

	private String password;

	private String email;

	private String firstName;

	private String lastName;

	private String description;

	private Set<Long> interests = new HashSet<>();
}
