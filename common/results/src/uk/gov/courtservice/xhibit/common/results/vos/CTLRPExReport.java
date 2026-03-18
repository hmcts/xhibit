package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class CTLRPExReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String dateOfRequest;
	private String courttelephoneno;
	private String courtaddress;
	private String inputDate;
	private String time;
	
	private ArrayList ctlrpexValues;

	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public ArrayList getCtlrpexValues() {
		return ctlrpexValues;
	}

	public void setCtlrpexValues(ArrayList ctlrpexValues) {
		this.ctlrpexValues = ctlrpexValues;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
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

	@Override
	public String getShortReportCode() {
		return "CTLRPEx";
	}	
}
