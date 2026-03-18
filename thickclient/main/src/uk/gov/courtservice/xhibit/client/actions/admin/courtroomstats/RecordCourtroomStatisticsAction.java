package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;


import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class RecordCourtroomStatisticsAction extends SynchXAction {
	
    private static final long serialVersionUID = 1L;
    
      
    public RecordCourtroomStatisticsAction() {
        populateFromBundle("RecordCourtroomStatistics");
    }
    
    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();    
        RecordCourtroomStatisticsModel model = new RecordCourtroomStatisticsModel(xac);
        RecordCourtroomStatisticsDialog dialog = new RecordCourtroomStatisticsDialog(xac, model);
		dialog.setVisible(true);
     }
    
    public void stepValidate() {
    	
    }
    
    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
 
    }
   
}


