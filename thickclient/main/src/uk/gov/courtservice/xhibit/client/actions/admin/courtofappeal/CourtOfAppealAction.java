
package uk.gov.courtservice.xhibit.client.actions.admin.courtofappeal;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchModel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CourtOfAppealAction extends SynchXAction {
	
    private static final long serialVersionUID = 1L;
    
      
    public CourtOfAppealAction() {
        populateFromBundle("CourtOfAppeal");
    }
    
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        
		// call close before opening court of appeal
		xac.close();
        
        CaseSearchModel caseSearchModel = new CaseSearchModel();
        caseSearchModel.setAppealCourt(true);
        CaseSearchDialog caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
     }
    
    public void stepValidate() {
    	
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
 
    }
   
}
