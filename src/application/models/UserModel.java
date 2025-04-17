package application.models;

public class UserModel {
	private Integer user_id;

	private String username;
	
	private String password;
	
	private String email;
	
	public UserModel() {
		// Empty constructor
	}
	
	public UserModel(Integer id, String username, String password, String email) {
		user_id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public Integer getId() {
		return user_id;
	}
	
	public void setId(Integer id) {
		user_id = id;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
