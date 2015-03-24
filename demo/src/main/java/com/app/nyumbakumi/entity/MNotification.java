package com.app.nyumbakumi.entity;

public class MNotification {
	private String user, group, message, time;
	
	public MNotification() {
		// TODO Auto-generated constructor stub
	}
	
	public MNotification(String user, String group, String message, String time) {
		setUser(user);
		setGroup(group);
		setMessage(message);
		setTime(time);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
