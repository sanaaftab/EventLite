package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.ac.man.cs.eventlite.testutil.MessageConverterUtil.getMessageConverters;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import uk.ac.man.cs.eventlite.EventLite;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@WithMockUser(username="admin", roles={"ADMINISTRATOR"})
public class VenuesControllerApiTest {

	private MockMvc mvc;

	@Autowired
	private Filter springSecurityFilterChain;

	@Mock
	private VenueService venueService;
	
	@Mock
	private EventService eventService;
	
	@InjectMocks
	private EventsControllerApi eventsController;
	
	@InjectMocks
	private VenuesControllerApi venuesController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(venuesController).apply(springSecurity(springSecurityFilterChain))
				.setMessageConverters(getMessageConverters()).build();
	}

	@Test
	public void getIndexWhenNoVenues() throws Exception {
		when(venueService.findAll()).thenReturn(Collections.<Venue> emptyList());

		mvc.perform(get("/api/venues").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(handler().methodName("getAllVenues")).andExpect(jsonPath("$.length()", equalTo(1)))
				.andExpect(jsonPath("$._links.self.href", endsWith("/api/venues")));

		verify(venueService).findAll();
	}

	
	public void getIndexWithVenues() throws Exception {
		Venue v = mock(Venue.class);
		when(venueService.findAll()).thenReturn(Collections.<Venue>singletonList(v));

		mvc.perform(get("/api/venues").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(handler().methodName("getAllVenues")).andExpect(jsonPath("$.length()", equalTo(2)))
				.andExpect(jsonPath("$._links.self.href", endsWith("/api/venues")))
				.andExpect(jsonPath("$._embedded.venues.length()", equalTo(1)));

		verify(venueService).findAll();
	}
	
	@Test
	public void testDeletebyID() throws Exception {
		Venue v = mock(Venue.class);
		when(venueService.findOne(0)).thenReturn(v);
		
		mvc.perform(delete("/api/venues/0").with(csrf()))
			.andExpect(status().isNoContent())
			.andExpect(handler().methodName("delVenue"));
		
		verify(venueService).deleteById(0);
	}
	
	@Test
	public void testGetNext3Events() throws Exception {
		Venue v = mock(Venue.class);
		ArrayList<Event> upcoming = new ArrayList<Event>();
		when(venueService.findOne(0)).thenReturn(v);
		when(eventService.findNext3ForVenue(0)).thenReturn(upcoming);
		Event a = new Event();
		Event b = new Event();
		Event c = new Event();
		a.setVenue(v);
		b.setVenue(v);
		c.setVenue(v);
		upcoming.add(a);
		upcoming.add(b);
		upcoming.add(c);
		
		mvc.perform(get("/api/venues/0/next3events").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getNext3Events"));

	}
	
	@Test
	public void testGetAllEventsAtVenue() throws Exception {
		Venue v = mock(Venue.class);
		ArrayList<Event> upcoming = new ArrayList<Event>();
		when(venueService.findOne(0)).thenReturn(v);
		when(eventService.findAllEventsAtVenue(0)).thenReturn(upcoming);
		Event a = new Event();

		a.setVenue(v);

		upcoming.add(a);

		mvc.perform(get("/api/venues/0/events").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("getAllEventsAtVenue"));

	}
	
	
}
