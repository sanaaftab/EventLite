package uk.ac.man.cs.eventlite.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
		
		model.addAttribute("standardDate", new Date());
		model.addAttribute("localDateTime", LocalDateTime.now());
		model.addAttribute("localDate", LocalDate.now());
		model.addAttribute("timestamp", Instant.now());
		LocalDate date = LocalDate.now();
		List<Event> eventList;
		eventList = eventService.find3MostRecent(date);
		model.addAttribute("events", eventList);
		
		List<Venue> venueList;
		venueList = venueService.findTop3MostPopularVenues();
		model.addAttribute("venues", venueList);
		
		return "home/home";
	}
	
}
