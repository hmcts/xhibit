package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.LFIX.RunLFIXReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: LFIXMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the JS report
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

public class LFIXMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunLFIXReportDialog dialog = null;
	
	public LFIXMenuItemAction(){
		populateFromBundle("LFIXReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();        
	     dialog = new RunLFIXReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}

