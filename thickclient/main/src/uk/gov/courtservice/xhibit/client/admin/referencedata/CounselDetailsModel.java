package uk.gov.courtservice.xhibit.client.admin.referencedata;

import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Counsel Details screen.
 * @author toftn
 *
 */
public class CounselDetailsModel {
	
	private XPanel callingClass;
	
	private RefAdvocateComplexValue refAdvocateComplexValue;
	private RefLegalRepresentativeBasicValue refLegalRepresentativeBasicValue;
	private Boolean readOnly;

	public CounselDetailsModel() {
		clearmodel();
	}
	
	public CounselDetailsModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}
	
	public CounselDetailsModel(XPanel callingClass, RefAdvocateComplexValue val, Boolean readOnly) {
		setCallingClass(callingClass);
		setRefAdvocateComplexValue(val);
		setReadOnly(readOnly);
	}
	
	public CounselDetailsModel(XPanel callingClass, RefAdvocateComplexValue AdvocateVal,
			RefLegalRepresentativeBasicValue legalRepVal, Boolean readOnly) {
		setCallingClass(callingClass);
		setRefAdvocateComplexValue(AdvocateVal);
		setRefLegalRepresentativeBasicValue(legalRepVal);
		setReadOnly(readOnly);
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public RefAdvocateComplexValue getRefAdvocateComplexValue() {
		return refAdvocateComplexValue;
	}

	public void setRefAdvocateComplexValue(RefAdvocateComplexValue refAdvocateComplexValue) {
		this.refAdvocateComplexValue = refAdvocateComplexValue;
	}
	
	public RefLegalRepresentativeBasicValue getRefLegalRepresentativeBasicValue() {
		return refLegalRepresentativeBasicValue;
	}

	public void setRefLegalRepresentativeBasicValue(RefLegalRepresentativeBasicValue refLegalRepresentativeBasicValue) {
		this.refLegalRepresentativeBasicValue = refLegalRepresentativeBasicValue;
	}
	
	public Boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void clearmodel() {
		setRefAdvocateComplexValue(null);
		setRefLegalRepresentativeBasicValue(null);
		setReadOnly(null);
    }	
}
