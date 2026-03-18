package uk.gov.courtservice.xhibit.client.actions.results.Reports;



import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.UNLC.RunUNLCReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: UNLCMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the UNLC report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */

public class UNLCMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunUNLCReportDialog dialog = null;
	
	public UNLCMenuItemAction(){
		populateFromBundle("UNLCReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();        
	     dialog = new RunUNLCReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}