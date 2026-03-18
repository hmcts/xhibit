package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class LFIXReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private ArrayList lfixReportValues;
	private String fixtureList;

	/**
	 * @return the lfixReportValues
	 */
	public ArrayList getLfixReportValues() {
		return lfixReportValues;
	}

	/**
	 * @param lfixReportValues the lfixReportValues to set
	 */
	public void setLfixReportValues(ArrayList lfixReportValues) {
		this.lfixReportValues = lfixReportValues;
	}

	@Override
	public String getShortReportCode() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns a list of case diary fixture ids from the report
	 * @return list of case diary fixture ids
	 */
	public String getFixtureList() {
		return fixtureList;
	}

	/**
	 * Sets the list case diary fixture ids from the report
	 * @param fixtureList
	 */
	public void setFixtureList(String fixtureList) {
		this.fixtureList = fixtureList;
	}
}
