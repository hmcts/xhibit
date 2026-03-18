package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.RRCA.RunRRACReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: RRACMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the RAGE report
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

public class RRCAMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunRRACReportDialog dialog = null;
	
	public RRCAMenuItemAction(){
		populateFromBundle("RRCAReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();        
	     dialog = new RunRRACReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
    	if (dialog != null)
            dialog.setVisible(true);
    }
}

