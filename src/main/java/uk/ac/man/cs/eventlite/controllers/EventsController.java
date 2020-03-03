package uk.ac.man.cs.eventlite.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET)
	public String getAllEvents(Model model) {

		model.addAttribute("events", eventService.findAll());

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
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createEvent(@RequestBody @Valid @ModelAttribute Event event,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {
		
		if (errors.hasErrors()) {
			model.addAttribute("events", event);

			return "events/new";
		}
		
		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/events";
	}
	
//	 @PostMapping("/events")
//	  public String eventSubmit(@ModelAttribute Event events) {
//	    return "events";
//	  }
	
	//update
	
		/*
	    @RequestMapping(value="/event/{eventId}/form", method=RequestMethod.POST) 
	    public String updateForm(ModelMap modelMap, @PathVariable eventId)
	    {
	        Event event = eventService.getById(eventId);
	        modelMap.addAttribute(event);
	        return "event/update";     
	    }
	    */
		
		
		@RequestMapping(value="/event/{eventId}", method=RequestMethod.POST)
	    public String update(ModelMap modelMap, @Valid @ModelAttribute Event event, BindingResult bindingResult)
	    {
	        if(bindingResult.hasErrors())
	        {
	            modelMap.addAttribute(event);
	            return "event/update";
	        }

	        eventService.save(event);

	        return "redirect:event/" + event.getId() + "/success";
	    }

}
