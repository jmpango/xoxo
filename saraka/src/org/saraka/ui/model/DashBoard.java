package org.saraka.ui.model;

public class DashBoard implements Comparable<DashBoard> {
	private int id;
	private String name;
	private String tagLine;

	public DashBoard() {
	}

	public DashBoard(int id, String name, String tagLine) {
		this.id = id;
		this.name = name;
		this.tagLine = tagLine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(DashBoard dashboard) {
		if (this.getName() == null || dashboard.getName() == null)
			return 0;
		return this.name.compareToIgnoreCase(dashboard.name);
	}

}
