package fr.moveit.api.service;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.UserCreationDTO;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.BadRequestException;
import fr.moveit.api.repository.RoleRepository;
import fr.moveit.api.repository.UserRepository;
import fr.moveit.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	final private UserRepository repository;

	final private RoleRepository roleRepository;

	final private ModelMapper modelMapper;

	final private PasswordEncoder passwordEncoder;


	@Override
	public User loadUserByUsername(String s) throws UsernameNotFoundException {
		return repository.findByUsernameAndDeletedAtIsNull(s).orElseThrow();
	}

	public User getCurrentUser(){
		return loadUserByUsername(SecurityUtils.getCurrentUserLogin().getUsername());
	}

	public User getUser(Long id) {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty())
			throw new IllegalArgumentException("user not found");
		return user.get();
	}

	public User createUser(UserCreationDTO dto){
		User user = modelMapper.map(dto, User.class);

		if (!repository.existsByUsername(user.getUsername())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(Collections.singleton(roleRepository.findByRole(Roles.USER)));

			return repository.save(user);
		} else {
			throw new RuntimeException("Username is already in use");
		}
	}

	public void addFriend(Long id){
		User user = getCurrentUser();
		User target = getUser(id);

		if(user.getFriends().contains(target))
			throw new BadRequestException("");

		user.getFriends().add(target);

		repository.save(user);
	}

	public void removeFriend(Long id){
		User user = getCurrentUser();
		User target = getUser(id);

		if(!user.getFriends().contains(target))
			throw new BadRequestException("");

		user.getFriends().remove(target);

		repository.save(user);
	}
}
