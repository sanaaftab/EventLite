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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.Optional;

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

import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class EventsControllerTest {

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
	private EventsController eventsController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(eventsController).apply(springSecurity(springSecurityFilterChain))
				.build();
	}

	@Test
	public void getIndexWhenNoEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> emptyList());
		when(venueService.findAll()).thenReturn(Collections.<Venue> emptyList());

		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

		verify(eventService).findAll();
		verifyZeroInteractions(event);
	}

	@Test
	public void getIndexWithEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> singletonList(event));
		when(venueService.findAll()).thenReturn(Collections.<Venue> singletonList(venue));

		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

		verify(eventService).findAll();
		verifyZeroInteractions(event);
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateEvent() throws Exception
    {
		Event e = mock(Event.class);
		when(eventService.findById(0)).thenReturn(Optional.ofNullable(e));
        //doReturn(e).when(eventService.findById(0)).get();
        
        
        mvc.perform(post("/events/update/0").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        		 .param("name", "event")
                 .param("date", "2021-12-11")
                 .sessionAttr("venue", venue)
                 .param("description", "TEST"))
                .andExpect(status().isFound())
                .andExpect(handler().methodName("update"))
                .andExpect(redirectedUrl("/events/update/0"));
        
    }
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateEventPastDate() throws Exception {
		
		Event e = mock(Event.class);
		when(eventService.findById(0)).thenReturn(Optional.ofNullable(e));
		
        mvc.perform(post("/events/update/0").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "event")
				.param("date", "2009-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "TEST")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("update"))
                .andExpect(redirectedUrl("/events/update/0"))
				.andExpect(flash().attribute("message", "Event can only take place in the future. "));

		verify(eventService, never()).save(any());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateEventNameLong() throws Exception {
		
		Event e = mock(Event.class);
		when(eventService.findById(0)).thenReturn(Optional.ofNullable(e));
		
        mvc.perform(post("/events/update/0").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "eventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventevent")
				.param("date", "2021-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "TEST")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("update"))
                .andExpect(redirectedUrl("/events/update/0"))
				.andExpect(flash().attribute("message", "Name must be less than 256 characters. "));

		verify(eventService, never()).save(any());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateEventDescriptionLong() throws Exception {
		
		Event e = mock(Event.class);
		when(eventService.findById(0)).thenReturn(Optional.ofNullable(e));
		
        mvc.perform(post("/events/update/0").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "event")
				.param("date", "2021-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "eventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventevent")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("update"))
                .andExpect(redirectedUrl("/events/update/0"))
				.andExpect(flash().attribute("message", "Description must be less than 500 characters. "));

		verify(eventService, never()).save(any());
	}

	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateEvent() throws Exception
    {
        when(eventService.findOne(0)).thenReturn(event);

        mvc.perform(post("/events/new").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        		.param("name", "event")
                .param("date", "2021-5-5")
                .param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "TEST"))
                .andExpect(status().isFound())
                .andExpect(handler().methodName("createEvent"))
                .andExpect(view().name("redirect:/events/new"));

    }
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateEventPastDate() throws Exception {
		
		when(eventService.findOne(0)).thenReturn(event);
		
		mvc.perform(post("/events/new").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "event")
				.param("date", "2009-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "TEST")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("createEvent"))
				.andExpect(view().name("redirect:/events/new"))
				.andExpect(flash().attribute("message", "Event can only take place in the future. "));

		verify(eventService, never()).save(any());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateEventNameLong() throws Exception {
		
		when(eventService.findOne(0)).thenReturn(event);
		
		mvc.perform(post("/events/new").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "eventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventevent")
				.param("date", "2021-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "TEST")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("createEvent"))
				.andExpect(view().name("redirect:/events/new"))
				.andExpect(flash().attribute("message", "Name must be less than 256 characters. "));

		verify(eventService, never()).save(any());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateEventDescriptionLong() throws Exception {
		
		when(eventService.findOne(0)).thenReturn(event);
		
		mvc.perform(post("/events/new").with(csrf()).accept(MediaType.TEXT_HTML).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("name", "event")
				.param("date", "2021-05-05")
				.param("time", "12:00")
                .sessionAttr("venue", venue)
                .param("description", "eventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventeventevent")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(handler().methodName("createEvent"))
				.andExpect(view().name("redirect:/events/new"))
				.andExpect(flash().attribute("message", "Description must be less than 500 characters. "));

		verify(eventService, never()).save(any());
	}

	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testDeleteEvent() throws Exception
    {
        when(eventService.findOne(0)).thenReturn(event);

        mvc.perform(delete("/events/0").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(handler().methodName("deletebyID"))
                .andExpect(view().name("redirect:/events"));
    }
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testCreateEventNotApproved() throws Exception {
		mvc.perform(post("/events/new").with(user("-")).accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testDeleteEventNotApproved() throws Exception {
		mvc.perform(delete("/events/0").with(user("-")).accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username="admin", roles= {"ADMINISTRATOR"})
	public void testUpdateEventNotApproved() throws Exception {
		mvc.perform(post("/events/update/0").with(user("-")).accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isForbidden());
	}

}
