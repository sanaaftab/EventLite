package uk.ac.man.cs.eventlite.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.man.cs.eventlite.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long>{

	public Iterable<Event> findAllByNameContainingIgnoreCaseOrderByDateAscTimeAsc(String searchQuery);
	
	public List<Event> findTop4ByOrderByDateAsc();
}
