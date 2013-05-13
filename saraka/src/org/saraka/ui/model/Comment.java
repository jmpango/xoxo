package org.saraka.ui.model;

public class Comment {

	private int id;
	private String comment;
	private String owner;
	private String postedDate;
	private int buddyId;

	public Comment() {
	}
	
	public Comment(int id, String comment, String owner, String postedDate, int buddyId){
		this.id = id;
		this.comment = comment;
		this.owner = owner;
		this.postedDate = postedDate;
		this.buddyId  = buddyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public int getBuddyId() {
		return buddyId;
	}

	public void setBuddyId(int buddyId) {
		this.buddyId = buddyId;
	}
	
}
