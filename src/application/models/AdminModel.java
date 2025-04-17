package application.models;

public class AdminModel {
	private Integer admin_id;

	private String username;
	
	private String password;
	
	private String email;
	
	public AdminModel() {
		// Empty constructor
	}
	
	public AdminModel(Integer id, String username, String password, String email) {
		admin_id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public Integer getId() {
		return admin_id;
	}
	
	public void setId(Integer id) {
		admin_id = id;
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
