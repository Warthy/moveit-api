package fr.moveit.api.utils;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.Date;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FreeTimeMatcher {

	private List<Iterator<VEvent>> timelines = new ArrayList<>();
	private Integer timelines_finished = 0;

	/**
	 * Find all free time slots shared between all calendars in the given time slots, all free time slots
	 * found should last for at list a given duration in ms
	 *
	 * @param start               Date Starting date of possible shared free time slot
	 * @param end                 Date Ending date of possible shared free time slot
	 * @param minimumDurationInMS Integer Minimum duration of a shared free time slot
	 * @param calendars           List<Calendar>
	 * @return slots
	 */
	public List<Date[]> findSharedSlot(Date start, Date end, Integer minimumDurationInMS, List<Calendar> calendars) {
		timelines = calendars.stream().map(c -> overlapping(c, start.getTime(), end.getTime())).collect(Collectors.toList());

		List<Date[]> slots = new ArrayList<>();
		if(timelines.stream().noneMatch(Iterator::hasNext)){
			slots.add(new Date[]{start, end});
			return slots;
		}

		List<VEvent> eventsPointer = timelines.stream().map(Iterator::next).collect(Collectors.toList());



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
		if (start.getTime() + minimumDurationInMS < leadEvent.getStartDate().getDate().getTime())
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
		if (leadEvent.getEndDate().getDate().getTime() + minimumDurationInMS < end.getTime())
			slots.add(new Date[]{leadEvent.getEndDate().getDate(), end});

		return slots;
	}

	private VEvent getNextEvent(int index) {
		if (timelines.get(index).hasNext()) {
			return timelines.get(index).next();
		} else {
			timelines_finished += 1;
			return null;
		}
	}

	private Iterator<VEvent> overlapping(Calendar calendar, long from, long to) {
		List<VEvent> events = new LinkedList<>();

		for (Object o : calendar.getComponents()) {
			VEvent e = (VEvent) o;
			long start = e.getStartDate().getDate().getTime();
			long end = e.getEndDate().getDate().getTime();

			if ((from <= start && start <= to)
					|| (from <= end && end <= to)
					|| (start <= from && end >= to))
			{
				events.add(e);
			}
		}

		return events.iterator();
	}
}
