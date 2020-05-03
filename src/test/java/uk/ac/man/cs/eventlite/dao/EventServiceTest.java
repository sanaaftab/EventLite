package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import uk.ac.man.cs.eventlite.EventLite;
import uk.ac.man.cs.eventlite.entities.Event;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@DirtiesContext
@ActiveProfiles("test")

public class EventServiceTest extends AbstractTransactionalJUnit4SpringContextTests {


	private EventRepository eventRepository;
	
	private EventService eventService;
	
	@Test
	public void testCount() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
		long x = 0;
		given(eventRepository.count()).willReturn(x);
		
		long y = eventService.count();
		
		assertThat(x, equalTo(y));
		
	}
	
	@Test
	public void testFindAll() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
		List<Event> data = new ArrayList();
		
		data.add(new Event());
		data.add(new Event());
		data.add(new Event());
		
		given(eventService.findAll()).willReturn(data);
		
		Iterable<Event> expected = eventService.findAll();
		Iterable<Event> datas = data;
		
		assertThat(expected, equalTo(datas));
		
	}
	
	@Test
	public void testFindById() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
		Event e = new Event();
		 
		long x = 0; 
		
		e.setId(x);
		
		given(eventService.findById(x)).willReturn(Optional.ofNullable(e));
		
		Optional<Event> exp = eventService.findById(x);
		
		assertThat(exp, equalTo(Optional.ofNullable(e)));
		
	}
	
	@Test
	public void testSave() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
		Event e = new Event();
		 
		long x = 0; 
		
		e.setId(x);
		
		doThrow(RuntimeException.class).when(eventService).save(e);
		
		Assertions.assertThrows(RuntimeException.class, () -> eventService.save(e));
		
	}
	
	@Test
	public void testFindAllByName() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
	    List<Event> data = new ArrayList();
		
		data.add(new Event());
		data.add(new Event());
		data.add(new Event());
	    
	    given(eventService.findAllByName(anyString())).willReturn(data);
		
	    Iterable<Event> expected = eventService.findAllByName("string");
		Iterable<Event> datas = data;
		
		assertThat(expected, equalTo(datas));
	}

	
	@Test
	public void testDeleteById() throws Exception{
		
		eventRepository = mock(EventRepository.class);
	    eventService = mock(EventService.class);
		
		final long id = 1L;
		
		eventService.deleteById(id);
		eventService.deleteById(id);
		
		verify(eventService, times(2)).deleteById(id);
		
	}
}
