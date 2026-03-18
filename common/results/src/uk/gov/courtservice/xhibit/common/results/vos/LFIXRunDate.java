package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class LFIXRunDate extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<LFIXRunDateValue> lfixRunDateValues;

	public LFIXRunDate(){
		setLfixRunDateValues(new ArrayList<LFIXRunDateValue>());
    }

	public ArrayList<LFIXRunDateValue> getLfixRunDateValues() {
		return lfixRunDateValues;
	}

	public void setLfixRunDateValues(ArrayList<LFIXRunDateValue> lfixRunDateValues) {
		this.lfixRunDateValues = lfixRunDateValues;
	}
}
