package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

public class ProsecutorRespondent extends RefProsecutorAgencyComplexValue {
	private static final long serialVersionUID = 1L;
	private RefSolicitorFirmComplexValue privateRepresentation;
	private Boolean isProsecutor;
	private String prosResp = "";
	
	
	public ProsecutorRespondent(Boolean isProsecutor) {
		//set whether object is a Prosecutor or Respondent
		this.isProsecutor = isProsecutor;
	}
	public ProsecutorRespondent(RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue, Boolean isProsecutor) {
		//set whether object is a Prosecutor or Respondent
		this.isProsecutor = isProsecutor;
		
		setTitle(refProsecutorAgencyComplexValue.getTitle());
		setInitials(refProsecutorAgencyComplexValue.getInitials());
		setProsecutorName1(refProsecutorAgencyComplexValue.getProsecutorName1());
		setProsecutorName2(refProsecutorAgencyComplexValue.getProsecutorName2());
		setProsecutorName3(refProsecutorAgencyComplexValue.getProsecutorName3());
		setFullName();
		setCpsCode(refProsecutorAgencyComplexValue.getCpsCode());
    	setAddress(refProsecutorAgencyComplexValue.getAddress());
    	setTelephoneNumber(refProsecutorAgencyComplexValue.getTelephoneNumber());
    	setFaxNumber(refProsecutorAgencyComplexValue.getFaxNumber());
    	setNonsecureEmailAddress(refProsecutorAgencyComplexValue.getNonsecureEmailAddress());
    	setSecureEmailAddress(refProsecutorAgencyComplexValue.getSecureEmailAddress());
    	setDxRef(refProsecutorAgencyComplexValue.getDxRef());
    	setRefProsecutorAgencyId(refProsecutorAgencyComplexValue.getRefProsecutorAgencyId());
	}
	
	public void setPrivateRepresentation(RefSolicitorFirmComplexValue privateRepresentation) {
		this.privateRepresentation = privateRepresentation;
	}
	public RefSolicitorFirmComplexValue getPrivateRepresentation() {
		return privateRepresentation;
	}
	public Boolean getIsProsecutor() {
		return isProsecutor;
	}
	public void setIsProsecutor(Boolean isProsecutor) {
		this.isProsecutor = isProsecutor;
	}
	public String getProsResp() {
		return prosResp;
	}
	public void setProsResp(String prosResp) {
		this.prosResp = prosResp;
	}
}
