package model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 120323295184921490L;
	
	public enum Role{
		CLIENT,
		ADMIN
	}
	
	public User() {
		super();
		//The class is a been so the constructor can be an empty constructor
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Role getRole() {
		return role;
	}
	
	public String getProfileImageId() {
		return profileImageId;
	}

	public int getGamesOwned() {
		return gamesOwned;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}

	public void setGamesOwned(int gamesOwned) {
		this.gamesOwned = gamesOwned;
	}

	private String username;
	private String password;
	private String email;
	private Role role;
	private String profileImageId;
	private int gamesOwned;
}
