package fr.moveit.api.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTPayload {
	private String token;
	private String refresh_token;
}
