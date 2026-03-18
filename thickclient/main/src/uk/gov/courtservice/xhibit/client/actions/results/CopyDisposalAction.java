package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.disposals.CopyDefOnOffenceDisposalDialog;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalHelper;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Action that initiates the copy disposal functionality.
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
 * @version $Id: CopyDisposalAction.java,v 1.17 2006/06/05 12:31:02 bzjrnl Exp $
 */

public class CopyDisposalAction extends XAction {

    public CopyDisposalAction() {
        populateFromBundle("CopyDisposal");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        if (getModel() != null) {
            DisposalController dc = ((OffencePanelModel) getModel()).getDisposalController();

            CopyDefOnOffenceDisposalDialog copyDialog = new CopyDefOnOffenceDisposalDialog(xac,
                    (OffencePanelModel) getModel());

            copyDialog.setVisible(true);
            if (copyDialog.isCancelClicked())
                throw new UserCancelException();
            dc.refreshInsertedData(DisposalHelper.getSelectedRow((OffencePanelModel) getModel()));
        }
    }
}