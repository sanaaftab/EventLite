package uk.ac.man.cs.eventlite.dao;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import uk.ac.man.cs.eventlite.entities.Event;

@Repository
@Transactional
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

}
