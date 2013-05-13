package org.saraka.ui.model;

public class Rate{
	private int id;
	private int rate;
	private int buddyId;
	
	public Rate(){
	}
	
	public Rate(int id, int rate, int buddyId){
		this.id = id;
		this.rate = rate;
		this.buddyId = buddyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getBuddyId() {
		return buddyId;
	}

	public void setBuddyId(int buddyId) {
		this.buddyId = buddyId;
	}

}
