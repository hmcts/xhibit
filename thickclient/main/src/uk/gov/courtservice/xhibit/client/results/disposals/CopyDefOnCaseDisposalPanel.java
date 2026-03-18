package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

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
 * @author Rakesh Lakhani
 * @version $Id: CopyDefOnCaseDisposalPanel.java,v 1.1 2004/06/15 15:34:43
 *          sz0t7n Exp $
 */
public class CopyDefOnCaseDisposalPanel extends XPanel {
    private static final String resources = XhibitBundles.Disposals;

    private JPanel myOKCancelPanel;

    private CopyDisposals_Title topPanel;

    private CopyDefOnCaseSelectorPanel middlePanel;

    private DefaultListModel disposalDefendants = new DefaultListModel();

    private DefaultListModel allDefendants = null;

    private Insets defaultInsets = XHIBITConstant.nonContainerInsets;

    private CopyDefOnCaseDisposalDialog parent;

    private OffencePanelModel model;

    private java.util.List fullRRVList;

    private ResultsRowValue selectedRRV;

    private Integer selectedDefOnCaseId;

    public CopyDefOnCaseDisposalPanel(CopyDefOnCaseDisposalDialog parent, OffencePanelModel model)
            throws CSRecoverableException {
        super(new GridBagLayout());
        this.parent = parent;
        myOKCancelPanel = parent.getButtonPanel();
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));
        this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    private CopyDisposals_Title getTopPanel() {
        if (topPanel == null) {
            String userInstructions = XHIBITConstant.getResource(resources, "defInstructionsText");
            String offenceDesc = "";
            if (selectedRRV.getOffenceValue() != null) {
                offenceDesc = selectedRRV.getOffenceValue().getOffenceDescription();
            }
            topPanel = new CopyDisposals_Title(offenceDesc, userInstructions);
        }

        return topPanel;
    }

    private CopyDefOnCaseSelectorPanel getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new CopyDefOnCaseSelectorPanel(this, allDefendants, disposalDefendants);
            middlePanel.setTargetDescription(XHIBITConstant.getResource(resources, "disposalDefendantsText"));
            middlePanel.setAllDescription(XHIBITConstant.getResource(resources, "allDefendantsText"));
            middlePanel.setButtonEnable();
        }
        return middlePanel;
    }

    public void stepInitialise() throws UserCancelException {
        selectedRRV = model.getResultRowValue();
        if (selectedRRV != null && selectedRRV.getDisposalValue() != null && selectedRRV.getDefendantOnCaseId() != null) {
            // set variable of last rrv defendantid
            // get defendantid of current defendant
            selectedDefOnCaseId = selectedRRV.getDefendantOnCaseId();

            fullRRVList = model.getDisposalController().getSelectedList();

            if (fullRRVList == null || fullRRVList.size() == 0) {
                JOptionPane.showConfirmDialog(parent.getParentFrame(), XHIBITConstant.getResource(resources,
                        "messageInformationCopyZero"), XHIBITConstant.getResource(resources, "messageTitleCopyZero"),
                        JOptionPane.INFORMATION_MESSAGE);
                throw new UserCancelException();
            } else {
                allDefendants = buildDefendantList();
                if (allDefendants == null || allDefendants.isEmpty()) {
                    JOptionPane.showConfirmDialog(parent.getParentFrame(), XHIBITConstant.getResource(resources,
                            "messageInformationCopyZero"), XHIBITConstant
                            .getResource(resources, "messageTitleCopyZero"), JOptionPane.CLOSED_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    throw new UserCancelException();
                }
            }
        }
    }

    /**
     * create a collection of rrvs of just defendants(no duplicates)
     * 
     * @return
     */
    private DefaultListModel buildDefendantList() {
        ResultsRowValue currentRRV;
        Integer lastDefOnCaseId = null;
        Integer currentDefOnCaseId;

        // create a collection of rrvs of just defendants(no duplicates)
        DefaultListModel allDefendants = new DefaultListModel();
        Iterator fullListIter = fullRRVList.iterator();
        while (fullListIter.hasNext()) {
            currentRRV = (ResultsRowValue) fullListIter.next();
            if (currentRRV != null) {
                currentDefOnCaseId = currentRRV.getDefendantOnCaseId();

                // check if rrv equals current
                if (selectedDefOnCaseId.compareTo(currentDefOnCaseId) != 0) {
                    // check if defendantid on rrv is equal to last
                    if (lastDefOnCaseId == null || lastDefOnCaseId.compareTo(currentDefOnCaseId) != 0) {
                        // add rrv to ListModel
                        allDefendants.addElement(currentRRV);
                    }
                }
                lastDefOnCaseId = currentDefOnCaseId;
            }
        }
        return allDefendants;
    }

    public void stepActivate() {
        // move info to screen
        stepUpdateViewState();
    }

    public void stepDeactivate() {

    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update && disposalDefendants.size() > 0) {
            for (int i = 0; i < disposalDefendants.size(); i++) {
                ResultsRowValue thisRRV = ((ResultsRowValue) disposalDefendants.getElementAt(i));
                if (thisRRV.getDefendantOnCaseId() != null) {
                    ResultsRowValue newRRV = (ResultsRowValue) thisRRV.clone();
                    newRRV.setDisposalValue(selectedRRV.getDisposalReferenceValue().copyDisposal(
                            selectedRRV.getDisposalValue(), newRRV.getDefendantOnCaseId()));

                    newRRV.setDisposalReferenceValue((DisposalReferenceValue) selectedRRV.getDisposalReferenceValue());
                    newRRV.setAction(ResultsRowValue.RESULT_ADD);
                    newRRV.setModified(true);
                    DisposalHelper.addToEndOfUnrelatedList(thisRRV.getDefendantOnCaseId(), fullRRVList, newRRV);
                }
            }
        }
    }

    public void stepUpdateViewState() {
        if (getMiddlePanel().getTargetListModel() == null || getMiddlePanel().getTargetListModel().isEmpty()) {
            ((OkCancelPanel) myOKCancelPanel).okButton.setEnabled(false);
        } else {
            ((OkCancelPanel) myOKCancelPanel).okButton.setEnabled(true);
        }
    }

    public void stepValidate() {

    }
}