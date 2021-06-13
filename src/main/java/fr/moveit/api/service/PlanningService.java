package fr.moveit.api.service;

import fr.moveit.api.dto.PlanningDTO;
import fr.moveit.api.dto.PlanningIntersectionDTO;
import fr.moveit.api.entity.Planning;
import fr.moveit.api.entity.User;
import fr.moveit.api.exceptions.BadRequestException;
import fr.moveit.api.exceptions.ForbiddenException;
import fr.moveit.api.exceptions.NotFoundException;
import fr.moveit.api.repository.PlanningRepository;
import fr.moveit.api.utils.FreeTimeMatcher;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PlanningService {
	private final PlanningRepository repository;

	private final UserService userService;

	public Planning getPlanning(Long id) {
		Optional<Planning> planning = repository.findById(id);

		if (planning.isEmpty())
			throw new NotFoundException("planning not found");
		return planning.get();
	}

	public Collection<Planning> getOwnPlanning(){
		return userService.getCurrentUser().getPlannings();
	}

	public Planning addPlanning(PlanningDTO dto){
		User user = userService.getCurrentUser();

		Planning planning = new Planning();
		planning.setUri(dto.getUri());
		planning.setOwner(user);


		return repository.save(planning);
	}

	public void removePlanning(Long id){
		Planning planning = getPlanning(id);

		if(!planning.getOwner().equals(userService.getCurrentUser()))
			throw new ForbiddenException("you're not allowed to remove this planning");

		repository.delete(planning);
	}

	public List<Date[]> intersection(PlanningIntersectionDTO dto) throws ParseException {
		Collection<Planning> plannings = userService.getCurrentUser().getPlannings();
		dto.getUsers().forEach(id -> plannings.addAll(userService.getUser(id).getPlannings()));

		List<Calendar> calendars = new ArrayList<>();
		plannings.forEach(p -> {
			try (InputStream ics = new URL(p.getUri()).openStream()) {
				CalendarBuilder builder = new CalendarBuilder();
				try {
					calendars.add(builder.build(ics));
				} catch (ParserException e) {
					e.printStackTrace();
					throw new BadRequestException("invalid ics source");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new BadRequestException("invalid ics url");
			}
		});

		FreeTimeMatcher matcher = new FreeTimeMatcher();

		return matcher.findSharedSlot(
				new Date(dto.getFrom()),
				new Date(dto.getTo()),
				dto.getMinimumDuration(),
				calendars
		);
	}

}
