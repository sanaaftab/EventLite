package uk.ac.man.cs.eventlite.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
public class HomeController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;
	
	@RequestMapping("/")
	public String GetAllEventsandVenues(Model model) {
		
		Iterable<Event> eventList;
		eventList = eventService.findAll();
		model.addAttribute("events", eventList);
		
		Iterable<Venue> venueList;
		venueList = venueService.findAll();
		model.addAttribute("venues", venueList);
		
		return "home/home";
	}
	//<form method="get" action="/">
  //  <button type="submit" class="fa fa-home">Home</button>
//</form>
}
