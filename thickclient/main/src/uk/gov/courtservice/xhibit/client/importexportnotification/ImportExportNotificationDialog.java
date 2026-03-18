package uk.gov.courtservice.xhibit.client.importexportnotification;

//awt
import java.awt.Frame;
import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: ImportExportNotificationDialog
 * </p>
 * <p>
 * Description: The dialog class for the notification of the import and export
 * statuses.
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
public class ImportExportNotificationDialog extends XDialog {

    private ImportExportNotificationModel _model;

    private ImportExportNotificationPanel _bodyPanel;

    private String _resources = XhibitBundles.ImportExportNotification;

    /**
     * Constructor that will set the model and the panel for Import/Export
     * notification and statuses.
     * 
     * @param frame
     *            Frame
     * @param model
     *            ImportExportNotificationModel
     * @throws CSRecoverableException
     */
    public ImportExportNotificationDialog(Frame frame, ImportExportNotificationModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(_resources, "lblTitleBarImportExportNotification"));
        _model = model;
        _bodyPanel = new ImportExportNotificationPanel(this, model);
        super.addBodyPanel(_bodyPanel);
        super.pack();
        super.setResizable(true);
    }

    /**
     * Method to process the event, just calls the super method with same
     * argument.
     * 
     * @param e
     *            WindowEvent
     */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
    }
}