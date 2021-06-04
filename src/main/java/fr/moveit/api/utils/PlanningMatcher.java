package fr.moveit.api.utils;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PlanningMatcher {

	private List<Iterator> timelines = new ArrayList<>();
	private Integer timelines_finished = 0;

	/**
	 *
	 * @param start Date
	 * @param end Date
 	 * @param minimumDurationInMS Integer
	 * @param calendars List<Calendar>
	 *
	 * @return List<Date[]> List of all free time slot shared between all calendars in the time slots
	 */
	public List<Date[]> findIntersections(Date start, Date end, Integer minimumDurationInMS, List<Calendar> calendars) {
		timelines = calendars.stream().map(c -> c.getComponents().iterator()).collect(Collectors.toList());
		List<VEvent> eventsPointer = timelines.stream().map(t -> (VEvent) t.next()).collect(Collectors.toList());


		List<Date[]> slots = new ArrayList<>();

		/* Find earliest event from timeline */
		int leadIndex = 0;
		VEvent leadEvent = eventsPointer.get(0);

		int index = 0;
		for (VEvent e : eventsPointer) {
			/* If event start sooner then it became the leading event */
			if (e.getStartDate().getDate().before(leadEvent.getStartDate().getDate())) {
				leadEvent = e;
				leadIndex = index;
			}
			index++;
		}
		eventsPointer.set(leadIndex, getNextEvent(leadIndex));

		/* Check if gap between first event and start date */
		if (start.getTime() + (minimumDurationInMS * 60 * 1000) < leadEvent.getStartDate().getDate().getTime())
			slots.add(new Date[]{start, leadEvent.getStartDate().getDate()});

		while (timelines.size() != timelines_finished) {
			VEvent gapBorder = null;
			VEvent nextLead = null;

			for (VEvent e : eventsPointer) {
				if (e != null) {
					/* If event intersect then we won't find any availability on this iteration */
					if (leadEvent.getEndDate().getDate().getTime() + minimumDurationInMS > e.getStartDate().getDate().getTime()) {
						gapBorder = null;
						nextLead = e;
						break;
					} else {
						gapBorder = e;
					}
				}
			}

			/* If gap border's defined, then we have a shared free time slot */
			if (gapBorder != null) {
				slots.add(new Date[]{leadEvent.getEndDate().getDate(), gapBorder.getStartDate().getDate()});
				nextLead = gapBorder;
			}


			/* Get new lead event's index */
			leadEvent = nextLead;
			leadIndex = eventsPointer.indexOf(leadEvent);
			eventsPointer.set(leadIndex, getNextEvent(leadIndex));

			/* Pop out all events that start after lead event but ends before */
			index = 0;
			for (VEvent e : eventsPointer) {
				if (e != null && e.getEndDate().getDate().before(leadEvent.getEndDate().getDate())) {
					eventsPointer.set(index, getNextEvent(index));
					break;
				}
				index++;
			}
		}

		/*  Check if gap between last event and end date */
		if (leadEvent.getEndDate().getDate().getTime() + (minimumDurationInMS * 60 * 1000) < end.getTime())
			slots.add(new Date[]{leadEvent.getEndDate().getDate(), end});

		return slots;
	}

	private VEvent getNextEvent(int index) {
		if (timelines.get(index).hasNext()) {
			return (VEvent) timelines.get(index).next();
		} else {
			timelines_finished += 1;
			return null;
		}
	}
}
