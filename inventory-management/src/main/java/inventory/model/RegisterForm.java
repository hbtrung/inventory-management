package inventory.model;

public class RegisterForm {

	private String userName;
	private String password;
	private String rePassword;
	private String email;
	private String name;
	
	public RegisterForm() {
		
	}

	public RegisterForm(String userName, String password, String rePassword, String email, String name) {
		this.userName = userName;
		this.password = password;
		this.rePassword = rePassword;
		this.email = email;
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RegisterForm [userName=" + userName + ", email=" + email + ", name=" + name + "]";
	}
	
}
