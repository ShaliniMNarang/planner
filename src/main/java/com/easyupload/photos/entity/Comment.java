package com.easyupload.photos.entity;

public class Comment {
	private String commentId;
	private Number eventId;
	private String commentDesc;
	
	
	public Comment(String commentId, Number eventId, String commentDesc) {
		super();
		this.commentId = commentId;
		this.eventId = eventId;
		this.commentDesc = commentDesc;
	}
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public Number getEventId() {
		return eventId;
	}
	public void setEventId(Number eventId) {
		this.eventId = eventId;
	}
	public String getCommentDesc() {
		return commentDesc;
	}
	public void setCommentDesc(String commentDesc) {
		this.commentDesc = commentDesc;
	}
	
	
}
