package uk.gov.courtservice.xhibit.client.actions;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * This action is for registering actions in the resource bundle prior to the
 * actions actually existing.
 */

public class TempEventAction extends XAction {

    public TempEventAction() {
        setName("TempAction");
        setShortDescription("TempAction");
        setLongDescription("Please implement the action!!");
    }

    public void xActionPerformed(ActionEvent e) {
        uk.gov.courtservice.xhibit.client.util.XMessageBox.alert("This action has not been implemented yet");
    }
}