package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class NFIXReport extends ReportAbsttractValue implements ISingleRunLetterReport{

	private static final long serialVersionUID = 1L;
	private String dateOfRequest;
	private List<NFIXFixtureValue> nfixFixtureValues;
	
	public NFIXReport(){
		nfixFixtureValues = new Vector<NFIXFixtureValue>();
    }
	
    /**
	 * @return the dateOfRequest
	 */
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	/**
	 * @param dateOfRequest the dateOfRequest to set
	 */
	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	@Override
	public String getShortReportCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer[] getReportedIDs() {
		Integer[] reportedFixturessArray = new Integer[getNfixFixtureValues().size()];
		for(int i = 0; i < getNfixFixtureValues().size(); i++)
    	{
    		NFIXFixtureValue cv =  getNfixFixtureValues().get(i);
    		reportedFixturessArray[i] = cv.getFixtureId();
    	}	
		
		
		return reportedFixturessArray;
	}

	@Override
	public String getDatabaseUpdateProcedureName() {
		return "XHB_REPORT_PKG.MARK_FIXTURES_AS_REPORTED";
	}

	public List<NFIXFixtureValue> getNfixFixtureValues() {
		return nfixFixtureValues;
	}

	public void setNfixFixtureValues(List<NFIXFixtureValue> nfixFixtureValues) {
		this.nfixFixtureValues = nfixFixtureValues;
	}
}
