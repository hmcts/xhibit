package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Frame;

import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Splash panel to show version and copyrigth<br>
 * Also setStatus(String s) will display loading messages
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class XhibitAboutDialog extends XDialog {
    private XhibitSplashPanel sp;

    public XhibitAboutDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal, XDialog.APPLYOKCANCEL, XDialog.CANCEL);
        sp = new XhibitSplashPanel(this);

        this.addBodyPanel(sp);
        ((ApplyOkCancelPanel) this.getButtonPanel()).okButton.setVisible(false);
        ((ApplyOkCancelPanel) this.getButtonPanel()).getCancelAction().setName(
                ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, "btnClose"));
        pack();
        setResizable(false);
        setVisible(true);
    }

    public void setStatus(String message) {
        sp.setStatus(message);
    }
}
