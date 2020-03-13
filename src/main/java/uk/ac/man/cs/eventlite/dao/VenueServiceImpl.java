package uk.ac.man.cs.eventlite.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class VenueServiceImpl implements VenueService{

	@Autowired
	private VenueRepository venueRepository;
	
	@Override
	public long count() {
		return venueRepository.count();
	}
	
	@Override
	public Iterable<Venue> findAll() {
		Sort sort = Sort.by(Sort.Order.asc("name").ignoreCase());
		return venueRepository.findAll(sort);
	}

	@Override
	public Venue save(Venue venue) {
		return venueRepository.save(venue);
		
	}

	@Override
	public Optional<Venue> findById(long id) {
		return venueRepository.findById(id);
	}

	
	@Override
	public Venue findOne(long id) {
		return findById(id).orElse(null);
	}
	
	@Override
	public Iterable<Venue> findAllByName(String searchString){
		return venueRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(searchString);
	}

	@Override
	public void deleteById(long id) {
		venueRepository.deleteById(id);
	}
	

}
