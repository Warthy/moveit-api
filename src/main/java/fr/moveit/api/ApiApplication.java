package fr.moveit.api;

import fr.moveit.api.configuration.Roles;
import fr.moveit.api.controller.SecurityController;
import fr.moveit.api.entity.Role;
import fr.moveit.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiApplication implements CommandLineRunner {

	final private Logger log = LoggerFactory.getLogger(ApiApplication.class);

	final private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... params) throws Exception {
		List<Role> roles = new ArrayList<>();
		List<Role> savedRoles = StreamSupport
				.stream(roleRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		for (Field f : Roles.class.getFields()) {
			try {
				Role role = new Role((String) f.get(Roles.class));
				if (!savedRoles.contains(role)) {
					roles.add(role);
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		roleRepository.saveAll(roles);
	}
}
