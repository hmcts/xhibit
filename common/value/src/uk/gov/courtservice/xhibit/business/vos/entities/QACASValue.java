package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * This object value is used to store values for each defendant that is returned
 * during the QACAS process.  Each Value is made up of two tables XHB_DEFENDANT_HISTORY and 
 * XHB_DEFENDANT_ON_CASE_HISTORY so this object is to store the entry of each to use in QACAS screen.
 */

public class QACASValue extends CSAbstractValue {

	private static final long serialVersionUID = 4880564542416477232L;
	private DefendantHistoryBasicValue defendantHistory;
	private DefendantOnCaseHistoryBasicValue defendantOnCase;
	
	public QACASValue() {
		super();
	}
	
	public QACASValue(DefendantHistoryBasicValue defendantHistory, DefendantOnCaseHistoryBasicValue defendantOnCase) {
		this.defendantHistory = defendantHistory;
		this.defendantOnCase = defendantOnCase;
	}

	public DefendantHistoryBasicValue getDefendantHistory() {
		return defendantHistory;
	}

	public void setDefendantHistory(DefendantHistoryBasicValue defendantHistory) {
		this.defendantHistory = defendantHistory;
	}

	public DefendantOnCaseHistoryBasicValue getDefendantOnCase() {
		return defendantOnCase;
	}

	public void setDefendantOnCase(DefendantOnCaseHistoryBasicValue defendantOnCase) {
		this.defendantOnCase = defendantOnCase;
	}
	
	public String getGenderString() {
		if(defendantHistory!=null) {
			return defendantHistory.getGenderString();
		}
   		return "";
    }


    }
