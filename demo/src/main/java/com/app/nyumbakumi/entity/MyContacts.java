package com.app.nyumbakumi.entity;

public class MyContacts {
	private String name, location;

	/**
	 * Constructor
	 * @param name Name of the Contact
	 * @param location Location of the Contact
	 */
	public MyContacts(String name, String location) {
		this.name = name;
		this.location = location;
	}

	public MyContacts() {
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "{name: "+name+", location: "+location+"}";
	}
	
}
