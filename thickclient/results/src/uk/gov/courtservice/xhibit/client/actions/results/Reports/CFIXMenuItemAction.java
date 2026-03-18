package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.results.CFIX.RunCFIXReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: CFIXMenuItemAction
 * </p>
 * <p>
 * Description: The action which is called to run the CFIX report
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

public class CFIXMenuItemAction extends SynchXAction  {
	private static final long serialVersionUID = 1L;
	
	private RunCFIXReportDialog dialog = null;
	
	public CFIXMenuItemAction(){
		populateFromBundle("CFIXReport");
	}


	public void synchActionPerformed(ActionEvent e) throws Exception {
		 XhibitApplicationController xac = (XhibitApplicationController) getController();
		 dialog = new RunCFIXReportDialog(xac);
		
	}
	 public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
	    	if (dialog != null)
	            dialog.setVisible(true);
	
	 }

}
