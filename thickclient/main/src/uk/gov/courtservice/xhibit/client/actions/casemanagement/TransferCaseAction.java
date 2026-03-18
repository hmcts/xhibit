package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchModel;
import uk.gov.courtservice.xhibit.client.casemanagement.TransferCaseDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.TransferCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class TransferCaseAction extends XAction {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private TransferCaseModel transferCaseModel;
    private TransferCaseDialog transferCaseDialog;
    private CaseSearchModel caseSearchModel;
    private CaseSearchDialog caseSearchDialog;
    private XhibitApplicationController xac; 
    private Integer caseId;
    
	public TransferCaseAction() {
        populateFromBundle("TransferCase");
	}
	
	private boolean closeOpenCase() throws CSRecoverableException {
		if (xac.getCaseStatus().getCaseCreateInProgressFlag() || ((XhibitApplicationControllerImpl)xac).isCaseOpened()) {
			//--- Case creation in progress ---
			if (!xac.getCaseStatus().getCaseCreateInProgressFlag()) { 	//Accessed from outside of Case Create/Amend
				xac.close();
			} else {
				//--- Only create the custom pop-up if accessed inside of CC/CA --- 
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?", "Case Transfer",
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not create new update case");
					return false;
				} else if (result == JOptionPane.YES_OPTION) {
					log.debug("Case cancelled, unsetting case create in progress flag");
					xac.getCaseStatus().setCaseCreateInProgressFlag(false);
					xac.getCaseStatus().setCaseId(0);
					xac.close();
				}
				// else continue to search	
			}
		}
		return true;
	}

	
	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController)getController();
        //--- If a case is currently open, close it or we're done, then search for case ---
		if (closeOpenCase()) {
			caseSearchModel = new CaseSearchModel();
			caseSearchModel.setTransferCase(true);
			caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
			caseSearchDialog.setVisible(true);
			caseId = caseSearchModel.getCaseId();
	        //--- If we've got a valid case number, transfer this case ---
	        if (caseId > 0) {
	        	transferCaseModel = new TransferCaseModel(caseId);
	        	transferCaseDialog = new TransferCaseDialog(xac, transferCaseModel);
	        	transferCaseDialog.setVisible(true);
	        }
		}
	}

}
