package fr.moveit.api.service;

import fr.moveit.api.entity.User;
import fr.moveit.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public User loadUserByUsername(String s) throws UsernameNotFoundException {
		return userRepository.findByUsernameAndDeletedAtIsNull(s).orElseThrow(RuntimeException::new);
	}
}
