package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * The model used for the fields on the public representation screen.
 * 
 * @author kudzinc
 *
 */
public class ProsecutorRespondentSearchModel {
	private Object callingClass;
	private XhibitApplicationController xac;
	private ProsecutorRespondent prosRes;

	public ProsecutorRespondentSearchModel(XhibitApplicationController xac, XPanel callingClass) {
		this.xac = xac;
		this.callingClass = callingClass;
	}

	public ProsecutorRespondentSearchModel() {
		clearmodel();
	}

	public Object getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(Object callingClass) {
		this.callingClass = callingClass;
	}

	public ProsecutorRespondent getProsecutorRespondent() {
		return prosRes;
	}

	public void setProsecutorRespondent(ProsecutorRespondent prosRes) {
		this.prosRes = prosRes;
	}

	public XhibitApplicationController getXhibitApplicationController() {
		return xac;
	}

	public void setXhibitApplicationController(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public void clearmodel() {
		// Add code to set all of the model values to null
		setXhibitApplicationController(null);
	}
}
