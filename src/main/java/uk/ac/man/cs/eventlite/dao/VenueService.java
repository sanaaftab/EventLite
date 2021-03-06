package uk.ac.man.cs.eventlite.dao;

import java.util.List;
import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public long count();
	
	public Venue save(Venue venue);

	public Iterable<Venue> findAll();
	
    public Venue findOne(long id);
	
	public Optional<Venue> findById(long id);
	
	public void deleteById(long id);
	
	public Iterable<Venue> findAllByName(String searchString);
    
	public List<Venue> findTop3MostPopularVenues();
}
