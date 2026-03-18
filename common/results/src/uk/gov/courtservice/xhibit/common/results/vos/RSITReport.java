package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RSITReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;

	private String weekdate;
	private String court_name;
	private String am_pm;
	private String crest_court_room_no;
	private String court_site_name;
	private String court_room_name;
	
	private ArrayList rsitValues;
	
	public String getCourt_name() {
		return court_name;
	}

	public void setCourt_name(String court_name) {
		this.court_name = court_name;
	}

	public String getAm_pm() {
		return am_pm;
	}

	public void setAm_pm(String am_pm) {
		this.am_pm = am_pm;
	}

	public String getCrest_court_room_no() {
		return crest_court_room_no;
	}

	public void setCrest_court_room_no(String crest_court_room_no) {
		this.crest_court_room_no = crest_court_room_no;
	}

	public String getCourt_site_name() {
		return court_site_name;
	}

	public void setCourt_site_name(String court_site_name) {
		this.court_site_name = court_site_name;
	}

	public String getCourt_room_name() {
		return court_room_name;
	}

	public void setCourt_room_name(String court_room_name) {
		this.court_room_name = court_room_name;
	}

	public String getWeekdate() {
		return weekdate;
	}

	public void setWeekdate(String weekdate) {
		this.weekdate = weekdate;
	}

	public ArrayList getRsitValues() {
		return rsitValues;
	}

	public void setRsitValues(ArrayList rsitValues) {
		this.rsitValues = rsitValues;
	}

	@Override
	public String getShortReportCode() {
		return "RSIT";
	}
}
