package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.models.RecentCase;
import uk.gov.courtservice.xhibit.client.util.PropertyChangeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;

/**
 * <p>
 * Title: Use this object to store XHIBIT cross view functions and objects
 * </p>
 * <p>
 * Description: Use this object to store XHIBIT cross view functions and objects
 * </p>
 * <P>
 * Note: This is different to XHIBIT Constants. That should be used for simple
 * static variables.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 * 
 */

public class XhibitSingleton extends PropertyChangeHelper {
    public static final String RECENTCASE = "RECENTCASE";

    public static final Logger log = CSServices.getLogger(XhibitSingleton.class);

    private static final String RECENTCASEFILE = PropertyHelper.getUserPropsDir()
            + System.getProperty("file.separator") + "cases.dat";

    /**
     * XhibitSingleton xs
     */
    private static XhibitSingleton xs = null;

    /**
     * Vector xacGroup
     */
    private static Vector xacGroup = new Vector();

    /**
     * ArrayList activeWindows
     */
    private ArrayList activeWindows = new ArrayList();

    /**
     * CSUserSession csUserSession
     */
    private CSUserSession csUserSession = null;

    /**
     * CourtBasicValue courtBasicValue
     */
    private CourtBasicValue courtBasicValue = null;

    /**
     * CourtRoomBasicValue courtRoomBasicValue
     */
    private CourtRoomBasicValue courtRoomBasicValue = null;

    /**
     * CourtStructureValue courtStructureValue
     */
    private CourtStructureValue courtStructureValue = null;

    /**
     * ArrayList recentCases
     */
    private ArrayList recentCases = null;

    /**
     * int recentCaseSize Number of recent cases to store
     */
    private int recentCaseSize = 5;

    /**
     * boolean userDeactivedScreen
     */
    private boolean userDeactivedScreen = false;

    /**
     * <init>
     */
    private XhibitSingleton() {
        loadRecentCaseList();
    }

    public void exitApplication() throws LoginException {
        storeRecentCaseList();
        getUserSession().logout();
    }

    /**
     * getInstance
     * 
     * @return the returned XhibitSingleton
     */
    public static synchronized XhibitSingleton getInstance() {
        if (xs == null) {
            log.info("XhibitSingleton:  Initialized (MAY ONLY OCCUR ONCE IN THE JVM'S LIFETIME)");
            xs = new XhibitSingleton();
        }
        return xs;
    }

    /**
     * // Xac Group methods
     */
    public static Vector getXacGroup() {
        return xacGroup;
    }

    /**
     * addXacToGroup
     * 
     * @param xac
     *            parameter for addXacToGroup
     */
    public static void addXacToGroup(XhibitApplicationController xac) {
        xacGroup.add(xac);
    }

    /**
     * removeXacFromGroup
     * 
     * @param xac
     *            parameter for removeXacFromGroup
     */
    public static void removeXacFromGroup(XhibitApplicationController xac) {
        xacGroup.remove(xac);
    }

    /**
     * xacLinkIfExistInGroup
     * 
     * @param caseId
     *            parameter for xacLinkIfExistInGroup
     * @return the returned boolean
     */
    // MOVE THIS METHOD INTO LINK CASE HELPER.
    // public static boolean xacLinkIfExistInGroup(Integer caseId)
    // {
    // boolean xacExists = false;
    // XhibitApplicationController xac;
    // Iterator xacIter = xacGroup.iterator();
    // while (xacIter.hasNext())
    // {
    // xac = (XhibitApplicationController)xacIter.next();
    // if (xac.getApplicationCaseModel() != null
    // && caseId.intValue() ==
    // xac.getApplicationCaseModel().getCaseId().intValue()
    // && !(xac.getBodyPanel() instanceof TodaysScheduleController))
    // {
    // xacExists = true;
    // //set linked to true (may already be linked)
    // xac.getApplicationCaseModel().setIsLinked(true);
    // break;
    // }
    // }
    //
    // return xacExists;
    // }
    /**
     * registerApp
     * 
     * @param xac
     *            parameter for registerApp
     */
    public void registerApp(XhibitApplicationController xac) {
        activeWindows.add(xac);
        enableCloseAction();
    }

    public ArrayList getRecentCaseList() {
        return recentCases;
    }

    public int getRecentCaseSize() {
        return recentCaseSize;
    }

    public void setRecentCaseSize(int newSize) {
        recentCaseSize = newSize;
    }

    public void maintainRecentCaseList(ApplicationCaseModel model) {
        RecentCase rc = new RecentCase(model);
        firePropertyChange(RECENTCASE, null, rc);
        recentCases.add(0, rc);
        for (int i = 1; i < recentCases.size(); i++) {
            RecentCase thisCase = (RecentCase) recentCases.get(i);
            if (thisCase.getCaseNumber().equals(rc.getCaseNumber())) {
                recentCases.remove(i);
            }
        }

        if (recentCases.size() > getRecentCaseSize()) {
            recentCases.remove(getRecentCaseSize());
        }
    }

    /**
     * Save the recent case list to file
     */
    private void storeRecentCaseList() {
        try {
            FileOutputStream ostream = new FileOutputStream(RECENTCASEFILE);
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            p.writeObject(recentCases);
            p.flush();
            ostream.close();
        } catch (IOException ex) {
            log.error("Recent Case List Not Saved:" + ex.getMessage());
        }
    }

    /**
     * Load the recent cases list from file, if available
     */
    private void loadRecentCaseList() {
        try {
            FileInputStream istream = new FileInputStream(RECENTCASEFILE);
            ObjectInputStream p = new ObjectInputStream(istream);
            recentCases = (ArrayList) p.readObject();
            istream.close();
        } catch (IOException ex) {
            log.error("Could not load recent cases:" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("File not an array list:" + ex.getMessage());
        } finally {
            if (recentCases == null)
                recentCases = new ArrayList();
        }
    }

    /**
     * Must be called when closing a window
     * 
     * @param xac -
     *            XhibitApplicationController
     */
    public void deregisterApp(XhibitApplicationController xac) {
        if (activeWindows.contains(xac)) {
            activeWindows.remove(xac);
            xac.dispose();
            enableCloseAction();
        }
    }

    /**
     * getApplications
     * 
     * @return the returned ArrayList
     */
    public ArrayList getApplications() {
        return activeWindows;
    }

    /**
     * setUserSession
     * 
     * @param csus
     *            parameter for setUserSession
     */
    public void setUserSession(CSUserSession csus) {
        csUserSession = csus;
    }

    /**
     * getUserSession
     * 
     * @return the returned CSUserSession
     */
    public CSUserSession getUserSession() {
        return csUserSession;
    }

    /**
     * getUserSession
     * 
     * @return the returned CSUserSession
     */
    public Subject getCurrentSubject() {
        CSUserSession session = getUserSession();
        if (session != null) {
            return session.getUserSubject();
        }
        return null;
    }

    /**
     * enableCloseAction
     */
    public void enableCloseAction() {
        if (getApplications().size() > 1) {
            Iterator iter = getApplications().iterator();
            while (iter.hasNext()) {
                XhibitApplicationController item = (XhibitApplicationController) iter.next();
                XhibitActions.getAction(item, XhibitActions.Close).setEnabled(true);
            }
        } else if (getApplications().size() == 1) {
            XhibitApplicationController xac = (XhibitApplicationController) getApplications().get(0);
            if (xac.getBodyPanel() == null) {
                XhibitActions.getAction(xac, XhibitActions.Close).setEnabled(false);
            }
        }

    }

    /**
     * If multiple windows are open close the window Otherwise only close the
     * panel in the center section
     * 
     * @return boolean
     */
    public boolean closeWindow() {
        if (activeWindows.size() > 1)
            return true;
        else
            return false;
    }

    /**
     * isUserInCourtroom
     * 
     * @return the returned boolean
     */
    public boolean isUserInCourtroom() {
        String cr = null;

        if (getUserSession() != null) {
            cr = getUserSession().getSessionProperty(UserTerminalProperties.COURT_ROOM_ID);
        }

        return (cr == null ? false : true);
    }

    /**
     * getCourtId
     * 
     * @return the returned Integer
     */
    public Integer getCourtId() {
        String value = null;
        if (getUserSession() != null) {
            value = getUserSession().getSessionProperty(UserTerminalProperties.COURT_ID);
        }
        if (value == null)
            return null;
        else
            return new Integer(value);
    }

    /**
     * getCourtSiteId
     * 
     * @return the returned Integer
     */
    public Integer getCourtSiteId() {
        String value = null;
        if (getUserSession() != null) {
            value = getUserSession().getSessionProperty(UserTerminalProperties.COURT_SITE_ID);
        }
        if (value == null)
            return null;
        else
            return new Integer(value);
    }

    /**
     * getCourtRoomId
     * 
     * @return the returned Integer
     */
    public Integer getCourtRoomId() {
        String value = null;
        if (getUserSession() != null) {
            value = getUserSession().getSessionProperty(UserTerminalProperties.COURT_ROOM_ID);
        }

        if (value == null)
            return null;
        else
            return new Integer(value);
    }

    public boolean isTerminalRoaming() {
        if (getUserSession() != null && getUserSession().getSessionProperty(UserTerminalProperties.ROAMING) != null) {
            return getUserSession().getSessionProperty(UserTerminalProperties.ROAMING).equalsIgnoreCase("Y");
        }
        return false;
    }

    /**
     * setUserDeactivedScreen
     * 
     * @param newValue
     *            parameter for setUserDeactivedScreen
     */
    public void setUserDeactivedScreen(boolean newValue) {
        boolean oldValue = userDeactivedScreen;
        userDeactivedScreen = newValue;
        firePropertyChange("screendisabled", oldValue, newValue);
    }

    /**
     * isScreenUserDeactived
     * 
     * @return the returned boolean
     */
    public boolean isScreenUserDeactived() {
        return userDeactivedScreen;
    }

    /**
     * getLinkGroup
     * 
     * @param xac
     *            parameter for getLinkGroup
     * @param includeThisOne
     *            parameter for getLinkGroup
     * @return the returned Collection
     * @throws HearingScheduleException -
     */
    public Collection getLinkGroup(XhibitApplicationController xac, boolean includeThisOne)
            throws HearingScheduleException {
        ArrayList al = new ArrayList();

     
        // make BD call
        CaseSchedHearingValue[] hv = getLinkedHearingList(xac.getApplicationCaseModel().getScheduledHearingId());

        // for list of id's returned, find xac in list and add to array
        for (int i = 0; i < hv.length; i++) {
            XhibitApplicationController thisXac = getXacForShID(hv[i].getScheduledHearingId());

            // Will not be added if duplicate.
            if (thisXac != null && thisXac != xac) {
                al.add(thisXac);
            }
        }
        
        // Only if not included
        // The order of the main controller being added last means that during refreshes the currect
        // active screen remains in focus (i.e. we do no swap to another case in the linked chain)
        
        if (includeThisOne) {
            al.add(xac);
        }

        
        return al;
    }

    /**
     * getXacForShID
     * 
     * @param shId
     *            parameter for getXacForShID
     * @return the returned XhibitApplicationController
     */
    public XhibitApplicationController getXacForShID(Integer shId) {
        XhibitApplicationController foundXac = null;
        Iterator iter = activeWindows.iterator();
        while (iter.hasNext()) {
            Object item = iter.next(); // may be null
            if (item != null && item instanceof XhibitApplicationController) {
                XhibitApplicationController xacitem = (XhibitApplicationController) item;
                if (xacitem.getApplicationCaseModel() != null
                        && xacitem.getApplicationCaseModel().getScheduledHearingId() != null
                        && xacitem.getApplicationCaseModel().getScheduledHearingId().equals(shId)) {
                    foundXac = xacitem;
                    break;
                }
            }
        }
        return foundXac;
    }

    /**
     * getLinkedHearingList
     * 
     * @param shId
     *            parameter for getLinkedHearingList
     * @return the returned CaseSchedHearingValue[]
     * @throws HearingScheduleException -
     */
    public CaseSchedHearingValue[] getLinkedHearingList(Integer shId) throws HearingScheduleException {
        return XhibitDelegateHelper.getHearingDelegate().getLinkedSchedHearingsByShId(shId);
    }

    /**
     * getCourtBasicValue
     * 
     * @param courtId
     *            parameter for getCourtBasicValue
     * @return the returned CourtBasicValue
     * @throws CSRecoverableException -
     */
    public CourtBasicValue getCourtBasicValue(Integer courtId) throws CSRecoverableException {
        BisRefControllerBeanBusinessDelegate delegate = getBisRefDelegate();

        CourtBasicValue courtBasicValue;

        CourtCriteria courtCriteria = new CourtCriteria();
        courtCriteria.setPrimaryKey(courtId);

        Collection courts = delegate.findCourts(courtCriteria);
        if (courts == null || courts.size() != 1) {
            Object[] params = { courts == null ? "is null" : "" + courts.size() + " courts" };
            CSRecoverableException csre = new CSRecoverableException("gui.log.sysref.findCourtText", params,
                    "gui.user.sysref.findCourtText");
            throw csre;
        } else {
            Iterator i = courts.iterator();
            courtBasicValue = (CourtBasicValue) i.next();
        }
        return courtBasicValue;
    }
    
    public RefCourtBasicValue getRefCourtByCourtID(Integer refCourtID)
    {
    	try {
    		BisRefControllerBeanBusinessDelegate delegate = getBisRefDelegate();
        
			RefCourtBasicValue courts = delegate.findCourtByRefId(refCourtID);
			
			if(courts!=null)
			{
				return courts;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return new RefCourtBasicValue();
    }

    /**
     * getCourtBasicValue
     * 
     * @return the returned CourtBasicValue
     * @throws CSRecoverableException -
     */
    public CourtBasicValue getCourtBasicValue() throws CSRecoverableException {
        if (courtBasicValue == null) {
            Integer courtId = getCourtId();
            courtBasicValue = getCourtBasicValue(courtId);
        }
        return courtBasicValue;
    }

    /**
     * getCourtRoomBasicValue
     * 
     * @param courtRoomId
     *            parameter for getCourtRoomBasicValue
     * @return the returned CourtRoomBasicValue
     * @throws CSRecoverableException -
     */
    public CourtRoomBasicValue getCourtRoomBasicValue(Integer courtRoomId) throws CSRecoverableException {
        BisRefControllerBeanBusinessDelegate delegate = getBisRefDelegate();
        CourtRoomBasicValue courtRoomBasicValue;

        CourtRoomCriteria courtRoomCriteria = new CourtRoomCriteria();
        courtRoomCriteria.setPrimaryKey(courtRoomId);

        Collection courtsRooms = delegate.findCourtRooms(courtRoomCriteria);
        if (courtsRooms == null || courtsRooms.size() != 1) {
            Object[] params = { courtsRooms == null ? "is null" : "" + courtsRooms.size() + " court rooms" };
            CSRecoverableException csre = new CSRecoverableException("gui.log.sysref.findCourtText", params,
                    "gui.user.sysref.findCourtText");
            throw csre;
        } else {
            Iterator i = courtsRooms.iterator();
            courtRoomBasicValue = (CourtRoomBasicValue) i.next();
        }
        return courtRoomBasicValue;
    }

    /**
     * getCourtRoomBasicValue
     * 
     * @return the returned CourtRoomBasicValue
     * @throws CSRecoverableException -
     */
    public CourtRoomBasicValue getCourtRoomBasicValue() throws CSRecoverableException {
        if (courtRoomBasicValue == null) {
            Integer courtRoomId = getCourtRoomId();
            courtRoomBasicValue = getCourtRoomBasicValue(courtRoomId);
        }
        return courtRoomBasicValue;
    }

    public void clearCourtDataCache() {
        // This is used primarily for hot switchnig on roaming terminals
        courtBasicValue = null;
        courtRoomBasicValue = null;
        courtStructureValue = null;
    }

    public CourtStructureValue getCourtStructureValue() {
        if (courtStructureValue == null) {
            courtStructureValue = XhibitDelegateHelper.getViewScheduleDelegate().getCourtStructure(getCourtId());
        }
        return courtStructureValue;
    }

    /**
     * getSysRefDelegate
     * 
     * @return the returned SysRefControllerBusinessDelegate
     * @throws CSRecoverableException -
     */
    private BisRefControllerBeanBusinessDelegate getBisRefDelegate() throws CSRecoverableException {
        return XhibitDelegateHelper.getBizRefDelegate();
    }
}