package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.DisposalDialog;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.actions.results.DisposalActionHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: For Criminal Appeals. Add Disposal for magistrate court
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

public class AddMagistrateDisposalAction extends XAction {
 
    private static final long serialVersionUID = 1L;

    private XhibitApplicationController xac;

    private ResultsRowValue rrv;

    private DisposalDialog disposalDialog;

    private DisposalController dc;
    
    private DisposalActionHelper actionHelper;
    
    private DeportationModel deportationReasons;
    
    private HateCrimeModel hateCrimeReasons;

    private AggravatingReasonsModel aggravatingReasons;
    
    public AddMagistrateDisposalAction() {
        populateFromBundle("AddMagistrateDisposal");
        this.setEnabled(false);
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        actionHelper = new DisposalActionHelper();
        if (getModel() != null) {
            dc = ((OffencePanelModel) getModel()).getDisposalController();
            rrv = ((OffencePanelModel) getModel()).getResultRowValue();
            if (rrv != null) {
                deportationReasons = actionHelper.getDeportationReasonFromDefendantOnCase(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                hateCrimeReasons = actionHelper.getHateCrimeReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                aggravatingReasons = actionHelper.getAggravatingReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                boolean hateCrimePanelAvailable = actionHelper.getHateIndicator(rrv.getCaseType(), rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                boolean aggravatingPanelAvailable = actionHelper.getAggravatingIndicator();
                if (rrv.getDefendantValue() != null) {
                    // Launch dialog for creation of magistrates disposal
                    if (rrv.getDefendantOnOffenceId() != null) {
                        // Related disposal
                        disposalDialog = new DisposalDialog(xac, rrv.getDefendantOnOffenceId(), true, true, deportationReasons, hateCrimeReasons, hateCrimePanelAvailable,
                        		aggravatingReasons, aggravatingPanelAvailable);
                    } else {
                        // Unrelated disposal
                        disposalDialog = new DisposalDialog(xac, rrv.getDefendantOnCaseId(), false, true, deportationReasons, hateCrimeReasons, hateCrimePanelAvailable,
                        		aggravatingReasons, aggravatingPanelAvailable);
                    }
                }
                
                disposalDialog.setVisible(true);
                
                // User has hit cancel so not continuing.
                if (disposalDialog.isCancelClicked()) {
                    throw new UserCancelException();
                }

                if (rrv.getDisposalValue() == null) {
                    // Retrieved values should have magistrate specific set.
                    rrv.setDisposalValue(disposalDialog.getValue());
                    rrv.setDisposalReferenceValue(disposalDialog.getReference());
                    rrv.setAction(ResultsRowValue.RESULT_ADD);
                    rrv.setModified(true);

                    dc.refreshData();
                } else {
                    ResultsRowValue newRrv = (ResultsRowValue) rrv.clone();
                    newRrv.setDisposalValue(disposalDialog.getValue());
                    newRrv.setDisposalReferenceValue(disposalDialog.getReference());
                    newRrv.setAction(ResultsRowValue.RESULT_ADD);
                    newRrv.setModified(true);
                    List rowList = dc.getSelectedList();
                    int index = rowList.indexOf(rrv);
                    rowList.add(index + 1, newRrv);

                    dc.refreshInsertedData(index + 1);
                }
            }
        }
    }
}