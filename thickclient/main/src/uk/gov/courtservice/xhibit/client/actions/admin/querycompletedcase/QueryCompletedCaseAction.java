
package uk.gov.courtservice.xhibit.client.actions.admin.querycompletedcase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.admin.querycompletedcase.CompletedCaseSearchDialog;
import uk.gov.courtservice.xhibit.client.admin.querycompletedcase.CompletedCaseSearchModel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class QueryCompletedCaseAction extends SynchXAction {
	
    private static final long serialVersionUID = 1L;
    
      
    public QueryCompletedCaseAction() {
        populateFromBundle("QueryCompletedCase");
    }
    
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();    
        CompletedCaseSearchModel caseSearchModel = new CompletedCaseSearchModel();
        CompletedCaseSearchDialog caseSearchDialog = new CompletedCaseSearchDialog(xac, caseSearchModel);
        caseSearchDialog.setLocationRelativeTo(xac);
		caseSearchDialog.setVisible(true);
     }
    
    public void stepValidate() {
    	
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
 
    }
   
}
