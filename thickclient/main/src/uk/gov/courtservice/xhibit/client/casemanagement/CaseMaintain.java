package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class CaseMaintain {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private Integer caseId;

	public CaseMaintain(XhibitApplicationController xac) throws CSRecoverableException {
		this(xac, null);
	}
	
	public CaseMaintain(XhibitApplicationController xac, List<String> validCaseTypes) throws CSRecoverableException {
		log.debug("CaseMaintain:");
		setCaseId(0);
		if (xac.getCaseStatus().getCaseCreateInProgressFlag() || ((XhibitApplicationControllerImpl)xac).isCaseOpened()) {
			//--- Case creation in progress ---
			if (!xac.getCaseStatus().getCaseCreateInProgressFlag()) { 	// Accessed from outside of Case Create/Amend
				xac.close();
			} else {
				// only create the custom pop-up if accessed inside of CC/CA 
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?", "Case Management",
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not create new update case");
					return;
				} else if (result == JOptionPane.YES_OPTION) {
					log.debug("Case cancelled, unsetting case create in progress flag");
					xac.getCaseStatus().setCaseCreateInProgressFlag(false);
					xac.getCaseStatus().setCaseId(0);
					xac.close();
				}
				// else continue to search	
			}
		}
		CaseSearchModel caseSearchModel = new CaseSearchModel();
		caseSearchModel.setValidCaseTypes(validCaseTypes);
		CaseSearchDialog caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
		caseId = caseSearchModel.getCaseId();
	}

	// Added for CTX-210 to allow extra criteria within the search
	public CaseMaintain(XhibitApplicationController xac, boolean overload) throws CSRecoverableException {
		setCaseId(0);
		if (xac.getCaseStatus().getCaseCreateInProgressFlag() || ((XhibitApplicationControllerImpl) xac).isCaseOpened()) {
			if(!xac.getCaseStatus().getCaseCreateInProgressFlag()) 	// Accessed from outside of Case Create/Amend
				xac.close();
			else {
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?", "Case Management",
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					return;
				}
			}
		}
		CaseSearchModel caseSearchModel = new CaseSearchModel();
		caseSearchModel.setTransferCase(true);
		CaseSearchDialog caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
		caseId = caseSearchModel.getCaseId();
		
		try {
			xac.getCaseStatus().setCaseCreateInProgressFlag(false);
			xac.getCaseStatus().setCaseId(0);			
			xac.close();
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}	
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public Integer getCaseId() {
		return caseId;
	}

}
