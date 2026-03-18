package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class EditCourtLogEventAction extends XAction {
    public EditCourtLogEventAction() {
        populateFromBundle("clEditCourtLog");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        CourtLogViewValue clv = (CourtLogViewValue) getModel();
        
        // ctx-3838, disable ability to edit/delete a bench warrant court log event
        if (clv.isBWHistoryIssueWarrantEvent() || clv.isBWHistoryEndWarrantEvent()) {
        	JOptionPane.showMessageDialog((JFrame) this.getController(), "Can't edit Bench Warrant court log events.", 
        			"Edit Court Log Event", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }

        //ctx-4621
        if (clv.isCrackedIneffectiveEvent()) {
        	JOptionPane.showMessageDialog((JFrame) this.getController(), "Can't edit Cracked or Ineffective court log events.", 
        			"Edit Court Log Event", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        // Get the action for the event type to be editted.
        XAction xa = XhibitActions.getCourtLogAction(xac, clv.getEventType().toString());

        // Create a new instance of it so that the original is not affected.
        XAction xaClone = (XAction) xa.getClass().newInstance();
        // In the new instance, set the controller
        xaClone.setController(xa.getController());
        // Get the model from the original action
        FreeTextModel fm = (FreeTextModel) ((FreeTextModel) xa.getModel()).clone();
        // set the event id to be modified
        fm.setEventId(clv.getLogEntryId());
        // Set the inEditMode boolean
        fm.setInEditMode(true);
        // Set the model in the new instance.
        xaClone.setModel(fm);
        // fire the event
        xaClone.actionPerformed(e);
    }
}
