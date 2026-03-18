package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: UnauthorisedCaseStatusReportAction
 * </p>
 * <p>
 * Description: The action which is called to print a report of the 
 * Unauthorised Cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */
public class UnauthorisedCaseStatusReportAction extends XAction{
    private static final long serialVersionUID = 1L;
    
    private UnauthorisedCaseStatusPanel parent;
    
    
    public UnauthorisedCaseStatusReportAction(UnauthorisedCaseStatusPanel parent){
        populateFromBundle("ucsReport");
        this.parent = parent;
                       
    }
    
    public void xActionPerformed(ActionEvent e) throws Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        //init();
        //Create and display the Unauthorised Cases Dialog
        UnauthorisedCaseStatusReportDialog dlg = new UnauthorisedCaseStatusReportDialog(xac,parent);
        dlg.setVisible(true);        
    }       
}
