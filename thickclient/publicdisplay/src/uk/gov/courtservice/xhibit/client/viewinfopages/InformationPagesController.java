package uk.gov.courtservice.xhibit.client.viewinfopages;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: InformationPagesController.java,v 1.2 2005/08/02 13:46:19
 *          szfnvt Exp $
 */

public class InformationPagesController extends XDialog {
    private static final String CLOSE_BUTTON = "btnClose";

    private static final String SHOW_BUTTON = "btnShow";

    private static final String TITLE = "pd.vip.title.infopages";

    private static InformationPagesController controller = null;

    /**
     * Create a modal dialog. Default on the cancel button. Hide the OK button
     * and disable it to be sure. Rename the Cancel button to Close Add the
     * ConfigurationPanel
     * 
     * @param parent
     * @throws CSRecoverableException
     */
    private InformationPagesController(java.awt.Frame parent) throws CSRecoverableException {
        super(parent, PublicDisplayUtils.getResource(TITLE), true, XDialog.APPLYOKCANCEL, XDialog.DEFAULTAPPLY);

        final String closeButtonText = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                CLOSE_BUTTON);
        final String showButtonText = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,
                SHOW_BUTTON);

        // Disable & Hide the OK button
        ((ApplyOkCancelPanel) this.getButtonPanel()).okButton.setEnabled(false);
        ((ApplyOkCancelPanel) this.getButtonPanel()).okButton.setVisible(false);
        // Set the cancel button text to be Close
        ((ApplyOkCancelPanel) this.getButtonPanel()).getCancelAction().setName(closeButtonText);
        ((ApplyOkCancelPanel) this.getButtonPanel()).cancelButton.setToolTipText(closeButtonText);

        // Set the apply button to be Show
        ((ApplyOkCancelPanel) this.getButtonPanel()).applyButton.setText(showButtonText);
        ((ApplyOkCancelPanel) this.getButtonPanel()).applyButton.setToolTipText(showButtonText);
        ((ApplyOkCancelPanel) this.getButtonPanel()).applyButton.setMnemonic(showButtonText.charAt(0));

        this.addBodyPanel(new InformationPagesPanel((ApplyOkCancelPanel) this.getButtonPanel()));
        pack();
        this.setResizable(false);
    }

    /**
     * Launch an instance of the configurator
     * 
     * @param parent
     *            The Frame that requested the configurator
     * @throws CSRecoverableException
     */
    public static synchronized void showInformationPages(java.awt.Frame parent) throws CSRecoverableException {
        if (controller == null) {
            controller = new InformationPagesController(parent);
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