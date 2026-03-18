package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.ActionMap;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XToolBarHelper;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description: The main application window interface
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XhibitApplicationController.java,v 1.6 2004/10/19 13:49:06
 *          rzvddy Exp $
 */

public abstract class XhibitApplicationController extends XFrame {
    public static final String propertyScreenActive = "screenactive";

    public static final String tbGeneral = "General";

    public static final String tbCharge = "Charge";

    public static final String tbCourtLog = "Court Log";

    public static final String tbResults = "Results";

    public static final String tbGeneralId = "General";

    public static final String tbChargeId = "Charge";

    public static final String tbCourtLogId = "CourtLog";

    public static final String tbResultsId = "Results";

    public abstract XPanel getBodyPanel();

    public abstract ApplicationCaseModel getApplicationCaseModel();

    public abstract void setApplicationCaseModel(ApplicationCaseModel newModel);

    public abstract void reloadApplicationCaseModel() throws HearingScheduleException;

    public abstract XToolBarHelper getToolBarHelper();

    /**
     * Disables all the actions at the start of the application
     */
    public abstract void enableCaseActions(boolean isEnabled);
    
    public abstract void enableCaseActions(boolean isEnabled, boolean force);

    public abstract void enableCourtLogActions(boolean enable);

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
    public abstract void openCase(ScheduledHearingValue shv, boolean isEdit, boolean isForAllDaysLogs)
            throws CSRecoverableException;
    
    /**
     * This method is essentially a clone of openCase( ScheduledHearingValue,
     * boolean ). It is used primarily by OpenOtherDaysLogPanel to open a case
     * for when the user wishes to see a range of court logs for a specific case. The
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
    public abstract void openCase(ScheduledHearingValue shv, boolean isEdit, boolean isForAllDaysLogs, Date fromLogsDate, Date toLogsDate)
            throws CSRecoverableException;

    /**
     * This method is only really used by the todays schedule and open case It
     * is used to open a case. The security check for edit mode is done in the
     * client but it is re-checked here.
     * 
     * @param ScheduledHearingValue
     * @param boolean
     */
    public abstract void openCase(ScheduledHearingValue shv, boolean isEdit) throws CSRecoverableException;

    public abstract void openCase(ApplicationCaseModel caseModel, boolean isEdit) throws CSRecoverableException;

    public abstract void closeCase() throws CSRecoverableException;

    /**
     * This method creates an instance of the XPanel, then tells the XHIBIT
     * Application Controller instance to display the panel in the main area
     * Some (deinitialising/closeing) actions on the currently displayed (if
     * any) XPanel may be undertaken.
     * 
     * @param xpanelClass
     * @param sig
     * @param args
     */
    public abstract void open(Class xpanelClass, Class[] sig, Object args[]);

    /**
     * Tells the XHIBIT Application Controller instance to display a specific
     * XPanel (newMainPanel) in the main area Some (deinitialising/closeing)
     * actions on the currently displayed (if any) XPanel may be undertaken.
     * 
     * @param newMainPanel
     */
    public abstract void open(XPanel newMainPanel); // , int panelType)

    /**
     * the close method is called by the closeXhibitApplication in
     * parentController or can be called by the open() method to firstly close
     * the panel before opening a new one. the close action needs to call
     * parentController.closeXhibitApplication
     */
    public abstract void close() throws CSRecoverableException;

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
    public abstract void close(boolean closeCase) throws CSRecoverableException;

    /**
     * This method calls the life cycle methods to finish any processing the
     * XPanel (active in the main display area) may have.
     * 
     * @throws CSRecoverableException
     */
    public abstract void callBodyPanelCloseLifeCycleMethods() throws CSRecoverableException;

    // Status bar methods
    public abstract String getStatusLabel();

    public abstract void setStatusLabel(String message);

    public abstract void setScreenLabel(String screenId);

    public abstract void setDialogLabel(String dialogId);

    public abstract ActionMap getActionMap();

    public abstract ActionMap getCourtLogActionMap();

    public abstract void setParentController(XhibitInterface controller);

    public abstract XhibitInterface getParentController();

    public abstract void setScreenActive(boolean newValue);

    public abstract boolean isScreenActive();

    public abstract void setHearingEnded(boolean newValue);

    public abstract boolean isHearingEnded();
    
    public abstract boolean isOkToAuthriseResults() 
        throws CSRecoverableException;

    /**
     * Will add the set of generic toolbars to the main container of this
     * dialog. Is thread-safe.
     */
    // public synchronized void addGenericToolBars ();
    public abstract void addGenericToolBars();
    
    public abstract CaseStatus getCaseStatus();
    
    public abstract void repaintScreen(Dimension newSize);
    
    public abstract void setCaseChargesDisposalsOpened(boolean enabled);
    
    public abstract boolean isCaseChargesDisposalsOpened();
}
