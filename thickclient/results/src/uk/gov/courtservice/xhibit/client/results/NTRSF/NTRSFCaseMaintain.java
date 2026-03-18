package uk.gov.courtservice.xhibit.client.results.NTRSF;

import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class NTRSFCaseMaintain {
	private final Logger log = CSServices.getLogger(getClass());
	private Integer caseId;
	private String caseNumber;
	private String caseTitle;

	public NTRSFCaseMaintain(XhibitApplicationController xac) throws CSRecoverableException {
		this(xac, null);
	}
	
	public NTRSFCaseMaintain(XhibitApplicationController xac, List<String> validCaseTypes) throws CSRecoverableException {
		log.debug("CaseMaintain:");
		NTRSFCaseSearchModel caseSearchModel = new NTRSFCaseSearchModel();
		caseSearchModel.setValidCaseTypes(validCaseTypes);
		NTRSFCaseSearchDialog caseSearchDialog = new NTRSFCaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
		caseId = caseSearchModel.getCaseId();
		caseNumber = caseSearchModel.getCaseType()+caseSearchModel.getCaseNumber();
		caseTitle = caseSearchModel.getCaseTitle();
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
}


