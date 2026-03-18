package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.results.INFTRPC.RunINFTRPCReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: INFTRPCMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the INFTRP/C report
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

public class INFTRPCMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunINFTRPCReportDialog dialog = null;
	
	public INFTRPCMenuItemAction(){
		populateFromBundle("INFTRPCReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();        
	     dialog = new RunINFTRPCReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}