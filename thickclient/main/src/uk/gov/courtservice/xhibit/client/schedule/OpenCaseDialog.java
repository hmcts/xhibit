package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;

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
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class OpenCaseDialog extends XDialog {
    private OpenCasePanel bodyPanel;

    private XAction okAction = null;

    public OpenCaseDialog(Frame frame, String title) throws CSRecoverableException {
        super(frame, title, true, OKCANCEL, DEFAULTOK);
        bodyPanel = new OpenCasePanel(this);
        addBodyPanel(bodyPanel);
        // add a new action
        setOkAction(getOkAction());
        pack();
    }

    public OpenCaseDialog(String title) throws CSRecoverableException {
        this(null, title);
    }

    protected XAction getOkAction() {
        if (okAction == null) {
            okAction = new OkAction();
        }
        return okAction;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue getShv() {
        return bodyPanel.getShv();
    }

    public boolean isReadOnly() {
        return bodyPanel.isReadOnly();
    }

    public class OkAction extends XAction {
        public OkAction() {
            populateFromBundle("btnOk");
        }

        public void xActionPerformed(ActionEvent ae) throws UserCancelException {
            bodyPanel.okClicked();
        }
    }
}