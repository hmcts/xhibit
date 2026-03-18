package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the public representation screen.
 * @author waltersn
 *
 */
public class PublicRepresentationModel {

	private Integer id;
	private XPanel callingClass;
	private CaseType caseType;

	
	//defOnCase and legalAid got /saved to db
	DefOnCaseRefSolFirmValue defOnCaseFirm;
	ProsecutorRefSolFirmValue prosFirm;
	LegalAidOrderBasicValue legalAidOrderValue;
	
	
	//amendment
	private Integer orderAmendmentCode;
	private Date dateOfAmendment;
	
	//revocation
	private String reasonForRevocation;
	private Date dateOfRevocation;	

	public PublicRepresentationModel() {
		clearmodel();
	}
	
	public PublicRepresentationModel(final Integer id, XPanel callingClass) {
		this.id = id;
		this.callingClass = callingClass;
		clearmodel();
	}
	
	public PublicRepresentationModel(final Integer id, XPanel callingClass, CaseType caseType) {
		this.id = id;
		this.callingClass = callingClass;
		this.caseType = caseType;
		clearmodel();
	}

	public Integer getOrderAmendmentCode() {
		return orderAmendmentCode;
	}

	public void setOrderAmendmentCode(Integer orderAmendmentCode) {
		this.orderAmendmentCode = orderAmendmentCode;
	}

	public Date getDateOfAmendment() {
		return dateOfAmendment;
	}

	public void setDateOfAmendment(Date dateOfAmendment) {
		this.dateOfAmendment = dateOfAmendment;
	}

	public String getReasonForRevocation() {
		return reasonForRevocation;
	}

	public void setReasonForRevocation(String reasonForRevocation) {
		this.reasonForRevocation = reasonForRevocation;
	}

	public Date getDateOfRevocation() {
		return dateOfRevocation;
	}

	public void setDateOfRevocation(Date dateOfRevocation) {
		this.dateOfRevocation = dateOfRevocation;
	}

	public DefOnCaseRefSolFirmValue getDefOnCaseFirm() {
		return defOnCaseFirm;
	}

	public void setDefOnCaseFirm(DefOnCaseRefSolFirmValue defOnCaseFirm) {
		this.defOnCaseFirm = defOnCaseFirm;
	}
	
	public ProsecutorRefSolFirmValue getProsFirm() {
		return prosFirm;
	}

	public void setProsFirm(ProsecutorRefSolFirmValue prosFirm) {
		this.prosFirm = prosFirm;
	}


	public Integer getId() {
		return id;
	}

	public LegalAidOrderBasicValue getLegalAidOrderValue() {
		return legalAidOrderValue;
	}

	public void setLegalAidOrderValue(LegalAidOrderBasicValue legalAidOrderValue) {
		this.legalAidOrderValue = legalAidOrderValue;
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public void clearmodel() {
		setDateOfRevocation(null);
		setReasonForRevocation(null);
		setDateOfAmendment(null);
		setOrderAmendmentCode(null);
		setDefOnCaseFirm(null);
		setLegalAidOrderValue(null);
		setProsFirm(null);
    }

	
	
}
