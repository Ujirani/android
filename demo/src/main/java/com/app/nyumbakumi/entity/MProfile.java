package com.app.nyumbakumi.entity;

import java.io.Serializable;

public class MProfile implements Serializable{
	private static final long serialVersionUID = 4946789343204451367L;
	private String id, name, phone_number, group, type;
	private String idNumber;
	private String hseEstateValue, hseNoValue;
	private String itemImage;

	/**
	 * Empty constructor
	 */
	public MProfile() {
	}
	
	/**
	 * Constructor
	 * @param id The Users ID 
	 * @param name The Users Name
	 * @param phone_number The Users phone number
	 * @param group The users Group ID
	 * @param type The User Type
	 */
	public MProfile(String id, String name, String phone_number, String group, String type) {
		setId(id);
		setName(name);
		setPhone_number(phone_number);
		setGroup(group);
		setType(type);
	}
	
	/**
	 * Constructor
	 * @param id The Users ID 
	 * @param name The Users Name
	 * @param phone_number The Users phone number
	 * @param group The users Group ID
	 * @param type The User Type
	 */
	public MProfile(String id, String name, String phone_number, String group, String type, String resImage) {
		setId(id);
		setName(name);
		setPhone_number(phone_number);
		setGroup(group);
		setType(type);
		setItemImage(resImage);
	}
	
	/**
	 * Constructor
	 * @param id The Users ID 
	 * @param name The Users Name
	 * @param phone_number The Users phone number
	 * @param group The users Group ID
	 * @param type The User Type
	 * @param hseEstateValue House Estate
	 * @param hseNoValue House Number
	 */
	public MProfile(String id, String name, String phone_number, String group, String type, String hseEstateValue, String hseNoValue) {
		setId(id);
		setName(name);
		setPhone_number(phone_number);
		setGroup(group);
		setType(type);
		setHseEstateValue(hseEstateValue);
		setHseNoValue(hseNoValue);
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

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void SetIDNumber(String idNumber) {
		this.setIdNumber(idNumber);
	}

	public String getIDNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	@Override
	public String toString() {
		return "{name: "+name+", phone: "+phone_number+", hseEstateValue: "+hseEstateValue+", hseNoValue: "+hseNoValue+"}";
	}
	
	public String getHseEstateValue() {
		return hseEstateValue;
	}

	public String getHseNoValue() {
		return hseNoValue;
	}

	public void setHseEstateValue(String hseEstateValue) {
		this.hseEstateValue = hseEstateValue;
	}

	public void setHseNoValue(String hseNoValue) {
		this.hseNoValue = hseNoValue;
	}

	public String getItemImage() {
		return itemImage;
	}

	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}

}
