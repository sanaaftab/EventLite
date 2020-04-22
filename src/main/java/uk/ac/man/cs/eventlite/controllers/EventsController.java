package uk.ac.man.cs.eventlite.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.search.SearchQuery;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET)
	public String getAllEvents(Model model, @ModelAttribute SearchQuery searchQuery) {

		model.addAttribute("searchqueryKey", new SearchQuery());
		model.addAttribute("standardDate", new Date());
		model.addAttribute("localDateTime", LocalDateTime.now());
		model.addAttribute("localDate", LocalDate.now());
		model.addAttribute("timestamp", Instant.now());
		Iterable<Event> eventList;
		if(searchQuery.getSearchString()==null || searchQuery.getSearchString().isEmpty() )
			eventList = eventService.findAll();
		else
			eventList = eventService.findAllByName(searchQuery.getSearchString());
		
		model.addAttribute("events", eventList);
		

		return "events/index";
		
	}

	@ModelAttribute
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newEvent(Model model) {
		if (!model.containsAttribute("events")) {
			
			model.addAttribute("events", new Event());
			model.addAttribute("venues", venueService.findAll());
		}

		return "events/new";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String DetailedEvent(@PathVariable("id") long id, Model model){

		Event event = eventService.findOne(id);
		model.addAttribute("event", event);
		model.addAttribute("venue", event.getVenue());
		model.addAttribute("lat", event.getVenue().getLatitude());
		model.addAttribute("lon", event.getVenue().getLongitude());


		return "events/info";
	}
	
	
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public String createEvent(@RequestBody @Valid @ModelAttribute Event event,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {
		
		if (errors.hasErrors()) {
			List<FieldError> all_errors = errors.getFieldErrors();
			String p_errors = "";
		    for (FieldError error : all_errors ) {
		    	switch (error.getField()) {
		    	case "name":
		    		p_errors += "Name must be less than 256 characters. ";
		    		break;
		    	case "date":
		    		p_errors += "Event can only take place in the future. ";
		    		break;
		    	case "description":
		    		p_errors += "Description must be less than 500 characters. ";
		    		break;
		    	}
		    }
			redirectAttrs.addFlashAttribute("message", p_errors);

			return "redirect:/events/new";

		}
		
		event.getVenue().setNumberOfEvents(event.getVenue().getNumberOfEvents() + 1);
		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/events";
	}
	
	
		@RequestMapping(value="update/{id}", method=RequestMethod.GET)
    	public String getFormData(Model model, @PathVariable("id") long id){
			
			Optional<Event> event = eventService.findById(id);
			
			if(event.isPresent()) {
				model.addAttribute("event", event.get());
				model.addAttribute("venues", venueService.findAll());
				return "events/update";
			}
			
			return "redirect:/events";
			
    	}
		
		@RequestMapping(value="/{id}" , method = RequestMethod.DELETE)
		public String deletebyID(@PathVariable long id) {
			//Here we call deleteByID from eventService which would delete an event according to id of event selected to delete
			eventService.deleteById(id);

			//redirect the user back to events after action to see new view
			return "redirect:/events";
		}

	
		
		@RequestMapping(value="/update/{id}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	    public String update(@RequestBody @Valid @ModelAttribute Event event, BindingResult errors, 
	    		Model model, RedirectAttributes redirectAttrs, @PathVariable("id") long id)
	    {
			Event eventToBeUpdated = eventService.findById(id).get();
	
			if (errors.hasErrors()) {
				List<FieldError> all_errors = errors.getFieldErrors();
				String p_errors = "";
			    for (FieldError error : all_errors ) {
			    	switch (error.getField()) {
			    	case "name":
			    		p_errors += "Name must be less than 256 characters. ";
			    		break;
			    	case "date":
			    		p_errors += "Event can only take place in the future. ";
			    		break;
			    	case "description":
			    		p_errors += "Description must be less than 500 characters. ";
			    		break;
			    	}
			    }
				redirectAttrs.addFlashAttribute("message", p_errors);

				return "redirect:/events/update/{id}";

			}
			
			if(event.getName()!=null && !event.getName().equals("")) {
				eventToBeUpdated.setName(event.getName());
			}
			else event.setName(eventToBeUpdated.getName());
			
			if(event.getDescription()!=null && !event.getDescription().equals("")) {
				eventToBeUpdated.setDescription(event.getDescription());
			}
			else event.setDescription(eventToBeUpdated.getDescription());
			
			if(event.getDate()!=null) {
				eventToBeUpdated.setDate(event.getDate());
			}
			else event.setDate(eventToBeUpdated.getDate());
			
			if(event.getTime()!=null) {
				eventToBeUpdated.setTime(event.getTime());
			}		
			else event.setTime(eventToBeUpdated.getTime());
			
			if(event.getVenue()!=null) {
				eventToBeUpdated.getVenue().setNumberOfEvents(eventToBeUpdated.getVenue().getNumberOfEvents() - 1);
				eventToBeUpdated.setVenue(event.getVenue());
				eventToBeUpdated.getVenue().setNumberOfEvents(eventToBeUpdated.getVenue().getNumberOfEvents() + 1);
			}	
			else event.setVenue(eventToBeUpdated.getVenue());
			

	        eventService.save(event);
	        
	        

	        return "redirect:/events";
	    }

}
