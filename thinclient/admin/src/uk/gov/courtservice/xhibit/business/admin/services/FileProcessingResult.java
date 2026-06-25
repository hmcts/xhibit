package uk.gov.courtservice.xhibit.business.admin.services;

import java.util.List;

public class FileProcessingResult {
	private List<String> errors;
	private int rowCount;
	private String content;
	private String checkSum;
	
	public FileProcessingResult(List<String> errors, int rowCount, String content, String checkSum) {
		this.errors = errors;
		this.rowCount = rowCount;
		this.content = content;
		this.checkSum = checkSum;
	}
	
	public List<String> getErrors() { return errors; }
	public int getRowCount() { return rowCount; }
	public String getContent() { return content; }
	public String getChecksum() { return checkSum; }
}