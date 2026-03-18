package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class NTRSFReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String courttoaddress;
	private String courtto;
	private String courttotelephoneno;
	
	private ArrayList ntrsfValues;

	public ArrayList getNtrsfValues() {
		return ntrsfValues;
	}

	public void setNtrsfValues(ArrayList ntrsfValues) {
		this.ntrsfValues = ntrsfValues;
	}

	public String getCourttoaddress() {
		return courttoaddress;
	}

	public void setCourttoaddress(String courttoaddress) {
		this.courttoaddress = courttoaddress;
	}

	public String getCourttotelephoneno() {
		return courttotelephoneno;
	}

	public void setCourttotelephoneno(String courttotelephoneno) {
		this.courttotelephoneno = courttotelephoneno;
	}

	public String getCourtto() {
		return courtto;
	}

	public void setCourtto(String courtto) {
		this.courtto = courtto;
	}

	@Override
	public String getShortReportCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
