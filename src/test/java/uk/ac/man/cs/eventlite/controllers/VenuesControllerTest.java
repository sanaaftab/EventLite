package uk.ac.man.cs.eventlite.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class VenuesControllerTest {

	private MockMvc mvc;

	@Autowired
	private Filter springSecurityFilterChain;

	@Mock
	private Event event;

	@Mock
	private Venue venue;

	@Mock
	private EventService eventService;

	@Mock
	private VenueService venueService;

	@InjectMocks
	private VenuesController venuesController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(venuesController).apply(springSecurity(springSecurityFilterChain))
				.build();
	}

	@Test
	public void getIndexWhenNoEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> emptyList());
		when(venueService.findAll()).thenReturn(Collections.<Venue> emptyList());

		mvc.perform(get("/venues").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("venues/index")).andExpect(handler().methodName("getAllVenues"));

		verify(venueService).findAll();
		verifyZeroInteractions(venue);
	}

	@Test
	public void getIndexWithEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> singletonList(event));
		when(venueService.findAll()).thenReturn(Collections.<Venue> singletonList(venue));

		mvc.perform(get("/venues").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("venues/index")).andExpect(handler().methodName("getAllVenues"));

		verify(venueService).findAll();
		verifyZeroInteractions(venue);
	}
	
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateVenue() throws Exception
    {
		Venue v = mock(Venue.class);
        when(venueService.findById(0).get()).thenReturn(v);

        mvc.perform(post("/venues/update/0").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        		 .param("name", "event")
                 .param("capacity", "200")
                 .param("roadname", "LastRoad")
                 .sessionAttr("event", event)
                 .param("postcode", "M144AL"))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("updateVenue"));
    }
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateVenue() throws Exception
    {
		Venue v = new Venue();
        when(venueService.findOne(0)).thenReturn(v);

        mvc.perform(post("/venues/new").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        		.param("name", "venue")
                .param("capacity", "200")
                .param("roadname", "LastRoad")
                .sessionAttr("event", event)
                .param("postcode", "M144AL"))
                .andExpect(status().isFound())
                .andExpect(handler().methodName("createVenue"));
    }
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testDeleteVenue() throws Exception
    {
		Venue v = mock(Venue.class);
		v.setName("Venue");
        when(venueService.findOne(0)).thenReturn(v);

        mvc.perform(delete("/venues/0").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(handler().methodName("deletebyID"));
    }
}
