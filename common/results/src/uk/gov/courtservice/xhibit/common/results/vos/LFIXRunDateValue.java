package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class LFIXRunDateValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;
	
	private String rundate;

	public String getRundate() {
		return rundate;
	}

	public void setRundate(String rundate) {
		this.rundate = rundate;
	}
}
