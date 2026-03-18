package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class CTLRPReportValue implements Serializable {
	
	private static final long serialVersionUID = 1090233670583365361L;
	
	private String casedefendantnumber;
	private String defendantname;
	private String committaldate;
	private String convictiondate;
	private String custodytimelimit;
	private String reminderprinted;
	private String listed;
	private String receipttype;
	private String prosecutorname;
	private String highlightedrow;
	private String expired;
	
	public String getCasedefendantnumber() {
		return casedefendantnumber;
	}
	public void setCasedefendantnumber(String casedefendantnumber) {
		this.casedefendantnumber = casedefendantnumber;
	}
	public String getDefendantname() {
		return defendantname;
	}
	public void setDefendantname(String defendantname) {
		this.defendantname = defendantname;
	}
	public String getConvictiondate() {
		return convictiondate;
	}
	public void setConvictiondate(String convictiondate) {
		this.convictiondate = convictiondate;
	}
	public String getCustodytimelimit() {
		return custodytimelimit;
	}
	public void setCustodytimelimit(String custodytimelimit) {
		this.custodytimelimit = custodytimelimit;
	}
	public String getReminderprinted() {
		return reminderprinted;
	}
	public void setReminderprinted(String reminderprinted) {
		this.reminderprinted = reminderprinted;
	}
	public String getListed() {
		return listed;
	}
	public void setListed(String listed) {
		this.listed = listed;
	}
	public String getHighlightedrow() {
		return highlightedrow;
	}
	public void setHighlightedrow(String highlightedrow) {
		this.highlightedrow = highlightedrow;
	}
	public String getCommittaldate() {
		return committaldate;
	}
	public void setCommittaldate(String committaldate) {
		this.committaldate = committaldate;
	}
	public String getReceipttype() {
		return receipttype;
	}
	public void setReceipttype(String receipttype) {
		this.receipttype = receipttype;
	}
	public String getProsecutorname() {
		return prosecutorname;
	}
	public void setProsecutorname(String prosecutorname) {
		this.prosecutorname = prosecutorname;
	}
	public String getExpired() {
		return expired;
	}
	public void setExpired(String expired) {
		this.expired = expired;
	}
}
