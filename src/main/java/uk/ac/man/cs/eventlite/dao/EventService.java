package uk.ac.man.cs.eventlite.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public Event findOne(long id);
	
	public Optional<Event> findById(long id);
	
	public void save(Event event);
	
    public Iterable<Event> findAllByName(String searchString);
    
    public void deleteById(long id);
    
    public List<Event> find3MostRecent(LocalDate date);

	public Iterable<Event> findNext3ForVenue(long id);

}
