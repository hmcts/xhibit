package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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
 * @author Stephen Tully
 * @version 1.0
 */
public class OpenOtherDaysLogDialog extends XDialog {
    public OpenOtherDaysLogDialog(Frame frame, OpenOtherDaysLogModel model) throws CSRecoverableException {
        super(frame, "", true);
        setTitle(XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.OpenOtherDaysLog),
                "titleBarLabel"));

        addBodyPanel(new OpenOtherDaysLogPanel(this, model));
        setResizable(false);
        pack();
    }
}
