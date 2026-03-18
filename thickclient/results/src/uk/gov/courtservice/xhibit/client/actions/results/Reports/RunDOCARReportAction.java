package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;
import uk.gov.courtservice.xhibit.client.results.DOCAR.RunDOCARReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
/**
 * <p>
 * Title: RunDOCARReportAction
 * </p>
 * <p>
 * Description: The action which is called to run the DOCAR report
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

public class RunDOCARReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private RunDOCARReportDialog dialog = null;
	
	public RunDOCARReportAction(){
		populateFromBundle("DOCARReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();        
        dialog = new RunDOCARReportDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (dialog != null)
            dialog.setVisible(true);
    }
}

