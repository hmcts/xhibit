package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RAGEReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String dateOfRequest;
	private String courttelephoneno;
	private String courtaddress;
	private String weekFrom;
	private String weekTo;
	private String time;
	private String caseclasses;
	private String sitecommited;
	private String username;
	
	private ArrayList rageValues;

	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public String getCourttelephoneno() {
		return courttelephoneno;
	}

	public void setCourttelephoneno(String courttelephoneno) {
		this.courttelephoneno = courttelephoneno;
	}

	public String getCourtaddress() {
		return courtaddress;
	}

	public void setCourtaddress(String courtaddress) {
		this.courtaddress = courtaddress;
	}

	public String getWeekFrom() {
		return weekFrom;
	}

	public void setWeekFrom(String weekFrom) {
		this.weekFrom = weekFrom;
	}

	public String getWeekTo() {
		return weekTo;
	}

	public void setWeekTo(String weekTo) {
		this.weekTo = weekTo;
	}

	public ArrayList getRageValues() {
		return rageValues;
	}

	public void setRageValues(ArrayList rageValues) {
		this.rageValues = rageValues;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSitecommited() {
		return sitecommited;
	}

	public void setSitecommited(String sitecommited) {
		this.sitecommited = sitecommited;
	}

	public String getCaseclasses() {
		return caseclasses;
	}

	public void setCaseclasses(String caseclasses) {
		this.caseclasses = caseclasses;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getShortReportCode() {
		return "RAGE";
	}	
}
