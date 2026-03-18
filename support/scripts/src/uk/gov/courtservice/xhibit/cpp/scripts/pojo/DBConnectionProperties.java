package uk.gov.courtservice.xhibit.cpp.scripts.pojo;

public class DBConnectionProperties {

	private String url;
	private String username;
	private String password;
	private int minIdle;
	private int maxIdle;
	private int maxOpenStatements;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxOpenStatements() {
		return maxOpenStatements;
	}
	public void setMaxOpenStatements(int maxOpenStatements) {
		this.maxOpenStatements = maxOpenStatements;
	}
	
	@Override
	public String toString() {
		return "DBConnectionProperties [url=" + url + ", username=" + username + ", password=" + password + ", minIdle="
				+ minIdle + ", maxIdle=" + maxIdle + ", maxOpenStatements=" + maxOpenStatements + "]";
	}
	
}
