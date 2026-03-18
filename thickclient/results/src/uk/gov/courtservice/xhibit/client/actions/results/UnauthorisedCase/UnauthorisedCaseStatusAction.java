package uk.gov.courtservice.xhibit.client.actions.results.UnauthorisedCase;

import java.awt.event.ActionEvent;
import uk.gov.courtservice.xhibit.client.results.UnauthorisedCase.UnauthorisedCaseStatusDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: UnauthorisedCaseStatusAction
 * </p>
 * <p>
 * Description: The Action which creates the dialog to display Unauthorised Cases
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
public class UnauthorisedCaseStatusAction extends SynchXAction {
    private static final long serialVersionUID = 1L;
    
    private UnauthorisedCaseStatusDialog dialog = null;
    
    public UnauthorisedCaseStatusAction() {
        populateFromBundle("UnauthorisedCaseStatus");
    }
    
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();        
        dialog = new UnauthorisedCaseStatusDialog(xac);
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (dialog != null)
            dialog.setVisible(true);
    }
}
