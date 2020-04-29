package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@RestController
@RequestMapping(value = "/api/venues", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class VenuesControllerApi {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET)
	public Resources<Resource<Venue>> getAllVenues() {

		return venueToResource(venueService.findAll());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Resource<Venue> venue(@PathVariable("id") long id) {

		return venueToResource(venueService.findOne(id));
	}
	
	@RequestMapping(value = "/{id}/next3events", method = RequestMethod.GET)
	public Resources<Resource<Event>> getNext3Events(@PathVariable(value = "id") long id) {
		return eventsToResource(eventService.findNext3ForVenue(id), id);
	}
	
	@RequestMapping(value = "/{id}/events", method = RequestMethod.GET)
	public Resources<Resource<Event>> getAllEventsAtVenue(@PathVariable(value = "id") long id) {
		return eventsToResource(eventService.findAllEventsAtVenue(id), id);
	}


	private Resource<Venue> venueToResource(Venue venue) {
		Link selfLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).withSelfRel();
		Link venuesLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).withRel("venue");
		Link eventsLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("events").withRel("events");
		Link next3Link = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("next3events").withRel("next3events");
		return new Resource<Venue>(venue, selfLink, venuesLink, eventsLink, next3Link);
	}

	private Resources<Resource<Venue>> venueToResource(Iterable<Venue> venues) {
		Link selfLink = linkTo(methodOn(VenuesControllerApi.class).getAllVenues()).withSelfRel();
		
		Link venuesLink = new Link("http://localhost:8080/api/profile/venues").withRel("profile");
		List<Resource<Venue>> resources = new ArrayList<Resource<Venue>>();
		for (Venue venue : venues) {
			resources.add(venueToResource(venue));
		}

		return new Resources<Resource<Venue>>(resources, selfLink, venuesLink);
	}
	
	private Resource<Event> eventToResource(Event event) {
		Link selfLink = linkTo(EventsControllerApi.class).slash(event.getId()).withSelfRel();
		Link venueLink = linkTo(EventsControllerApi.class).slash(event.getId()).slash("venue").withRel("venue");
		Link eventLink = linkTo(EventsControllerApi.class).slash(event.getId()).withRel("event");
		return new Resource<Event>(event, selfLink, eventLink,  venueLink);
	}
	
	private Resources<Resource<Event>> eventsToResource(Iterable<Event> events, long id) {
		Link selfLink = linkTo(methodOn(EventsControllerApi.class).getAllEvents()).withSelfRel();
		List<Resource<Event>> resources = new ArrayList<Resource<Event>>();
		for (Event event : events) {
			resources.add(eventToResource(event));
		}

		return new Resources<Resource<Event>>(resources, selfLink);
	}


	
	//used for deleting a venue 
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delVenue(@PathVariable("id") long id) {
		
		Venue venue = venueService.findOne(id);
		if ((venue.getEvents()).size() == 0)
				venueService.deleteById(id);
			
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
