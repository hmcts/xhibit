package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * The model used for the fields on the Defendant/Appellant Search screen.
 * 
 * @author toftn
 *
 */
public class DefendantAppellantSearchModel {

	private DefendantValue DV;
	private DefendantAppellant DA;
	private XhibitApplicationController xac;

	private XPanel callingClass;

	public DefendantAppellantSearchModel(XhibitApplicationController xac, XPanel callingClass) {
		setCallingClass(callingClass);
		setXhibitApplicationController(xac);
	}

	public DefendantAppellantSearchModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}

	public DefendantValue getDV() {
		return DV;
	}

	public void setDV(DefendantValue dV) {
		DV = dV;
	}

	public DefendantAppellant getDA() {
		return DA;
	}

	public void setDA(DefendantAppellant dA) {
		DA = dA;
	}

	public XhibitApplicationController getXhibitApplicationController() {
		return xac;
	}

	public void setXhibitApplicationController(XhibitApplicationController xac) {
		this.xac = xac;
	}

	public void clearmodel() {
		setDV(null);
		setDA(null);
		setXhibitApplicationController(null);
	}
}
