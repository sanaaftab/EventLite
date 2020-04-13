package uk.ac.man.cs.eventlite.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;



@Entity
@Table(name = "venues")
public class Venue{
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Size(max=256)
	private String name;
	
	@NotNull
	@Size(max=300)
	private String roadname;
	
	@NotNull
	@Size(max = 10)
	@Pattern(regexp = "[a-zA-Z0-9 -]*" )
	private String postcode;
	
	
	@NotNull
	@Min(1)  
	private int capacity;
    
	
	@Autowired
	@OneToMany(mappedBy = "venue")
	private Set<Event> events;
	
	private int numberOfEvents;
	
	
	public Venue() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String getRoadname() {
		return roadname;
	}

	public void setRoadname(String roadname) {
		this.roadname = roadname;
	}
	
	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	
	public int getNumberOfEvents() {
		return getEvents().size();
	}
	
	public void setNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
	}
	
}
