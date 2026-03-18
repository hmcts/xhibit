/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 20, 2003
 * Time: 9:48:37 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.client.skeletonschedule.NotesDialog;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class EditNotesAction extends XAction {
    private static final Logger log = CSServices.getLogger(ViewNextWeekAction.class);

    public EditNotesAction() {
        log.debug("EditNotesAction()");

        populateFromBundle("EditNotes");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        XhibitApplicationController xac = (XhibitApplicationController) getController();

        NotesDialog notesDialog = new NotesDialog(xac, NotesDialog.EDIT);
        SkeletonSchedulePanel skeletonSchedulePanel = ((SkeletonSchedulePanel) getModel());
        Integer witnessId = skeletonSchedulePanel.getCurrentWitness();

        try {
            notesDialog.init(witnessId);
            skeletonSchedulePanel.getNotesButton().setEnabled(false);
            skeletonSchedulePanel.refreshWitnessPanel();
        } catch (WitnessNotFoundException ex) {
            // todo: nice dialog or something here would be good.
            log.error(ex);

        }
    }
}