package com.easyupload.photos.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Event {
	
	@javax.persistence.Id
	@GeneratedValue
	private Long eventId;
	
	private String userId;
	private String title;
	private String location;
	private String eventDate;
	private String eventTime;
	private String description;
	private String voiceInviteKey;
	

	public Event() {
		super();
	}

	public Event(String userId,String title, String location, String eventDate, String eventTime, String description) {
		this.userId = userId;
		this.title = title;
		this.location = location;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.description = description;
	}
	
	@OneToMany
	@JoinColumn(name="eventId",referencedColumnName="eventId")
	private List<Invitee> invitees;
	
	@OneToMany
	@JoinColumn(name="eventId",referencedColumnName="eventId")
	private List<EventPhoto> eventPhotos;
	
	public Long getId() {
		return eventId;
	}
	public void setId(Long eventId) {
		this.	eventId = eventId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Invitee> getInvitees() {
		return invitees;
	}
	public void setInvitees(List<Invitee> invitees) {
		this.invitees = invitees;
	}
	public List<EventPhoto> getEventPhotos() {
		return eventPhotos;
	}
	public void setEventPhotos(List<EventPhoto> eventPhotos) {
		this.eventPhotos = eventPhotos;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getVoiceInviteKey() {
		return voiceInviteKey;
	}

	public void setVoiceInviteKey(String voiceInviteKey) {
		this.voiceInviteKey = voiceInviteKey;
	}

}
