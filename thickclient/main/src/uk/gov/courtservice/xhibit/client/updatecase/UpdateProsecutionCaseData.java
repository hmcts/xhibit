package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindLegalRepresentativeDialog;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindLegalRepresentativeModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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
public class UpdateProsecutionCaseData extends JPanel implements ActionListener {
    private static final String X_Action_Remove_ProSAdv = "X_ActionCommand_Remove_ProAdv";

    private static final String X_Action_Remove_RespAdv = "X_ActionCommand_Remove_ResAdv";

    private static final String X_Action_Remove_ObjAdv = "X_ACtion_Remove_ObjAdv";

    private static final String X_Action_Add_ProAdv = "X_Action_Add_ProAdv";

    private static final String X_Action_Add_ResAdv = "X_Action_Add_ResAdv";

    private static final String X_Action_Add_ObjAdv = "X_Action_Add_ObjAdv";

    private final Logger log = CSServices.getLogger(UpdateProsecutionCaseData.class);

    private UpdateCasePanel uc;

    // next fields are used for both 'prosecution' (normal case) and
    // 'respondant' (appeals)
    private XProsecutionRepresentationTableModel genAdvTableModel;

    private XTable genAdvXTable;

    private JButton genAdvAddJButton;

    private JButton genAdvRemoveJButton;

    // always require the add and remove vectors
    private final Collection genAdv_toAdd = new Vector();

    private final Collection genAdv_toRemove = new Vector();

    // next fields are only used for both 'objector' - misc appeals
    private XProsecutionRepresentationTableModel objAdvTableModel;

    private XTable objAdvXTable;

    private JButton objAdvAddJButton;

    private JButton objAdvRemoveJButton;

    // these will only be set with proper values in the constructor
    private Collection objAdv_toAdd = null;

    private Collection objAdv_toRemove = null;

    /**
     * @param uc
     * @throws CSRecoverableException
     */
    public UpdateProsecutionCaseData(UpdateCasePanel _uc) throws CSRecoverableException {
        try {
            uc = _uc;

            if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(uc.hhv)) {
                objAdv_toAdd = new Vector();
                objAdv_toRemove = new Vector();
            }

            jbInit();

            stepUpdateViewState();
        } catch (Exception ex) {
            String errorMessage = "UpdateProsecutionCaseData(UpdateCase uc) threw exception";
            CSRecoverableException CSce = new CSRecoverableException("gui.updateProsecutionCaseData.ConstructorFailed",
                    errorMessage, ex);
            throw (CSce);
        }
    }

    /**
     * Initialized the Prosecution tab of the Case Properties/Maintain Hearing
     * Header component.
     * 
     * @throws CSRecoverableException
     */
    private void jbInit() throws CSRecoverableException {
        final Dimension tableMinSize = new Dimension(425, 125);
        final String colNameCourtClerkInfo = getResource("columnNameInformationCourtClerk");
        // listener for enabling/disabling remove buttons, etc.
        final ListSelectionListener selectionListener = new XTableListSelectionListener();

        setToolTipText(getResource("updateProsecutionCasePropertiesToolTip."
                + (uc.isCriminalAppealHearing() || uc.isMiscAppealCase() ? "appeal" : "normal")));
        setLayout(new GridBagLayout());

        JPanel containerPanel = new JPanel(new GridBagLayout());

        // CASE PROSECUTION / RESPONDENCE
        genAdvAddJButton = new JButton();
        genAdvRemoveJButton = new JButton();

        genAdvAddJButton.setText(getResource("lblSignIn"));
        genAdvAddJButton.addActionListener(this);

        genAdvRemoveJButton.setText(getResource("lblRemove"));
        genAdvRemoveJButton.addActionListener(this);

        Vector genAdvVector;
        String advocateType;

        if (uc.isCriminalAppealHearing() || uc.isMiscAppealCase()) {
            // respondents if appeal
            advocateType = getResource("columnNameRespondentAdvocate");
            genAdvAddJButton.setToolTipText(getResource("signInCounselToolTipText"));
            genAdvAddJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Add_ResAdv);
            genAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_RespAdv);
            genAdvRemoveJButton.setToolTipText(getResource("removeCounselToolTipText"));
            try {
                final Collection genAdvCol = this.uc.hhv.getLegalRepValues();
                genAdvVector = prepareCounselVector(genAdvCol, PersonValue.RESPONDENT);
            } catch (Exception e) {
                throw new CSRecoverableException("gui.updateProsecutionCaseData.getRespondentAdvocates",
                        "Exception occured whilst trying to process respondent advocates from the HearingHeaderValue",
                        e);
            }
            if (uc.ucd.internalDebug)
                log.debug("Number of Respondent Advocates: " + genAdvVector.size());
            genAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_RespAdv);
        } else {
            // reg prosecution when non-appeal
            advocateType = getResource("columnNameProsecutionAdvocate");
            // bug X54354, corrected the tool-tip resource name
            genAdvAddJButton.setToolTipText(getResource("signInCounselToolTipText"));
            genAdvAddJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Add_ProAdv);
            genAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_ProSAdv);
            genAdvRemoveJButton.setToolTipText(getResource("removeCounselToolTipText"));
            try {
                final Collection genAdvCol = this.uc.hhv.getLegalRepValues();
                genAdvVector = prepareCounselVector(genAdvCol, PersonValue.PROSECUTION);
            } catch (Exception e) {
                throw new CSRecoverableException("gui.updateProsecutionCaseData.getProsecutionAdvocates",
                        "Exception occured whilst trying to process prosecution advocates from the HearingHeaderValue",
                        e);
            }
            if (uc.ucd.internalDebug)
                log.debug("Number of Prosecution Advocates: " + genAdvVector.size());
            genAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_ProSAdv);
        }

        genAdvTableModel = new XProsecutionRepresentationTableModel(advocateType, colNameCourtClerkInfo, genAdvVector);
        genAdvXTable = XTableFactory.getInstance().createMultiLineTable(genAdvTableModel);

        // ensure that only one row can be selected at a time
        final ListSelectionModel genAdvLSM = new DefaultListSelectionModel();
        genAdvLSM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        genAdvLSM.addListSelectionListener(selectionListener);
        genAdvXTable.setSelectionModel(genAdvLSM);

        JScrollPane scrollPane1 = new JScrollPane(genAdvXTable);
        scrollPane1.setMinimumSize(tableMinSize);

        JPanel genAdvChangePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc4 = XHIBITConstant.getDefaultGridBagConstraints();
        gbc4.gridheight = 2;
        gbc4.gridwidth = 1;
        gbc4.gridx = 0;
        gbc4.gridy = 0;

        genAdvChangePanel.add(scrollPane1, gbc4);

        gbc4.gridheight = 1;
        gbc4.gridx++;
        gbc4.anchor = GridBagConstraints.CENTER;
        genAdvChangePanel.add(genAdvAddJButton, gbc4);
        gbc4.gridy++;
        genAdvChangePanel.add(genAdvRemoveJButton, gbc4);

        GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;

        containerPanel.add(genAdvChangePanel, gbc);

        if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(uc.hhv)) { // objectors
            // if
            // appeal
            if (uc.ucd.internalDebug)
                log.debug("this is a misc appeal case!!");

            objAdvAddJButton = new JButton();
            objAdvAddJButton.setText(getResource("lblSignIn"));
            objAdvAddJButton.setToolTipText(getResource("signInCounselToolTipText"));
            objAdvAddJButton.addActionListener(this);
            objAdvAddJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Add_ObjAdv);

            objAdvRemoveJButton = new JButton();
            objAdvRemoveJButton.setText(getResource("lblRemove"));
            objAdvRemoveJButton.setToolTipText(getResource("removeCounselToolTipText"));
            objAdvRemoveJButton.addActionListener(this);
            objAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_ObjAdv);

            Vector objAdvVector;

            try {
                Collection objAdvCol = this.uc.hhv.getLegalRepValues();
                objAdvVector = this.prepareCounselVector(objAdvCol, PersonValue.OBJECTOR);
            } catch (Exception e) {
                String msgKey = "gui.updateProsecutionCaseData.getObjectorAdvocates";
                String msgStr = "Exception occured whilst trying to process objector advocates from the HearingHeaderValue";
                log.error(msgStr);
                log.error(e);
                CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, e);
                throw (csre);
            }
            if (uc.ucd.internalDebug)
                log.debug("Number of Respondent Advocates: " + genAdvVector.size());
            objAdvRemoveJButton.setActionCommand(UpdateProsecutionCaseData.X_Action_Remove_ObjAdv);

            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy++;

            String tmp = getResource("columnNameObjectorAdvocate");

            objAdvTableModel = new XProsecutionRepresentationTableModel(tmp, colNameCourtClerkInfo, objAdvVector);
            objAdvXTable = XTableFactory.getInstance().createMultiLineTable(objAdvTableModel);

            // ensure that only one row can be selected at a time
            final ListSelectionModel objAdvLSM = new DefaultListSelectionModel();
            objAdvLSM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            objAdvLSM.addListSelectionListener(selectionListener);
            objAdvXTable.setSelectionModel(objAdvLSM);

            JScrollPane scrollPane2 = new JScrollPane(objAdvXTable);
            scrollPane2.setMinimumSize(tableMinSize);

            JPanel objAdvChangePanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc5 = (GridBagConstraints) XHIBITConstant.getDefaultGridBagConstraints().clone();
            gbc5.gridheight = 2;
            gbc5.gridwidth = 1;
            gbc5.gridx = 0;
            gbc5.gridy = 0;

            objAdvChangePanel.add(scrollPane2, gbc5);

            gbc5.gridheight = 1;
            gbc5.gridx++;
            gbc5.anchor = GridBagConstraints.CENTER;
            objAdvChangePanel.add(objAdvAddJButton, gbc5);
            gbc5.gridy++;
            objAdvChangePanel.add(objAdvRemoveJButton, gbc5);

            gbc.gridx++;
            containerPanel.add(objAdvChangePanel, gbc);
        }

        add(containerPanel);
    }

    /**
     * Life-cycle method to enable/disable screen components
     */
    protected void stepUpdateViewState() {
        final boolean enabled = uc.ucd.openingAction.isEditable();
        final boolean notExported = !uc.isExported().booleanValue();

        genAdvAddJButton.setEnabled(enabled && notExported);
        // only enable the remove button if row selected
        genAdvRemoveJButton.setEnabled(enabled && notExported && (genAdvXTable.getSelectedRow() != -1));

        // PRE00123, also disable the objector add/remove buttons
        if (objAdvAddJButton != null) {
            objAdvAddJButton.setEnabled(enabled && notExported);
        }

        if (objAdvRemoveJButton != null) {
            // only enable the remove button if row selected
            objAdvRemoveJButton.setEnabled(enabled && notExported && (objAdvXTable.getSelectedRow() != -1));
        }
    }

    private void removeFromVectors(Vector selectedRow, Collection toAdd, Collection toRemove) {
        final Object selectedSH = selectedRow.get(3);

        boolean removedFromNew = false;
        if ((toAdd != null) && (toAdd.size() > 0))
            removedFromNew = toAdd.remove(selectedSH);

        if (!removedFromNew)
            toRemove.add(selectedRow);
    }

    /**
     * Action listening method for the Counsel Sign In & Removal Button(s)
     * 
     * @param anAction
     */
    public void actionPerformed(ActionEvent anAction) {
        final String command = anAction.getActionCommand();

        if (command != null) {
            if (command.equals(UpdateProsecutionCaseData.X_Action_Remove_RespAdv)
                    || command.equals(UpdateProsecutionCaseData.X_Action_Remove_ProSAdv)) {
                final int selectedIndex = genAdvXTable.getSelectedRow();

                if (selectedIndex > -1) {
                    final Vector selectedRow = genAdvTableModel.getSelectedRow(selectedIndex);
                    removeFromVectors(selectedRow, genAdv_toAdd, genAdv_toRemove);
                    genAdvTableModel.removeRow(selectedIndex);

                    uc.setModified(true);
                }
            } else if (command.equals(UpdateProsecutionCaseData.X_Action_Add_ResAdv)
                    || command.equals(UpdateProsecutionCaseData.X_Action_Add_ProAdv)) {
                final Vector v = findSHLegRepBasicValue();
                if (v != null) {
                    final RefLegalRepresentativeBasicValue rl = (RefLegalRepresentativeBasicValue) v.elementAt(0);
                    final SHLegRepBasicValue sh = (SHLegRepBasicValue) v.elementAt(1);

                    if (command.equals(UpdateProsecutionCaseData.X_Action_Add_ResAdv))
                        sh.setLegalRole(PersonValue.RESPONDENT);
                    else
                        sh.setLegalRole(PersonValue.PROSECUTION);

                    genAdv_toAdd.add(sh);
                    genAdvTableModel.addRow(rl, sh);

                    uc.setModified(true);
                }
            } else if (command.equals(UpdateProsecutionCaseData.X_Action_Add_ObjAdv)) {
                final Vector v = findSHLegRepBasicValue();

                if (v != null) {
                    final RefLegalRepresentativeBasicValue rl = (RefLegalRepresentativeBasicValue) v.elementAt(0);
                    final SHLegRepBasicValue sh = (SHLegRepBasicValue) v.elementAt(1);

                    sh.setLegalRole(PersonValue.OBJECTOR);

                    objAdv_toAdd.add(sh);
                    objAdvTableModel.addRow(rl, sh);

                    uc.setModified(true);
                }
            } else if (command.equals(UpdateProsecutionCaseData.X_Action_Remove_ObjAdv)) {
                final int selectedIndex = objAdvXTable.getSelectedRow();

                if (selectedIndex > -1) {
                    final Vector selectedRow = objAdvTableModel.getSelectedRow(selectedIndex);
                    removeFromVectors(selectedRow, objAdv_toAdd, objAdv_toRemove);
                    objAdvTableModel.removeRow(selectedIndex);

                    uc.setModified(true);
                }
            }
        }
    }

    /**
     * This method takes a collection of counsel value objects (types: ...) and
     * transforms this into a vector of vectors. The inner vector contains idx=0 :
     * RefLegalRepresentativeBasicValue lrv idx=1 : null (not a SHLegalRepValue -
     * this is only for the ones to be added)
     * 
     * @param counselCollection
     * @return
     */
    private Vector prepareCounselVector(Collection counselCollection, String personValueStaticRoleField) {
        if (uc.ucd.internalDebug)
            log.debug("---- prepareCounselVector >>> ");
        Vector preparedCounselVector = new Vector();
        Iterator prAdIt = counselCollection.iterator();

        while (prAdIt.hasNext()) {
            LegalRepValue legalRepValue = (LegalRepValue) prAdIt.next();
            if (uc.ucd.internalDebug) {
                log.debug("legalRepValue = " + legalRepValue.toString());
                log.debug("legalRepValue.getLegalRep()" + legalRepValue.getLegalRep());
            }
            PersonValue legalRepPerson = legalRepValue.getLegalRep();
            if (legalRepPerson != null) {
                String legalRole = legalRepPerson.getPersonType();
                log.debug("-- " + legalRepPerson.getFullName());
                if (personValueStaticRoleField.equals(legalRole)) {
                    Vector v = new Vector();
                    RefLegalRepresentativeBasicValue lrv = new RefLegalRepresentativeBasicValue();
                    lrv.setSurname(legalRepValue.getLegalRep() == null ? " no legal rep set " : (legalRepValue
                            .getLegalRep().getFullName() == null ? "n/a" : legalRepValue.getLegalRep().getFullName()));
                    v.add(lrv); // 0
                    v.add(legalRepValue.getLegalRep().getId()); // 1
                    v.add(legalRepValue.getSHLegRep().getId()); // 2 - to
                    // facilitate
                    // remove
                    v.add(legalRepValue.getSHLegRep()); // 3 - the whole object
                    // to remove
                    // X54573 - need to be able to get the court clerk info
                    v.add(legalRepValue.getCcInfo()); // 4 - the court clerk
                    // info

                    if (uc.ucd.internalDebug)
                        log.debug("the vector prepared is : " + v.toString());
                    preparedCounselVector.add(v);
                }
            } else {
                if (uc.ucd.internalDebug)
                    log.debug("***");
            }
        }
        if (uc.ucd.internalDebug)
            log.debug("---- prepareCounselVector <<<   ");
        return preparedCounselVector;
    }

    public Vector<Integer> getLegalReps() {
        Vector<Integer> reps = 
            new Vector<Integer>();
        
        if (objAdvTableModel != null) {
            Vector rows = objAdvTableModel.getData();
            Iterator iter = rows.iterator();
            while (iter.hasNext()) {
                Vector row = (Vector)iter.next();
                Integer legalRepId = 
                    (Integer)row.elementAt(1);
                SHLegRepBasicValue sHLegRep =
                    (SHLegRepBasicValue)row.elementAt(3);
                
                if (legalRepId != null) {
                    reps.add(legalRepId);
                } else if (sHLegRep != null && sHLegRep.getRefLegalRepID() != null) {
                    reps.add(sHLegRep.getRefLegalRepID());
                }
            }
        }
        
        if (genAdvTableModel != null) {
            Vector rows = genAdvTableModel.getData();
            Iterator iter = rows.iterator();
            while (iter.hasNext()) {
                Vector row = (Vector)iter.next();
                Integer legalRepId = 
                    (Integer)row.elementAt(1);
                SHLegRepBasicValue sHLegRep =
                    (SHLegRepBasicValue)row.elementAt(3);
                
                if (legalRepId != null) {
                    reps.add(legalRepId);
                } else if (sHLegRep != null && sHLegRep.getRefLegalRepID() != null) {
                    reps.add(sHLegRep.getRefLegalRepID());
                }
            }
        }
        
        return reps;
    }
    
    public void stepValidate() throws CSValidationException {
        // empty
    }

    public void stepDeactivate() throws HearingScheduleException, CSRecoverableException {
        try {
            // these can never be null!
            if ((genAdv_toAdd.size() > 0) || (genAdv_toRemove.size() > 0)) {
                if (uc.ucd.internalDebug)
                    log.debug("prosecution advocate data changed. saveing now.");
                Collection toAdd = new Vector();
                Collection toRemove = new Vector();
                Collection added = Collections.synchronizedCollection(genAdv_toAdd);

                synchronized (added) {
                    Iterator addedIt = added.iterator();
                    while (addedIt.hasNext()) {
                        SHLegRepBasicValue s = (SHLegRepBasicValue) addedIt.next();
                        if (uc.ucd.internalDebug)
                            log.debug("the shlegrep to added is [" + s + "].");
                        toAdd.add(s);
                    }
                }
                try {
                    if (toAdd.size() > 0) {
                        uc.getHearingScheduleBD().addLegalReps(uc.scheduledHearingId, toAdd,
                                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                        genAdv_toAdd.removeAll(toAdd);
                        if (uc.ucd.internalDebug)
                            log.debug("added " + toAdd.size() + " representations.");
                    } else {
                        if (uc.ucd.internalDebug)
                            log.debug("no prosection to add");
                    }
                } catch (Exception e) {
                    CSRecoverableException csre = new CSRecoverableException(
                            "updateCase.save_Prosecution_Exception_SignIn_Counsel",
                            "Exception whilst adding prosecution/respondent advocates", e);
                    throw (csre);
                }

                Collection removed = Collections.synchronizedCollection(genAdv_toRemove);
                Iterator remIt = removed.iterator();
                while (remIt.hasNext()) {
                    Vector v = (Vector) remIt.next();
                    if (v != null) {
                        if (uc.ucd.internalDebug)
                            log.debug("the representation vector for removal is " + v);

                        try {

                            SHLegRepBasicValue sh = (SHLegRepBasicValue) v.elementAt(3);
                            if (uc.ucd.internalDebug)
                                log.debug("the shlegrep to remove is [" + sh + "].");
                            toRemove.add(sh);
                        } catch (Exception ee) {
                            if (uc.ucd.internalDebug) {
                                log
                                        .debug("Exception when getting the ShLegRepBasicValue for a Counsel to delete - so don't delete.");
                            }
                        }
                    }
                }

                try {
                    if (toRemove.size() > 0) {
                        uc.getHearingScheduleBD().removeLegalReps(uc.scheduledHearingId, uc.hhv.getHhCase().getId(), toRemove);
                        genAdv_toRemove.removeAll(toRemove);
                        toRemove.clear();
                    } else if (uc.ucd.internalDebug)
                        log.debug("no representation to remove.");
                } catch (Exception e) {
                    CSRecoverableException csre = new CSRecoverableException(
                            "updateCase.save_Prosecution_Exception_Removing_Counsel",
                            "Exception whilst removing prosecution/respondent advocates", e);
                    throw (csre);
                }
            } // end of prosection or repsondent saving

            // now check for objectors
            // do need to check for null in this instance!
            if (((objAdv_toAdd != null) && (objAdv_toAdd.size() > 0))
                    || ((objAdv_toRemove != null) && (objAdv_toRemove.size() > 0))) {
                if (uc.ucd.internalDebug)
                    log.debug("objector advocate data changed. saveing now.");
                Collection toAdd = new Vector();
                Collection toRemove = new Vector();
                Collection added = Collections.synchronizedCollection(objAdv_toAdd);

                synchronized (added) {
                    Iterator addedIt = added.iterator();
                    while (addedIt.hasNext()) {
                        SHLegRepBasicValue s = (SHLegRepBasicValue) addedIt.next();
                        if (uc.ucd.internalDebug)
                            log.debug("the shlegrep to added is [" + s + "].");
                        toAdd.add(s);
                    }
                }

                Collection removed = Collections.synchronizedCollection(objAdv_toRemove);
                Iterator remIt = removed.iterator();
                while (remIt.hasNext()) {
                    Vector v = (Vector) remIt.next();
                    if (v != null) {
                        if (uc.ucd.internalDebug)
                            log.debug("the representation vector for removal is " + v);
                        SHLegRepBasicValue sh = (SHLegRepBasicValue) v.elementAt(3);
                        if (uc.ucd.internalDebug)
                            log.debug("the shlegrep to remove is [" + sh + "].");
                        toRemove.add(sh);
                    }
                }

                try {
                    if (toAdd.size() > 0) {
                        uc.getHearingScheduleBD().addLegalReps(uc.scheduledHearingId, toAdd,
                                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                        objAdv_toAdd.removeAll(toAdd);
                        if (uc.ucd.internalDebug)
                            log.debug("added " + toAdd.size() + " representations.");
                    } else {
                        if (uc.ucd.internalDebug)
                            log.debug("no objector to add");
                    }
                } catch (Exception e) {
                    CSRecoverableException csre = new CSRecoverableException(
                            "updateCase.save_Objector_Exception_SignIn_Counsel", "Exception whilst adding objectors", e);
                    throw (csre);
                }
                try {
                    if (toRemove.size() > 0) {
                        uc.getHearingScheduleBD().removeLegalReps(uc.scheduledHearingId, uc.hhv.getHhCase().getId(), toRemove);
                        genAdv_toRemove.removeAll(toRemove);
                        toRemove.clear();
                    } else if (uc.ucd.internalDebug)
                        log.debug("no representation to remove.");
                } catch (Exception e) {
                    CSRecoverableException csre = new CSRecoverableException(
                            "updateCase.save_Prosecution_Exception_Removing_Counsel",
                            "Exception whilst removing objectors", e);
                    throw (csre);
                }
            } // end of prosection or objector saving
            if (uc.ucd.internalDebug)
                log.debug("ready applying changes in representation for the sheduled hearing/case.");
        } catch (CSRecoverableException hse) {
            throw (hse);
        } catch (Exception e) {
            log.error(e);
            CSRecoverableException csre = new CSRecoverableException("xhibit.error.unexpected",
                    "---exception during prosecuftion case data step deactivate", e);
            throw (csre);
        }
    }

    /**
     * This private method link the Case Properties / Hearing Header component
     * with the FindLegalRepresentativeDialog
     * 
     * @return a vector containing a RefLegalRepresentativeBasicValue and a
     *         SHLegRepBasicValue representing the counsel member to sign in.
     */
    private Vector findSHLegRepBasicValue() {
        Vector v = null;

        try {
            FindLegalRepresentativeModel flrModel = new FindLegalRepresentativeModel();

            flrModel.setXac((XhibitApplicationController) uc.ucd.openingAction.getController());
            flrModel.setDisableInPerson(true);
            flrModel.setDisableNonAttendance(true);
            FindLegalRepresentativeDialog flrDialog = new FindLegalRepresentativeDialog(uc.ucd.getParentFrame(),
                    flrModel);
            flrDialog.show();

            if (flrDialog.isOkClicked()) {
                RefLegalRepresentativeBasicValue rl = new RefLegalRepresentativeBasicValue();
                SHLegRepBasicValue sh = new SHLegRepBasicValue();

                if (uc.ucd.internalDebug)
                    log.debug("trying to add a legal rep to the genAdvListModel.");
                FindLegalRepresentativeTableRowModel flrtrModel = flrModel.getFindLegalRepresentativeTableRowModel();
                if (uc.ucd.internalDebug) {
                    log.debug("the model is:");
                    flrtrModel.printModel();
                }

                rl.setLegalRepType(flrtrModel.getLegalRepType());
                rl.setFirstName(flrtrModel.getFirstName());
                rl.setSurname(flrtrModel.getSurname());

                if ((rl.getFirstName().trim().length() == 0) && (rl.getSurname().trim().length() == 0)) {
                    rl.setSurname(flrtrModel.getFullName());
                }

                sh.setRefLegalRepID(flrtrModel.getLegalRepId());
                sh.setScheduledHearingID(uc.scheduledHearingId);
                sh.setCcInfoID(null);
                sh.setCrestSequenceNo(null);
                sh.setIsSignIn("Y");
                sh.setRefDefenceCategoryID(null);
                sh.setRefLegalRepID(flrtrModel.getLegalRepId());

                if (uc.ucd.internalDebug)
                    log.debug("the SH is :" + sh.toString());
                if (flrtrModel.getLegalRepType() == CounselFacilitiesHelper.BARRADIO) {
                    sh.setSolFirmOrRefLegalRep("L");
                } else {
                    sh.setSolFirmOrRefLegalRep("S");
                    sh.setRefSolicitorFirmID(flrtrModel.getChambersId());
                }
                v = new Vector();
                v.add(rl);
                v.add(sh);
            }
        } catch (Exception e) {
            log.debug("UpdateProsecutionCaseData encountered Exception whilst running FindLegalRepresentativeDialog");
            log.error(e);
        }

        return v;
    }

    private String getResource(final String resourceKey) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader, resourceKey);
    }

    /**
     * Private class used to act as the model used by the tables displayed on
     * screen.
     */
    private class XProsecutionRepresentationTableModel extends XHIBITTableModel {
        /**
         * Only constructor that sets the two column names, and populates itself
         * from the passed in <code>Collection</code> which is copied to the
         * internal <code>Vector</code>
         * 
         * @param columnOneTitle
         * @param columnTwoTitle
         * @param data
         */
        public XProsecutionRepresentationTableModel(final String columnOneTitle, final String columnTwoTitle,
                final Collection data) {
            this.setColumnNames(new String[] { columnOneTitle, columnTwoTitle });

            if (data != null) {
                final Iterator it = data.iterator();

                while (it.hasNext()) {
                    this.getData().add(it.next());
                }
            }
        }

        /**
         * Add a new row to the table represented by this instance of this
         * model, constructed using the passed in value objects
         * 
         * @param value
         * @param shBV
         */
        public void addRow(RefLegalRepresentativeBasicValue value, SHLegRepBasicValue shBV) {
            final Vector newRow = new Vector();

            newRow.add(value); // 0
            newRow.add(value.getId()); // 1
            newRow.add(shBV.getId()); // 2 - to facilitate remove
            newRow.add(shBV); // 3 - the whole object to remove
            newRow.add(null); // 4 - the court clerk info

            synchronized (data) {
                data.add(newRow);
                fireTableRowsInserted(data.size() - 1, data.size());
            }
        }

        public void removeRow(final int index) {
            if (index > -1) {
                this.getData().remove(index);
                this.fireTableRowsDeleted(index, index);
            }
        }

        /**
         * Overridden method to allow for the different ordering of the columns
         * on screen instead of the ordering in the <code>Vector</code>
         * 
         * @param row
         * @param col
         * @return
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(final int row, int col) {
            // the ordering is different in the data set
            if (col == 1)
                col = 4;

            Object obj = super.getValueAt(row, col);

            if (col == 0)
                obj = getFormattedName((RefLegalRepresentativeBasicValue) obj);

            if (col == 4)
                obj = getCCInfo(obj);

            return obj;
        }

        /**
         * Return the <code>Vector</code> found at the specified index
         * 
         * @param index
         * @return
         */
        public Vector getSelectedRow(final int index) {
            return (Vector) ((index == -1) ? null : getData().get(index));
        }
        
        /**
         * Return the formatted name of the legal representative passed in
         * 
         * @param rjbv
         * @return
         */
        private String getFormattedName(final RefLegalRepresentativeBasicValue rjbv) {
            final StringBuffer formattedName = new StringBuffer();

            if (rjbv.getTitle() != null)
                formattedName.append(rjbv.getTitle()).append(' ');

            if (rjbv.getInitials() != null)
                formattedName.append(rjbv.getInitials()).append(' ');

            if (rjbv.getFirstName() != null)
                formattedName.append(rjbv.getFirstName()).append(' ');

            if (rjbv.getMiddleName() != null)
                formattedName.append(rjbv.getMiddleName()).append(' ');

            if (rjbv.getSurname() != null)
                formattedName.append(rjbv.getSurname());

            final String object = formattedName.toString().trim();
            return ((object.length() == 0) ? "name not set" : object);
        }

        /**
         * Get the court clerk information for the passed in id. If no id passed
         * (i.e. is <i>null</i>) then the <code>String</code> constant "---"
         * will be returned instead.
         * 
         * @param ccInfoIdObj
         * @return
         */
        private String getCCInfo(final Object ccInfoIdObj) {
            if (ccInfoIdObj != null) {
                String ccInfoIdString = ((String) ccInfoIdObj).trim();

                if (ccInfoIdString.length() > 0) {
                    return ResourceBundleHelper
                            .getResource(XhibitBundles.CourtClerkInformationMessages, ccInfoIdString);
                }
            }

            return "---";
        }
    }

    /**
     * Private class used to act as a listener to enable/disable buttons, etc.
     */
    private class XTableListSelectionListener implements ListSelectionListener {
        /**
         * Required implemented method that calls the stepUpdateViewState method
         * 
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         * @see uk.gov.courtservice.xhibit.client.updatecase.UpdateProsecutionCaseData#stepUpdateViewState()
         */
        public void valueChanged(ListSelectionEvent e) {
            stepUpdateViewState();
        }
    }
}
