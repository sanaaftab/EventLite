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
import uk.ac.man.cs.eventlite.entities.Venue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@DirtiesContext
@ActiveProfiles("test")

public class VenueServiceTest extends AbstractTransactionalJUnit4SpringContextTests {


	private VenueRepository venueRepository;
	
	private VenueService venueService;
	
	@Test
	public void testCount() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
		long x = 0;
		given(venueRepository.count()).willReturn(x);
		
		long y = venueService.count();
		
		assertThat(x, equalTo(y));
		
	}
	
	@Test
	public void testFindAll() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
		List<Venue> data = new ArrayList();
		
		data.add(new Venue());
		data.add(new Venue());
		data.add(new Venue());
		
		given(venueService.findAll()).willReturn(data);
		
		Iterable<Venue> expected = venueService.findAll();
		Iterable<Venue> datas = data;
		
		assertThat(expected, equalTo(datas));
		
	}
	
	@Test
	public void testFindById() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
		Venue e = new Venue();
		 
		long x = 0; 
		
		e.setId(x);
		
		given(venueService.findById(x)).willReturn(Optional.ofNullable(e));
		
		Optional<Venue> exp = venueService.findById(x);
		
		assertThat(exp, equalTo(Optional.ofNullable(e)));
		
	}
	
	@Test
	public void testSave() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
		Venue e = new Venue();
		 
		long x = 0; 
		
		e.setId(x);
		
		doThrow(RuntimeException.class).when(venueService).save(e);
		
		Assertions.assertThrows(RuntimeException.class, () -> venueService.save(e));
		
	}
	
	@Test
	public void testFindAllByName() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
	    List<Venue> data = new ArrayList();
		
		data.add(new Venue());
		data.add(new Venue());
		data.add(new Venue());
	    
	    given(venueService.findAllByName(anyString())).willReturn(data);
		
	    Iterable<Venue> expected = venueService.findAllByName("string");
		Iterable<Venue> datas = data;
		
		assertThat(expected, equalTo(datas));
	}

	
	@Test
	public void testDeleteById() throws Exception{
		
		venueRepository = mock(VenueRepository.class);
	    venueService = mock(VenueService.class);
		
		final long id = 1L;
		
		venueService.deleteById(id);
		venueService.deleteById(id);
		
		verify(venueService, times(2)).deleteById(id);
		
	}
}
