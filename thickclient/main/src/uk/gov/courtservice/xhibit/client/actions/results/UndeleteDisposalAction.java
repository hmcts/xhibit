package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.XAction;

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
 * @author Bal Bhamra
 * @version 1.0
 */

public class UndeleteDisposalAction extends XAction {
    private ResultsRowValue rrv;

    private DisposalController dc;

    public UndeleteDisposalAction() {
        populateFromBundle("UndeleteDisposal");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (getModel() != null) {
            dc = ((OffencePanelModel) getModel()).getDisposalController();
            rrv = ((OffencePanelModel) getModel()).getResultRowValue();
            if (rrv != null) {
                /**
                 * @todo This should be ok but may need changes (more info set
                 *       on RRV.) What to do with setModified???
                 */
                if (rrv.getDefendantValue() != null) {
                    rrv.setAction(rrv.getPreDeleteAction());
                    rrv.setPreDeleteAction(ResultsRowValue.RESULT_UNCHANGED);
                    dc.refreshData();
                }
            }
        }
    }
}