package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class CTLRLReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String time;
	private String casesList;
	private String courttelephoneno;
	private String courtaddress;
	private ArrayList cases;

	public ArrayList getCases() {
		return cases;
	}

	public void setCases(ArrayList cases) {
		this.cases = cases;
	}

	public String getCasesList() {
		return casesList;
	}

	public void setCasesList(String casesList) {
		this.casesList = casesList;
	}

	@Override
	public String getShortReportCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
	

}
