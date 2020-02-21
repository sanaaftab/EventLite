package uk.ac.man.cs.eventlite.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import uk.ac.man.cs.eventlite.entities.Event;

@Service
public class EventServiceImpl implements EventService {

	//private final static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

	//private final static String DATA = "data/events.json";
	
	@Autowired
	private EventRepository eventRepository;
    
	
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
	public Event save(Event event) {
		return eventRepository.save(event);
	}
}
