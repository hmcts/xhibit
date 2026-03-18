package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RRECReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;

	private String weekmonthdate;
	
	private ArrayList rrecValues;
	private ArrayList rrecSummaryValues;
	private List<List> rrecSites;
	private ArrayList rrecSummaryDetail;

	public ArrayList getRrecValues() {
		return rrecValues;
	}

	public void setRrecValues(ArrayList rrecValues) {
		this.rrecValues = rrecValues;
	}

	public ArrayList getRrecSummaryValues() {
		return rrecSummaryValues;
	}

	public void setRrecSummaryValues(ArrayList rrecSummaryValues) {
		this.rrecSummaryValues = rrecSummaryValues;
	}

	@Override
	public String getShortReportCode() {
		return "RREC";
	}

	public String getWeekmonthdate() {
		return weekmonthdate;
	}

	public void setWeekmonthdate(String weekmonthdate) {
		this.weekmonthdate = weekmonthdate;
	}

	public List<List> getRrecSites() {
		return rrecSites;
	}

	public void setRrecSites(List<List> rrecSites) {
		this.rrecSites = rrecSites;
	}

	public ArrayList getRrecSummaryDetail() {
		return rrecSummaryDetail;
	}

	public void setRrecSummaryDetail(ArrayList rrecSummaryDetail) {
		this.rrecSummaryDetail = rrecSummaryDetail;
	}
}
