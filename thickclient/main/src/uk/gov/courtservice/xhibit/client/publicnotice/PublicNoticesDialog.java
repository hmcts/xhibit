package uk.gov.courtservice.xhibit.client.publicnotice;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Dialog contains PublicNoticesPanel which displays all public notices
 * </p>
 * <p>
 * Description: Clicking ok on the Dialog saves activation settings
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 * 
 */

public class PublicNoticesDialog extends XDialog {
    /**
     * PublicNoticesPanel bodyPanel
     */
    private PublicNoticesPanel bodyPanel;

    /**
     * <init>
     * 
     * @param frame
     *            parameter for <init>
     * @throws CSRecoverableException -
     */
    public PublicNoticesDialog(Frame frame) throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(XhibitBundles.PublicNotices, "titleBarLabel"));

        this.bodyPanel = new PublicNoticesPanel();
        super.addBodyPanel(bodyPanel);
        super.setResizable(false);
        super.pack();
    }
}