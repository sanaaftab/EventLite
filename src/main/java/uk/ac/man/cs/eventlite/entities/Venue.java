package uk.ac.man.cs.eventlite.entities;

import java.time.LocalDate;
import java.util.List;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mapbox.geojson.Point;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.man.cs.eventlite.config.data.InitialDataLoader;



@Entity
@Table(name = "venues")
public class Venue{
	
	static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiaWxpbmNhaW9uIi"
					    + "wiYSI6ImNrOHk2Ym04cDB0cjgzaG1pc25uNzF1aTkifQ.7t7_eaFGSaNWVSUpBmWxAQ" ;
	
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Size(max=256)
	private String name;
	
	@NotNull
	@Size(max=300)
	@Pattern(regexp = "[a-zA-Z0-9 -]*" )
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
	
	private double longitude;
	private double latitude;
	
	
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
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longtitude) {
		this.longitude = longtitude;
	}
	
	public String displayUpcomingEvents()
	{
		int counter = 0;
		String futureEvents = "";
		for(Event event : this.getEvents()){
			   if(event.IsitAfter() && counter == 0)
				   {
				      futureEvents = event.getName();
				      counter = 1;
				   }
			   else futureEvents = futureEvents+ " & " +event.getName();
			}
		return futureEvents;
	}
}
