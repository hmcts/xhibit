package uk.gov.courtservice.xhibit.client.skeletonschedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoDirectionsForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSessionSelector;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ComponentUtil;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ControllerUtil;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.PrimitiveUtil;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.listeners.DoubleClickListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TablePopupListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TableRowListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: SkeletonShedulePanel
 * </p>
 * <p>
 * Description: The view skeleton schedule panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public class SkeletonSchedulePanel extends XPanel implements PrintFunction {

    /*
     * Resource Key Constants
     */

    private static final String SKELETONSCHEDULE_TITLE_LABEL_KEY = "skeletonschedule.title.label";

    private static final String SKELETONSCHEDULE_CASEREF_LABEL_KEY = "skeletonschedule.case.label";

    private static final String SKELETONSCHEDULE_DEFENDANTS_LABEL_KEY = "skeletonschedule.defendants.label";

    private static final String SKELETONSCHEDULE_COURT_LABEL_KEY = "skeletonschedule.court.label";

    private static final String SKELETONSCHEDULE_WEEK_LABEL_KEY = "skeletonschedule.week.label";

    private static final String SKELETONSCHEDULE_WEEK_FORMAT_KEY = "skeletonschedule.week.format";

    private static final String SKELETONSCHEDULE_TRIALTIME_LABEL_KEY = "skeletonschedule.trialtime.label";

    private static final String SKELETONSCHEDULE_TRIALTIME_FORMAT_KEY = "skeletonschedule.trialtime.format";

    private static final String SKELETONSCHEDULE_NO_WITNESS_TITLE_KEY = "skeletonschedule.no.witness.title";

    private static final String SKELETONSCHEDULE_NO_WITNESS_MESSAGE_KEY = "skeletonschedule.no.witness.message";

    private static final String SKELETONSCHEDULE_MAX_TT_EST_TITLE_KEY = "skeletonschedule.max.time.trial.estimate.title";

    private static final String SKELETONSCHEDULE_MAX_TT_EST_MESSAGE_KEY = "skeletonschedule.max.time.trial.estimate.message";

    private static final String SKELETONSCHEDULE_MAX_TT_ESTIMATE_KEY = "skeletonschedule.max.time.trial.estimate";

    /*
     * Format
     */

    private static DecimalFormat trialTimeEstimateFormat;

    private static DecimalFormat currentWeekFormat;

    private static WitnessSessionSelector selector = WitnessFactory.getInstance().getWitnessSessionSelector();

    /*
     * Logger
     */

    private static final Logger log = CSServices.getLogger(SkeletonSchedulePanel.class);

    /*
     * Panel Constraints
     */

    private static final GridBagConstraints caseDetailsPanelConstraints = new GridBagConstraints();
    static {
        caseDetailsPanelConstraints.gridx = 0;
        caseDetailsPanelConstraints.gridy = 0;
        caseDetailsPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        caseDetailsPanelConstraints.weightx = 1.0;
        caseDetailsPanelConstraints.insets = new Insets(4, 4, 2, 4);
    }

    private static final GridBagConstraints trialTimeEstimatePanelConstraints = new GridBagConstraints();
    static {
        trialTimeEstimatePanelConstraints.gridx = 0;
        trialTimeEstimatePanelConstraints.gridy = 1;
        trialTimeEstimatePanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        trialTimeEstimatePanelConstraints.weightx = 1.0;
        trialTimeEstimatePanelConstraints.insets = new Insets(2, 4, 2, 4);
    }

    private static final GridBagConstraints witnessPanelConstraints = new GridBagConstraints();
    static {
        witnessPanelConstraints.gridx = 0;
        witnessPanelConstraints.gridy = 2;
        witnessPanelConstraints.fill = GridBagConstraints.BOTH;
        witnessPanelConstraints.weightx = 1.0;
        witnessPanelConstraints.weighty = 1.0;
        witnessPanelConstraints.insets = new Insets(2, 4, 2, 4);
    }

    private static final GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
    static {
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 3;
        buttonPanelConstraints.anchor = GridBagConstraints.WEST;
        buttonPanelConstraints.weightx = 1.0;
        buttonPanelConstraints.insets = new Insets(2, 4, 4, 4);
    }

    /*
     * Case Detail Component Constraints
     */

    private static final GridBagConstraints titleLabelConstraints = new GridBagConstraints();
    static {
        titleLabelConstraints.gridx = 0;
        titleLabelConstraints.gridy = 0;
        titleLabelConstraints.anchor = GridBagConstraints.WEST;
        titleLabelConstraints.gridwidth = 4;
        titleLabelConstraints.weightx = 1.0;
        titleLabelConstraints.insets = new Insets(4, 4, 2, 2);
    }

    private static final GridBagConstraints caseRefLabelConstraints = new GridBagConstraints();
    static {
        caseRefLabelConstraints.gridx = 0;
        caseRefLabelConstraints.gridy = 1;
        caseRefLabelConstraints.anchor = GridBagConstraints.WEST;
        caseRefLabelConstraints.insets = new Insets(2, 4, 2, 2);
    }

    private static final GridBagConstraints caseRefValueConstraints = new GridBagConstraints();
    static {
        caseRefValueConstraints.gridx = 1;
        caseRefValueConstraints.gridy = 1;
        caseRefValueConstraints.anchor = GridBagConstraints.WEST;
        caseRefValueConstraints.insets = new Insets(2, 2, 2, 2);
    }

    private static final GridBagConstraints defendantNamesLabelConstraints = new GridBagConstraints();
    static {
        defendantNamesLabelConstraints.gridx = 2;
        defendantNamesLabelConstraints.gridy = 1;
        defendantNamesLabelConstraints.anchor = GridBagConstraints.WEST;
        defendantNamesLabelConstraints.insets = new Insets(2, 2, 2, 2);
    }

    private static final GridBagConstraints defendantNamesValueConstraints = new GridBagConstraints();
    static {
        defendantNamesValueConstraints.gridx = 3;
        defendantNamesValueConstraints.gridy = 1;
        defendantNamesValueConstraints.anchor = GridBagConstraints.WEST;
        defendantNamesValueConstraints.weightx = 1.0;
        defendantNamesValueConstraints.insets = new Insets(2, 2, 2, 4);
    }

    private static final GridBagConstraints courtNameLabelConstraints = new GridBagConstraints();
    static {
        courtNameLabelConstraints.gridx = 0;
        courtNameLabelConstraints.gridy = 2;
        courtNameLabelConstraints.anchor = GridBagConstraints.WEST;
        courtNameLabelConstraints.insets = new Insets(2, 4, 4, 2);
    }

    private static final GridBagConstraints courtNameValueConstraints = new GridBagConstraints();
    static {
        courtNameValueConstraints.gridx = 1;
        courtNameValueConstraints.gridy = 2;
        courtNameValueConstraints.gridwidth = 3;
        courtNameValueConstraints.weightx = 1.0;
        courtNameValueConstraints.anchor = GridBagConstraints.WEST;
        courtNameValueConstraints.insets = new Insets(2, 2, 4, 4);
    }

    /*
     * Trial Time Estimate Panel Constraints
     */

    private static final GridBagConstraints trialTimeEstimateLabelConstraints = new GridBagConstraints();
    static {
        trialTimeEstimateLabelConstraints.gridx = 0;
        trialTimeEstimateLabelConstraints.gridy = 0;
        trialTimeEstimateLabelConstraints.anchor = GridBagConstraints.WEST;
        trialTimeEstimateLabelConstraints.insets = new Insets(4, 2, 4, 2);
    }

    private static final GridBagConstraints trialTimeEstimateValueConstraints = new GridBagConstraints();
    static {
        trialTimeEstimateValueConstraints.gridx = 1;
        trialTimeEstimateValueConstraints.gridy = 0;
        trialTimeEstimateValueConstraints.anchor = GridBagConstraints.WEST;
        trialTimeEstimateValueConstraints.insets = new Insets(4, 2, 4, 4);
    }

    private static final GridBagConstraints trialTimeEstimateButtonConstraints = new GridBagConstraints();
    static {
        trialTimeEstimateButtonConstraints.gridx = 2;
        trialTimeEstimateButtonConstraints.gridy = 0;
        trialTimeEstimateButtonConstraints.weightx = 1.0;
        trialTimeEstimateButtonConstraints.anchor = GridBagConstraints.WEST;
        trialTimeEstimateButtonConstraints.insets = new Insets(4, 2, 4, 4);
    }

    /*
     * Witness Panel Constraints
     */

    private static final GridBagConstraints currentWeekLabelConstraints = new GridBagConstraints();
    static {
        currentWeekLabelConstraints.gridx = 0;
        currentWeekLabelConstraints.gridy = 0;
        currentWeekLabelConstraints.anchor = GridBagConstraints.WEST;
        currentWeekLabelConstraints.insets = new Insets(4, 4, 4, 2);
    }

    private static final GridBagConstraints currentWeekValueConstraints = new GridBagConstraints();
    static {
        currentWeekValueConstraints.gridx = 1;
        currentWeekValueConstraints.gridy = 0;
        currentWeekValueConstraints.anchor = GridBagConstraints.WEST;
        currentWeekValueConstraints.weightx = 1.0;
        currentWeekValueConstraints.insets = new Insets(4, 2, 4, 2);
    }

    private static final GridBagConstraints witnessTableConstraints = new GridBagConstraints();
    static {
        witnessTableConstraints.gridx = 0;
        witnessTableConstraints.gridy = 1;
        witnessTableConstraints.weightx = 1.0;
        witnessTableConstraints.weighty = 1.0;
        witnessTableConstraints.gridwidth = 2;
        witnessTableConstraints.fill = GridBagConstraints.BOTH;
        witnessTableConstraints.insets = new Insets(2, 4, 4, 4);
    }

    /*
     * Implementation
     */

    private final XhibitApplicationController xac;

    private JLabel caseRefValue;

    private JLabel defendantNamesValue;

    private JLabel courtNameCodeValue;

    private JLabel trialTimeEstimateValue;

    private JLabel currentWeekValue;

    private JButton previousWeekButton;

    private JButton nextWeekButton;

    private WitnessSessionTableModel witnessTableModel;

    private XTable witnessTable;

    private JButton addWitnessButton;

    private JButton editWitnessButton;

    private JButton deleteScheduleButton;

    private JButton issueScheduleButton;

    private JButton printScheduleByDayButton;

    private JButton printScheduleByWeekButton;

    private JButton notesButton;

    private JButton trialTimeButton;

    private JPopupMenu witnessTablePopup;

    private Integer currentWitness;

    private SkeletonSchedule skeletonSchedule;

    /*
     * Construction
     */

    /**
     * Constructor
     * 
     * @param newXac
     *            the controller
     * @throws CSRecoverableException
     */
    public SkeletonSchedulePanel(XhibitApplicationController newXac) throws CSRecoverableException {
        super(new GridBagLayout());
        xac = newXac;
        loadSkeletonSchedule(newXac);

        log.debug("SkeletonSchedulePanel(" + newXac + ")");

        initPanels(); // must init xac first

        stepInitialise();
    }

    /**
     * Retrieve the Skeleton Schedule for the case. If one does not exist, it is
     * created
     * 
     * @param newXac
     *            the controller
     * @throws ScheduleModificationException
     * @throws ScheduleNotFoundException
     */
    private void loadSkeletonSchedule(XhibitApplicationController newXac) throws ScheduleModificationException,
            ScheduleNotFoundException {
        SkeletonSchedule skeletonSchedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(
                newXac.getApplicationCaseModel().getCaseId());
        // If no schedule create one.
        if (skeletonSchedule == null) {
            skeletonSchedule = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(
                    xac.getApplicationCaseModel().getCaseId(), true);
        }
        skeletonSchedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(
                xac.getApplicationCaseModel().getCaseId());
        setSkeletonSchedule(skeletonSchedule);

    }

    /**
     * Initialise the Skeleton Schedule panels
     */
    private void initPanels() {
        initCaseDetailsPanel();
        initTrialTimeEstimatePanel();
        // need to do button panel first as witness panel
        // required buttons to have been created
        initButtonPanel();
        initWitnessPanel();
    }

    /**
     * Initialise the case details panel
     */
    private void initCaseDetailsPanel() {
        JPanel caseDetailsPanel = ComponentUtil.createPanel(false);

        caseDetailsPanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_TITLE_LABEL_KEY)),
                titleLabelConstraints);

        caseDetailsPanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_CASEREF_LABEL_KEY)),
                caseRefLabelConstraints);
        caseRefValue = ComponentUtil.createValue();
        caseDetailsPanel.add(caseRefValue, caseRefValueConstraints);

        caseDetailsPanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_DEFENDANTS_LABEL_KEY)),
                defendantNamesLabelConstraints);
        defendantNamesValue = ComponentUtil.createValue();
        caseDetailsPanel.add(defendantNamesValue, defendantNamesValueConstraints);

        caseDetailsPanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_COURT_LABEL_KEY)),
                courtNameLabelConstraints);
        courtNameCodeValue = ComponentUtil.createValue();
        caseDetailsPanel.add(courtNameCodeValue, courtNameValueConstraints);

        add(caseDetailsPanel, caseDetailsPanelConstraints);
    }

    /**
     * Initialise the trial time estimate panel
     */
    private void initTrialTimeEstimatePanel() {
        JPanel trialTimeEstimatePanel = ComponentUtil.createPanel(true);

        trialTimeEstimatePanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_TRIALTIME_LABEL_KEY)),
                trialTimeEstimateLabelConstraints);
        trialTimeEstimateValue = ComponentUtil.createValue();
        trialTimeEstimatePanel.add(trialTimeEstimateValue, trialTimeEstimateValueConstraints);

        trialTimeButton = ComponentUtil.createButton(xac, XhibitActions.EditTrialTimeEstimate);
        trialTimeButton.setEnabled(!isWitnessListOrIssued());
        trialTimeEstimatePanel.add(trialTimeButton, trialTimeEstimateButtonConstraints);

        add(trialTimeEstimatePanel, trialTimeEstimatePanelConstraints);
    }

    private void initWitnessPanel() {
        JPanel witnessPanel = ComponentUtil.createPanel(true);

        witnessPanel.add(ComponentUtil.createLabel(getResource(SKELETONSCHEDULE_WEEK_LABEL_KEY)),
                currentWeekLabelConstraints);
        currentWeekValue = ComponentUtil.createValue();
        witnessPanel.add(currentWeekValue, currentWeekValueConstraints);

        witnessTableModel = new WitnessSessionTableModel();
        witnessTable = ComponentUtil.createSortableTable(witnessTableModel);
        ListSelectionModel rowSM = witnessTable.getSelectionModel();
        rowSM.addListSelectionListener(new TableRowListener(this));
        witnessTable.addMouseListener(new TablePopupListener(getTablePopUp(), witnessTable));
        witnessTable.addMouseListener(new DoubleClickListener((XAction) getEditWitnessButton().getAction()));

        // Install the custom renderer on the description column
        TableColumn col = witnessTable.getColumnModel().getColumn(WitnessSessionTableModel.EXPECTED_COL);
        col.setCellRenderer(new WitnessSessionTableCellRenderer());

        witnessPanel.add(ComponentUtil.createScrollPane(witnessTable), witnessTableConstraints);

        add(witnessPanel, witnessPanelConstraints);
    }

    private void initButtonPanel() {
        JPanel buttonPanel = ComponentUtil.createPanel(new GridLayout(2, 0, 4, 4), true);

        addWitnessButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.AddWitness, this);
        addWitnessButton.setEnabled(!isWitnessListOrIssued());
        buttonPanel.add(addWitnessButton);

        editWitnessButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.EditWitness, this);
        editWitnessButton.setEnabled(false);
        buttonPanel.add(editWitnessButton);

        previousWeekButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.ViewPreviousWeek, this);
        previousWeekButton.getAction().setEnabled(false);
        buttonPanel.add(previousWeekButton);

        nextWeekButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.ViewNextWeek, this);
        nextWeekButton.getAction().setEnabled(false);
        buttonPanel.add(nextWeekButton);

        notesButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.EditNotes, this);
        notesButton.setEnabled(false);
        buttonPanel.add(notesButton);

        issueScheduleButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.IssueSchedule, this);
        issueScheduleButton.setEnabled(!isWitnessListOrIssued());
        buttonPanel.add(issueScheduleButton);

        deleteScheduleButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.DeleteSchedule, this);
        deleteScheduleButton.setEnabled(!isWitnessListOrIssued());
        buttonPanel.add(deleteScheduleButton);

        printScheduleByDayButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.PrintScheduleDay, this);
        printScheduleByDayButton.setEnabled(hasWitnesses());
        buttonPanel.add(printScheduleByDayButton);

        printScheduleByWeekButton = ComponentUtil.createButtonWithOwner(xac, XhibitActions.PrintScheduleWeek, this);
        printScheduleByWeekButton.setEnabled(hasWitnesses());
        buttonPanel.add(printScheduleByWeekButton);

        add(buttonPanel, buttonPanelConstraints);
    }

    /**
     * Witness List check and disable buttons as if a Skeleton Schedule has been
     * issued Should only be able to view only To determine if the skeleton
     * schedule returned is a witness list deliverable will be 'N'
     * skeleton_delivery_status_id will be null the schedule has trial sessions
     * set up
     */
    private boolean isWitnessListOrIssued() {
        boolean returnValue = false;
        // test if a witness list or isIssued
        int noOfTrialSessions = getSkeletonSchedule().getTrialSessions().length; // get
        // no
        // of
        // trial
        // sessions
        if ((!getSkeletonSchedule().isDeliverable() && getSkeletonSchedule().getDeliveryStatus() == null && noOfTrialSessions > 0)
                || getSkeletonSchedule().isIssued()) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Method to return if the case has any witnesses added
     */
    private boolean hasWitnesses() {
        // check if there are any witnesses for the case
        return selector.areWitnessesOnCase(ControllerUtil.getCaseId(xac));
    }

    /*
     * Common Functions Implementation
     */
    /**
     * Tells framework if print also saves to file
     * 
     * @return FALSE
     */
    public boolean autoSaveToFile() {
        return false;
    }

    /**
     * Print the schedule
     * 
     * @return
     * @throws UserCancelException
     */
    public String[] print() throws UserCancelException {
        PrintDialog pd = new PrintDialog(xac);
        pd.setVisible(true);
        if (!pd.isCancelClicked()) {
            if (pd.getPrintMode() == pd.BYDAY) {
                getPrintScheduleDayButton().doClick();
            } else if (pd.getPrintMode() == pd.BYWEEK) {
                getPrintScheduleWeekButton().doClick();
            }
        }
        throw new UserCancelException();
    }

    /*
     * Life cycle methods
     */

    /**
     * Lifecycle
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("stepInitialise()");

        CaseDetail caseDetail = ControllerUtil.getCaseDetail(xac);
        log.debug("Case Id: " + caseDetail.getId());

        setCaseRef(caseDetail.getCaseNumber()); // getCaseNumber is ref (type +
        // number)
        setDefendantNames(caseDetail.getDefendantNames());
        setCourtNameCode(caseDetail.getCourtName(), caseDetail.getCourtCode());
        setCurrentWeek(1, caseDetail);
        try {
            int maxEst = Integer.parseInt(getResource(SKELETONSCHEDULE_MAX_TT_ESTIMATE_KEY));
            if (caseDetail.getEstimatedCaseDuration() > maxEst) {
                JOptionPane.showMessageDialog(this, getResource(SKELETONSCHEDULE_MAX_TT_EST_MESSAGE_KEY),
                        getResource(SKELETONSCHEDULE_MAX_TT_EST_TITLE_KEY), JOptionPane.WARNING_MESSAGE);

            }
            setTrialTimeEstimate(Math.min(new Float(caseDetail.getEstimatedCaseDuration()).intValue(), maxEst));
        } catch (NoDirectionsForCaseException ex) {
            log.error(ex);
        }
        // display the warning message is there is no witnesses to display for
        // the week
        // or it is a witness list with no trial sessions or an issued skeleton
        // schedule
        if (witnessTableModel.getRowCount() == 0 && !isWitnessListOrIssued()) {
            JOptionPane.showMessageDialog(this, getResource(SKELETONSCHEDULE_NO_WITNESS_MESSAGE_KEY),
                    getResource(SKELETONSCHEDULE_NO_WITNESS_TITLE_KEY), JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Lifecycle
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("stepActivate()");
        stepUpdateViewState();
    }

    /**
     * Lifecycle
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("stepUpdateViewState()");

        // Get the selected row
        int selectedRow = witnessTable.getSelectedRow();

        /*
         * establish trial time is > 0 If the the trialTimeEstimate is 0,
         * addWitnessButton, deleteScheduleButton and issueScheduleButton should
         * be disabled. This is to prevent unpredictable behavious on the middle
         * tier
         */
        boolean enableTT;
        try {
            enableTT = getTrialTimeEstimateFormat().parse(trialTimeEstimateValue.getText()).intValue() > 0;
        } catch (ParseException ex) {
            enableTT = false;
        }

        // add button
        addWitnessButton.getAction().setEnabled(enableTT && !isWitnessListOrIssued());

        boolean haveWitnesses = hasWitnesses();

        // enable issue and delete buttons
        // bug 54387 - only enable issue if at least one witness
        boolean enableIssue = (enableTT && !isWitnessListOrIssued() && haveWitnesses);

        issueScheduleButton.getAction().setEnabled(enableIssue);
        issueScheduleButton.setEnabled(enableIssue);
        deleteScheduleButton.getAction().setEnabled(enableIssue);

        // enable print if at least one witness
        getPrintScheduleDayButton().getAction().setEnabled(haveWitnesses);
        getPrintScheduleWeekButton().getAction().setEnabled(haveWitnesses);
        XhibitActions.getAction(xac, XhibitActions.Print).setEnabled(haveWitnesses);
        XhibitActions.getAction(xac, XhibitActions.PrintPreview).setEnabled(haveWitnesses);

        // enable previous and next
        int week = currentWeekValue.getText().trim().length() > 0 ? getCurrentWeek() : -1;
        if (week >= 0) {
            previousWeekButton.getAction().setEnabled(areThereMoreWitnesses(week, false));
            nextWeekButton.getAction().setEnabled(areThereMoreWitnesses(week, true));
        }

        // enable edit witness and notes
        if (selectedRow >= 0) {
            editWitnessButton.getAction().setEnabled(!isWitnessListOrIssued());
            notesButton.getAction().setEnabled(!isWitnessListOrIssued());

            currentWitness = ((WitnessSession) ((XHIBITTableModelInterface) witnessTable.getModel())
                    .getDataAt(selectedRow)).getId();
        } else {
            editWitnessButton.getAction().setEnabled(false);
            notesButton.getAction().setEnabled(false);
        }
    }

    /**
     * Lifecycle
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        log.debug("stepValidate()");
    }

    /**
     * Lifecycle
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("stepDeactivate()");
    }

    /**
     * Lifecycle
     * 
     * @param update
     *            whether to save or not
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        log.debug("stepDeinitialise(" + update + ")");
    }

    /*
     * Helpers
     */

    /**
     * Set the case reference on the case reference value
     * 
     * @param caseRef
     *            the case reference
     */
    public void setCaseRef(String caseRef) {
        caseRefValue.setText(caseRef);
    }

    /**
     * Sets the defendant names on the defendant name value
     * 
     * @param names
     *            defendant names for the defendant name value
     */
    public void setDefendantNames(String[] names) {
        defendantNamesValue.setText(PrimitiveUtil.toString(names));
    }

    /**
     * Sets the court name code on the court name code value
     * 
     * @param courtName
     * @param courtCode
     */
    public void setCourtNameCode(String courtName, String courtCode) {
        courtNameCodeValue.setText(courtName + " " + courtCode);
    }

    /**
     * If the the trialTimeEstimate is 0.0, addWitnessButton,
     * deleteScheduleButton and issueScheduleButton should be disabled. This is
     * to prevent unpredictable behavious on the middle tier
     * 
     * @param trialTimeEstimate
     */
    public void setTrialTimeEstimate(int trialTimeEstimate) throws CSRecoverableException {
        trialTimeEstimateValue.setText(getTrialTimeEstimateFormat().format(trialTimeEstimate));

        Number trialEst = null;
        boolean enable = false;
        try {
            trialEst = getTrialTimeEstimateFormat().parse(trialTimeEstimateValue.getText());
            enable = trialEst.doubleValue() > 0.0;
        } catch (ParseException ex) {
            log.error("ParseException", ex);
        }
        stepUpdateViewState();
    }

    public int getTrialTimeEstimate() {
        try {
            return getTrialTimeEstimateFormat().parse(trialTimeEstimateValue.getText()).intValue();
        } catch (ParseException ex) {
            return 0;
        }
    }

    /**
     * Reset the witness panel
     * 
     * @throws CSRecoverableException
     */
    public void refreshWitnessPanel() throws CSRecoverableException {
        try {
            setCurrentWeek(Integer.parseInt(currentWeekValue.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Set the id of the current week
     * 
     * @param week
     *            the week id
     * @throws CSRecoverableException
     */
    public void setCurrentWeek(int week) throws CSRecoverableException {
        setCurrentWeek(week, ControllerUtil.getCaseId(xac));
    }

    /**
     * Set the id of the current week
     * 
     * @param week
     *            the week id
     * @param caseDetail
     *            the case detail
     * @throws CSRecoverableException
     */
    private void setCurrentWeek(int week, CaseDetail caseDetail) throws CSRecoverableException {
        setCurrentWeek(week, caseDetail.getId());
    }

    /**
     * Set the id of the current week
     * 
     * @param week
     *            the week id
     * @param caseId
     *            the case
     * @throws CSRecoverableException
     */
    private void setCurrentWeek(int week, Integer caseId) throws CSRecoverableException {
        currentWeekValue.setText(getCurrentWeekFormat().format(week));
        WitnessSession[] witnessesForWeek = selector.getWitnessesForWeek(caseId, week);
        ((XHIBITTableModelInterface) witnessTable.getModel()).setData(witnessesForWeek);
        stepUpdateViewState();
    }

    /**
     * Return the id of the current week
     * 
     * @return the week
     */
    public int getCurrentWeek() {
        return Integer.parseInt(currentWeekValue.getText());
    }

    /*
     * Utility Methods
     */

    /**
     * Return the resource for the given name
     * 
     * @param resourceName
     *            the name of the resource
     * @return the resource value
     */
    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }

    /**
     * Return the format for the trial time estimate
     * 
     * @return the format
     */
    private static DecimalFormat getTrialTimeEstimateFormat() {
        if (trialTimeEstimateFormat == null) {
            trialTimeEstimateFormat = new DecimalFormat(getResource(SKELETONSCHEDULE_TRIALTIME_FORMAT_KEY));
        }
        return trialTimeEstimateFormat;
    }

    /**
     * Return the format for the weekly report
     * 
     * @return the format
     */
    private static DecimalFormat getCurrentWeekFormat() {
        if (currentWeekFormat == null) {
            currentWeekFormat = new DecimalFormat(getResource(SKELETONSCHEDULE_WEEK_FORMAT_KEY));
        }
        return currentWeekFormat;
    }

    /**
     * Return the current witness
     * 
     * @return the witness
     */
    public Integer getCurrentWitness() {
        return currentWitness;
    }

    /**
     * Set the skeleton schedule
     * 
     * @param skeletonSchedule
     *            the schedule
     */
    public void setSkeletonSchedule(SkeletonSchedule skeletonSchedule) {
        this.skeletonSchedule = skeletonSchedule;
    }

    /**
     * Return the issue button
     * 
     * @return the button
     */
    public JButton getIssueScheduleButton() {
        return issueScheduleButton;
    }

    /**
     * Return the print daily button
     * 
     * @return the button
     */
    public JButton getPrintScheduleDayButton() {
        return printScheduleByDayButton;
    }

    public JPopupMenu getTablePopUp() {
        if (witnessTablePopup == null) {
            JPopupMenu jpm = new DefaultPopup(DefaultPopup.COPY);
            jpm.add(new JMenuItem(getNotesButton().getAction()));
            jpm.add(new JMenuItem(getEditWitnessButton().getAction()));
            jpm.add(new JMenuItem(getAddWitnessButton().getAction()));
            jpm.addSeparator();
            jpm.add(new JMenuItem(previousWeekButton.getAction()));
            jpm.add(new JMenuItem(nextWeekButton.getAction()));
            witnessTablePopup = jpm;
        }
        return witnessTablePopup;
    }

    /**
     * Return the print weekly button
     * 
     * @return the button
     */
    public JButton getPrintScheduleWeekButton() {
        return printScheduleByWeekButton;
    }

    /**
     * Return the skeleton schedule
     * 
     * @return the schedule
     */
    public SkeletonSchedule getSkeletonSchedule() {
        return skeletonSchedule;
    }

    /**
     * Return the add witness button
     * 
     * @return the button
     */
    public JButton getAddWitnessButton() {
        return addWitnessButton;
    }

    /**
     * Return the edit witness button
     * 
     * @return the button
     */
    public JButton getEditWitnessButton() {
        return editWitnessButton;
    }

    /**
     * Return the delete button
     * 
     * @return the button
     */
    public JButton getDeleteScheduleButton() {
        return deleteScheduleButton;
    }

    /**
     * Return the trial time button
     * 
     * @return the button
     */
    public JButton getTrialTimeButton() {
        return trialTimeButton;
    }

    /**
     * Return the notes button
     * 
     * @return the button
     */
    public JButton getNotesButton() {
        return notesButton;
    }

    /**
     * Return the next week with witnesses - this may not be the following week,
     * but might be any week in the future for the schedule
     * 
     * @param currentWeek
     * @return the next week number with witnesses
     */
    public int getNextPopulatedWeek(int currentWeek) {
        log.debug("In getNextPopulatedWeek(" + currentWeek + ")");
        boolean result = false;
        int duration = ((int) ((getTrialTimeEstimate() + 4) / 5));
        int weekCounter = currentWeek;
        Integer caseId = ControllerUtil.getCaseId(xac);

        while (result == false && weekCounter != duration) {
            weekCounter++;
            result = selector.areWitnessesInWeek(caseId, weekCounter);
        }
        if (result == false) {
            return currentWeek;
        } else {
            return weekCounter;
        }
    }

    /**
     * Return the previous week with witnesses - this may not be the preceding
     * week, but might be any week in the past for the schedule
     * 
     * @param currentWeek
     * @return the previous week number with witnesses
     */
    public int getPrevPopulatedWeek(int currentWeek) {
        log.debug("In getPrevPopulatedWeek(" + currentWeek + ")");
        boolean result = false;
        int weekCounter = currentWeek;
        Integer caseId = ControllerUtil.getCaseId(xac);

        while (result == false && weekCounter != 1) {
            weekCounter--;
            result = selector.areWitnessesInWeek(caseId, weekCounter);
        }
        if (result == false) {
            return currentWeek;
        } else {
            return weekCounter;
        }
    }

    /**
     * Checks if there are more witnesses for the schedule, other than in the
     * current week
     * 
     * @param week
     *            The current week
     * @param direction
     *            1 for future, -1 for past
     * @return true if there are more witnesses, otherwise false
     */
    public boolean areThereMoreWitnesses(int week, boolean forward) {
        boolean result = false;
        int target = 0;
        int duration = ((int) ((getTrialTimeEstimate() + 4) / 5));
        if (duration == 0) {
            return false;
        }

        // adjust the week counter depending on if we are looking forward or
        // back
        int weekCounter = week + (forward ? 1 : -1);
        Integer caseId = ControllerUtil.getCaseId(xac);

        if (forward) {
            target = duration + 1;
            if (weekCounter > target) {
                // will never converge so return false
                return false;
            }
        }

        // loop round all the weeks until we match the target
        while (result == false && weekCounter != target) {
            result = selector.areWitnessesInWeek(caseId, weekCounter);
            weekCounter += (forward ? 1 : -1);
        }
        return result;
    }

    // This renderer extends a component. It is used each time a
    // cell must be displayed.
    public class WitnessSessionTableCellRenderer extends JLabel implements TableCellRenderer {
        public WitnessSessionTableCellRenderer() {
            super();
            // Set opaque to true to enable the JLabel background/foreground
            // to be set
            setOpaque(true);
        }

        // This method is called each time a cell in a column
        // using this renderer needs to be rendered.
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at
            // (rowIndex, vColIndex)

            if (isSelected) {
                // cell (and perhaps other cells) are selected
                // set background to the same selected colour that the table
                // displays
                setBackground(table.getSelectionBackground());
                // set foreground to the same selected colour that the table
                // displays
                setForeground(table.getSelectionForeground());
            } else {
                // cell is deselected
                // set background to the same unselected colour that the table
                // displays
                setBackground(table.getBackground());
                // set foreground to the same unselected colour that the table
                // displays
                setForeground(table.getForeground());
                setBorder(null);
            }

            if (hasFocus) {
                // this cell is the anchor and the table has the focus
                // set the border to yellow to reproduce the behaviour of the
                // table
                setBorder(new LineBorder(Color.yellow));
            } else {
                setBorder(null);
            }

            // The original data is untouched
            setText(((Time) value).toString().substring(0, 5));

            // Since the renderer is a component, return itself
            return this;
        }

        // The following methods override the defaults for performance reasons
        public void validate() {
        }

        public void revalidate() {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

    }

}