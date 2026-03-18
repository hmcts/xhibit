package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.ADJSS.RunADJSSReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: ADJSSMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the ADJSS report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author David Burden
 * @version 1.0
 */

public class ADJSSMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunADJSSReportDialog dialog = null;
	
	public ADJSSMenuItemAction(){
		populateFromBundle("ADJSSReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();        
        dialog = new RunADJSSReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (dialog != null)
            dialog.setVisible(true);
    }
}

