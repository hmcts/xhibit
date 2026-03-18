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
 * @version 1.0 $Log: RemoveUsherAction.java,v $
 * @version 1.0 Revision 1.12  2006/06/05 12:31:04  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.11 2006/05/31 14:25:08 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version 1.0 Revision 1.10 2003/11/21 09:44:23 tz0d5m Bug X54946 -
 *          alterations to the court staff and history - extensive changes to
 *          simplify code and allow continued work on this bug.
 * 
 * Revision 1.9 2003/11/20 11:37:18 szfnvt Bug 54946 - Removal of court staff.
 * 
 * Revision 1.8 2003/08/15 12:42:26 bzw8gp Jon Powell
 * 
 * remove unused variables / associated imports
 * 
 * Revision 1.7 2003/08/15 09:36:45 bzw8gp Jon Powell
 * 
 * organise imports (remove unused) unused imports cause misleading dependencies
 * 
 * Revision 1.6 2003/05/01 10:48:13 nz5zpz XI2B015
 * 
 */

public class RemoveUsherAction extends XAction {

    public RemoveUsherAction() {
        populateFromBundle("RemoveUsherAction");
    }

    public void xActionPerformed(ActionEvent e) {
        debug("RemoveUsherAction: Start remove usher action.");
        if (this.getCaller() != null) {
            Class callerClass = this.getCaller().getClass();
            if (callerClass
                    .isAssignableFrom(uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData.class)) {
                UpdateCourtStaffCaseData ucsd = (UpdateCourtStaffCaseData) this.getCaller();
                debug("RemoveUsherAction: Caller is UpdateCourtStaffCaseData");
                if (ucsd.getUsherLst().getSelectedIndex() > -1) {
                    UpdateCourtStaffModel model = ((UpdateCourtStaffCaseData) this.getCaller())
                            .getUpdateCourtStaffModel();
                    PersonValue pv = (PersonValue) model.getUsherModel().elementAt(
                            ucsd.getUsherLst().getSelectedIndex());
                    model.removeUsher(pv);
                    ucsd.setModified();
                    debug("RemoveUsherAction: Removed Usher");
                }
            }
        }
        XHIBITConstant.debug("RemoveCourtClerkAction: finished removing usher action.");
    }

    private void debug(String text) {
        if (XAction.internalDebug)
            XHIBITConstant.debug(text);
    }
}