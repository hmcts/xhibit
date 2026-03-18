package uk.gov.courtservice.xhibit.client.schedule.othercase;

import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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
 */

public class OpenOtherCaseDialog extends XDialog {
    OpenOtherCasePanel openOtherCasePanel = new OpenOtherCasePanel(this);

    public OpenOtherCaseDialog(java.awt.Frame frame) {
        super(frame, XHIBITConstant.getResource(XhibitBundles.TodaysSchedule, "OtherCaseTitle"), true,
                XDialog.OKCANCEL, XDialog.DEFAULTOK);
        addBodyPanel(openOtherCasePanel);
        pack();
    }

}