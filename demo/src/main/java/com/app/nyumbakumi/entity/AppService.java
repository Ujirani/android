package com.app.nyumbakumi.entity;

public class AppService {
	/**
	"id": 1,
    "name": "G4S",
    "service_id": 1,
    "location": "Nairobi",
    "phone_number": "254788223399",
	 */
	private String id, name, service_id, location, phone_Number;
	
	public AppService() {
	}
	
	/**
	 * Constructor 
	 * @param id
	 * @param name
	 * @param service_id
	 * @param location
	 * @param phone_Number
	 */
	public AppService(String id, String name, String service_id, String location, String phone_Number) {
		setId(id);
		setName(name);
		setService_id(service_id);
		setLocation(location);
		setPhone_Number(phone_Number);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone_Number() {
		return phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		this.phone_Number = phone_Number;
	}
	
	@Override
	public String toString() {
		return "{name: "+name+", svs_id: "+service_id+", location: "+location+", phone: "+phone_Number+"}";
	}
}
