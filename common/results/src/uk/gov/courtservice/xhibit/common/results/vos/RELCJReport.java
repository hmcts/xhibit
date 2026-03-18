package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RELCJReport extends  ReportAbsttractValue{

	private static final long serialVersionUID = 1L;
	
	private ArrayList relcjValues;
	
	public ArrayList getRelcjValues() {
		return relcjValues;
	}
	public void setRelcjValues(ArrayList relcjValues) {
		this.relcjValues = relcjValues;
	}
	@Override
	public String getShortReportCode() {
		return "RELCJ";
		
	}
	
}
