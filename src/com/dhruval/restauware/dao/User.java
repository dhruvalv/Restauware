package com.dhruval.restauware.dao;

public class User {
	private String username,password,name;
	private boolean isAdmin;
	private java.util.Date lastLogin;
	
	public User(){
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public java.util.Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(java.util.Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
