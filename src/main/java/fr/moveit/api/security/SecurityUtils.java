package fr.moveit.api.security;

import fr.moveit.api.configuration.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public final class SecurityUtils {

	private SecurityUtils() {}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user.
	 */
	public static User getCurrentUserLogin() {
		return (User) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
	}

	/**
	 * Checks if the current user has a specific authority.
	 *
	 * @param authority the authority to check.
	 * @return true if the current user has the authority, false otherwise.
	 */
	public static Boolean hasCurrentUserThisAuthority(String authority) {
		return SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals(authority));
	}

}

