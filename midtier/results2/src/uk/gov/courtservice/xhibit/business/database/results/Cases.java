package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class Cases implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	private String caseNumber;
    private Integer defendantNumber; // defendant_number
    private String defendant; // defendant
    private String dob; // dob
    private String orderDate; // order_date
    private String costs; // costs
    private String fined; // fined
    private String compensation; // compensation
    private String ptiurn; //  (ptiurn)
	private String todayDate; // today_date
    

	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public Integer getDefendantNumber() {
		return defendantNumber;
	}
	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}
	public String getDefendant() {
		return defendant;
	}
	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCosts() {
		return costs;
	}
	public void setCosts(String costs) {
		this.costs = costs;
	}
	public String getFined() {
		return fined;
	}
	public void setFined(String fined) {
		this.fined = fined;
	}
	public String getCompensation() {
		return compensation;
	}
	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}
	public String getPtiurn() {
		return ptiurn;
	}
	public void setPtiurn(String ptiurn) {
		this.ptiurn = ptiurn;
	}
	
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


}
