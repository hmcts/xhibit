package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.DisposalActionHelper;
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
 * @version $Id: CopyDefOnOffenceDisposalPanel.java,v 1.1 2004/06/15 15:34:44
 *          sz0t7n Exp $
 */
public class CopyDefOnOffenceDisposalPanel extends XPanel {
    private static final String resources = XhibitBundles.Disposals;

    private CopyDisposals_Title topPanel;

    private CopyDefOnOffenceSelectorPanel middlePanel;

    private DefaultListModel selectedDefendantsOnOffence = new DefaultListModel();

    private DefaultListModel allDefendantsOnOffence = null;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private Insets defaultInsets = XHIBITConstant.nonContainerInsets;

    private JPanel myOKCancelPanel;

    private CopyDefOnOffenceDisposalDialog parent;

    private Integer defendantOnOffenceId;

    private ResultsRowValue selectedRRV;

    private ResultsRowValue currentRRV;

    private java.util.List fullRRVList;

    private Integer selectedDefOnOffenceId;

    private Integer lastDefOnOffenceId;

    private Integer currentDefOnOffenceId;

    private OffencePanelModel model;

    public CopyDefOnOffenceDisposalPanel(CopyDefOnOffenceDisposalDialog parent, OffencePanelModel model)
            throws CSRecoverableException {
        this.parent = parent;
        myOKCancelPanel = parent.getButtonPanel();
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        this.add(getTopPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));
        this.add(getMiddlePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 20, 0));
    }

    private CopyDisposals_Title getTopPanel() {
        if (topPanel == null) {
            String userInstructions = XHIBITConstant.getResource(resources, "defOffenceInstructionsText");
            String offenceDesc = selectedRRV.getOffenceValue().getOffenceDescription();

            topPanel = new CopyDisposals_Title(offenceDesc, userInstructions);
        }

        return topPanel;
    }

    private CopyDefOnOffenceSelectorPanel getMiddlePanel() {
        if (middlePanel == null) {
            middlePanel = new CopyDefOnOffenceSelectorPanel(this, allDefendantsOnOffence, selectedDefendantsOnOffence);
            middlePanel.setTargetDescription(XHIBITConstant.getResource(resources, "disposalDefOffencesText"));
            middlePanel.setAllDescription(XHIBITConstant.getResource(resources, "allDefOffencesText"));
            middlePanel.setButtonEnable();
        }
        return middlePanel;
    }

    public void stepInitialise() throws UserCancelException {
        selectedRRV = model.getResultRowValue();

        if (selectedRRV != null && selectedRRV.getDisposalValue() != null
                && selectedRRV.getDefendantOnOffenceId() != null) {
            // get defendantid of current defendant
            selectedDefOnOffenceId = selectedRRV.getDefendantOnOffenceId();

            fullRRVList = model.getDisposalController().getSelectedList();

            if (fullRRVList == null || fullRRVList.size() == 0) {
                JOptionPane.showConfirmDialog(parent.getParentFrame(), XHIBITConstant.getResource(resources,
                        "messageInformationCopyZero"), XHIBITConstant.getResource(resources, "messageTitleCopyZero"),
                        JOptionPane.INFORMATION_MESSAGE);
                throw new UserCancelException();
            } else {
                allDefendantsOnOffence = buildDefendantOnOffenceList();
                if (allDefendantsOnOffence == null || allDefendantsOnOffence.isEmpty()) {
                    // An appropriate Message informing user of no other
                    // defendants
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
     * create a collection of rrvs of just defendants and offences(no
     * duplicates)
     * 
     * @return
     */
    private DefaultListModel buildDefendantOnOffenceList() {
        // create a collection of rrvs of just defendants and offences(no
        // duplicates)
        DefaultListModel allDefendantsOnOffence = new DefaultListModel();
        Iterator fullListIter = fullRRVList.iterator();
        while (fullListIter.hasNext()) {
            currentRRV = (ResultsRowValue) fullListIter.next();
            if (currentRRV != null) {
                currentDefOnOffenceId = currentRRV.getDefendantOnOffenceId();
                
                // Skip this on if its a def on case id
                if (currentDefOnOffenceId == null) {
                	break;
                }

                // check if rrv equals current
                if (selectedDefOnOffenceId.compareTo(currentDefOnOffenceId) != 0) {
                    // check if defendantid on rrv is equal to last
                    if (lastDefOnOffenceId == null || lastDefOnOffenceId.compareTo(currentDefOnOffenceId) != 0) {
                        // add rrv to ListModel
                        allDefendantsOnOffence.addElement(currentRRV);
                    }
                }
                lastDefOnOffenceId = currentDefOnOffenceId;
            }
        }
        return allDefendantsOnOffence;
    }

    public void stepActivate() {
        // move info to screen
        stepUpdateViewState();
    }

    public void stepDeactivate() {

    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            if (selectedDefendantsOnOffence.size() > 0) {
                Vector defendantOnOffenceIds = new Vector();
                for (int i = 0; i < selectedDefendantsOnOffence.size(); i++) {
                    ResultsRowValue thisRRV = ((ResultsRowValue) selectedDefendantsOnOffence.getElementAt(i));
                    if (thisRRV.getDefendantOnOffenceId() != null) {
                        ResultsRowValue newRRV = (ResultsRowValue) thisRRV.clone();
                        newRRV.setDisposalValue(selectedRRV.getDisposalReferenceValue().copyDisposal(
                                selectedRRV.getDisposalValue(), newRRV.getDefendantOnOffenceId()));
                        //PR6109 - use a clone, otherwise overwriting the deportation reasons also overwrites the 
                        //previous disposal value
                        newRRV.setDisposalReferenceValue((DisposalReferenceValue) selectedRRV
                                .getDisposalReferenceValue().clone());                        
                        //PR6109 Overwrite deportation reasons with correct values
                        copyDeportationDetails(newRRV);
                        newRRV.setAction(ResultsRowValue.RESULT_ADD);
                        newRRV.setModified(true);
                        DisposalHelper.addToEndOfList(thisRRV.getDefendantOnOffenceId(), fullRRVList, newRRV);
                    }
                }
            }
        }
    }
    
    /**
     * Takes a RRV and sets the deportation reasons so that deportation reasons from the old disposal/deft are not carried over
     * @param rrv
     * @throws CSRecoverableException
     */
    private void copyDeportationDetails(ResultsRowValue rrv)throws CSRecoverableException{
        DisposalActionHelper actionHelper = new DisposalActionHelper();
        DeportationModel dm = actionHelper.getDeportationReasonFromDefendantOnCase(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
        if(rrv.getDisposalReferenceValue() != null){
            rrv.getDisposalReferenceValue().setCustodial(dm.getCustodial());
            rrv.getDisposalReferenceValue().setSuspended(dm.getSuspended());
            rrv.getDisposalReferenceValue().setSeriousDrugOffence(dm.getSeriousDrugOffence());
            rrv.getDisposalReferenceValue().setRecommendedDeportation(dm.getRecommendedDeportation());
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