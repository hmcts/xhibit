package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.util.Locale;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * @author Bal Bhamra
 * @version 1.0
 */
public class MoveCaseDialog extends XDialog {
    private ResourceBundle myResource;

    private ResourceBundle utilResource;

    private MoveCaseModel moveCaseModel;

    /** todo@ TEMP constructor remove after TestMoveCase amended */
    public MoveCaseDialog(java.awt.Frame frame, Integer acm) throws CSRecoverableException {
        super(frame, "", true);
    }

    /**
     * @todo This constructor needs to take in the case selected and court
     *       How???
     */
    public MoveCaseDialog(java.awt.Frame frame, ScheduledHearingValue shv) throws CSRecoverableException {
        super(frame, "", true);

        try {
            myResource = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
            utilResource = XHIBITConstant.getResourceBundle("XHIBITUtilResources");
        } catch (java.util.MissingResourceException ex) {
            XHIBITConstant.error("ResourceBundle could not be found for " + Locale.getDefault());
            XHIBITConstant.error(ex);
        }

        super.setTitle(XHIBITConstant.getResource(myResource, "moveCase"));

        moveCaseModel = new MoveCaseModel();
        moveCaseModel.setMyResource(myResource);
        moveCaseModel.setScheduledHearingValue(shv);

        // MoveCaseWiz1 moveCaseWiz1 = new MoveCaseWiz1(moveCaseModel,
        // getButtonPanel());
        MoveCaseWiz1 moveCaseWiz1 = new MoveCaseWiz1(moveCaseModel, this);
        addBodyPanel(moveCaseWiz1);
        pack();
    }
}