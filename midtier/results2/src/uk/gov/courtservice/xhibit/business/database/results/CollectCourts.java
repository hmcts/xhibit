package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class CollectCourts implements Serializable {
	
	private static final long serialVersionUID = 1L;	


	private String courtName; // court_name
	private String collectCourtName; // collect_court_name (magistrates)
	private String collectCourtAddress; // collect_court_address (magistrates)
	private String ctAddress; // ct_address (issuing court)
	private String ctPhone; // ct_phone (issuing court)
	private String todayDate; // today_date
	private List<Cases> cases;
	
	
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName.substring(0,1).toUpperCase() + courtName.substring(1).toLowerCase();
	}
	
	public String getCollectCourtName() {
		return collectCourtName;
	}
	public void setCollectCourtName(String collectCourtName) {
		this.collectCourtName = collectCourtName;
	}
	
	public String getCollectCourtAddress() {
		return collectCourtAddress;
	}
	public void setCollectCourtAddress(String collectCourtAddress) {
		this.collectCourtAddress = collectCourtAddress;
	}
	
	public String getCtAddress() {
		return ctAddress;
	}
	public void setCtAddress(String ctAddress) {
		this.ctAddress = ctAddress;
	}
	public String getCtPhone() {
		return ctPhone;
	}
	public void setCtPhone(String ctPhone) {
		this.ctPhone = ctPhone;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	
	public List<Cases> getCases() {
		return cases;
	}
	public void setCases(List<Cases> cases) {
		this.cases = cases;
	}

}
