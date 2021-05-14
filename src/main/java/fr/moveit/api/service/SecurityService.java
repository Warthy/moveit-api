package fr.moveit.api.service;

import fr.moveit.api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

	final private UserService userService;

	static public boolean isUserAnonymous() {
		/* By default spring security set the principal as "anonymousUser" */
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal()
				.equals("anonymousUser");
	}

	static public Long getLoggedId() {
		Object a = SecurityContextHolder.getContext().getAuthentication();
		if (!isUserAnonymous()) {
			a = SecurityContextHolder.getContext().getAuthentication();
			System.out.println(a);
			return null;
		}
		return null;
	}

	public User getLoggedUser() {
		return userService.getUser(getLoggedId());
	}

}
