package uk.ac.man.cs.eventlite.config.data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.dao.EventRepository;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.EventServiceImpl;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.dao.VenueServiceImpl;
import uk.ac.man.cs.eventlite.entities.Venue;;
@Component
@Profile({ "default", "test" })
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	@Autowired
	private EventServiceImpl eventService;

	@Autowired
	private VenueServiceImpl venueService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (eventService.count() > 0) {
			log.info("Database already populated. Skipping data initialization.");
			return;
		}
		// Build and save initial models here.
		
		Venue venue1 = new Venue();
		venue1.setCapacity(1000);
		venue1.setName("Venue 1");
		venueService.save(venue1);
		
		Venue venue2 = new Venue();
		venue2.setCapacity(1000);
		venue2.setName("Venue 2");
		venueService.save(venue2);
		
		Event event1 = new Event();
		event1.setDate(LocalDate.of(2020, 02, 25));
		event1.setTime(LocalTime.of(18, 00));
		event1.setName("Event 1");
		event1.setVenue(venue1);
		eventService.save(event1);
		
		Event event2 = new Event();
		event2.setDate(LocalDate.of(2021, 02, 25));
		event2.setTime(LocalTime.of(19, 00));
		event2.setName("Event 2");
		event2.setVenue(venue2);
		eventService.save(event2);

	}

}
