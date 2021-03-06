package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import uk.ac.man.cs.eventlite.entities.Event;

//import map
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/events", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class EventsControllerApi {

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET)
	public Resources<Resource<Event>> getAllEvents() {

		return eventToResource(eventService.findAll());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Resource<Event> event(@PathVariable("id") long id) {
		//Event event = eventService.findOne(id);

		return eventToResource(eventService.findOne(id));
	}
	
	//used for deleting an event 
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<?> delEvent(@PathVariable("id") long id) {
			
			eventService.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		}

	
	private Resource<Event> eventToResource(Event event) {
		Link selfLink = linkTo(EventsControllerApi.class).slash(event.getId()).withSelfRel();
		Link venueLink = linkTo(EventsControllerApi.class).slash(event.getId()).slash("venue").withRel("venue");
		return new Resource<Event>(event, selfLink, venueLink);
	}

	private Resources<Resource<Event>> eventToResource(Iterable<Event> events) {
		Link selfLink = linkTo(methodOn(EventsControllerApi.class).getAllEvents()).withSelfRel();

		List<Resource<Event>> resources = new ArrayList<Resource<Event>>();
		for (Event event : events) {
			resources.add(eventToResource(event));
		}

		return new Resources<Resource<Event>>(resources, selfLink);
	}
	
	
	

}
