package uk.ac.man.cs.eventlite.controllers;
import java.util.List;
import java.util.Optional;

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

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.search.SearchQuery;

@Controller
@RequestMapping(value = "/venues", produces = { MediaType.TEXT_HTML_VALUE })
public class VenuesController {

	static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiaWxpbmNhaW9uIi"
		    + "wiYSI6ImNrOHk2Ym04cDB0cjgzaG1pc25uNzF1aTkifQ.7t7_eaFGSaNWVSUpBmWxAQ" ;
	
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
	public String createVenue(@RequestBody @Valid @ModelAttribute Venue venue,
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
		    		p_errors += "Road name must be less than 300 characters and can only contain letters and numbers. ";
		    		break;
		    	case "postcode":
		    		p_errors += "Invalid postcode. Postcode can only contain letters and numbers";
		    		break;
		    	}
		    }
			redirectAttrs.addFlashAttribute("message", p_errors);

			return "redirect:/venues/new";

		}
		MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
				.accessToken(MAPBOX_ACCESS_TOKEN)
				.query(venue.getPostcode() + " " + venue.getRoadname())
				.build();

			mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
			@Override
			public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
		
				List<CarmenFeature> results = response.body().features();
	
				if (results.size() > 0) {
				  Point firstResultPoint = results.get(0).center();
				  venue.setLatitude(firstResultPoint.latitude());
				  venue.setLongitude(firstResultPoint.longitude());
				}
			}
		
			@Override
			public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				

		venueService.save(venue);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/venues";
	}
	
	@RequestMapping(value="update/{id}", method=RequestMethod.GET)
	public String getFormData(Model model, @PathVariable("id") long id){
		
		Optional<Venue> venue = venueService.findById(id);
		
		if(venue.isPresent()) {
			model.addAttribute("venue", venue.get());
			return "venues/update";
		}
		
		return "redirect:/venues";
		
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String update(@RequestBody @Valid @ModelAttribute Venue venue, BindingResult errors, 
    		Model model, RedirectAttributes redirectAttrs, @PathVariable("id") long id)
    {
		Venue venueToBeUpdated = venueService.findById(id).get();

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
		    		p_errors += "Road name must be less than 300 characters and can only contain letters and numbers. ";
		    		break;
		    	case "postcode":
		    		p_errors += "Invalid postcode. Postcode can only contain letters and numbers";
		    		break;
		    	}
		    }
			redirectAttrs.addFlashAttribute("message", p_errors);

			return "redirect:/venues/update/{id}";

		}
		
		if(venue.getName()!=null && !venue.getName().equals("")) {
			venueToBeUpdated.setName(venue.getName());
		}
		else venue.setName(venueToBeUpdated.getName());
		
		if(venue.getPostcode()!=null && !venue.getPostcode().equals("")) {
			venueToBeUpdated.setPostcode(venue.getPostcode());
		}
		else venue.setPostcode(venueToBeUpdated.getPostcode());
		
		if(venue.getRoadname()!=null && !venue.getRoadname().equals("")) {
			venueToBeUpdated.setRoadname(venue.getRoadname());
		}
		else venue.setRoadname(venueToBeUpdated.getRoadname());
		
		if(venue.getCapacity()!=0) {
			venueToBeUpdated.setCapacity(venue.getCapacity());
		}		
		else venue.setCapacity(venueToBeUpdated.getCapacity());
		
		MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
				.accessToken(MAPBOX_ACCESS_TOKEN)
				.query(venue.getPostcode() + " " + venue.getRoadname())
				.build();

			mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
			@Override
			public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
		
				List<CarmenFeature> results = response.body().features();
	
				if (results.size() > 0) {
				  Point firstResultPoint = results.get(0).center();
				  venue.setLatitude(firstResultPoint.latitude());
				  venue.setLongitude(firstResultPoint.longitude());
				}
			}
		
			@Override
			public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
        venueService.save(venue);

        return "redirect:/venues";
    }

	

	@RequestMapping(value="/{id}" , method = RequestMethod.DELETE)
	public String deletebyID(@PathVariable long id, RedirectAttributes redirectAttributes) {
		//Here we call deleteByID from venueService which would delete a venue according to id of venue selected to delete
		
		//find get venue with this id
		Venue venue = venueService.findOne(id);
		
		//check if venue has any events assigned to it and only delete if no events
		if ((venue.getEvents()).size() == 0)
		{	
			//delete venue
			venueService.deleteById(id);
			
			String sucMes = "Success! Venue " + venue.getName() + " deleted.";
			
			//message sent to display
			redirectAttributes.addFlashAttribute("message", sucMes);
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			return "redirect:/venues";
		}	
		else
		{	//message sent to display
			redirectAttributes.addFlashAttribute("message", "Failed to delete venue. There are one or more events linked to the venue you are trying to delete");
	    	redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			return "redirect:/venues/{id}";
		}

	} 

}
