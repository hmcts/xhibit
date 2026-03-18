package uk.gov.courtservice.xhibit.client.actions.results;

// Java
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;

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

public class DeleteDisposalAction extends XAction {
    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.Disposals);

    private XhibitApplicationController xac;

    private ResultsRowValue rrv;

    private DisposalController dc;

    public DeleteDisposalAction() {
        populateFromBundle("DeleteDisposal");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        boolean inCourtRoom = XhibitSingleton.getInstance().isUserInCourtroom();

        if (getModel() != null) {
            dc = ((OffencePanelModel) getModel()).getDisposalController();
            rrv = ((OffencePanelModel) getModel()).getResultRowValue();
            boolean magistrates = rrv.getDisposalValue().getCourtType().equals("M");

            int rc = JOptionPane.DEFAULT_OPTION;
            if (magistrates) {
                rc = JOptionPane.showConfirmDialog(xac, XHIBITConstant.getResource(resources,
                        "messageQuestionDeleteMagistrateDisposal"), XHIBITConstant.getResource(resources,
                        "messageTitleDeleteMagistrateDisposal"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (rc == JOptionPane.YES_OPTION) {
                    java.util.List criminalList = dc.getSelectedList();
                    if (magistrates) // delete magistrates and all related
                    // variation disposals
                    {
                        ResultsRowValue criminalRRV;
                        DisposalValue criminalDisposal;
                        for (int i = 0; i < dc.getSelectedList().size(); i++) {
                            criminalRRV = (ResultsRowValue) criminalList.get(i);

                            // The rrv is the disposal being deleted
                            criminalDisposal = criminalRRV.getDisposalValue();
                            if (criminalDisposal != null) {
                                if (rrv.getDisposalValue().getDisposal2Id() != null
                                        && rrv.getDisposalValue().getDisposal2Id().equals(
                                                criminalDisposal.getPsdDisposal2Id())) {
                                    criminalList.set(i, processDelete(criminalRRV));
                                }
                            }
                        }
                        processDelete(rrv);
                    }
                    dc.refreshData();
                }
            } else {
                rc = JOptionPane.showConfirmDialog(xac, XHIBITConstant.getResource(resources,
                        "messageQuestionDeleteDisposal"), XHIBITConstant.getResource(resources,
                        "messageTitleDeleteDisposal"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (rc == JOptionPane.YES_OPTION) {
                    processDelete(rrv);
                    dc.refreshData();
                }
            }
        }
    }

    private ResultsRowValue processDelete(ResultsRowValue deleteRRV) {
        if (deleteRRV != null) {
            if (deleteRRV.getDefendantValue() != null) {
                if (deleteRRV.getAction() == ResultsRowValue.RESULT_ADD) {
                    deleteRRV.setPreDeleteAction(ResultsRowValue.RESULT_ADD);
                    deleteRRV.setAction(ResultsRowValue.RESULT_UNCHANGED);
                } else {
                    deleteRRV.setPreDeleteAction(rrv.getAction());
                    deleteRRV.setAction(ResultsRowValue.RESULT_DELETE);
                    deleteRRV.setModified(true);
                }
            }
        }
        return deleteRRV;

    }
}