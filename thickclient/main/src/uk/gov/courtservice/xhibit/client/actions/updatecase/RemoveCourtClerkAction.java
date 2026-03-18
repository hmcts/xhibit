package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0 $Log: RemoveCourtClerkAction.java,v $
 * @version 1.0 Revision 1.13  2006/06/05 12:31:04  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.12 2006/05/31 14:25:08 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version 1.0 Revision 1.11 2003/11/21 15:57:05 szfnvt Bug 54946 - calling
 *          setModified.
 * 
 * Revision 1.10 2003/11/21 09:44:24 tz0d5m Bug X54946 - alterations to the
 * court staff and history - extensive changes to simplify code and allow
 * continued work on this bug.
 * 
 * Revision 1.9 2003/11/20 11:37:18 szfnvt Bug 54946 - Removal of court staff.
 * 
 * Revision 1.8 2003/08/15 09:36:44 bzw8gp Jon Powell
 * 
 * organise imports (remove unused) unused imports cause misleading dependencies
 * 
 * Revision 1.7 2003/05/12 13:48:13 nz5zpz X3.0 52441, X2.0 52446, X3.0-52579
 * 
 * Revision 1.6 2003/05/01 10:48:13 nz5zpz XI2B015
 * 
 */

public class RemoveCourtClerkAction extends XAction {
    public RemoveCourtClerkAction() {
        populateFromBundle("RemoveCourtClerkAction");
    }

    public void xActionPerformed(ActionEvent e) {
        debug("RemoveCourtClerkAction: start removing court clerk dialog.");
        if (this.getCaller() != null) {
            Class callerClass = this.getCaller().getClass();
            if (callerClass
                    .isAssignableFrom(uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData.class)) {
                UpdateCourtStaffCaseData ucsd = (UpdateCourtStaffCaseData) this.getCaller();
                if (ucsd.getCourtClerksLst().getSelectedIndex() > -1) {
                    debug("RemoveCourtClerkAction: caller is UpdateCourtStaffCaseData");
                    UpdateCourtStaffModel model = ((UpdateCourtStaffCaseData) this.getCaller())
                            .getUpdateCourtStaffModel();

                    PersonValue pv = (PersonValue) model.getCourtClerkModel().elementAt(
                            ucsd.getCourtClerksLst().getSelectedIndex());
                    model.removeCourtClerk(pv);
                    ucsd.setModified();
                    debug("RemoveUsherAction: Removed Court Clerk");
                }
            }
        }
        XHIBITConstant.debug("RemoveCourtClerkAction: finished removing court clerk dialog.");
    }

    private void debug(String text) {
        if (XAction.internalDebug)
            XHIBITConstant.debug(text);
    }
}