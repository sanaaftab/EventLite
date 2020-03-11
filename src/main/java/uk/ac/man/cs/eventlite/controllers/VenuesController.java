package uk.ac.man.cs.eventlite.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	@RequestMapping(value="/{id}" , method = RequestMethod.DELETE)
	public String deletebyID(@PathVariable long id) {
		//Here we call deleteByID from venueService which would delete a venue according to id of venue selected to delete
		
		//find get venue with this id
		Venue venue = venueService.findOne(id);
		
		//check if venue has any events assigned to it and only delete if no events
		if ((venue.getEvents()).size() == 0)
				venueService.deleteById(id);

		//redirect the user back to venues after action to see new view
		return "redirect:/venues";
	}

}
