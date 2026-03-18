package uk.gov.courtservice.xhibit.client.admin.referencedata;

import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Chamber/Advocate Details screen.
 * @author toftn
 *
 */
public class ChamberAndAdvocateDetailsModel {
	
	private XPanel callingClass;
	private Boolean readOnly;
	
	public ChamberAndAdvocateDetailsModel() {
		clearmodel();
	}
	
	public ChamberAndAdvocateDetailsModel(XPanel callingClass) {
		clearmodel();
		setCallingClass(callingClass);		
	}
	
	public ChamberAndAdvocateDetailsModel(Boolean readOnly) {
		clearmodel();
		setReadOnly(readOnly);
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public Boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public void clearmodel() {
		setCallingClass(null);
		setReadOnly(null);
    }	
}
