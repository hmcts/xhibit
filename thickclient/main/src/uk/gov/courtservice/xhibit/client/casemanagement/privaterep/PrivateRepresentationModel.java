package uk.gov.courtservice.xhibit.client.casemanagement.privaterep;


import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.util.XPanel;

public class PrivateRepresentationModel {

	private Integer defendantOnCaseId;
	private String repType;
	private XPanel callingClass;
	private CaseType caseType;

	DefOnCaseRefSolFirmValue docRefSolFirm;
	ProsecutorRefSolFirmValue prosRefSolFirm;
	RefSolicitorFirmComplexValue refSolFirm;

	public PrivateRepresentationModel() {
		clearmodel();
	}

	public PrivateRepresentationModel(final Integer defendantOnCaseId, XPanel callingClass) {
		this.defendantOnCaseId = defendantOnCaseId;
		this.callingClass = callingClass;
		clearmodel();
	}
	
	public PrivateRepresentationModel(final Integer defendantOnCaseId, XPanel callingClass, CaseType caseType) {
		this.defendantOnCaseId = defendantOnCaseId;
		this.callingClass = callingClass;
		this.caseType = caseType;
		clearmodel();
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}

	public void clearmodel() {

		setDocRefSolFirm(null);
		setProsRefSolFirm(null);
		setRefSolicitorFirm(null);
	}


	public DefOnCaseRefSolFirmValue getDocRefSolFirm() {
		return docRefSolFirm;
	}

	public void setDocRefSolFirm(DefOnCaseRefSolFirmValue doc) {
		docRefSolFirm = doc;
	}

	public ProsecutorRefSolFirmValue getProsRefSolFirm() {
		return prosRefSolFirm;
	}

	public void setProsRefSolFirm(ProsecutorRefSolFirmValue pros) {
		prosRefSolFirm = pros;
	}

	public RefSolicitorFirmComplexValue getRefSolFirm() {
		return refSolFirm;
	}

	public void setRefSolicitorFirm(RefSolicitorFirmComplexValue refSolValue) {
		refSolFirm = refSolValue;
	}

	public String getRepType() {
		return repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
	}

	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}
}
