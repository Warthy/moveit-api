package fr.moveit.api.service;

import fr.moveit.api.dto.InterestCreationDTO;
import fr.moveit.api.entity.Interest;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.NotFoundException;
import fr.moveit.api.repository.ActivityRepository;
import fr.moveit.api.repository.InterestRepository;
import fr.moveit.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestService {

	private final ModelMapper mapper;
	private final InterestRepository repository;
	private final ActivityRepository activityRepository;
	private final UserRepository userRepository;

	private Interest getUnknownInterest(){
		return repository.getUnknownInterest();
	}

	public Interest createInterest(InterestCreationDTO dto) {
		Interest interest = mapper.map(dto, Interest.class);

		return repository.save(interest);
	}

	public Interest getInterest(Long id) {
		Optional<Interest> interest = repository.findById(id);

		if (interest.isEmpty())
			throw new NotFoundException("interest not found");
		return interest.get();
	}

	public Iterable<Interest> getAll(){
		return repository.findAll();
	}

	public Iterable<Interest> getUserInterests(User user){
		return user.getInterests();
	}


	public void deleteInterest(Long id) {
		Interest interest = getInterest(id);
		Interest unknown = getUnknownInterest();

		activityRepository.findAllActivityByInterest(interest).forEach(activity -> {
			activity.setInterest(unknown);
			activityRepository.save(activity);
		});

		interest.getUsers().forEach(user -> {
			user.getInterests().remove(interest);
			user.getInterests().add(unknown);

			userRepository.save(user);
		});

		repository.delete(interest);
	}

}
