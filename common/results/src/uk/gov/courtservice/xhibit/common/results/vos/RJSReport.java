package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RJSReport extends ReportAbsttractValue{
	
	private static final long serialVersionUID = 1L;

	private String sittingDate;
	private ArrayList<RJSJudgeType> rjsJudgeTypeValues;

	public final String getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(String sittingDate) {
		this.sittingDate = sittingDate;
	}
	
	public ArrayList<RJSJudgeType> getRjsJudgeTypeValues() {
		return rjsJudgeTypeValues;
	}

	public void setRjsJudgeTypeValues(ArrayList<RJSJudgeType> rjsJudgeTypeValues) {
		this.rjsJudgeTypeValues = rjsJudgeTypeValues;
	}
	
	@Override
	public String getShortReportCode() {
		return "RJS";
	}
}
