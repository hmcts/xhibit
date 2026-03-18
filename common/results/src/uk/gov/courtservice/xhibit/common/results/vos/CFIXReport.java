package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class CFIXReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;	
	
	private String hearingFromDate;
	private String hearingEndDate;
	private ArrayList<CFIXValue> cfixValues;
	private ArrayList hearingValues;
	
	public CFIXReport(){
		cfixValues = new ArrayList<CFIXValue> ();
	}

	public String getHearingFromDate() {
		return hearingFromDate;
	}


	public void setHearingFromDate(String hearingFromDate) {
		this.hearingFromDate = hearingFromDate;
	}


	public String getHearingEndDate() {
		return hearingEndDate;
	}


	public void setHearingEndDate(String hearingEndDate) {
		this.hearingEndDate = hearingEndDate;
	}


	public ArrayList<CFIXValue> getCFIXValues() {
		return cfixValues;
	}


	public void setCFIXValues(ArrayList<CFIXValue> cfixValues) {
		this.cfixValues = cfixValues;
	}


	public ArrayList getHearingValues() {
		return hearingValues;
	}


	public void setHearingValues(ArrayList hearingValues) {
		this.hearingValues = hearingValues;
	}

	@Override
	public String getShortReportCode() {
		return "CFIX";
	}
}
