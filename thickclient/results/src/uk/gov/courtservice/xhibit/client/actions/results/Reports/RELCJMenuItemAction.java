package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;


public class RELCJMenuItemAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	
	public RELCJMenuItemAction(){
		populateFromBundle("RELCJReport");
	}
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		XhibitApplicationController xac = (XhibitApplicationController) getController();  
		 Frame parentFrame = (Frame) getController();
		 DisplayRELCJReportAction action = new DisplayRELCJReportAction(parentFrame);	    
		 action.actionPerformed(new ActionEvent(xac, 0, "Call DisplayRELCJReportAction"));
   }
}
