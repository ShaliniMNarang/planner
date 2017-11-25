package com.easyupload.photos.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class EventPhoto {
	
	@javax.persistence.Id
	@GeneratedValue
	private Long Id;
	private Long eventId;
	private String fileName;
	private String s3Url;
	private String s3ThumbNailUrl;
	
	
	public EventPhoto() {
		super();
	}
	public EventPhoto(Long eventId, String fileName, String s3Url, String s3ThumbNailUrl) {
		this.eventId = eventId;
		this.fileName = fileName;
		this.s3Url = s3Url;
		this.s3ThumbNailUrl = s3ThumbNailUrl;
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
	public String getS3Url() {
		return s3Url;
	}
	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}
	public String getS3ThumbNailUrl() {
		return s3ThumbNailUrl;
	}
	public void setS3ThumbNailUrl(String s3ThumbNailUrl) {
		this.s3ThumbNailUrl = s3ThumbNailUrl;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
