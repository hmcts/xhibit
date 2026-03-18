package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class CTLRPExReportValue implements Serializable {
	
	private static final long serialVersionUID = 1090233670583365361L;
	
	private String casedefendantnumber;
	private String defendantname;
	private String committaldate;
	private String custodytimelimit;
	private String listed;
	private String prosecutorname;
	private String ctlapplies;
	private String currentbcstatus;
	
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
	public String getCustodytimelimit() {
		return custodytimelimit;
	}
	public void setCustodytimelimit(String custodytimelimit) {
		this.custodytimelimit = custodytimelimit;
	}
	public String getListed() {
		return listed;
	}
	public void setListed(String listed) {
		this.listed = listed;
	}
	public String getCommittaldate() {
		return committaldate;
	}
	public void setCommittaldate(String committaldate) {
		this.committaldate = committaldate;
	}
	public String getProsecutorname() {
		return prosecutorname;
	}
	public void setProsecutorname(String prosecutorname) {
		this.prosecutorname = prosecutorname;
	}
	public String getCtlapplies() {
		return ctlapplies;
	}
	public void setCtlapplies(String ctlapplies) {
		this.ctlapplies = ctlapplies;
	}
	public String getCurrentbcstatus() {
		return currentbcstatus;
	}
	public void setCurrentbcstatus(String currentbcstatus) {
		this.currentbcstatus = currentbcstatus;
	}	
}