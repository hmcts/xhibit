package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogControllerModel;
import uk.gov.courtservice.xhibit.client.linkedcases.LinkedCasesHelper;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.menu.XhibitMenus;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XToolBarHelper;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Desiption: The main application window
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 */
public class XhibitApplicationControllerImpl extends XhibitApplicationController {

    private static final long serialVersionUID = 1L;
    
    private static String selectedCourt = "";

    /**
     * framework provided log - we are not going to use the XHIBITConstant.log
     * for the AppController - too important!
     */
    private final Logger log = CSServices.getLogger(XhibitApplicationController.class);

    /** A link to the Xhibit object */
    private XhibitInterface parentController;

    /**
     * The ActionMap used to store all actions (except court log events) that
     * are per xhibit application
     */
    private ActionMap actionMap = new ActionMap();

    /**
     * A list of the actions that are enabled/disabled when a case is
     * opened/closed
     */
    private ArrayList<XAction> caseActionsList = null;

    /** A reference to the status bar */
    private StatusPanel statusPanel = new StatusPanel();

    /** this is not an XPanel 'cause it is just holding other XPanels */
    private JPanel mainPanel = new JPanel(new BorderLayout());

    /**
     * The current panel that is populating the centre section of the
     * application
     */
    private XPanel bodyPanel = null;

    /** The model containing the currently selected case */
    private ApplicationCaseModel applicationCaseModel = null;

    /** The toolbar associated to this application. */
    private XToolBarHelper toolBarHelper = null;

    private boolean screenActive = false;

    /**
     * The following variables are used to indicate whether or not the hearing
     * has been ended and a case has been opened respectively. When the user
     * elects to exit a case but they have not ended the hearing, the variables
     * are interrogated to determine if the user should be provided with the
     * opportunity to cancel the exit. Note: Exiting a case is considered to be
     * actions such as switching from the court log screen to today's schedule
     * or opening a new case.
     */
    private boolean hearingEnded = false;

    private boolean caseOpened = false;

    // used only by the open and close methods
    private boolean opening = false;

    /** The panel used to hold all the generic toolbars. */
    private JPanel genericToolBarPanel = null;

    // These variables are used to monitor the generic toolbar functionality
    // for wrapping and adding.
    private int toolBarRowIdx = 0;

    private int toolBarColIdx = 0;

    private int widthOffset = 0;

    private boolean initialised = false;

    private JPanel rowPanel = null;

    /**
     * A list of actions that are enabled/disabled by default when a case is
     * opened/closed
     * 
     * @return
     */
    private ArrayList<XAction> bcu_case_ignore_list = null;

    private ArrayList<XAction> case_update_depend_list = null;

    private XhibitMenus xhibitMenus = null;
    
    private CaseStatus caseStatus = new CaseStatus();
    
    private boolean caseChargesDisposalsOpened = false;
    
	private CaseControllerBeanBusinessDelegate caseDelegate;
	
	private static final String OPENING_CASE = "opening Case";
	private static final String SET_CASE_MODEL = "set case model";

    public XhibitApplicationControllerImpl() {
    	caseDelegate =  XhibitDelegateHelper.getCaseDelegate();
        this.getContentPane().add(mainPanel);
        log.debug("added mainpanel");

        // Status bar
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        log.debug("added statuspanel");

        // Instantiate the toolbar.
        this.toolBarHelper = new XToolBarHelper(this);

        // Add a container
        mainPanel.addContainerListener(new XACContainerListener(this));

        mainPanel.setPreferredSize(new Dimension(800, 600));

        this.setSize(this.getPreferredSize());

        // Add a resize listener to refresh the wrapping state of the generic
        // toolbars.
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                XhibitApplicationControllerImpl.this.addGenericToolBars();
            }
        });

        // Prep Menu & Toolbar
        prepareMenuToolbar();

        mainPanel.setPreferredSize(new Dimension(800, 600));

        setTitle("");

        // Add the toolbars.
        this.addGenericToolBars();

        log.debug("Instantiation ready.");

        // Disable Public Notice on application load.
        XhibitActions.getAction(this, XhibitActions.PublicNotice).setEnabled(false);
    }

    private void prepareMenuToolbar() {
        this.xhibitMenus = new XhibitMenus(this);
        log.debug("created new XhibitMenus(this)");
        this.xhibitMenus.buildMenus();
        log.debug("Had XhibitMenus building menus.");

        // Add default toolbars
        JPanel toolbarPanel = new JPanel(new GridBagLayout());

        this.genericToolBarPanel = new JPanel(new GridBagLayout());

        // Use FlowLayout so that the toolbars are not adjusted in any way if
        // the dialog becomes small.
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolbarPanel.add(genericToolBarPanel);
        mainPanel.add(toolbarPanel, BorderLayout.NORTH);

        // Refresh the toolbar state.
        this.toolBarHelper.refreshToolBarState(XToolBarHelper.TOOLBAR_ON_PERSISTENT);
    }

    private String getBundle(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, key);
    }

    /**
     * @return true if the screen does not have(need) Case data i.e. does not
     *         have a case open in view or update.
     */
    private boolean isScreenWithNoCaseData() {
        return (getBodyPanel() instanceof TodaysScheduleController || getBodyPanel() instanceof ManageListTablePanel);
    }
    
    public void setNewTitle(String title) {
    	StringBuffer caseNumber = new StringBuffer();
    	caseNumber.append(title);
        caseNumber.append((caseNumber.length() <= 0 ? "" : " - "));
        caseNumber.append(getBundle("applicationName"));
        //Add court name as part of remote access change
        if (selectedCourt.equals("")) {
            //first time run so set selectedCourt variable
            setSelectedCourt();
        }
        caseNumber.append(" " + selectedCourt);
        super.setTitle(caseNumber.toString());
    }

    public void setTitle(String title) {
        StringBuffer caseNumber = new StringBuffer();

        if (getApplicationCaseModel() == null || isScreenWithNoCaseData()) {
            // don't have or want any case details
        } else {
            caseNumber.append(getBundle("Case"));
            caseNumber.append(" ");
            caseNumber.append(getApplicationCaseModel().getCaseType());
            caseNumber.append(getApplicationCaseModel().getCaseNumber());

            if (getApplicationCaseModel().isLinked()) {
                caseNumber.append(" " + getBundle("linkedTitle"));
            }
            // if not in a court room or not in the same courtroom as the
            // case
            if (!XhibitSingleton.getInstance().isUserInCourtroom()
                    || !getApplicationCaseModel().getScheduledHearingValue().getCourtRoomBasicValue().getId().equals(
                            XhibitSingleton.getInstance().getCourtRoomId())) {
                caseNumber.append(" " + getBundle("from") + " ");
                caseNumber.append(getApplicationCaseModel().getScheduledHearingValue().getCourtRoomName());
            }
            caseNumber.append((caseNumber.length() <= 0 ? "" : " - "));
            caseNumber.append(getBundle((getApplicationCaseModel().isInEditMode() ? "update" : "view")) + " ");
        }

        caseNumber.append(title);
        caseNumber.append((caseNumber.length() <= 0 ? "" : " - "));
        caseNumber.append(getBundle("applicationName"));
        //Add court name as part of remote access change
        if (selectedCourt.equals("")) {
            //first time run so set selectedCourt variable
            setSelectedCourt();
        }
        caseNumber.append(" " + selectedCourt);
        super.setTitle(caseNumber.toString());
    }
    
    private void setSelectedCourt(){
        XhibitCallbackHandler xch = new XhibitCallbackHandler();
        selectedCourt = xch.getSelectedCourt();
    }
    public XPanel getBodyPanel() {
        return bodyPanel;
    }

    private void setBodyPanel(XPanel xp) {
        this.bodyPanel = xp;

        if (xp != null) {
            XhibitListeners.setDefaultListeners(xp);
            mainPanel.add(xp, BorderLayout.CENTER);
        }
    }

    public ApplicationCaseModel getApplicationCaseModel() {
        return applicationCaseModel;
    }

    public void setApplicationCaseModel(ApplicationCaseModel newModel) {
        if (newModel != null) {
            newModel.setXhibitApplicationController(this);
        }

        applicationCaseModel = newModel;

        Iterator iter = getCaseActionsList().iterator();
        while (iter.hasNext()) {
            XAction item = (XAction) iter.next();
            item.setModel(newModel);
        }

        enableCaseActions(newModel == null ? false : true);
    }

    public void reloadApplicationCaseModel() throws HearingScheduleException {
        getApplicationCaseModel().refresh();
    }

    public XToolBarHelper getToolBarHelper() {
        return this.toolBarHelper;
    }

    /**
     * Disables all the actions at the start of the application
     */
    private void disableAllActions() {
        // This call will disable the court log, case properties and the view
        // items associated with a case.
        setApplicationCaseModel(null);

        disableActions();
    }

    private void disableActions() {
        // This call will disable the court log, case properties and the view
        // items associated with a case.
        enableCourtLogActions(false);
        // CR46; Enable Export Results
        XhibitActions.getAction(this, XhibitActions.AuthoriseResults).setEnabled(false);
        XhibitActions.getAction(this, XhibitActions.OriginalCharges).setEnabled(false);


        disableCommonActions();
    }

    private void disableCommonActions() {
        XhibitActions.getAction(this, XhibitActions.Save).setEnabled(false);
        XhibitActions.getAction(this, XhibitActions.Print).setEnabled(false);
        XhibitActions.getAction(this, XhibitActions.PrintToolbar).setEnabled(false);
        XhibitActions.getAction(this, XhibitActions.PrintPreview).setEnabled(false);
        XhibitActions.getAction(this, XhibitActions.EditSelectAll).setEnabled(false);

        // Disables the Close option after a close action has been performed
        XhibitActions.getAction(this, XhibitActions.Close).setEnabled(false);
    }

    /**
     * Enables/Disables the case properties and the menu items in the View menu
     * relating to a case
     * 
     * @param isEnabled
     */
    public void enableCaseActions(boolean isEnabled) {
    	if (getApplicationCaseModel() != null && !getCaseStatus().getCaseCreateInProgressFlag()) {	// fudge CC
            Iterator iter = getCaseActionsList().iterator();
            while (iter.hasNext()) {
                XAction item = (XAction) iter.next();
                item.setEnabled(isEnabled);
                // Add the model to the actions
                if (isEnabled) {
                    item.setModel(getApplicationCaseModel());
                }
            }

            try {
                ScheduledHearingValue shv = getApplicationCaseModel().getScheduledHearingValue();
                // CR46; Authorise Results Use Case Pre-conditions
                if (!(CaseTypeHelper.isBail_CaseType(shv) || CaseTypeHelper.isUndefined_CaseType(shv) || CaseTypeHelper
                        .isMiscelleanousAppeal_CaseType(shv))) {
                	// If the case is migrated then authorise results will be greyed out
                	if (XhibitDelegateHelper.getMigrateCaseDelegate()
                			.isCaseMigrated(getApplicationCaseModel().getCaseId())) {
                		XhibitActions.getAction(this, XhibitActions.AuthoriseResults).setEnabled(false);
                	} else {
                		XhibitActions.getAction(this, XhibitActions.AuthoriseResults).setEnabled(true);
                	}
                    XhibitActions.getAction(this, XhibitActions.OriginalCharges).setEnabled(
                        CaseTypeHelper.isTrial_CaseType(shv)
                    );
                }
                

                // Appeals
                if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(getApplicationCaseModel().getScheduledHearingValue())
                        || CaseTypeHelper.isCriminalAppeal_CaseType(getApplicationCaseModel()
                                .getScheduledHearingValue())) {
                    // disable pleas
                    XhibitActions.getAction(this, XhibitActions.Plea).setEnabled(false);
                    XhibitActions.getAction(this, XhibitActions.PleasAndDirections).setEnabled(false);
                    // enable appeal result
                    XhibitActions.getAction(this, XhibitActions.Verdict).setEnabled(false);
                    XhibitActions.getAction(this, XhibitActions.AppealResult).setEnabled(
                            isEnabled && getApplicationCaseModel().isInEditMode());
                    XhibitActions.getAction(this, XhibitActions.AppealResultOrder).setEnabled(
                            isEnabled && getApplicationCaseModel().isInEditMode());
                } else {
                    // enable verdict
                    XhibitActions.getAction(this, XhibitActions.Verdict).setEnabled(isEnabled);
                    XhibitActions.getAction(this, XhibitActions.AppealResult).setEnabled(false);
                    XhibitActions.getAction(this, XhibitActions.AppealResultOrder).setEnabled(false);
                }
                if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(getApplicationCaseModel().getScheduledHearingValue())) {
                    XhibitActions.getAction(this, XhibitActions.Sentence).setEnabled(false);
                }

                // Sentence Case
                if (CaseTypeHelper.isSentence_CaseType(getApplicationCaseModel().getScheduledHearingValue())) {
                    XhibitActions.getAction(this, XhibitActions.Verdict).setEnabled(false);
                    XhibitActions.getAction(this, XhibitActions.AppealResult).setEnabled(false);
                    XhibitActions.getAction(this, XhibitActions.AppealResultOrder).setEnabled(false);

                }
                // For Orders, the create option should be disabled if the Case
                // is not in edit mode.
                // Neil Entwistle - 22/05/2003
                // RL - Commented as loop below takes care of multiple actions
                // that need this as well.

            } catch (UnknownCaseTypeException ex) {
                // close case
                try {
                    enableCaseActions(false);
                    close();
                } catch (CSRecoverableException rex) {
                    // Just log as exception is thrown in finally block
                    log.error("error closing case", rex);
                }

                throw new CSUnrecoverableException(ex);
            }

            // various actions should be disabled if the Case is not in edit
            // mode.
            if (isEnabled && !getApplicationCaseModel().isInEditMode()) {
                Iterator ignoreIter = getCaseUpdateDependList().iterator();
                while (ignoreIter.hasNext()) {
                    XAction item = (XAction) ignoreIter.next();
                    item.setEnabled(false);
                }
            }

            if (isEnabled) {
                try {
                    ScheduledHearingValue shv = getApplicationCaseModel().getScheduledHearingValue();
                    // If it is a B, U or a C case do not enable certain
                    // actions
                    
                    if (CaseTypeHelper.isBail_CaseType(shv) || CaseTypeHelper.isCombinedCourt_CaseType(shv)
                            || CaseTypeHelper.isUndefined_CaseType(shv)) {
                        /**
                         * Added as changes in RFS4417 in 2015 as B cases now need to have access to Bail Orders so ensuring regular orders arent disabled
                         * for B cases.
                         */
                        boolean isBCase = CaseTypeHelper.isBail_CaseType(shv);
                        if (isBCase) {
                            Iterator ignoreIter = getBCU_CaseIgnoreList().iterator();
                            while (ignoreIter.hasNext()) {
                                XAction item = (XAction) ignoreIter.next();
                                String className = item.getClass().getName();
                                if (!(className.equals("uk.gov.courtservice.xhibit.client.order.actions.OrderCopyAction") ||
                                            className.equals("uk.gov.courtservice.xhibit.client.order.actions.OrderViewAction") ||
                                            className.equals("uk.gov.courtservice.xhibit.client.order.actions.OrderCreateAction"))) {

                                    item.setEnabled(false);
                                }
                            }
                        } else {
                            Iterator ignoreIter = getBCU_CaseIgnoreList().iterator();
                            while (ignoreIter.hasNext()) {
                                XAction item = (XAction) ignoreIter.next();
                                item.setEnabled(false);
                            }
                        }
                    }

                    // If it is a Misc Appeal disable certain actions
                    if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(shv)) {
                        XhibitActions.getAction(this, XhibitActions.OpenLinkedHearingsSummary).setEnabled(false);
                    }
                } catch (UnknownCaseTypeException ex) {
                    try {
                        // close case
                        enableCaseActions(false);
                        close();
                    } catch (CSRecoverableException rex) {
                        // Just log as exception is thrown in finally block
                        log.error("error closing case", rex);
                    }

                    throw new CSUnrecoverableException(ex);
                }
            }

            if (getApplicationCaseModel().isLinked()) {
                XhibitActions.getAction(this, XhibitActions.UnlinkCase).setEnabled(isEnabled);
            }
        }

        XhibitActions.getAction(this, XhibitActions.Close).setEnabled(true);
    }
    
    public void enableCaseActions(boolean isEnabled, boolean force) {
    	getCaseStatus().setCaseCreateInProgressFlag(false);
    	enableCaseActions(isEnabled);
    	getCaseStatus().setCaseCreateInProgressFlag(true);
    }

    /**
     * Enable/disables all the actions relating to the court log screen
     * 
     * @param enable
     */
    public void enableCourtLogActions(boolean enable) {
        try {
            if (getApplicationCaseModel() != null
                    && CaseTypeHelper.isCombinedCourt_CaseType(getApplicationCaseModel().getScheduledHearingValue())) {
                // enable only C case actions
                String s = getBundle("C_Case_events");
                StringTokenizer st = new StringTokenizer(s, ",");
                while (st.hasMoreTokens()) {
                    XAction a = XhibitActions.getCourtLogAction(this, st.nextToken());
                    if (a != null) {
                        a.setEnabled(enable);
                    }
                }
            } else {
                ActionMap courtLogActions = this.xhibitMenus.getCourtLogActionMap();

                if (courtLogActions != null) {
                    // Enable actions
                    Object[] a1 = courtLogActions.keys();
                    if (a1 != null) {
                        for (int i = 0; i < a1.length; i++) {
                            courtLogActions.get(a1[i]).setEnabled(enable);
                        }
                    }
                    // Enable actions from parent map
                    Object[] a2 = courtLogActions.getParent().keys();
                    if (a2 != null) {
                        for (int i = 0; i < a2.length; i++) {
                            courtLogActions.get(a2[i]).setEnabled(enable);
                        }
                    }
                }
            }

            XhibitActions.getAction(this, XhibitActions.EditClEvent).setEnabled(false);
            XhibitActions.getAction(this, XhibitActions.DeleteClEvent).setEnabled(false);
        } catch (UnknownCaseTypeException ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }

    private ArrayList getBCU_CaseIgnoreList() {
        if (bcu_case_ignore_list == null) {
            getCaseActionsList();
        }
        return bcu_case_ignore_list;
    }


    private ArrayList getCaseUpdateDependList() {
        if (case_update_depend_list == null) {
            getCaseActionsList();
        }
        return case_update_depend_list;
    }

    private ArrayList getCaseActionsList() {
        try {
            if (caseActionsList == null) {
                
                caseActionsList = new ArrayList<XAction>();

                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.PleasAndDirections));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.Plea));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.Verdict));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.AppealResult));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.Sentence));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.OrderCreate));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.OrderCopy));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.AppealResultOrder));

                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.MonetaryOrderCreate));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.MonetaryOrderCopy));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.D20Create));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.PreliminaryHearings));

                case_update_depend_list = (ArrayList<XAction>) caseActionsList.clone();

                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.ViewCharges));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.ViewCaseProgress));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.OrderView));                
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.MonetaryOrderView));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.D20View));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.ViewCrestFormsBF));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.OpenLinkedHearingsSummary));

                bcu_case_ignore_list = (ArrayList<XAction>) caseActionsList.clone();

                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.EditSkeletonSchedule));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.LinkCases));
                case_update_depend_list.add(XhibitActions.getAction(this, XhibitActions.EditSkeletonSchedule));
                case_update_depend_list.add(XhibitActions.getAction(this, XhibitActions.LinkCases));

                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.CaseProps));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.ViewCourtLog));
                caseActionsList.add(XhibitActions.getAction(this, XhibitActions.OpenOtherDaysLog));
            } else {
                /**
                 * Added as changes in RFS4417 in 2015 as B cases now need to have access to Bail Orders so ensuring regular orders arent disabled
                 * for B cases.
                 * 
                 * @return
                 */
                ScheduledHearingValue shv = null;
                try {
                    shv = getApplicationCaseModel().getScheduledHearingValue();
                    if (shv != null && CaseTypeHelper.isBail_CaseType(shv)) {
                        /*case_update_depend_list.remove(XhibitActions.getAction(this, XhibitActions.OrderCreate));
                        caseActionsList.remove(XhibitActions.getAction(this, XhibitActions.OrderCreate));
                        caseActionsList.remove(XhibitActions.getAction(this, XhibitActions.OrderView));
                        caseActionsList.remove(XhibitActions.getAction(this, XhibitActions.OrderCopy));
                        bcu_case_ignore_list = (ArrayList<XAction>) caseActionsList.clone();*/
                    }
                } catch (UnknownCaseTypeException ucte) {
                    log.error("Unknown case type exception for order create action");
                } catch (Exception e) {
                    // Data may not yet be populated...
                }
            }
        } catch (ActionNotFoundException ex) {
            XHIBITErrorHandler.handleError(ex);
        }

        return caseActionsList;
    }

    /**
     * This method is essentially a clone of openCase( ScheduledHearingValue,
     * boolean ). It is used primarily by OpenOtherDaysLogPanel to open a case
     * for when the user wishes to see all court logs for a specific case. The
     * security check for edit mode is done in the client but it is re-checked
     * here.
     * 
     * @param ScheduledHearingValue -
     *            the scheduled hearing value for the case to be opened
     * @param boolean -
     *            indicates whether or not the court log should be opened in
     *            edit mode
     * @param boolean -
     *            indicates whether or not the court log should display all
     *            events for a case
     */
    public void openCase(ScheduledHearingValue shv, boolean isEdit, boolean isForAllDaysLogs)
            throws CSRecoverableException {
        log.debug(OPENING_CASE);
        ApplicationCaseModel caseModel = new ApplicationCaseModel();
        caseModel.setScheduledHearingValue(shv);
        caseModel.setForAllDaysLogs(isForAllDaysLogs);
        log.debug(SET_CASE_MODEL);
        openCase(caseModel, isEdit);
    }
    
    /**
     * This method is essentially a clone of openCase( ScheduledHearingValue,
     * boolean ). It is used primarily by OpenOtherDaysLogPanel to open a case
     * for when the user wishes to see all court logs for a specific case. The
     * security check for edit mode is done in the client but it is re-checked
     * here.
     * 
     * @param ScheduledHearingValue -
     *            the scheduled hearing value for the case to be opened
     * @param boolean -
     *            indicates whether or not the court log should be opened in
     *            edit mode
     * @param boolean -
     *            indicates whether or not the court log should display events
     *            in a selected date range for a case
     */
    public void openCase(ScheduledHearingValue shv, boolean isEdit, boolean isShowRangeLogs, Date fromLogsDate, Date toLogsDate)
            throws CSRecoverableException {
        log.debug(OPENING_CASE);
        ApplicationCaseModel caseModel = new ApplicationCaseModel();
        caseModel.setScheduledHearingValue(shv);
        caseModel.setForRangeLogs(isShowRangeLogs);
        caseModel.setScheduledHearingDateFrom(fromLogsDate);
        caseModel.setScheduledHearingDateTo(toLogsDate);
        log.debug(SET_CASE_MODEL);
        openCase(caseModel, isEdit);
    }

    /**
     * This method is only really used by the todays schedule and open case It
     * is used to open a case. The security check for edit mode is done in the
     * client but it is re-checked here.
     * 
     * @param ScheduledHearingValue
     * @param boolean
     */
    public void openCase(ScheduledHearingValue shv, boolean isEdit) throws CSRecoverableException {
        log.debug(OPENING_CASE);
        ApplicationCaseModel caseModel = new ApplicationCaseModel();
        caseModel.setScheduledHearingValue(shv);
        log.debug(SET_CASE_MODEL);
        openCase(caseModel, isEdit);
    }

    public void openCase(ApplicationCaseModel caseModel, boolean isEdit) throws CSRecoverableException {
        constructDefOnCaseSeqNosMap(caseModel);
        getCaseStatus().setCaseProcess(CaseProcess.UPDATE);
        SynchOpenCase action = new SynchOpenCase(caseModel, isEdit, this);
        ActionEvent ae = new ActionEvent(this, 0, "OPEN");
        action.actionPerformed(ae);
    }
    
    /**
     * Constructs the DefOnCaseSeqNos hashmap and stores it in app case model
     * 
     * @param caseModel
     * @throws CSRecoverableException
     */
    public void constructDefOnCaseSeqNosMap(ApplicationCaseModel caseModel) throws CSRecoverableException{
        Integer caseID = caseModel.getCaseId();
        ChargeCompositeValue ccv = null;
        
        //Get Charges
        try{
            ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
        }catch (Exception e){
           throw new CSRecoverableException("gui.XhibitApplicationController.constructDefOnCaseSeqNosMap",
                   "Exception whilst getting the charge composite value object from the mid tier", e);
        }
       
        HashMap<Integer,List> defOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(ccv.getCharges(), ccv.getAllDefendants());
        
        caseModel.setDefOnCaseSeqNosMap(defOnCaseSeqNosMap);
    }

    public void closeCase() throws CSRecoverableException {
        if (getApplicationCaseModel() != null) {
            // Add case to recent file list
            XhibitSingleton.getInstance().maintainRecentCaseList(getApplicationCaseModel());

            // empty local references to case (and disable actions)
            setApplicationCaseModel(null);

            // turn public display is off.
            XhibitActions.getAction(this, XhibitActions.ActivatePublicDisplay).setEnabled(false);
            // Indicate whether or not the public display is active
            setScreenActive(false);
            // Indicate whether or not the hearing has been ended
            setHearingEnded(false);
            setCaseOpened(false);
        }

        disableAllActions();
    }

    /**
     * This method opens
     * 
     * @param xpanelClass
     * @param sig
     * @param args
     */
    public void open(Class xpanelClass, Class[] sig, Object args[]) {
        try {
            if (xpanelClass == null) {
                throw new IllegalArgumentException("xpanelClass");
            }

            close(false);

            CreateXPanelAction createXPanelAction = new CreateXPanelAction(xpanelClass, sig, args);
            createXPanelAction.actionPerformed(new ActionEvent(this, 0, "CREATE"));
        } catch (CSRecoverableException ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }

    /**
     * Tells the XHIBIT Application Controller instance to display a specific
     * XPanel (newMainPanel) in the main area Some (deinitialising/closeing)
     * actions on the currently displayed (if any) XPanel may be undertaken.
     * 
     * @param newMainPanel
     */
    public void open(XPanel newMainPanel) // , int panelType)
    {
        try {
            // Refresh Application Toolbars
            getToolBarHelper().refreshApplicationToolbars();

            if (newMainPanel instanceof TodaysScheduleController) {
                log.debug(this.getClass().getName() + ": new main XPanel is a TodaysScheduleController");
                close(true);
                enableCaseActions(false);
                XhibitActions.getAction(this, XhibitActions.PublicNotice).setEnabled(false);
            } else {
                log.debug(this.getClass().getName() + ": new main XPanel is a NOT TodaysScheduleController, but a "
                        + mainPanel.getClass().getName());
                opening = true;
                close(false); // why is this necessary?
                opening = false;
                
                // need to properly reset this variable if opening another panel/closing one
                if (!(newMainPanel instanceof ChargesController) && 
                		!(newMainPanel instanceof DisposalController)) {
                	this.setCaseChargesDisposalsOpened(false);
                }
                
                if (!this.isCaseChargesDisposalsOpened()) {
                	enableCaseActions(true);
                }
            }
            
            setBodyPanel(newMainPanel);
            newMainPanel.setVisible(true); // so that stepActivate on the
            // XPanel
            this.validate(); // is called to repaint the screen
        } catch (CSRecoverableException ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }

    // the close method is called by the closeXhibitApplication in
    // parentController
    // or can be called by the open() method to firstly close the panel
    // before
    // opening a new one.
    // the close action needs to call
    // parentController.closeXhibitApplication
    public void close() throws CSRecoverableException {
        close(true);
    }

    /**
     * This method closes the XPanel active in the main display area. If the
     * closeCase argument is true, then a check will be performed and if
     * relevant, then the user will be asked whether or not to turn of the
     * public display.
     * 
     * @param closeCase
     *            boolean indicating whether or not too check if we need to
     *            check to turn off the public display
     * @throws CSRecoverableException
     */
    public void close(boolean closeCase) throws CSRecoverableException {   	
    	// Check if file -> close called on case create
    	
    	if(this.getCaseStatus().getCaseCreateInProgressFlag() && !this.isCaseChargesDisposalsOpened()) {
    		if(!opening) { // opening set true if open called, has a close() call so care needed
    			// invoke pop up, return if false else do close()
    			String abortMessage = null;
				if (this.getCaseStatus().isCaseProcess(CaseProcess.AMEND)) {
					abortMessage = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
							"confirmDialog.caseAmend.abortMessage");
				} else {
					abortMessage = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
							"confirmDialog.caseCreate.abortMessage");
				}
    			
				int result = JOptionPane.showConfirmDialog((Component) null,
						abortMessage,
						"Confirm Discard", JOptionPane.OK_CANCEL_OPTION);
				if (0 != result) {	// if ok not selected, go back, else do the close
					throw new UserCancelException(); 	// no need to further, don't want to close (user said no).
				} else {
					this.getCaseStatus().setCaseCreateInProgressFlag(false);	// otherwise triggers 2 close dialogs
				}
    		}
    	} else if (this.isCaseChargesDisposalsOpened()) {	// still case create so this needs to be cleared
    		this.getCaseStatus().setCaseCreateInProgressFlag(false);
    	}
    	
    	if (!opening) {
    		this.setCaseChargesDisposalsOpened(false);
    	}
    	
    	// If closing the case, check if hearing is ended & check if pubilc display
        // is still active & check for mandatory charge values    	
        if (closeCase) {
            checkForMandatoryChargeValues();
            checkForEndedHearing();

            /**
             * The screen will only be active if the case was opened in edit
             * mode. If the user has read access to the public display action,
             * then the Public Display toggle button will have been added to the
             * admin toolbar so allow them to switch off the display first.
             */
            if (isScreenActive() && hasReadAccessOnAction(XhibitActions.PublicDisplayConfigV2)) {
                boolean rc = XMessageBox.alert(this, getBundle("ScreenStillActiveTitle"), true,
                        XMessageBox.ICONQUESTION, getBundle("ScreenStillActiveMessage"), XDialog.YESNO,
                        XDialog.DEFAULTNO);
                if (!rc) {
                    throw new UserCancelException();
                }
            }
        }

        // Refresh Application Toolbars
        getToolBarHelper().refreshApplicationToolbars();

        // dispose the bodypanel
        if (hasBodyPanel()) {
            XPanel xp = getBodyPanel();
            log.debug("The bodyPanel to remove is " + xp.getClass().getName());
            
            callBodyPanelCloseLifeCycleMethods(xp);

            mainPanel.remove(xp);

            // Perf PR 56516 - ensure that all listeners and memory-leaking
            // components are removed...
            XhibitListeners.removeAllListeners(xp);

            setBodyPanel(null);
            // disable all the case actions
            enableCaseActions(false);
            enableCourtLogActions(false);
            XhibitActions.getAction(this, XhibitActions.Sentence).setEnabled(false);
            XhibitActions.getAction(this, XhibitActions.ViewCharges).setEnabled(false);
            if (closeCase) {
                closeCase();
                setTitle("");
            }
            if (!opening) {
                setStatusLabel("");
            }
            repaint();
        }
    }

    /**
     * This method calls the life cycle methods to finish any processing the
     * given XPanel may have.
     * 
     * @param xp
     *            The XPanel that issaving or closing.
     * @throws CSRecoverableException
     */
    private void callBodyPanelCloseLifeCycleMethods(final XPanel xp) throws CSRecoverableException {
        xp.stepValidate();
        xp.stepDeactivate();
        xp.stepDeinitialise(true);
    }

    /**
     * This method calls the life cycle methods to finish any processing the
     * XPanel (active in the main display area) may have.
     * 
     * @throws CSRecoverableException
     */
    public void callBodyPanelCloseLifeCycleMethods() throws CSRecoverableException {
        if (hasBodyPanel()) {
            callBodyPanelCloseLifeCycleMethods(getBodyPanel());
        }
    }

    /**
     * See if the this xac has a body panel (e.g. CourtLogController,
     * TodaysScheduleController).
     * 
     * @return true if a body panel exists.
     */
    public boolean hasBodyPanel() {
        return getBodyPanel() != null;
    }

    /**
     * Determines whether or not the user has read access on a given action
     * 
     * @param actionName -
     *            the name of the action to be interrogated
     * @return true if the user has read access on the action
     */
    private boolean hasReadAccessOnAction(String actionName) {
        return XhibitActions.getAction(this, actionName, this).hasReadAccess();
    }

    private JToolBar getToolBar(String toolbarId) {
        if (xhibitMenus.getToolBarHash().containsKey(toolbarId)) {
            return (JToolBar) xhibitMenus.getToolBarHash().get(toolbarId);
        }

        return null;
    }

    // Status bar methods
    public String getStatusLabel() {
        return statusPanel.getStatusLabel() == null ? "" : statusPanel.getStatusLabel();
    }

    public void setStatusLabel(String message) {
        statusPanel.setStatusLabel(message);
    }

    public void setScreenLabel(String screenId) {
        statusPanel.setScreenLabel(screenId);
    }

    public void setDialogLabel(String dialogId) {
        statusPanel.setDialogLabel(dialogId);
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public ActionMap getCourtLogActionMap() {
        return xhibitMenus.getCourtLogActionMap();
    }

    public void setParentController(XhibitInterface controller) {
        parentController = controller;
    }

    public XhibitInterface getParentController() {
        return parentController;
    }

    public void setScreenActive(boolean newValue) {
        boolean oldValue = screenActive;
        screenActive = newValue;
        firePropertyChange(propertyScreenActive, new Boolean(oldValue), new Boolean(newValue));
    }

    public boolean isScreenActive() {
        return screenActive;
    }

    // Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            try {
                if (XhibitSingleton.getInstance().closeWindow()) {
                    this.toolBarHelper.storeProperties();
                    getParentController().closeXhibitApplication(this);
                } else {
                    this.toolBarHelper.storeProperties();
                    getParentController().exitXhibitApplication(this);
                }

                super.processWindowEvent(e);
            } catch (Exception ex) {
                XHIBITErrorHandler.handleError(ex);
            }
        } else {
            super.processWindowEvent(e);
        }
    }

    /**
     * Will add the set of generic toolbars to the main container of this
     * dialog. Is thread-safe.
     */
    public synchronized void addGenericToolBars() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                addToolBars();
            }
        });
    }

    private void addToolBars() {
        rowPanel = new JPanel(new GridBagLayout());
        toolBarRowIdx = 0;
        toolBarColIdx = 0;
        widthOffset = 0;

        if (genericToolBarPanel == null) {
            return;
        }

        // fixed memory leak - ensure that we maintain no latches onto previous
        // toolbars...
        genericToolBarPanel.removeAll();

        GridBagConstraints genericConstraints = new GridBagConstraints(0, toolBarRowIdx, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        genericToolBarPanel.add(rowPanel, genericConstraints);

        if (getToolBar(tbGeneralId).isVisible()) {
            addToolBar(getToolBar(tbGeneralId));

            // We want to keep the general toolbar alone on its row.
            // So set the offset outside the dialog to continue adding
            // toolbars
            // from the next row.
            widthOffset = getSize() == null ? 1 : (getSize().width + 1);
        }
        if ((getToolBar(tbCourtLogId) != null) && (getToolBar(tbCourtLogId).isVisible())) {
            addToolBar(getToolBar(tbCourtLogId));
        }
        if ((getToolBar(tbChargeId) != null) && (getToolBar(tbChargeId).isVisible())) {
            addToolBar(getToolBar(tbChargeId));
        }

        Enumeration enumeration = xhibitMenus.getToolBarHash().elements();
        while (enumeration.hasMoreElements()) {
            JToolBar j = (JToolBar) enumeration.nextElement();
            if (!initialised) {
                this.toolBarHelper.refreshToolBarState(XToolBarHelper.TOOLBAR_ON_PERSISTENT);
                initialised = true;
            }

            if (!((j.getName().equals(tbGeneral)) || (j.getName().equals(tbCourtLog)) || (j.getName().equals(tbCharge)))) {
                if (j.isVisible()) {
                    addToolBar(j);
                }
            }
        }
        genericToolBarPanel.revalidate();
        genericToolBarPanel.repaint();
    }

    /**
     * Will add a toolbar to the specified container. The method takes into
     * accound width dimensions of the container and toolbar and will wrap the
     * toolbar if necessary.
     * 
     * @param toolBar
     *            The toolbar to be added to the container.
     */
    private void addToolBar(JToolBar toolBar) {
        // Defined widths
        int dialogWidth = this.getSize() == null ? 0 : this.getSize().width;
        int toolBarWidth = toolBar.getPreferredSize().width;

        if (dialogWidth < (toolBarWidth + widthOffset)) {
            toolBarRowIdx++;
            toolBarColIdx = 0;
            widthOffset = toolBarWidth;
            rowPanel = new JPanel(new GridBagLayout());
            GridBagConstraints genericConstraints = new GridBagConstraints(0, toolBarRowIdx, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
            genericToolBarPanel.add(rowPanel, genericConstraints);
        } else {
            toolBarColIdx++;
            widthOffset += toolBarWidth;
        }
        GridBagConstraints gbConstraints = new GridBagConstraints(toolBarColIdx, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        rowPanel.add(toolBar, gbConstraints);
    }

    public boolean isHearingEnded() {
        return hearingEnded;
    }

    public void setHearingEnded(boolean newValue) {
        this.hearingEnded = newValue;
    }

    public boolean isCaseOpened() {
        return caseOpened;
    }

    public void setCaseOpened(boolean newValue) {
        this.caseOpened = newValue;
    }

    public boolean isOkToAuthriseResults() throws CSRecoverableException {
        
        if (isCaseOpened() && getApplicationCaseModel() != null && isMandatoryChargeValuesPromptRequired()){
            String okToAuthoriseWithMissingInfo = 
                ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "okToAuthoriseWithMissingInfo");
            
            String dlgTitle = 
                ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, "bc_InvalidBichardCharges_titleBar");
            
            MandatoryChargeFieldsDialog dialog = 
                new MandatoryChargeFieldsDialog(this, dlgTitle, true, getApplicationCaseModel(), okToAuthoriseWithMissingInfo);
            
            dialog.setVisible(true);
            if (!dialog.isOkClicked()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * If there are charges with mandatory values which have not been filled in then
     * need to stop user from exiting.
     * 
     * @throws UserCancelException
     */
    private void checkForMandatoryChargeValues() throws UserCancelException,CSRecoverableException {
        
        /*Iterate through all charges for this case and check the following:
          If (at least 1 bichard style record) AND (at least one non-Bichard style records then)
             -Stop user exiting
             -Show info dialog and when OK pressed, throw UserCancelException*/
        if (isCaseOpened() && getApplicationCaseModel() != null && isMandatoryChargeValuesPromptRequired()){
            String dlgTitle = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,"bc_InvalidBichardCharges_titleBar");
            new MandatoryChargeFieldsDialog(this,dlgTitle,true,getApplicationCaseModel());        
            throw new UserCancelException();
        }
        /*Otherwise, let the user exit because:
            -Case is either post-Bichard because all mandatory fields filled in
            -Or case is pre-Bichard but no mandatory fields at all have been filled in*/
    }
    
    private boolean isMandatoryChargeValuesPromptRequired() throws CSRecoverableException{        
        boolean bichardFound = false;
        boolean preBichardFound = false;        
        ChargeCompositeValue ccv = null;
        
        Integer caseID = getApplicationCaseModel().getCaseId();
        
        try{
             ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
        }catch (Exception e){
            throw new CSRecoverableException("gui.XhibitApplicationController.isMandatoryChargeValuesPromptRequired",
                    "Exception whilst getting the charge composite value object from the mid tier", e);
        }
        
        Collection charges = ccv.getCharges();
        
        Iterator chargeIterator = charges.iterator();
        while(chargeIterator.hasNext()){
            ChargeValue chargeValue = (ChargeValue) chargeIterator.next();
            //Only process charges of type Indictment, Breach, section_41 (Summary) or committal for sentance
            String chargeType = chargeValue.getChargeType(); 
            if(chargeType.equals(ChargeTypes.INDICTMENT.getChargeType()) ||
                    chargeType.equals(ChargeTypes.BREACH.getChargeType()) ||
                    chargeType.equals(ChargeTypes.SECTION_41.getChargeType()) ||
                    chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())){
                Collection offences = chargeValue.getOffenceValues();
                Iterator offenceIterator = offences.iterator();
                while(offenceIterator.hasNext()){
                    OffenceValue offenceValue = (OffenceValue) offenceIterator.next();
                    
                    if (isValidBichardCharge(offenceValue)){
                        bichardFound = true;
                    }else{
                        preBichardFound = true;
                    }
                    
                    //No need to process any more if we have found at least one of each
                    if(bichardFound && preBichardFound){
                        return true;
                     }    
                }            
            }
        }        
        return false;        
    }
    

    /**This method works out whether a given offence is valid in bichard.
     * Currently only the offence StartDateTime and 2 lines of the address are currently mandatory
     * 
     * @param offenceValue
     * @return
     */
    private boolean isValidBichardCharge(OffenceValue offenceValue){
        if (offenceValue.getOffenceStartDateTime() == null){
            return false;
        }
        if (offenceValue.getAddressId() == null){
            return false;
        }
        return true;
    }
    

    
    /**
     * If the user is exiting from the court log screen that was in "edit" mode
     * and the hearing has not been ended, then inform them of this and provide
     * them with the opportunity to cancel the exit.
     * 
     * @throws UserCancelException
     */
    private void checkForEndedHearing() throws UserCancelException {
        if (isEndHearingPromptRequired()) {
            try {
                String message = null;
                ScheduledHearingValue shv = applicationCaseModel.getScheduledHearingValue();
                // B, U and C cases do not have defendants.
                if (CaseTypeHelper.isBail_CaseType(shv) || CaseTypeHelper.isCombinedCourt_CaseType(shv)
                        || CaseTypeHelper.isUndefined_CaseType(shv)) {
                    message = getBundle("eh_endHearing_message_UBC");
                } else {
                    message = getBundle("eh_endHearing_message");
                }

                int x = JOptionPane.showConfirmDialog(this, message, getBundle("eh_endHearing_titleBar"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (x != JOptionPane.YES_OPTION) {
                    throw new UserCancelException();
                }
            } catch (UnknownCaseTypeException ex) {
                log.warn("UnknownCaseTypeException - should never happen as "
                        + "never get to end hearing if the case type is unknown.");
            }
        }
    }

    private boolean isEndHearingPromptRequired() {
        return (isCaseOpened() && !isHearingEnded() && (getApplicationCaseModel() != null) && getApplicationCaseModel()
                .isInEditMode(FunctionList.ECourtLog));
    }

    private class SynchOpenCase extends SynchXAction {
 
        private static final long serialVersionUID = 1L;

        private ApplicationCaseModel caseModel;

        private boolean isEdit;

        private XhibitApplicationController xac;

        private CourtLogController cc;

        public SynchOpenCase(ApplicationCaseModel caseModel, boolean isEdit, XhibitApplicationController controller) {
            setName("OpenCase");
            this.caseModel = caseModel;
            this.isEdit = isEdit;
            this.xac = controller;
        }

        public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
            // Ensure any open case is closed first
            checkForMandatoryChargeValues();
            checkForEndedHearing();
            closeCase();
            xac.close();

            caseModel.setXhibitApplicationController(xac);
            caseModel.setInEditMode(isEdit);
            setApplicationCaseModel(caseModel);

            //need to update case last updated to fix unauth report...but only if user has opened the case for editting!
            if (caseModel.isInEditMode()) {
	            String userOpeningCase = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
	            caseDelegate.updateLastUpdateOnCaseOpen(caseModel.getCaseId(), userOpeningCase);
            }
            
            // DEBUG
            if (log.isDebugEnabled()) {
            	caseModel.printModel();
            }
        }

        public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
            // create court log
            CourtLogControllerModel clcModel = new CourtLogControllerModel();
            clcModel.setAcm(caseModel);
            cc = new CourtLogController(clcModel);
        }

        public void postSynchActionPerformed(ActionEvent parm1) throws Exception {
            // call open
            open(cc);

            // Initialise state of public notices for case
            if (XhibitSingleton.getInstance().getCourtRoomId() != null) {
                try {
                    XhibitDelegateHelper.getCaseDelegate().checkCaseHistory(caseModel.getCaseId(),
                            XhibitSingleton.getInstance().getCourtRoomId(),
                            XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                } catch (CSUnrecoverableException ex) {
                    XHIBITErrorHandler.handleError(ex);
                } catch (CaseControllerException ex) {
                    XHIBITErrorHandler.handleError(ex);
                }
            }

            // Check state of public display

            // Check to see if case is in user court room
            if (!XhibitSingleton.getInstance().isUserInCourtroom()
                    || !((getApplicationCaseModel()).getScheduledHearingValue().getCourtRoomId().equals(XhibitSingleton
                            .getInstance().getCourtRoomId()))) {
                XhibitActions.getAction(xac, XhibitActions.ActivatePublicDisplay).setEnabled(false);
                XhibitActions.getAction(xac, XhibitActions.PublicNotice).setEnabled(false);
            } else {
                XhibitActions.getAction(xac, XhibitActions.ActivatePublicDisplay).setEnabled(true);
                XhibitActions.getAction(xac, XhibitActions.PublicNotice).setEnabled(true);
            }

            // Indicate whether or not the public display is active
            Integer scheduledHearingId = caseModel.getScheduledHearingId();
            setScreenActive(XhibitDelegateHelper.getDisplayDelegate().isPublicDisplayActive(scheduledHearingId));

            // Indicate whether or not the hearing has been ended
            setHearingEnded(XhibitDelegateHelper.getHearingDelegate().isHearingEnded(caseModel.getScheduledHearingId())
                    .booleanValue());
            setCaseOpened(true);

            // launch linked case functionality if necessary
            LinkedCasesHelper linkedCasesHelper = new LinkedCasesHelper(caseModel.getXhibitApplicationController());
            linkedCasesHelper.processLinkedCases();
        }
    }

    private class CreateXPanelAction extends SynchXAction {

        private static final long serialVersionUID = 1L;

        XPanel p = null;

        Class xpanelClass;

        Class[] sig;

        Object args[];

        public CreateXPanelAction(final Class xpanelClass, final Class[] sig, final Object args[]) {
            this.xpanelClass = xpanelClass;
            this.sig = sig;
            this.args = args;
        }

        public void synchActionPerformed(ActionEvent ae) throws Exception {
            p = (XPanel) xpanelClass.getDeclaredConstructor(sig).newInstance(args);
        }

        public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
            if (p != null) {
                open(p);
            }
        }
    }

    /**
     * Method to dispose of this object. This method de-registers all of the
     * standard listeners from all contained components, and resets the actions
     * of all sub-components (i.e. buttons). Also, all actions are removed from
     * the custom lists contained in this class to remove a "memory leak"
     * defined with actions
     * 
     * @see java.awt.Window#dispose()
     * @see uk.gov.courtservice.xhibit.client.menu.XhibitMenus#dispose()
     * @see uk.gov.courtservice.xhibit.client.listeners.XhibitListeners
     *      #removeAllListeners()
     */
    public void dispose() {
        super.dispose();

        XhibitListeners.removeAllListeners(this);
        XhibitListeners.removeAllListeners(this.getContentPane());
        XhibitListeners.removeAllListeners(genericToolBarPanel);

        // this is accessed in a way that if the required value is not
        // contained, then it will be put in...
        this.actionMap.clear();

        // clear these values to null, as they are all acquired via a get
        // method so if accessed after destruction, they will simply be
        // re-initialised...
        this.caseActionsList = null;
        this.bcu_case_ignore_list = null;
        this.case_update_depend_list = null;

        // this will force this class to be in an unusable state, the
        // xhibitMenus are only created on startup...
        if (this.xhibitMenus != null) {
            this.xhibitMenus.dispose();
            this.xhibitMenus = null;
        }
    }

    @Override
	public CaseStatus getCaseStatus() {
		return caseStatus;
	}
    
    // Added to be able to force a resize of the main xhibit panel from within CaseXPanel if necessary
    @Override
    public void repaintScreen(Dimension newSize) {
    	Dimension copySize = newSize;
    	
    	if(this.getSize().height > newSize.height) {
    		copySize.height = this.getSize().height;	// dont shrink the panel! 
    	}
    	
    	if(this.getSize().width > newSize.width) {
    		copySize.width = this.getSize().width;		// dont shrink the panel! 
    	}

    	this.setSize(copySize);
    }

    
    public void setCaseChargesDisposalsOpened(boolean enabled) {
    	this.caseChargesDisposalsOpened = enabled;
    }
    
    public boolean isCaseChargesDisposalsOpened() {
    	return caseChargesDisposalsOpened;
    }
}
