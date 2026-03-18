package uk.gov.courtservice.xhibit.cpp.scripts.pojo;

public class FileSystemProperties {

	private String incomingFolder;
	private String afterProcessSuccessFolder;
	private String afterProcessFailureFolder;
	private String stopFile;
	
	
	public String getIncomingFolder() {
		return incomingFolder;
	}
	public void setIncomingFolder(String incomingFolder) {
		this.incomingFolder = incomingFolder;
	}
	public String getAfterProcessSuccessFolder() {
		return afterProcessSuccessFolder;
	}
	public void setAfterProcessSuccessFolder(String afterProcessSuccessFolder) {
		this.afterProcessSuccessFolder = afterProcessSuccessFolder;
	}
	public String getAfterProcessFailureFolder() {
		return afterProcessFailureFolder;
	}
	public void setAfterProcessFailureFolder(String afterProcessFailureFolder) {
		this.afterProcessFailureFolder = afterProcessFailureFolder;
	}
	
	public String getStopFile() {
		return stopFile;
	}
	public void setStopFile(String stopFile) {
		this.stopFile = stopFile;
	}
	
	@Override
	public String toString() {
		return "FileSystemProperties [incomingFolder=" + incomingFolder + ", afterProcessSuccessFolder="
				+ afterProcessSuccessFolder + ", afterProcessFailureFolder=" + afterProcessFailureFolder + ", stopFile="
				+ stopFile + "]";
	}
	
	
}
