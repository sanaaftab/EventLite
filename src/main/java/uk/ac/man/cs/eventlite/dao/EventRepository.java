package uk.ac.man.cs.eventlite.dao;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.repository.CrudRepository;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventRepository extends CrudRepository<Event, Long>{

	public long count(); 
	
	public Iterable<Event> findByEventOrderByDateAscTimeAsc(LocalDate date, LocalTime time);
	
	
}
