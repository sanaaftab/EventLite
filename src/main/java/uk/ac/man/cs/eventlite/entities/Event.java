package uk.ac.man.cs.eventlite.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.format.DateTimeFormatter;  
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="events") 
public class Event {

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	@Future
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime time;

	@NotNull
	@Size(max=256)
	private String name;
	
	@Size(max=500)
	private String description;

	@NotNull
	@ManyToOne
	private Venue venue;

	public Event() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public boolean IsitAfter() 
	{
  		
		LocalDate now  = LocalDate.now();
		LocalTime timenow = LocalTime.now();
		LocalDate eventDate = this.getDate();
		LocalTime time = this.getTime();
		if(eventDate.isAfter(now))
			return true;
		else if(eventDate.isEqual(now) && time.isAfter(timenow))
			return true;
		else
			return false;
		
	}
	
	public boolean IsitBefore() 
	{
  		
		LocalDate now  = LocalDate.now();
		LocalTime timenow = LocalTime.now();
		LocalDate eventDate = this.getDate();
		LocalTime time = this.getTime();
		
		if(eventDate.isAfter(now))
			return false;
		else if(eventDate.isEqual(now) && time.isAfter(timenow))
			return false;
		else
			return true;
			
	}
}
