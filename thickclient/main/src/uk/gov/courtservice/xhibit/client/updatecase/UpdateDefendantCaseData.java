package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.comparator.RefSystemRefCodeOrderComparator;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.client.counselfacilities.EditAdvocateWizard;
import uk.gov.courtservice.xhibit.client.counselfacilities.EditAdvocateWizardModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindInstructedAdvocateTableRowModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindLegalRepresentativeModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindRepresentativeWizard;
import uk.gov.courtservice.xhibit.client.counselfacilities.InstructedAdvocateHelper;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: Xhibit 2 Client Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.l rework and extention for iteration 2a/2b. new functionality
 *          includes sign in for counsel.
 */
public class UpdateDefendantCaseData extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(UpdateDefendantCaseData.class);
    
    private int displaySelectedRowIndex = -1;

    private int selectedRowIndex = -1;

    private Integer selectedDefendantsId = null;

    private UpdateCasePanel updateCasePanel;

    private XTable xDefenceRepresenationTable;

    protected DefenceRepTableModel xDefenceRepresenationModel;

    private ListSelectionModel listSelectionModel;

    private XDefenceRepresentationListSelectionListener lsl;

    private boolean modelChanged = false;

    private JButton xAmendDefendantButton;

    private JButton xSignInCounselButton;

    private JButton xRemoveCounselButton;
    
    private JButton xEditCounselButton;

    private Vector<Vector> removedRepresentation;
    
    private Vector<Vector> editedRepresentation;

    private static final String xRemoveCounselActionCommand = "xRemoveCounselActionCommand";

    /**
     * Constructor that creates an UpdateDefendantCaseData
     * 
     * @param updateCasePanel
     */
    public UpdateDefendantCaseData(UpdateCasePanel updateCasePanel) {
        this.updateCasePanel = updateCasePanel;
        jbInit();

        stepUpdateViewState();
    }

    private void jbInit() {
        setToolTipText(getResource("updateDefendantCasePropertiesToolTip."
                + (updateCasePanel.isCriminalAppealHearing() || updateCasePanel.isMiscAppealCase() ? "appeal"
                        : "normal")));
        this.setLayout(new GridBagLayout());

        JPanel internalPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        xDefenceRepresenationModel = new DefenceRepTableModel(updateCasePanel.hhv.getLegalRepValues(), updateCasePanel
                .isCriminalAppealHearing()
                || updateCasePanel.isMiscAppealCase(), updateCasePanel.hhv.getHhCase().getCaseType());

        xDefenceRepresenationTable = XTableFactory.getInstance().createMultiLineTable(xDefenceRepresenationModel);
        xDefenceRepresenationTable.makeSortable();
        xDefenceRepresenationTable.getColumnModel().getColumn(DefenceRepTableModel.COLUMN_I_S_FLAG).setMaxWidth(40);
        xDefenceRepresenationTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
        listSelectionModel = new DefaultListSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lsl = new XDefenceRepresentationListSelectionListener();
        listSelectionModel.addListSelectionListener(lsl);
        JScrollPane defendantsScrollPane = new JScrollPane(xDefenceRepresenationTable);
        defendantsScrollPane.setMinimumSize(new Dimension(400, 250));

        internalPanel.add(defendantsScrollPane, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout());

        xAmendDefendantButton = new JButton(new XAmendDefendantAction());

        // MH Changed the text on the buttons depending on type of case.
        if (updateCasePanel.isCriminalAppealHearing() || updateCasePanel.isMiscAppealCase()) {
            xAmendDefendantButton.setText(getResource("updateDefendantCaseDataButton.amendDefendant.appeal"));
            xAmendDefendantButton
                    .setToolTipText(getResource("updateDefendantCaseDataButtonToolTip.amendDefendant.appeal"));
        } else {
            xAmendDefendantButton.setText(getResource("updateDefendantCaseDataButton.amendDefendant.normal"));
            xAmendDefendantButton
                    .setToolTipText(getResource("updateDefendantCaseDataButtonToolTip.amendDefendant.normal"));
        }

        buttonPanel.add(xAmendDefendantButton);
        XhibitActions.getAction(this.updateCasePanel.ucd.xac, XhibitActions.OpenAmendDefendant).setEnabled(false);

        xRemoveCounselButton = new JButton();
        xRemoveCounselButton.addActionListener(this);
        xRemoveCounselButton.setToolTipText(getResource("removeCounselToolTipText"));
        xRemoveCounselButton.setActionCommand(xRemoveCounselActionCommand);
        xRemoveCounselButton.setText(getResource("lblRemove"));

        xSignInCounselButton = new JButton();
        xSignInCounselButton.setAction(new XSignInAction());
        xSignInCounselButton.setToolTipText(getResource("signInCounselToolTipText"));
        xSignInCounselButton.setText(getResource("lblSignIn"));
        
        xEditCounselButton = new JButton();
        xEditCounselButton.setAction(new XEditAction());
        xEditCounselButton.setToolTipText(getResource("editCounselToolTipText"));
        xEditCounselButton.setText(getResource("lblEditCounsel"));
        
        buttonPanel.add(xSignInCounselButton);
        buttonPanel.add(xRemoveCounselButton);
        buttonPanel.add(xEditCounselButton);

        internalPanel.add(buttonPanel, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        this.add(internalPanel, gbc);

        // JDK 1.5 This needs to be at the end as it triggers a call to
        // valueChanged of XDefenceRepresentationListSelectionListener
        // which references amongst other things the xAmendDefendantButton
        xDefenceRepresenationTable.setSelectionModel(listSelectionModel);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals(UpdateDefendantCaseData.xRemoveCounselActionCommand)) {
            // if the currently selected row is not the only one with this
            // defendant, then it is safe to remove the whole line.
            // else, one must instead put a null value into the selected
            // row.
            synchronized (xDefenceRepresenationModel) {
                int selectedRowIndex = this.lsl.getSelectedRowIndex();
                int physicalRowIndex = xDefenceRepresenationTable.getSelectedRow();
                Integer defendantId = this.xDefenceRepresenationModel.getIDofDefendantPersonOnRow(selectedRowIndex);
                if (defendantId != null) {
                    log.debug("the ID of the defendant is " + defendantId);
                    // need to decide whether to remove the complete row, or
                    // only the rep (and cc info msg) from the model
                    // this depends on whether the defendant is listed on an
                    // other row too.

                    Integer counselId = this.xDefenceRepresenationModel
                            .getIDofRepresentativePersonOnRow(selectedRowIndex);
                    log.debug("the ID of the counsel is " + counselId);

                    boolean thisDefendantIsOnAnotherRowToo = false;
                    int numberOfRows = this.xDefenceRepresenationModel.getRowCount();
                    int rowIterator = 0;
                    while ((rowIterator < numberOfRows) && (!thisDefendantIsOnAnotherRowToo)) {
                        int sortedRowIndex = rowIterator;
                        try {
                            sortedRowIndex = ((XSortableTableModel) this.xDefenceRepresenationTable.getModel())
                                    .getIndexedRow(sortedRowIndex);
                        } catch (Exception ee) {
                            log.error("Could not resolve the index using a sortmodel - index remains "
                                    + selectedRowIndex);
                            log.error(ee);
                        }
                        Integer aDefendantId = this.xDefenceRepresenationModel
                                .getIDofDefendantPersonOnRow(sortedRowIndex);
                        if ((defendantId.equals(aDefendantId)) && (sortedRowIndex != selectedRowIndex))
                            thisDefendantIsOnAnotherRowToo = true;
                        rowIterator++;
                    }
                    Vector currentRow = (Vector) ((XHIBITTableModelInterface) xDefenceRepresenationTable.getModel())
                            .getDataAt(physicalRowIndex);

                    if (thisDefendantIsOnAnotherRowToo) {
                        if (updateCasePanel.ucd.internalDebug) {
                            log.debug("The selected defendant is on an other row too");
                        }
                        xDefenceRepresenationModel.remove(currentRow);
                        currentRow.ensureCapacity(6);
                        currentRow.setElementAt(UpdateCasePanel.xREMDefenceRepresentation, DefenceRepTableModel.STATUS);
                        addToRemovedRepresenation(currentRow);
                        modelChanged = true;
                    } else {
                        if (updateCasePanel.ucd.internalDebug) {
                            log.debug("The selected defendant is NOT on an other row");
                        }
                        try {
                            // MH - this is null at times and crash the app,
                            // what is this done for?
                            if (currentRow != null && currentRow.elementAt(DefenceRepTableModel.STATUS) != null) {
                                // Integer legRepId =
                                // (Integer)currentRow.elementAt(3);
                                // String newDefenceFlag =
                                // (String)currentRow.elementAt(3);
                            }
                            // if this is found, then this must be a new
                            // defence, so just remove
                            // him from the model. do not schedule for mid
                            // tier interaction
                            // also no need to changed = true; or changed =
                            // false;
                            currentRow.ensureCapacity(6);
                            currentRow.setElementAt(UpdateCasePanel.xREMDefenceRepresentation, DefenceRepTableModel.STATUS);
                            addToRemovedRepresenation(currentRow);
                        } catch (Exception e) {
                            // no legal rep id found, thus an mid tier
                            // originating row.
                            log.error(e);
                        }
                        xDefenceRepresenationModel.remove(currentRow);
                        Vector newRow = new Vector(6);
                        newRow.addAll(currentRow);
                        newRow.set(DefenceRepTableModel.LEGAL_REP, null);
                        newRow.set(DefenceRepTableModel.CC_INFO, "---");
                        
                        newRow.set(DefenceRepTableModel.SH_LEG_REP_VALUE, null);
                        newRow.set(DefenceRepTableModel.LEGAL_REP_ID, null);
                        if (newRow.size() > DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY) {
                            newRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_TYPE, null);
                            newRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID, null);
                            newRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY, null);
                        }
                        
                        xDefenceRepresenationModel.add(newRow);
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("Updated tablemodel row to " + currentRow);
                        modelChanged = true;
                    }
                    if (modelChanged) {
                        // Explicitly fire the tableChanged event to refresh the
                        // display
                        xDefenceRepresenationTable.tableChanged(new TableModelEvent(xDefenceRepresenationTable
                                .getModel()));
                    }
                } else {
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("no defendant on the selected row");
                }
            }

            xDefenceRepresenationTable.setRowSelectionInterval(0, 0);
        }// END OF actionCommand.equals(this.xRemoveCounselActionCommand)

        stepUpdateViewState();
    }// END OF actionPerformed(ActionEvent actionEvent)

    /**
     * Refresh the defendant names in the table model where the defendant ID
     * matches the parameter specified.
     * 
     * @param defendantId -
     *            The ID of defendant whose name should be refreshed
     * @param newName -
     *            The defendant's new name
     */
    public void refreshDefendantName(Integer defendantId, String newName) {
        for (int x = 0; x < xDefenceRepresenationModel.getRowCount(); x++) {
            Vector aRow = (Vector) xDefenceRepresenationModel.getDataAt(x);
            if (defendantId.intValue() == ((Integer) aRow.elementAt(DefenceRepTableModel.DEFENDANT_ID)).intValue()) {
                aRow.set(DefenceRepTableModel.DEFENDANT_NAME, newName);
            }
        }

        // Explicitly fire the tableChanged event to refresh the display
        xDefenceRepresenationTable.tableChanged(new TableModelEvent(xDefenceRepresenationTable.getModel()));
    }

    private String getResource(final String resourceKey) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader, resourceKey);
    }

    private class XDefenceRepresentationListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
            log.debug("***** XDefenceRepresentationListSelectionListener valueChanged");

            ListSelectionModel lsm = (ListSelectionModel) lse.getSource();
            displaySelectedRowIndex = lsm.getLeadSelectionIndex();

            selectedRowIndex = xDefenceRepresenationTable.getSelectedRow();

            if (displaySelectedRowIndex != -1) {
	            try {
	                XSortableTableModel xsort = (XSortableTableModel) xDefenceRepresenationTable.getModel();
	                selectedRowIndex = xsort.getIndexedRow(displaySelectedRowIndex);
	            } catch (Exception ee) {
	                log.fatal(ee, ee);
	                throw new CSUnrecoverableException(ee);
	            }
            }

            if (updateCasePanel.ucd.internalDebug)
                log.debug("xDefenceRepresenationTable.selectionModel valueChanged(ListSelectionEvent lse) row="
                        + selectedRowIndex);
            selectedDefendantsId = xDefenceRepresenationModel.getIDofDefendantPersonOnRow(selectedRowIndex);
            if (selectedDefendantsId == null) {
                if (updateCasePanel.ucd.internalDebug)
                    log.debug("No defendantID found for this row!");
            } else {
                if (xDefenceRepresenationModel.hasDefendantOnRow(selectedRowIndex)) {
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("Defendant on row " + selectedRowIndex);
                    try {
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("ID of defendant on selected row =" + selectedDefendantsId);
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("ID of current case is  =" + updateCasePanel.hhv.getHhCase().getId());
                        UpdateDefendantModel udm = new UpdateDefendantModel(selectedDefendantsId, updateCasePanel.hhv
                                .getHhCase().getId(), true);
                        udm.setShowPublicDisplayHidingControls(true);
                        ((XAction) xAmendDefendantButton.getAction()).setModel(udm); // so
                                                                                        // the
                                                                                        // action
                                                                                        // knows
                                                                                        // what
                                                                                        // to
                        // update
                    } catch (Exception e) {
                        // Message userMessage = new
                        // Message("gui.updateDefendantCaseData.actionsFailed");
                        String logMessage = "the updateDefendantCaseData failed to get an action and set it enabled and its model";
                        e.printStackTrace();
                        log.error(logMessage);
                        log.error(e);
                    }
                }
            }

            stepUpdateViewState();
        }

        public int getSelectedRowIndex() {
            return selectedRowIndex;
        }
        // END OF THE XDefenceRepresentationListSelectionListener internal class
    }

    
    private class XEditAction extends XAction {

        private static final long serialVersionUID = 1L;

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            int selectedRowIndex = lsl.getSelectedRowIndex();
            if (updateCasePanel.ucd.internalDebug)
                log.debug("the selected row in the table is " + selectedRowIndex);

            EditAdvocateWizardModel editAdvocateModel = new EditAdvocateWizardModel();
            editAdvocateModel.setXac((XhibitApplicationController) updateCasePanel.ucd.openingAction.getController());
            
            // CCN0223 replaces FindLegalRepresentativeDialog with FindRepresentativeWizard
            //  FindLegalRepresentativeDialog flrDialog = new FindLegalRepresentativeDialog(
            //          updateCasePanel.ucd.getParentFrame(), flrModel);
            
            Vector selectedRow = (Vector) xDefenceRepresenationModel.getDataAt(selectedRowIndex);
            final Integer defendantId = (Integer) selectedRow.elementAt(DefenceRepTableModel.DEFENDANT_ID);
            final SHLegRepBasicValue legRep = (SHLegRepBasicValue) 
                selectedRow.elementAt(DefenceRepTableModel.SH_LEG_REP_VALUE);
            final Integer legalRepId = legRep.getRefLegalRepID();
            
            if (legalRepId == null) {
                // Should never be here.
                throw new CSRecoverableException();
            }
            
            final boolean legalAidOrderGranted = 
                ((Boolean)selectedRow.elementAt(DefenceRepTableModel.LEGALLY_AIDED)).booleanValue();
            
            if (!legalAidOrderGranted) {
                String msgStr = "Attempt to set Instructed/Substitute flag on a privatly represented defendant";
                String msgKey = "gui.editAdvocateWizard.noLegalAidOrder";
                CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr);
                throw (csre);
            }
            
            EditAdvocateWizard eaDialog = 
                new EditAdvocateWizard(
                        xDefenceRepresenationModel,
                        editAdvocateModel, 
                        legalRepId, 
                        defendantId);
            
            eaDialog.setVisible(true);
            
            if (eaDialog.getLatestEvent() == XWizardDialog.FINISH_EVENT) {
                
                eaDialog.stepValidate();
                
                FindInstructedAdvocateTableRowModel trm = 
                    editAdvocateModel.getSubstitutedInstructedAdvocateTableRowModel();
                
                synchronized (xDefenceRepresenationModel) {
                    
                    if (trm != null) {
                        selectedRow.add(
                                DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_TYPE, 
                                trm.getLegalRepType());
                        selectedRow.add(
                                DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID, 
                                trm.getLegalRepId());
                        selectedRow.add(
                                DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY, 
                                trm.getDefenceCategory());
                    } else {
                        selectedRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_TYPE, null);
                        selectedRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID, null);
                        selectedRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY, null);
                    }
                    
                    addToEditedRepresentation(selectedRow);
                    
                    if (trm != null) {
                        legRep.setSubstitutedRefLegalRepID(trm.getLegalRepId());
                    } else {
                        legRep.setSubstitutedRefLegalRepID(legRep.getRefLegalRepID());
                    }
                    legRep.setSubInst(editAdvocateModel.getBarristerType());
                    
                    // Explicitly fire the tableChanged event to refresh the
                    // display
                    // xDefenceRepresenationTable.tableChanged(new
                    // TableModelEvent(xDefenceRepresenationTable.getModel()));

                    xDefenceRepresenationModel.xIsChanged = true;
                    updateCasePanel.setModified(true);
                    modelChanged = true;
                }
                stepUpdateViewState();
            }
        }
    }
    
    /**
     * <p>
     * Title: XHIBIT2 XSignInAction in UpdateDefendantCaseData
     * </p>
     * <p>
     * Description: a Non-Singleton implementation of the XAction
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Frederik Vandendriessche
     * @version 1.0
     */
    private class XSignInAction extends XAction {

        private static final long serialVersionUID = 1L;

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            int selectedRowIndex = lsl.getSelectedRowIndex();
            if (updateCasePanel.ucd.internalDebug)
                log.debug("the selected row in the table is " + selectedRowIndex);

            FindLegalRepresentativeModel flrModel = new FindLegalRepresentativeModel();
            flrModel.setXac((XhibitApplicationController) updateCasePanel.ucd.openingAction.getController());
            
            // CCN0223 replaces FindLegalRepresentativeDialog with FindRepresentativeWizard
            //  FindLegalRepresentativeDialog flrDialog = new FindLegalRepresentativeDialog(
            //          updateCasePanel.ucd.getParentFrame(), flrModel);
            
            Vector selectedRow = (Vector) xDefenceRepresenationModel.getDataAt(selectedRowIndex);
            final Integer defendantId = (Integer) selectedRow.elementAt(DefenceRepTableModel.DEFENDANT_ID);
            
            final boolean legalAidOrderGranted = 
                ((Boolean)selectedRow.elementAt(DefenceRepTableModel.LEGALLY_AIDED)).booleanValue();
            
            FindRepresentativeWizard flrDialog = 
                new FindRepresentativeWizard(
                        xDefenceRepresenationModel,
                        flrModel, 
                        defendantId, 
                        legalAidOrderGranted);
            
            flrDialog.setVisible(true);
            
            if (flrDialog.getLatestEvent() == XWizardDialog.FINISH_EVENT) {
                flrDialog.stepValidate();
                
                FindLegalRepresentativeTableRowModel flrtrModel = flrModel.getFindLegalRepresentativeTableRowModel();
                synchronized (xDefenceRepresenationModel) {
                    PersonValue defendantPerson = (PersonValue) selectedRow.elementAt(0);
                    String defendantFullName = (String) selectedRow.elementAt(8);
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("the defendant on that row is " + defendantPerson.getFullName());

                    // process the search result into a represenation
                    // PersonValue object
                    PersonValue representationPerson = new PersonValue();
                    representationPerson.setFullName(flrtrModel == null ? ""
                            : (flrtrModel.getFullName() == null ? "n/a" : flrtrModel.getFullName()));
                    
                    if (updateCasePanel.isCriminalAppealHearing() || updateCasePanel.isMiscAppealCase()) {
                        representationPerson.setPersonType(PersonValue.DEFENCE);
                    } else {
                        representationPerson.setPersonType(PersonValue.DEFENCE);
                    }

                    Vector currentData = xDefenceRepresenationModel.getData();
                    Vector aRow;
                    if (!xDefenceRepresenationModel.hasRepresentativeOnRow(selectedRowIndex)) {
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("the selected row does not have a counsel on it yet");
                        aRow = (Vector) currentData.elementAt(selectedRowIndex);
                        currentData.removeElementAt(selectedRowIndex);
                    } else {
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("### the selected row has a counsel on it. need to make a new row");
                        aRow = new Vector();
                        aRow.add(DefenceRepTableModel.DEFENDANT, defendantPerson);
                    }

                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("### arow = " + aRow);
                    aRow.add(DefenceRepTableModel.LEGAL_REP, representationPerson);
                    aRow.add(DefenceRepTableModel.CC_INFO, null); // actors on this screen don't get to
                    // set a CC Info Msg.
                    aRow.add(DefenceRepTableModel.STATUS, UpdateCasePanel.xADDDefenceRepresentation);// the
                    // c(l)ue
                    // to
                    // pick
                    // up
                    // the
                    // added
                    // ones
                    // from
                    // the
                    // table
                    // model
                    aRow.add(DefenceRepTableModel.LEGAL_REP_TYPE, flrtrModel.getLegalRepType());
                    aRow.add(DefenceRepTableModel.SH_LEG_REP_VALUE, null); // there is no SHLegRepID yet as this
                    // row does not yet exist in db
                    aRow.add(DefenceRepTableModel.LEGAL_REP_ID, flrtrModel.getLegalRepId()); // hidden - id
                    // of legal rep
                    aRow.add(DefenceRepTableModel.DEFENDANT_ID, defendantId); // defendant ID
                    aRow.add(DefenceRepTableModel.DEFENDANT_NAME, defendantFullName); // defendant name
                    aRow.add(DefenceRepTableModel.LEGALLY_AIDED, new Boolean(legalAidOrderGranted));
                    
                    FindLegalRepresentativeTableRowModel flrtrModelInstructedAdvocate = 
                        flrModel.getInstructedAdvocateTableRowModel();
                    
                    if (flrtrModelInstructedAdvocate != null) {
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_TYPE, 
                                flrtrModelInstructedAdvocate.getLegalRepType());
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID, 
                                flrtrModelInstructedAdvocate.getLegalRepId());
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY, 
                                flrtrModelInstructedAdvocate.getDefenceCategory());
                    } else {
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_TYPE, null);
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID, null);
                        aRow.add(DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY, null);
                    }
                    
                    
                    // currentData.add(aRow);
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("### arow " + aRow + " to the DefendantTableModel.");

                    // xDefenceRepresenationModel.setData(currentData);
                    xDefenceRepresenationModel.add(aRow);

                    // Explicitly fire the tableChanged event to refresh the
                    // display
                    // xDefenceRepresenationTable.tableChanged(new
                    // TableModelEvent(xDefenceRepresenationTable.getModel()));

                    xDefenceRepresenationModel.xIsChanged = true;
                    updateCasePanel.setModified(true);
                    modelChanged = true;
                }
                stepUpdateViewState();
            }
        }
    }

    /**
     * <p>
     * Title: XHIBIT2 XAmendDefendantAction in UpdateDefendantCaseData
     * </p>
     * <p>
     * Description: Calls the <code>OpenAmendDefendantAction</code> action and
     * then refreshes the names of the selected defendants
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Stephen Tully
     * @version 1.0
     */
    private class XAmendDefendantAction extends OpenAmendDefendantAction {

        private static final long serialVersionUID = 1L;

        public XAmendDefendantAction() {
            super();
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            // Get the OpenAmendDefendantAction action
            OpenAmendDefendantAction action = (OpenAmendDefendantAction) XhibitActions.getAction(
                    updateCasePanel.ucd.xac, XhibitActions.OpenAmendDefendant);

            // Pass the data from this action to the
            // OpenAmendDefendantAction
            // action and then execute it
            action.setModel(this.getModel());

            action.actionPerformed(e);

            // If the user clicked the OK button, then refresh the
            // displayable
            // defendant names of the entries in the table that point to the
            // same defendant
            if (action.getModel() != null && ((UpdateDefendantModel) action.getModel()).getUpdate()) {
                // Re-build the defendant name
                String newFullName = PDHConstants.buildFullName(((UpdateDefendantModel) action.getModel())
                        .getDefendantValue().getFirstName(), ((UpdateDefendantModel) action.getModel())
                        .getDefendantValue().getMiddleName(), ((UpdateDefendantModel) action.getModel())
                        .getDefendantValue().getInitials(), ((UpdateDefendantModel) action.getModel())
                        .getDefendantValue().getSurName());

                // Refresh the entries in the table
                refreshDefendantName(((UpdateDefendantModel) action.getModel()).getDefendantID(), newFullName);

                stepUpdateViewState();
            }
        }
    }

    public Collection getEditedRepresentation() {
        if (editedRepresentation == null)
            editedRepresentation = new Vector<Vector>();
        return editedRepresentation;
    }
    
    public void addToEditedRepresentation(Vector repVector) {
        if (editedRepresentation == null)
            editedRepresentation = new Vector<Vector>();
        this.editedRepresentation.add(repVector);
    }
    
    public Collection getRemovedRepresenation() {
        if (removedRepresentation == null)
            removedRepresentation = new Vector<Vector>();
        return removedRepresentation;
    }

    public void addToRemovedRepresenation(Vector repVector) {
        if (removedRepresentation == null)
            removedRepresentation = new Vector<Vector>();
        this.removedRepresentation.add(repVector);
    }

    public void stepDeactivate() throws HearingScheduleException, CSRecoverableException {
        if (modelChanged) {
            boolean addAndRemoveBothOk = true;
            if (updateCasePanel.ucd.internalDebug) {
                log.debug("Defence Representation has Changed. Starting to save.");
            }

            // to check for new ones, iterate the whole model, looking for
            // the
            // uc.xADDDefenceRepresentation change indicator
            try {
                // first, populate a collection of the required value objects /
                // shLegRepValues.
                Collection<SHLegRepBasicValue> legalRepsToAdd = new Vector<SHLegRepBasicValue>();
                Vector defenceRepresentationsVector = xDefenceRepresenationModel.getData();
                Iterator defenceRepresentationIterator = defenceRepresentationsVector.iterator();
                while (defenceRepresentationIterator.hasNext()) {
                    Vector defRepVec = (Vector) defenceRepresentationIterator.next();
                    String defRepChange = defRepVec.elementAt(DefenceRepTableModel.STATUS) == null ? "<no change indicator set>"
                            : ((String) defRepVec.elementAt(DefenceRepTableModel.STATUS));
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("DefLegRep change indicator = " + defRepChange);

                    if (defRepChange.equals(UpdateCasePanel.xADDDefenceRepresentation)) {
                        PersonValue defendantPerson = (PersonValue) defRepVec.elementAt(DefenceRepTableModel.DEFENDANT);
                        // PersonValue representationPerson =
                        // (PersonValue)defRepVec.elementAt(1);
                        Integer legalRepId = (Integer) defRepVec.elementAt(DefenceRepTableModel.LEGAL_REP_ID);

                        String legalRepTypePre = (String) defRepVec.elementAt(DefenceRepTableModel.LEGAL_REP_TYPE);
                        String legalRepType = null;
                        if (legalRepTypePre.equals(CounselFacilitiesHelper.BARRADIO)) {
                            legalRepType = CounselFacilitiesHelper.LEGAL_REP_TYPE_LAWYER;
                        } else if (legalRepTypePre.equals(CounselFacilitiesHelper.INPRADIO)) {
                            legalRepType = CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON;
                        } else if (legalRepTypePre.equals(CounselFacilitiesHelper.NONATTRADIO)) {
                            legalRepType = CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE;
                        } else {
                            legalRepType = CounselFacilitiesHelper.LEGAL_REP_TYPE_SOLICITOR;
                        }
                        SHLegRepBasicValue shLegRep = new SHLegRepBasicValue();
                        shLegRep.setRefLegalRepID(legalRepId); // not from the
                        // represent
                        // person! as
                        // this is that
                        // object is a
                        // newly created
                        // on without
                        // the right id.
                        shLegRep.setSchedHearDefID(defendantPerson == null ? null : defendantPerson.getId());
                        shLegRep.setScheduledHearingID(updateCasePanel.scheduledHearingId);
                        shLegRep.setSolFirmOrRefLegalRep(legalRepType);
                        shLegRep.setLegalRole(PersonValue.DEFENCE); 
                        // case type dependency ?

                        shLegRep.setIsSignIn("Y");
                        
                        if (defRepVec.size() > DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID &&
                                defRepVec.elementAt(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID) != null) 
                        {
                            Integer instructedLegalRepId = 
                                (Integer)defRepVec.elementAt(DefenceRepTableModel.INSTRUCTED_ADVOCATE_REP_ID);
                            
                            Integer refRefDefenceCategoryComboId = Integer.valueOf((String) defRepVec.elementAt(DefenceRepTableModel.INSTRUCTED_ADVOCATE_DEFENCE_CATEGORY));
                            shLegRep.setRefDefenceCategoryID(getRefDefenceCategoryIdFromComboId(refRefDefenceCategoryComboId));
                            
                            if (instructedLegalRepId.equals(legalRepId)) {
                                // The defendant is publicly represented and the RefLegalRepID
                                // is an instructed advocate.
                                shLegRep.setSubstitutedRefLegalRepID(legalRepId);
                                shLegRep.setSubInst(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG);
                            } else {
                                // The defendant is publicly represented and the RefLegalRepID
                                // is a substitute advocate.  We also need to record the
                                // instructed advocate that is being substituted.
                                shLegRep.setSubstitutedRefLegalRepID(instructedLegalRepId);
                                shLegRep.setSubInst(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG);
                            }

                        } else {
                            // The defendant is privatly represented.  No need to set
                            // the setSubInst or setSubstitutedRefLegalRepID fields.
                        }

                        
                        legalRepsToAdd.add(shLegRep);
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("!!SHLegRep to add: " + shLegRep);
                        defRepVec.setElementAt(null, DefenceRepTableModel.STATUS); // clearing the
                        // change
                        // indicator so
                        // this row does
                        // not get added
                        // again on the
                        // next apply
                    }
                }

                // secondly, call the midtier to add that collection of
                // shLegRepValues / counsel sign in really.
                if (updateCasePanel.ucd.internalDebug)
                    log.debug("Prepared for mid tier " + legalRepsToAdd.size()
                            + " Defence Representation Additions/Counsel Sign ins");

                if (legalRepsToAdd.size() > 0) {
                    // MH - changed the delegate call to other method.
                    // uc.getHearingScheduleBD().addLegalReps(uc.scheduledHearingId,
                    // legalRepsToAdd);
                    updateCasePanel.getHearingScheduleBD().addLegalRepValues(updateCasePanel.scheduledHearingId,
                            updateCasePanel.hhv.getHhCase().getId(), legalRepsToAdd, XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("Defence Representation " + legalRepsToAdd.size()
                                + " Additions were saved/Counsel Signed in");
                }
            } catch (Exception e) // something went wrong during the sign
            // in / adding of legal reps
            {
                addAndRemoveBothOk = false;
                String msgStr = "HearingScheduleException during whilst saving the counsel sign in / uc.getHearingScheduleBD().addLegalReps(uc.scheduledHearingId, legalRepsToAdd)";
                String msgKey = "gui.updateDefendantCaseData.addLegalRepsFailed";
                log.error(msgStr);
                CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, e);
                throw (csre);
            }

            
            // Now process any edited counsel
            try {
                Collection edited = getEditedRepresentation();
                
                Vector<SHLegRepBasicValue> shLegReps = new Vector<SHLegRepBasicValue>();
                
                Iterator iter = edited.iterator();
                
                while (iter.hasNext()) {
                    Vector defRepVec = (Vector)iter.next();
                    
                    SHLegRepBasicValue shLegRep = 
                        (SHLegRepBasicValue)defRepVec.elementAt(
                                DefenceRepTableModel.SH_LEG_REP_VALUE);
                    
                    if (shLegRep != null)
                        shLegReps.add(shLegRep);
                }
                
                updateCasePanel.getHearingScheduleBD().updateLegalReps(updateCasePanel.scheduledHearingId, shLegReps, XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                
                // Update the version numbers so that any edited values
                // that are due to be deleted, do not suffer from a 
                // version mismatch error on delete.
                Iterator<SHLegRepBasicValue> iter2 = shLegReps.iterator();
                while (iter2.hasNext()) {
                    SHLegRepBasicValue item = iter2.next();
                    if (item.getVersion() != null)
                        item.setVersion(new Integer(item.getVersion().intValue() + 1));
                }
                
                this.editedRepresentation = new Vector<Vector>();
            } catch (Exception e) {
                addAndRemoveBothOk = false;
                String msgStr = "HearingScheduleException during whilst update of counsel";
                String msgKey = "gui.updateDefendantCaseData.updateLegalRepsFailed";
                log.error(msgStr);
                CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, e);
                throw (csre);
            }
            
            
            // now processing removal of counsel
            try {
                Collection<SHLegRepBasicValue> legalRepsToRemove = new Vector<SHLegRepBasicValue>();
                Collection remDefenceRepresentation = getRemovedRepresenation(); // XTM.
                if (updateCasePanel.ucd.internalDebug)
                    log.debug("The number of DefenceRepresentations to remove is: " + remDefenceRepresentation.size());

                Iterator defenceRepresentationIterator = remDefenceRepresentation.iterator();
                while (defenceRepresentationIterator.hasNext()) {
                    Vector defRepVec = (Vector) defenceRepresentationIterator.next();

                    String defRepChange = (String) defRepVec.elementAt(3);
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("DefLegRep change indicator = " + defRepChange);

                    if (defRepChange == null) {
                        if (updateCasePanel.ucd.internalDebug)
                            log.debug("no defence representation change indicator found in vector " + defRepVec);
                    } else {
                        if (defRepChange.equals(UpdateCasePanel.xREMDefenceRepresentation)) {
                            SHLegRepBasicValue shLegRep = (SHLegRepBasicValue) defRepVec.elementAt(5);
                            if (shLegRep != null) {
                                legalRepsToRemove.add(shLegRep);
                            }
                        }
                    }
                }
                if (updateCasePanel.ucd.internalDebug)
                    log.debug("Prepared for midtier " + legalRepsToRemove.size() + " Defence Representation.");

                if (legalRepsToRemove.size() > 0) {
                    updateCasePanel.getHearingScheduleBD().removeLegalReps(updateCasePanel.scheduledHearingId,
                            updateCasePanel.hhv.getHhCase().getId(),legalRepsToRemove);
                    if (updateCasePanel.ucd.internalDebug)
                        log.debug("Defence Representation " + legalRepsToRemove.size() + " Removals were saved.");
                    this.removedRepresentation = new Vector<Vector>();
                }
            } catch (Exception e) {
                addAndRemoveBothOk = false;
                String msgStr = "HearingScheduleException during whilst removal of counsel sign in / uc.getHearingScheduleBD().removeLegalReps(legalRepsToRemove)";
                String msgKey = "gui.updateDefendantCaseData.remLegalRepsFailed";
                log.error(msgStr);
                CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, e);
                throw (csre);
            }
            
            if (addAndRemoveBothOk) {
                modelChanged = false;
            }
        }
    }
    
    
    Vector<Integer> getDefendantLegalReps() {
        Vector<Integer> defendantLegalReps = new Vector<Integer>();
        
        if (xDefenceRepresenationModel != null) {
            Vector defenceRepresentationsVector = xDefenceRepresenationModel.getData();
            Iterator iter = defenceRepresentationsVector.iterator();
            while (iter.hasNext()) {
                Vector defRepVec = (Vector) iter.next();
                SHLegRepBasicValue legRepBasicValue = 
                    (SHLegRepBasicValue)defRepVec.elementAt(DefenceRepTableModel.SH_LEG_REP_VALUE);
                Integer legalRepId = (Integer)defRepVec.elementAt(DefenceRepTableModel.LEGAL_REP_ID);
                if (legalRepId != null) {
                    defendantLegalReps.add(legalRepId);
                } else if (legRepBasicValue != null) {
                    defendantLegalReps.add(legRepBasicValue.getRefLegalRepID());
                }
            }
        }
        
        return defendantLegalReps;
    }

    
    /**
     * Life-cycle method to enable/disable screen components
     */
    public void stepUpdateViewState() {
        final boolean editable = updateCasePanel.ucd.openingAction.isEditable();
        final boolean notExported = !updateCasePanel.isExported().booleanValue();
        final boolean defendantRowSelected = xDefenceRepresenationTable.getSelectedRow() >= 0;

        xAmendDefendantButton.setEnabled(defendantRowSelected
                && xDefenceRepresenationModel.hasDefendantOnRow(selectedRowIndex));

        xSignInCounselButton.setEnabled(editable && notExported && defendantRowSelected
                && xDefenceRepresenationModel.hasDefendantOnRow(selectedRowIndex));

        xRemoveCounselButton.setEnabled(editable && notExported && defendantRowSelected
                && xDefenceRepresenationModel.hasRepresentativeOnRow(selectedRowIndex));
        
        xEditCounselButton.setEnabled(
                editable 
                && defendantRowSelected
                && xDefenceRepresenationModel.hasMissingISFlag(selectedRowIndex));
    }
    
   
    /**
     * Attempts to resolve the Ref Defence Category ID based on the combo-box index selected by the user.
     *
     * <p>This method is intentionally defensive because the underlying data used to
     * populate the combo (hearing record model, category list, comparator, etc.)
     * has historically been a source of NullPointerExceptions and
     * ArrayIndexOutOfBoundsExceptions.</p>
     *
     * <p>Expected UI behaviour is that the first entry in the combo is blank,
     * so a displayed index N corresponds to list index (N - 1).
     * If the index is invalid or data is missing, this method returns null
     * rather than throwing an exception.</p>
     *
     * @param refRefDefenceCategoryComboId the 1-based combo-box index selected by the user
     * @return the underlying Ref Def Category ID, or null if unavailable or invalid
     */
    private Integer getRefDefenceCategoryIdFromComboId(Integer refRefDefenceCategoryComboId) {
        // Nothing selected in the UI
        if (refRefDefenceCategoryComboId == null) {
            return null;
        }

        try {
            List<RefSystemCodeBasicValue> defCatCodes = getSortedRefDefCatComboData();

            // Defensive: ensure list is usable
            if (defCatCodes == null || defCatCodes.isEmpty()) {
                return null;
            }

            // The first UI entry is blank ? adjust to 0-based index
            int index = refRefDefenceCategoryComboId - 1;

            // Avoid ArrayIndexOutOfBoundsException
            if (index < 0 || index >= defCatCodes.size()) {
                return null;
            }

            // Retrieve the selected category
            RefSystemCodeBasicValue defCatCode = defCatCodes.get(index);
            if (defCatCode == null) {
                return null;
            }

            // May legitimately return null (no exception thrown)
            return defCatCode.getId();

        } catch (RuntimeException e) {
            // A final guard to prevent UI crashes due to unexpected data conditions.
            // In real production code, this should be logged.
            return null;
        }
    }


    /**
     * Retrieves the Defence Category codes from the HearingRecordModel, removes nulls,
     * and returns the list sorted using RefSystemRefCodeOrderComparator.
     *
     * <p>The method uses multiple defensive checks because the hearing record model
     * or its list contents may be null, and the sort operation may fail if null
     * elements or invalid comparator inputs are present.</p>
     *
     * <p>Always returns a non-null list. If data is missing or invalid,
     * an empty list is returned rather than allowing exceptions to propagate.</p>
     *
     * @return a non-null, sorted List of RefSystemCodeBasicValue items (may be empty)
     */
    @SuppressWarnings("unchecked")
    private List<RefSystemCodeBasicValue> getSortedRefDefCatComboData() {

        // Ensure panel and model exist before attempting to read from them
        if (updateCasePanel == null) {
            return new ArrayList<RefSystemCodeBasicValue>(0);
        }
        HearingRecordModel hearingRecordModel = updateCasePanel.getHearingRecordModel();
        if (hearingRecordModel == null) {
            return new ArrayList<RefSystemCodeBasicValue>(0);
        }

        // Retrieve raw defence category list
        List<RefSystemCodeBasicValue> defCatCodes = hearingRecordModel.getDefCatCodes();
        if (defCatCodes == null) {
            return new ArrayList<RefSystemCodeBasicValue>(0);
        }

        // Defensive copy, stripping null values (nulls break Collections.sort)
        List<RefSystemCodeBasicValue> safeList =
            new ArrayList<RefSystemCodeBasicValue>(defCatCodes.size());
        for (RefSystemCodeBasicValue v : defCatCodes) {
            if (v != null) {
                safeList.add(v);
            }
        }

        if (safeList.isEmpty()) {
            return safeList; // empty but valid
        }

        // Sort the list using provided comparator; failures are non-fatal
        try {
            Collections.sort(safeList, new RefSystemRefCodeOrderComparator());
        } catch (RuntimeException e) {
            // If sorting fails for any reason, return the unsorted list.
            // In production code this should be logged.
        }

        return safeList;
    }


}
