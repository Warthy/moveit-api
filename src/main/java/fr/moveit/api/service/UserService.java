package fr.moveit.api.service;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.dto.PasswordChangeDTO;
import fr.moveit.api.dto.UserCreationDTO;
import fr.moveit.api.dto.UserModificationDTO;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.BadRequestException;
import fr.moveit.api.exceptions.ForbiddenException;
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
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	final private UserRepository repository;

	final private RoleRepository roleRepository;

	final private ModelMapper mapper;

	final private PasswordEncoder passwordEncoder;

	final private InterestService interestService;


	@Override
	public User loadUserByUsername(String s) throws UsernameNotFoundException {
		return repository.findByUsernameAndDeletedAtIsNull(s).orElseThrow();
	}

	public Iterable<User> getAll(){
		return repository.findAll();
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
		User user = mapper.map(dto, User.class);

		user.setInterests(new HashSet<>());
		dto.getInterests().forEach(id -> {
			user.getInterests().add(interestService.getInterest(id));
		});

		if (!repository.existsByUsername(user.getUsername())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(Collections.singleton(roleRepository.findByRole(Roles.USER)));

			return repository.save(user);
		} else {
			throw new RuntimeException("Username is already in use");
		}
	}

	public void editUser(User user, UserModificationDTO dto){
		mapper.map(dto, user);

		repository.save(user);
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

	public void addInterest(Long id){
		User user = getCurrentUser();
		user.getInterests().add(interestService.getInterest(id));

		repository.save(user);
	}

	public void removeInterest(Long id){
		User user = getCurrentUser();
		user.getInterests().remove(interestService.getInterest(id));

		repository.save(user);
	}


	public void changePassword(User user, PasswordChangeDTO dto){
		if(!user.getPassword().equals(passwordEncoder.encode(dto.getCurrentPassword())))
			throw new ForbiddenException("invalid password");

		if(!dto.getNewPassword().equals(dto.getConfirmNewPassword()))
			throw new BadRequestException("password and password confirmation are different");

		user.setPassword(passwordEncoder.encode(dto.getCurrentPassword()));
		repository.save(user);
	}
}
