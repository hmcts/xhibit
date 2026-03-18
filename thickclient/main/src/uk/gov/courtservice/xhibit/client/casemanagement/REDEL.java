package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class REDEL {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private Integer caseId;

	public REDEL(XhibitApplicationController xac) throws CSRecoverableException {
		log.debug("REDEL:");
		setCaseId(0);
		if (xac.getCaseStatus().getCaseCreateInProgressFlag() == true
				|| ((XhibitApplicationControllerImpl) xac).isCaseOpened()) {
			log.debug("Already a case creation in progress");

			if (!xac.getCaseStatus().getCaseCreateInProgressFlag()) // Accessed
																	// from
																	// outside
																	// of Case
																	// Create/Amend
				xac.close();
			else {
				// only create the custom pop-up if accessed inside of CC/CA
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?",
						"Replace/Delete", JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not create new update case");
					return;
				} // else continue to search
			}
		}
		try {
			xac.getCaseStatus().setCaseCreateInProgressFlag(false);
			xac.getCaseStatus().setCaseId(0);
			xac.close();
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}
		CaseSearchModel caseSearchModel = new CaseSearchModel();
		caseSearchModel.setREDEL(true);
		CaseSearchDialog caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setLocationRelativeTo(xac);
		caseSearchDialog.setVisible(true);
		caseId = caseSearchModel.getCaseId();
	}

	// Added for CTX-210 to allow extra criteria within the search
	public REDEL(XhibitApplicationController xac, boolean overload) throws CSRecoverableException {
		setCaseId(0);
		if (xac.getCaseStatus().getCaseCreateInProgressFlag()
				|| ((XhibitApplicationControllerImpl) xac).isCaseOpened()) {
			if (!xac.getCaseStatus().getCaseCreateInProgressFlag()) // Accessed
																	// from
																	// outside
																	// of Case
																	// Create/Amend
				xac.close();
			else {
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?",
						"Replace/Delete", JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					return;
				}
			}
		}
		try {
			xac.getCaseStatus().setCaseCreateInProgressFlag(false);
			xac.getCaseStatus().setCaseId(0);
			xac.close();
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}
		CaseSearchModel caseSearchModel = new CaseSearchModel();
		caseSearchModel.setREDEL(true);
		CaseSearchDialog caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
		caseId = caseSearchModel.getCaseId();
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getCaseId() {
		return caseId;
	}

}
