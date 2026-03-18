package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class RAGEReportValue implements Serializable {
	
	private static final long serialVersionUID = 1227014888998573013L;
	
	private String casenumber;
	private String casetitle;
	private String juvenile;
	private String classcode;
	private String bcstatus;
	private String commitalsent;
	private String linkedcaseslist;
	private String listhistorylist;
	private String noteslist;
	private String sitecommited;
	private String weekdiff;
	private String offences;
	private String monitoringcategory; 
	private String benchwarrantdate;
	private String timeestimate;

	public String getCasetitle() {
		return casetitle;
	}

	public void setCasetitle(String casetitle) {
		this.casetitle = casetitle;
	}
	
	public String getJuvenile() {
		return juvenile;
	}
	
	public void setJuvenile(String juvenile) {
		this.juvenile = juvenile;
	}

	public String getClasscode() {
		return classcode;
	}

	public void setClasscode(String classcode) {
		this.classcode = classcode;
	}

	public String getBcstatus() {
		return bcstatus;
	}

	public void setBcstatus(String bcstatus) {
		this.bcstatus = bcstatus;
	}

	public String getCommitalsent() {
		return commitalsent;
	}

	public void setCommitalsent(String commitalsent) {
		this.commitalsent = commitalsent;
	}

	public String getSitecommited() {
		return sitecommited;
	}

	public void setSitecommited(String sitecommited) {
		this.sitecommited = sitecommited;
	}

	public String getLinkedcaseslist() {
		return linkedcaseslist;
	}

	public void setLinkedcaseslist(String linkedcaseslist) {
		this.linkedcaseslist = linkedcaseslist;
	}

	public String getNoteslist() {
		return noteslist;
	}

	public void setNoteslist(String noteslist) {
		this.noteslist = noteslist;
	}

	public String getWeekdiff() {
		return weekdiff;
	}

	public void setWeekdiff(String weekdiff) {
		this.weekdiff = weekdiff;
	}

	public String getOffences() {
		return offences;
	}

	public void setOffences(String offences) {
		this.offences = offences;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getListhistorylist() {
		return listhistorylist;
	}

	public void setListhistorylist(String listhistorylist) {
		this.listhistorylist = listhistorylist;
	}
	
	public String getMonitoringcategory() {
		return monitoringcategory;
	}

	public void setMonitoringcategory(String monitoringcategory) {
		this.monitoringcategory = monitoringcategory;
	}

	public String getBenchwarrantdate() {
		return benchwarrantdate;
	}

	public void setBenchwarrantdate(String benchwarrantdate) {
		this.benchwarrantdate = benchwarrantdate;
	}

	public String getTimeestimate() {
		return timeestimate;
	}

	public void setTimeestimate(String timeestimate) {
		this.timeestimate = timeestimate;
	}
}
