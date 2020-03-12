package uk.ac.man.cs.eventlite.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.search.SearchQuery;

@Controller
@RequestMapping(value = "/venues", produces = { MediaType.TEXT_HTML_VALUE })
public class VenuesController {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getAllVenues(Model model, @ModelAttribute SearchQuery searchQuery) {

		model.addAttribute("searchqueryKeyVenues", new SearchQuery());
		Iterable<Venue> venueList;
		if(searchQuery.getSearchString()==null || searchQuery.getSearchString().isEmpty() )
			venueList = venueService.findAll();
		else
			venueList = venueService.findAllByName(searchQuery.getSearchString());
		
		model.addAttribute("venues", venueList);
		return "venues/index";
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String DetailedVenue(@PathVariable("id") long id, Model model){

		Venue venue = venueService.findOne(id);
		model.addAttribute("venue", venue);


		return "venues/vinfo";
	}
	
	@ModelAttribute
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newVenue(Model model) {
		if (!model.containsAttribute("venue")) {
			model.addAttribute("venue", new Venue());
		}

		return "venues/new";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public String createEvent(@RequestBody @Valid @ModelAttribute Venue venue,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {
		
		if (errors.hasErrors()) {
			List<FieldError> all_errors = errors.getFieldErrors();
			String p_errors = "";
		    for (FieldError error : all_errors ) {
		    	switch (error.getField()) {
		    	case "name":
		    		p_errors += "Name must be less than 256 characters. ";
		    		break;
		    	case "capacity":
		    		p_errors += "Capacity must be a positive integer. ";
		    		break;
		    	case "roadname":
		    		p_errors += "Road name must be less than 300 characters. ";
		    		break;
		    	}
		    }
			redirectAttrs.addFlashAttribute("message", p_errors);

			return "redirect:/venues/new";

		}
		
		venueService.save(venue);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/venues";
	}

}
