package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;


import uk.gov.courtservice.xhibit.client.results.OUTC.RunOUTCReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: OUTCMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the OUTC report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */

public class OUTCMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunOUTCReportDialog dialog = null;
	
	public OUTCMenuItemAction(){
		populateFromBundle("OUTCReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();        
	     dialog = new RunOUTCReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}