package uk.gov.courtservice.xhibit.business.services.courtel.helpers;

import org.apache.http.HttpResponse;

/**
 * Helper class for Courtel
 * 
 * @author shaheeni
 *
 */
public class CourtelListHelper {
	
	private int id;
	private HttpResponse responseServer1;
	private HttpResponse responseServer2;
	private String responseBodyServer1;
	private String responseBodyServer2;
	private String zipFilePath;
	private String fileName; 
	private String zipFileName;
	private String courtelServer1;
	private String courtelServer2;
	private int numberOfserversUploaded;
	private boolean serverOneTimeOut;
	private boolean serverTwoTimeOut;
	private boolean validResponse;
	
	public String getCourtelServer1() {
		return courtelServer1;
	}

	public void setCourtelServer1(String courtelServer1) {
		this.courtelServer1 = courtelServer1;
	}

	public String getCourtelServer2() {
		return courtelServer2;
	}

	public void setCourtelServer2(String courtelServer2) {
		this.courtelServer2 = courtelServer2;
	}

	public String getZipFilePath() {
		return zipFilePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public String getResponseBodyServer1() {
		return responseBodyServer1;
	}

	public void setResponseBodyServer1(String responseBodyServer1) {
		this.responseBodyServer1 = responseBodyServer1;
	}

	public String getResponseBodyServer2() {
		return responseBodyServer2;
	}

	public void setResponseBodyServer2(String responseBodyServer2) {
		this.responseBodyServer2 = responseBodyServer2;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}


	public HttpResponse getResponseServer1() {
		return responseServer1;
	}

	public void setResponseServer1(HttpResponse responseServer1) {
		this.responseServer1 = responseServer1;
	}

	public HttpResponse getResponseServer2() {
		return responseServer2;
	}

	public void setResponseServer2(HttpResponse responseServer2) {
		this.responseServer2 = responseServer2;
	}

	public int getNumberOfserversUploaded() {
		return numberOfserversUploaded;
	}

	public void setNumberOfserversUploaded(int numberOfserversUploaded) {
		this.numberOfserversUploaded = numberOfserversUploaded;
	}

	public boolean isValidResponse() {
		return validResponse;
	}

	public void setValidResponse(boolean validResponse) {
		this.validResponse = validResponse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isServerOneTimeOut() {
		return serverOneTimeOut;
	}

	public void setServerOneTimeOut(boolean serverOneTimeOut) {
		this.serverOneTimeOut = serverOneTimeOut;
	}

	public boolean isServerTwoTimeOut() {
		return serverTwoTimeOut;
	}

	public void setServerTwoTimeOut(boolean serverTwoTimeOut) {
		this.serverTwoTimeOut = serverTwoTimeOut;
	}


}
