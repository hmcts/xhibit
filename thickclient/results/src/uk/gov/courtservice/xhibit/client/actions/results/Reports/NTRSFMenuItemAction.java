package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.NTRSF.NTRSFCaseMaintain;
import uk.gov.courtservice.xhibit.client.results.NTRSF.NTRSFCaseSearchModel;
import uk.gov.courtservice.xhibit.client.results.NTRSF.RunNTRSFReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: NTRSFMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the NTRSF report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */

public class NTRSFMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunNTRSFReportDialog dialog = null;
	
	public NTRSFMenuItemAction(){
		populateFromBundle("NTRSFReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();
		 
		 //Search for case
		 NTRSFCaseMaintain searchController = new NTRSFCaseMaintain(xac, NTRSFCaseSearchModel.ValidValues.CASE_TYPES);
	     if (searchController.getCaseId() > 0) {
	    	 RunNTRSFReportDialog(xac, searchController.getCaseId(), searchController.getCaseNumber(), searchController.getCaseTitle());
	     }
    }
    
    private void RunNTRSFReportDialog(XhibitApplicationController xac, Integer caseId, String caseNumber, String caseTitle) throws CSRecoverableException {
    	RunNTRSFReportDialog runNTRSFReportDialog = new RunNTRSFReportDialog(xac, caseId, caseNumber, caseTitle);
    	runNTRSFReportDialog.setVisible(true);
	}

	public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}

