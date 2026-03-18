package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;
import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class RUMOReport extends ReportAbsttractValue {

	private static final long serialVersionUID = 1L;

	private ArrayList collectCourtsValues;


	
	public ArrayList getCollectCourtsValues() {
		return collectCourtsValues;
	}


	public void setCollectCourtsValues(ArrayList collectCourtsValues) {
		this.collectCourtsValues = collectCourtsValues;
	}

	@Override
	public String getShortReportCode() {
		return "RUMO";
	}
}
