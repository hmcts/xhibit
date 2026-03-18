package uk.gov.courtservice.xhibit.client.admin.referencedata;

import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Chamber Details screen.
 * @author toftn
 *
 */
public class ChamberDetailsModel {
	
	private XPanel callingClass;
	
	private RefChamberComplexValue refChamberComplexValue;
	
	private Boolean readOnly;

	public ChamberDetailsModel() {
		clearmodel();
	}
	
	public ChamberDetailsModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}
	
	public ChamberDetailsModel(XPanel callingClass, RefChamberComplexValue val, Boolean readOnly) {
		setCallingClass(callingClass);
		setRefChamberComplexValue(val);
		setReadOnly(readOnly);
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public RefChamberComplexValue getRefChamberComplexValue() {
		return refChamberComplexValue;
	}

	public void setRefChamberComplexValue(RefChamberComplexValue refChamberComplexValue) {
		this.refChamberComplexValue = refChamberComplexValue;
	}
	
	public Boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void clearmodel() {
		setRefChamberComplexValue(null);
		setReadOnly(null);
    }	
}
