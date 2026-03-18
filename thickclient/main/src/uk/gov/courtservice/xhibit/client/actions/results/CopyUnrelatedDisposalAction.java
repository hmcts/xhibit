package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.disposals.CopyDefOnCaseDisposalDialog;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalHelper;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Action that initiates copy unrelated disposal/p>
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
 * @version $Id: CopyUnrelatedDisposalAction.java,v 1.15 2004/06/15 15:34:45
 *          sz0t7n Exp $
 */

public class CopyUnrelatedDisposalAction extends XAction {

    public CopyUnrelatedDisposalAction() {
        populateFromBundle("CopyUnrelatedDisposal");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        if (getModel() != null) {
            CopyDefOnCaseDisposalDialog copyUnrelatedDialog = new CopyDefOnCaseDisposalDialog(xac,
                    (OffencePanelModel) getModel());
            copyUnrelatedDialog.setVisible(true);
            if (copyUnrelatedDialog.isCancelClicked())
                throw new UserCancelException();

            DisposalController dc = ((OffencePanelModel) getModel()).getDisposalController();
            dc.refreshInsertedData(DisposalHelper.getSelectedRow((OffencePanelModel) getModel()));
        }
    }
}