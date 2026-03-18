package uk.gov.courtservice.xhibit.client.order.util;

import java.util.Date;

public class XhbOrderConvictionData {
	
	
	private Date convictionDate = new Date();
	private String convictCourtID=""; 
	private String driverNo="";
	private String licenceType="";
	private String licenceIssueNo=""; 
	private String gender ="";
	private String courtCode;
	
	
	public Date getConvictionDate() {
		return convictionDate;
	}
	public void setConvictionDate(Date convictionDate) {
		this.convictionDate = convictionDate;
	}
	public String getConvictCourtID() {
		return convictCourtID;
	}
	public void setConvictCourtID(String convictCourtID) {
		this.convictCourtID = convictCourtID;
	}
	public String getDriverNo() {
		return driverNo;
	}
	public void setDriverNo(String driverNo) {
		this.driverNo = driverNo;
	}
	public String getLicenceType() {
		return licenceType;
	}
	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}
	public String getLicenceIssueNo() {
		return licenceIssueNo;
	}
	public void setLicenceIssueNo(String licenceIssueNo) {
		this.licenceIssueNo = licenceIssueNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCourtCode(){
		return courtCode;
	}
	public void setCourtCode(String courtID) {
		this.courtCode = courtID;
	}
	

}
