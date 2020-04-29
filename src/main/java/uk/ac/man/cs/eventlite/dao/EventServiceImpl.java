package uk.ac.man.cs.eventlite.dao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Repository
@Transactional
public class EventServiceImpl implements EventService {

	//private final static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

	//private final static String DATA = "data/events.json";
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private VenueRepository venueRepository;
    
	
	@Override
	public long count() {
		return eventRepository.count();
	}
	
	
	@Override
	public Iterable<Event> findAll() {
		Sort sort = Sort.by(Sort.Order.asc("date"), Sort.Order.asc("time"));
		return eventRepository.findAll(sort);
	}
	
	@Override
	public Optional<Event> findById(long id) {
		return eventRepository.findById(id);
	}

	
	@Override
	public Event findOne(long id) {
		return findById(id).orElse(null);
	}
	
	
	@Override
	public void save(Event event) {
		 eventRepository.save(event);
	}
	
	@Override
	public Iterable<Event> findAllByName(String searchString){
		return eventRepository.findAllByNameContainingIgnoreCaseOrderByDateAscTimeAsc(searchString);
	}
	
	@Override
	public void deleteById(long id) {
		eventRepository.deleteById(id);
	}
	
	@Override
	public List<Event> find3MostRecent(LocalDate date) {

		return eventRepository.findTop3ByDateAfterOrderByDateAscTimeAscNameAsc(date);
	}
	
	@Override
	public Iterable<Event> findAllEventsAtVenue(long id) {
	
		Venue venue = venueRepository.findById(id).get();
		Set<Event> eventsAtVenue = venue.getEvents();

		return eventsAtVenue;
	}
	
	
	@Override
	public Iterable<Event> findNext3ForVenue(long id) {
		Sort sort = Sort.by(Sort.Order.asc("date"), Sort.Order.asc("time"));
		Iterable<Event> allEvents = eventRepository.findAll(sort);
		
		Venue venue = venueRepository.findById(id).get();
		Set<Event> eventsAtVenue = venue.getEvents();
		
		ArrayList<Event> upcoming = new ArrayList<Event>();
	    Iterator<Event> it = allEvents.iterator();
	    int count = 0;
	    while (it.hasNext() && count < 3) {
	    	Event nextEvent = it.next();
	    	if (eventsAtVenue.contains(nextEvent)) {
	    		upcoming.add(nextEvent);
	    		count++;
	    	}
	    }
	    
	    return upcoming;

	}
	

}
