package com.easyupload.photos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Invitee {
	
	@javax.persistence.Id
	@GeneratedValue
	private Long Id;
	private Long eventId;
	private String emailId;
	private String phoneNumber;
	private String guestName;
	
	
	public Invitee() {
		super();
	}

	
	public Invitee(Long id, Long eventId, String emailId, String phoneNumber, String guestName) {
		this.Id = id;
		this.eventId = eventId;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.guestName = guestName;
	}


	public Invitee(Long eventId, String emailId, String phoneNumber, String guestName) {
		this.eventId = eventId;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.guestName = guestName;
	}
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	
	
}
