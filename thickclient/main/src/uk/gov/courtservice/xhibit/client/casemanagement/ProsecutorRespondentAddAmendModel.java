package uk.gov.courtservice.xhibit.client.casemanagement;

import javax.swing.JDialog;

import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Prosecutor Respondent Model
 * 
 * @author c.kudzin
 *
 */
public class ProsecutorRespondentAddAmendModel {

	private Object callingClass;
	private RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue;

	public ProsecutorRespondentAddAmendModel() {
		clearmodel();
	}

	public ProsecutorRespondentAddAmendModel(RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue,
			XPanel callingClass) {
		this.refProsecutorAgencyComplexValue = refProsecutorAgencyComplexValue;
		this.callingClass = callingClass;
	}

	public void setRefProsecutorAgencyComplexValue(RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue) {
		this.refProsecutorAgencyComplexValue = refProsecutorAgencyComplexValue;
	}

	public RefProsecutorAgencyComplexValue getRefProsecutorAgencyComplexValue() {
		return refProsecutorAgencyComplexValue;
	}

	public Object getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(JDialog callingClass) {
		this.callingClass = callingClass;
	}

	public void clearmodel() {
		setRefProsecutorAgencyComplexValue(null);
	}

}
