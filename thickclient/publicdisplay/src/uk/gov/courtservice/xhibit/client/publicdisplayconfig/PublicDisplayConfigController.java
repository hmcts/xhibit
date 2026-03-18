package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Entry point for launching the Public Display Configurator
 * </p>
 * <p>
 * Description: Main Controller to initiate the configuration panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PublicDisplayConfigController.java,v 1.5 2004/01/28 16:47:59
 *          sz0t7n Exp $
 */
public class PublicDisplayConfigController extends XDialog {
    private static final String CLOSE_BUTTON = "btnClose";

    private static final String TITLE = "pd.title.configuration";

    private static PublicDisplayConfigController controller = null;

    /**
     * Create a modal dialog. Default on the cancel button. Hide the OK button
     * and disable it to be sure. Rename the Cancel button to Close Add the
     * ConfigurationPanel
     * 
     * @param parent
     * @throws CSRecoverableException
     */
    private PublicDisplayConfigController(Frame parent) throws CSRecoverableException {
        super(parent, PublicDisplayUtils.getResource(TITLE), true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);

        final String buttonText = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                CLOSE_BUTTON);

        // Disable & Hide the OK button
        ((OkCancelPanel) this.getButtonPanel()).okButton.setEnabled(false);
        ((OkCancelPanel) this.getButtonPanel()).okButton.setVisible(false);
        // Set the cancel button text to be Close
        ((OkCancelPanel) this.getButtonPanel()).getCancelAction().setName(buttonText);

        this.addBodyPanel(new ConfigurationPanel());
        pack();
    }

    /**
     * Launch an instance of the configurator
     * 
     * @param parent
     *            The Frame that requested the configurator
     * @throws CSRecoverableException
     */
    public static synchronized void showConfigurationController(Frame parent) throws CSRecoverableException {
        if (controller == null) {
            controller = new PublicDisplayConfigController(parent);
        }
        controller.setVisible(true);
    }

    /**
     * Override dispose to null out the instance of the configurator.
     */
    public void dispose() {
        controller = null;
        super.dispose();
    }

}