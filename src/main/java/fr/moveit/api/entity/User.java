package fr.moveit.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@Entity(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	@Column(unique = true, nullable = false)
	private String username;

	@JsonIgnore
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(length = 500)
	private String description;

	@JsonIgnore
	private Date deletedAt;

	private String firstName;

	private String lastName;

	@OneToMany(mappedBy = "owner")
	@JsonIgnore
	private Collection<Planning> plannings;

	@ManyToMany
	@JsonIgnore
	private Collection<User> friends;

	@Override
	@JsonIgnore
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
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return deletedAt == null || deletedAt.after(new Date());
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

}
