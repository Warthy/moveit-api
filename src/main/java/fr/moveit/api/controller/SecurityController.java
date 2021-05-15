package fr.moveit.api.controller;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.AuthentificationDTO;
import fr.moveit.api.dto.UserCreationDTO;
import fr.moveit.api.entity.User;
import fr.moveit.api.repository.UserRepository;
import fr.moveit.api.security.jwt.JWTProvider;
import fr.moveit.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController("/s")
@RequiredArgsConstructor
public class SecurityController {

	final private AuthenticationManager authenticationManager;

	final private UserService userService;

	final private JWTProvider tokenProvider;

	@PostMapping("/auth")
	public String login(@RequestBody AuthentificationDTO credentials){
		String username = credentials.getUsername();
		String password = credentials.getPassword();

		try {
			return tokenProvider.createToken(
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
							username,
							password,
							userService.loadUserByUsername(username).getRoles()
					)),
					credentials.getRememberMe()
			);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("authentification failed");
		}
	}


	@PostMapping("/register")
	public String register(@RequestBody UserCreationDTO dto){
		User createdUser = userService.createUser(dto);

		return tokenProvider.createToken(
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						createdUser.getUsername(),
						createdUser.getPassword(),
						Collections.singletonList(new SimpleGrantedAuthority(Roles.USER))
				)),
				false
		);
	}

	@GetMapping("/refresh")
	public String refresh(HttpServletRequest req) {
		return tokenProvider.createToken(
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						req.getRemoteUser(),
						"",
						userService.loadUserByUsername(req.getRemoteUser()).getRoles()
				)),
				false
		);
	}


}
