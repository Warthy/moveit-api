package fr.moveit.api.dto;

import lombok.Data;

@Data
public class CASUserDTO {
	Long numero;
	String nom;
	String prenom;
	String mail;
	String login;
	String titre;
}
