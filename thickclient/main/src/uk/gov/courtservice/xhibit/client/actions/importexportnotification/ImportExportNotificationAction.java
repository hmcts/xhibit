package uk.gov.courtservice.xhibit.client.actions.importexportnotification;

//awt
import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.importexportnotification.ImportExportNotificationDialog;
import uk.gov.courtservice.xhibit.client.importexportnotification.ImportExportNotificationModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ImportExportNotificationAction
 * </p>
 * <p>
 * Description: This is the Action class for the notification of the import and
 * export statuses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class ImportExportNotificationAction extends XAction {
    // private ImportExportNotificationModel _model;

    /**
     * Default constructor that reads in the ImportExportNotification from the
     * bundle.
     */
    public ImportExportNotificationAction() {
        populateFromBundle("ImportExportNotification");
    }

    /**
     * This will perform the action, create a model for the
     * ImportExportNotification and set the application controller.
     * 
     * @param e
     *            ActionEvent
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        ImportExportNotificationModel model = new ImportExportNotificationModel();
        model.setXac((XhibitApplicationController) getController());
        ImportExportNotificationDialog myDialog = new ImportExportNotificationDialog((Frame) getController(), model);

        myDialog.setVisible(true);

        if (myDialog.isCancelClicked()) {
            throw new UserCancelException();
        }
    }
}