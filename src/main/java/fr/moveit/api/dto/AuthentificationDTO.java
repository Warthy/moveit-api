package fr.moveit.api.dto;

import lombok.Data;

@Data
public class AuthentificationDTO {
	private String username;
	private String password;
	private Boolean rememberMe = false;
}
