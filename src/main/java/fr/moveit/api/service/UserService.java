package fr.moveit.api.service;

import fr.moveit.api.entity.User;
import fr.moveit.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository repository;

	@Override
	public User loadUserByUsername(String s) throws UsernameNotFoundException {
		return repository.findByUsernameAndDeletedAtIsNull(s).orElseThrow(RuntimeException::new);
	}


	public User getUser(Long id) {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty())
			throw new IllegalArgumentException("user not found");
		return user.get();
	}
}
