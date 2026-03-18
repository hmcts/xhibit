package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RRCAReport extends ReportAbsttractValue{
	
	private static final long serialVersionUID = 1L;

	private String weekmonthdate;
	
	private ArrayList rrcaValues;
	private ArrayList rrcaSummaryValues;
	private List<List> rrcaSites;
	private ArrayList rrcaSummaryDetail;
	
	@Override
	public String getShortReportCode() {
		return "RRCA";
	}

	public String getWeekmonthdate() {
		return weekmonthdate;
	}

	public void setWeekmonthdate(String weekmonthdate) {
		this.weekmonthdate = weekmonthdate;
	}

	public ArrayList getRrcaValues() {
		return rrcaValues;
	}

	public void setRrcaValues(ArrayList rrcaValues) {
		this.rrcaValues = rrcaValues;
	}

	public ArrayList getRrcaSummaryValues() {
		return rrcaSummaryValues;
	}

	public void setRrcaSummaryValues(ArrayList rrcaSummaryValues) {
		this.rrcaSummaryValues = rrcaSummaryValues;
	}

	public List<List> getRrcaSites() {
		return rrcaSites;
	}

	public void setRrcaSites(List<List> rrcaSites) {
		this.rrcaSites = rrcaSites;
	}

	public ArrayList getRrcaSummaryDetail() {
		return rrcaSummaryDetail;
	}

	public void setRrcaSummaryDetail(ArrayList rrcaSummaryDetail) {
		this.rrcaSummaryDetail = rrcaSummaryDetail;
	}
}
