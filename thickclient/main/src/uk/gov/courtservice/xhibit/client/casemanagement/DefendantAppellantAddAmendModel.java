package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.casemanagement.DefendantAppellant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Defendant/Appellant Add/Amend screen.
 * @author toftn
 *
 */
public class DefendantAppellantAddAmendModel {
		
	private DefendantValue dV;
	private DefendantAppellant dA;
	private AddressValue address;
	private AddressBasicValue addBV;
	private DefendantReferenceBasicValue df1;
	private DefendantReferenceBasicValue df2;
	private DefendantReferenceBasicValue defRefLicenceType;
	private DefendantReferenceBasicValue defRefLicenceIssueNumber;

	public DefendantReferenceBasicValue getDefRefLicenceType() {
		return defRefLicenceType;
	}

	public void setDefRefLicenceType(DefendantReferenceBasicValue defRefLicenceType) {
		this.defRefLicenceType = defRefLicenceType;
	}

	public DefendantReferenceBasicValue getDefRefLicenceIssueNumber() {
		return defRefLicenceIssueNumber;
	}

	public void setDefRefLicenceIssueNumber(DefendantReferenceBasicValue defRefLicenceIssueNumber) {
		this.defRefLicenceIssueNumber = defRefLicenceIssueNumber;
	}

	private XPanel callingClass;
	
	public DefendantAppellantAddAmendModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}
	
	public DefendantAppellantAddAmendModel(Integer defendantId, Integer positionInTable, XPanel callingClass) {
		setDA(new DefendantAppellant());
		getDA().setDefendantId(defendantId);
		getDA().setPositionInTable(positionInTable);
		this.callingClass = callingClass;
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}

	public DefendantValue getDV() {
		return dV;
	}

	public void setDV(DefendantValue dV) {
		this.dV = dV;
	}

	public DefendantAppellant getDA() {
		return dA;
	}

	public void setDA(DefendantAppellant dA) {
		this.dA = dA;
	}

	public AddressValue getAddress() {
		return address;
	}

	public void setAddress(AddressValue address) {
		this.address = address;
	}

	public AddressBasicValue getAddBV() {
		return addBV;
	}

	public void setAddBV(AddressBasicValue addBV) {
		this.addBV = addBV;
	}

	public DefendantReferenceBasicValue getDf1() {
		return df1;
	}

	public void setDf1(DefendantReferenceBasicValue df1) {
		this.df1 = df1;
	}

	public DefendantReferenceBasicValue getDf2() {
		return df2;
	}

	public void setDf2(DefendantReferenceBasicValue df2) {
		this.df2 = df2;
	}

	public void clearmodel() {
		setDV(null);
		setDA(null);
		setAddress(null);
		setAddBV(null);
		setDf1(null);
		setDf2(null);
		setDefRefLicenceType(null);
		setDefRefLicenceIssueNumber(null);
    }
}
