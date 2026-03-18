package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.AttendeeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
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
 * @author Frederik Vandendriessche, Sherie De Silva
 * @version 1.0 Frederik Vandendriessche Iteration 1
 * @version 1.1 Sherie De Silva, Frederik Vandendriessche Itertain 2
 */
public class UpdateCourtStaffCaseData extends JPanel {
    private final Logger log = CSServices.getLogger(UpdateCourtStaffCaseData.class);

    private final UpdateCasePanel uc;

    private final UpdateCourtStaffModel courtStaffModel;

    private JList courtClercksLst;

    private JList usherLst;

    private JButton remUsherButton;

    private JButton addCourtClerkButton;

    private JButton remCourtClerkButton;

    private JButton addUsherButton;

    private XTable historyTable;

    private boolean courtStaffForSittingPrompted = false;

    private boolean courtStaffForSitting = false;

    public void setModified() {
        this.uc.setModified(true);
    }

    public UpdateCourtStaffCaseData(UpdateCasePanel iUC) throws CSRecoverableException {
        try {
            uc = iUC;
            courtStaffModel = new UpdateCourtStaffModel(uc.hhv.getStaffValues(), uc.hhv.getAttendeeHistoryValues(),
                    uc.scheduleHearingStartTime);
            // this is not the history table model!

            jbInit();

            stepUpdateViewState();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex);
            throw new CSRecoverableException("key", "logmessage", ex);
        }
    }

    private void jbInit() throws Exception {
        XAction remUsherAction = new RemoveUsherAction();
        XAction remCourtClerkAction = new RemoveCourtClerkAction();
        XAction addUserAction = XhibitActions.getAction(uc.ucd.xac, XhibitActions.OpenAddUsher);
        XAction addCourtClerkAction = XhibitActions.getAction(uc.ucd.xac, XhibitActions.OpenAddCourtClerk);

        addUserAction.setCaller(this);
        addCourtClerkAction.setCaller(this);

        remUsherButton = new JButton(remUsherAction);
        addUsherButton = new JButton(addUserAction);
        remCourtClerkButton = new JButton(remCourtClerkAction);
        addCourtClerkButton = new JButton(addCourtClerkAction);

        setToolTipText(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
                "updateCourtStaffCasePropertiesToolTip"));
        setLayout(new BorderLayout());
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();

        final JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setPreferredSize(new Dimension(350, 180));
        inputPanel.setBorder(BorderFactory.createTitledBorder(loweredbevel, XHIBITConstant.getResource(
                XhibitBundles.MaintainHearingHeader, "borderTitleCourtStaff")));
        GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;

        final JLabel courtClercksLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
                "lblCourtClerk"));
        inputPanel.add(courtClercksLbl, gbc);

        gbc.gridwidth = 2;

        final JScrollPane courtClercksSp = new JScrollPane(getCourtClerksLst());
        courtClercksSp.setMinimumSize(new Dimension(175, 70));
        inputPanel.add(courtClercksSp, gbc);
        final JLabel ushersLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblUsher"));
        gbc.gridwidth = 1;
        inputPanel.add(ushersLbl, gbc);

        final JScrollPane ushersSp = new JScrollPane(getUsherLst());
        ushersSp.setMinimumSize(new Dimension(177, 70));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(ushersSp, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(XHIBITConstant.getSpacer(), gbc);

        JPanel ccPanel = new JPanel();
        ccPanel.add(addCourtClerkButton, gbc);
        ccPanel.add(remCourtClerkButton, gbc);

        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(ccPanel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(XHIBITConstant.getSpacer(), gbc);

        JPanel usherPanel = new JPanel();
        usherPanel.add(addUsherButton, gbc);
        usherPanel.add(remUsherButton);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(usherPanel, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        inputPanel.add(XHIBITConstant.getSpacer(), gbc);

        this.add(inputPanel, BorderLayout.NORTH);

        // ======================================================================================

        final JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder(loweredbevel, XHIBITConstant.getResource(
                XhibitBundles.MaintainHearingHeader, "borderTitleCourtStaffHistory")));

        historyTable = XTableFactory.getInstance().createMultiLineTable(
                courtStaffModel.getCourtStaffHistoryTableModel());
        historyTable.setRowSelectionAllowed(false);
        historyTable.setColumnSelectionAllowed(false);
        historyTable.setPreferredScrollableViewportSize(new Dimension(300, 200));
        final JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setMinimumSize(new Dimension(300, 200));

        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        this.add(historyPanel, BorderLayout.SOUTH);
    }

    public JList getCourtClerksLst() {
        if (courtClercksLst == null) {
            courtClercksLst = new JList(courtStaffModel.getCourtClerkModel());
            ListSelectionModel lsm = courtClercksLst.getSelectionModel();
            lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            courtClercksLst.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return courtClercksLst;
    }

    public JList getUsherLst() {
        if (usherLst == null) {
            usherLst = new JList(courtStaffModel.getUsherModel());
            ListSelectionModel lsm = usherLst.getSelectionModel();
            lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            usherLst.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    stepUpdateViewState();
                }
            });
        }
        return usherLst;
    }

    public UpdateCourtStaffModel getUpdateCourtStaffModel() {
        return this.courtStaffModel;
    }

    public void stepDeactivate() throws HearingScheduleException, UserCancelException {
        final boolean courtClerksAdded = ((courtStaffModel.addedCourtClerks != null) && (courtStaffModel.addedCourtClerks
                .size() > 0));
        final boolean courtClerksRemoved = ((courtStaffModel.removedCourtClerks != null) && (courtStaffModel.removedCourtClerks
                .size() > 0));
        final boolean ushersAdded = ((courtStaffModel.addedUshers != null) && (courtStaffModel.addedUshers.size() > 0));
        final boolean ushersRemoved = ((courtStaffModel.removedUshers != null) && (courtStaffModel.removedUshers.size() > 0));

        if (courtClerksAdded || courtClerksRemoved) {
            // Ask if want same court staff for sitting
            courtStaffSittingPrompt();

            // ADDING COURT CLERKS
            if (courtClerksAdded) {
                Collection attendeesToAdd = createAttendees(courtStaffModel.addedCourtClerks, courtStaffForSitting);

                if (attendeesToAdd.size() > 0) {
                    uc.getHearingScheduleBD().addAttendees(uc.scheduledHearingId, attendeesToAdd, XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                }
            }

            // REMOVING COURT CLERKS
            if (courtClerksRemoved) {
                Collection attendeesToRemove = createAttendees(courtStaffModel.removedCourtClerks, courtStaffForSitting);

                if (attendeesToRemove.size() > 0) {
                    uc.getHearingScheduleBD().removeAttendees(uc.scheduledHearingId, attendeesToRemove,
                            new Boolean(courtStaffForSitting));
                }
            }
        }

        if (ushersAdded || ushersRemoved) {
            // Ask if want same court staff for sitting
            courtStaffSittingPrompt();

            // ADDING USHERS
            if (ushersAdded) {
                Collection ushersToAdd = createAttendees(courtStaffModel.addedUshers, courtStaffForSitting);

                if (ushersToAdd.size() > 0) {
                    uc.getHearingScheduleBD().addAttendees(uc.scheduledHearingId, ushersToAdd, XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                }
            }

            // REMOVING USHERS
            if (ushersRemoved) {
                Collection ushersToRemove = createAttendees(courtStaffModel.removedUshers, courtStaffForSitting);

                if (ushersToRemove.size() > 0) {
                    uc.getHearingScheduleBD().removeAttendees(uc.scheduledHearingId, ushersToRemove,
                            new Boolean(courtStaffForSitting));
                }
            }
        }
    }

    /**
     * courtStaffSittingPrompt() - Ask user if newly added court staff are for
     * hearing or sitting
     */
    private void courtStaffSittingPrompt() throws UserCancelException {
        // Ask user only if no asked before
        if (!courtStaffForSittingPrompted) {
            int clerksForAllCases = JOptionPane.showOptionDialog(this, XHIBITConstant.getResource(
                    XhibitBundles.MaintainHearingHeader, "onCourtStaffChanged.changeOnCaseLevelQuestion"),
                    XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader,
                            "onCourtStaffChanged.changeOnCaseLevelTitle"), JOptionPane.OK_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Current Hearing", "Current and Future" },
                    "Current Hearing");

            switch (clerksForAllCases) {
            case JOptionPane.CLOSED_OPTION:
                uc.cancelledPrompt = true;
                throw new UserCancelException();

            case 0:
                courtStaffForSitting = false;
                break;
            case 1:
                courtStaffForSitting = true;
                break;
            }

            // The user has been prompted for court staff sitting now
            courtStaffForSittingPrompted = true;
        }
    }

    /**
     * Life-cycle method to enable/disable screen components
     */
    public void stepUpdateViewState() {
        final boolean editable = uc.ucd.openingAction.isEditable();
        final boolean notExported = !uc.isExported().booleanValue();

        addCourtClerkButton.setEnabled(editable && notExported);
        addUsherButton.setEnabled(editable && notExported);

        if (editable && notExported) {
            // Check if empty list
            if (getUpdateCourtStaffModel().getCourtClerkModel().getSize() > 0) {
                // Item selected?
                remCourtClerkButton.setEnabled(getCourtClerksLst().getSelectedIndex() > -1);
            } else {
                remCourtClerkButton.setEnabled(false);
            }

            // Check if empty list
            if (getUpdateCourtStaffModel().getUsherModel().getSize() > 0) {
                // Item selected?
                remUsherButton.setEnabled(getUsherLst().getSelectedIndex() > -1);
            } else {
                remUsherButton.setEnabled(false);
            }

        } else {
            remUsherButton.setEnabled(false);
            remCourtClerkButton.setEnabled(false);
        }
    }

    private class RemoveUsherAction extends XAction {
        public RemoveUsherAction() {
            super(XhibitActions.RemoveUsher);
        }

        public void xActionPerformed(ActionEvent e) {
            log.debug("RemoveUsherAction: Caller is UpdateCourtStaffCaseData");
            if (getUsherLst().getSelectedIndex() > -1) {
                UpdateCourtStaffModel model = getUpdateCourtStaffModel();
                PersonValue pv = (PersonValue) model.getUsherModel().elementAt(getUsherLst().getSelectedIndex());
                courtStaffModel.removeUsher(pv);
                stepUpdateViewState();
                setModified();
            }
        }
    }

    private class RemoveCourtClerkAction extends XAction {
        public RemoveCourtClerkAction() {
            super(XhibitActions.RemoveCourtClerk);
        }

        public void xActionPerformed(ActionEvent e) {
            if (getCourtClerksLst().getSelectedIndex() > -1) {
                log.debug("RemoveCourtClerkAction: caller is UpdateCourtStaffCaseData");
                UpdateCourtStaffModel model = getUpdateCourtStaffModel();

                PersonValue pv = (PersonValue) model.getCourtClerkModel().elementAt(
                        getCourtClerksLst().getSelectedIndex());
                courtStaffModel.removeCourtClerk(pv);
                stepUpdateViewState();
                setModified();
            }
        }
    }

    private Collection createAttendees(Collection from, boolean staffForSitting) {
        Collection attendeesToSave = new Vector();

        synchronized (from) {
            Iterator addedIterator = from.iterator();

            while (addedIterator.hasNext()) {
                PersonValue addedPerson = (PersonValue) addedIterator.next();
                AttendeeValue av = new AttendeeValue();
                av.setPerson(addedPerson);
                av.setIsAttendingSubsequentSH(new Boolean(staffForSitting));
                attendeesToSave.add(av);
            }
        }

        return attendeesToSave;
    }
}