package uk.ac.man.cs.eventlite.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long>{
	
	//Venue findById(long id);
	public Iterable<Venue> findAllByNameContainingIgnoreCaseOrderByNameAsc(String searchQuery);
	
	public List<Venue> findTop3ByOrderByNumberOfEventsDesc();
}
