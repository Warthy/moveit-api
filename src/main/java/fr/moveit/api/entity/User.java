package fr.moveit.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Entity(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue
	private Long id;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	@Column(unique = true, nullable = false)
	private String username;

	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	private LocalDateTime deletedAt;

	private String firstName;

	private String lastName;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return deletedAt == null || deletedAt.isAfter(LocalDateTime.now());
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
