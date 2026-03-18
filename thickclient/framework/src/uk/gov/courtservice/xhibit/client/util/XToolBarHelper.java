package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.common.ViewToolbarAction;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: This class is responsible for containing all the toolbar
 * functionality for the client application. Note that
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
public class XToolBarHelper extends Object implements java.awt.event.ComponentListener {

    /**
     * Constants that determine the current status of a toolbar: <p/>
     * <code>TOOLBAR_OFF</code> means that the user deactivated the toolbar it
     * should not be displayed in the application. <p/> <code>TOOLBAR_OFF</code>
     * means that the toolbar is currently not visible. <p/>
     * <code>TOOLBAR_ON_APPLICATION</code> means that the toolbar should be
     * displayed during the life scope of the application from the time it is
     * set. <p/> <code>TOOLBAR_ON_PERSISTENT</code> means that the toolbar
     * should be displayed at ALL times.
     */
    public static final int TOOLBAR_USER_OFF = -1;

    public static final int TOOLBAR_APPLICATION_OFF = 0;

    public static final int TOOLBAR_ON_APPLICATION = 1;

    public static final int TOOLBAR_ON_PERSISTENT = 2;

    /**
     * This references the name of the toolbar properties file stored in the
     * userhome directory of the user.
     */
    public static final String LOCAL_TOOLBAR_PROPERTIES = "GUI.LocalToolBar.properties";

    /**
     * The key for the toolbar properties in config, representing the keyword
     * list.
     */
    public static final String KEY_WORD_LIST = "keywordlist";

    /**
     * The ID value for Court Log, needed to add other toolbars against hearing
     * type.
     */
    public static final String COURT_LOG_ID = "CourtLog";

    /**
     * The properties for the toolbars.
     */
    private static Properties toolBarProperties = null;

    /**
     * The properties for the toolbars status'.
     */
    private static Properties toolBarStatusProperties = null;

    /**
     * The logger used for monitoring the operations of this class.
     */
    private static Logger logger = null;

    /** The reference to the singleton instance. */
    // private static XToolBarHelper toolBarHelper = null;
    /**
     * Contains the toolbars used to include in Xhibit.
     */
    private Hashtable toolBarHash = null;

    /**
     * Contains Checkbox menu items, used to render the toolbars. key = toolbar
     * id value = checkbox
     */
    private HashMap toolBarCheckBoxs = null;

    /**
     * Cache for vector of toolbars associated to class. <p/> key: Class. value:
     * Vector of toolbars.
     */
    private HashMap toolBarsForClass = null;

    /**
     * Cache for vector of toolbars associated to keyword. <p/> key: Keyword
     * value: Vector of toolbars
     */
    private HashMap toolBarsForKeyword = null;

    /**
     * The hearing type associated to the application.
     */
    private String hearingType = null;

    /**
     * An array list containing the keywords in the properties file.
     */
    private ArrayList keywords;

    /**
     * The application containing this instance of the toolbar.
     */
    private XhibitApplicationController xhibitAppController = null;

    private ArrayList applicationToolBarsList;

    private static final Logger log = CSServices.getLogger(XToolBarHelper.class);

    /** Load the properties for the toolbars. */
    static {
        logger = CSServices.getLogger(XToolBarHelper.class);
        logger.debug("Started static initialisation of ToolBarHelper - Created a Logger instance.");
        try {
            toolBarProperties = PropertyHelper.getProperties(XhibitProperties.ToolBar);
            toolBarStatusProperties = PropertyHelper.getUserHomeProperties(LOCAL_TOOLBAR_PROPERTIES);
            if (toolBarStatusProperties == null) {
                PropertyHelper.createUserHomeProperties(LOCAL_TOOLBAR_PROPERTIES, "General.status="
                        + TOOLBAR_ON_PERSISTENT);
                toolBarStatusProperties = PropertyHelper.getUserHomeProperties(LOCAL_TOOLBAR_PROPERTIES);
            }

        } catch (Exception e) {
            logger.debug("Error getting toolBar properties.");
            logger.debug(e.getMessage());
        }
    }

    /**
     * Default constructor. Creates an instance of this helper class, with
     * cleared caches.
     */
    public XToolBarHelper(XhibitApplicationController appController) {
        super();
        this.toolBarsForClass = new HashMap();
        this.toolBarCheckBoxs = new HashMap();
        this.toolBarsForKeyword = new HashMap();
        this.keywords = new ArrayList();
        this.storeKeywords();
        this.xhibitAppController = appController;

        // Create an ArrayList to hold Application Toolbar references
        this.applicationToolBarsList = new ArrayList();
    }

    public void refreshApplicationToolbars() {
        // clear Application toolbar settings
        this.applicationToolBarsList.clear();
    }

    /**
     * @return the menu of toolbars.
     */
    public JMenu getMenu() {
        ResourceBundle menuResources = XHIBITConstant.getResourceBundle("XhibitMenuResources");
        JMenu menu = new JMenu(menuResources.getString("ToolbarMenu"));
        menu.setMnemonic(menuResources.getString("ToolbarMenuMnemonic").charAt(0));
        menu.setIcon(new BlankIcon());

        JCheckBoxMenuItem checkBoxMenuItem = null;
        JToolBar toolBar = null;
        Iterator toolBarList = this.toolBarHash.values().iterator();
        while (toolBarList.hasNext()) {
            toolBar = (JToolBar) toolBarList.next();
            ((Component) toolBar).addComponentListener(this);
            checkBoxMenuItem = new JCheckBoxMenuItem(toolBar.getName(), toolBar.isShowing());
            checkBoxMenuItem.addActionListener(ViewToolbarAction.getInstance());
            checkBoxMenuItem.putClientProperty("toolBarId", toolBar.getClientProperty("id"));
            checkBoxMenuItem.putClientProperty("xhibitController", this.xhibitAppController);

            // Set the checkbox as selected, if its status is on persistent.
            String status = this.getProperty(this.toolBarStatusProperties, toolBar.getClientProperty("id") + ".status");
            if ((status != null) && (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT)) {
                checkBoxMenuItem.setSelected(true);
            }

            // Cache the checkbox for future use.
            this.toolBarCheckBoxs.put(toolBar.getClientProperty("id"), checkBoxMenuItem);

            menu.add(checkBoxMenuItem);
        }
        return menu;
    }

    /**
     * @return the singleton instance.
     */
    // public static XToolBarHelper getInstance ()
    // {
    // return toolBarHelper;
    // }
    /**
     * Set the hashtable of toolbars to be used for this application.
     * 
     * @param toolBarHash
     *            the hashtable of toolbars.
     */
    public void setToolBarHash(Hashtable toolBarHash) {
        this.toolBarHash = toolBarHash;
    }

    /**
     * @return the toolbars used in the application.
     */
    public Hashtable getToolBarHash() {
        return this.toolBarHash;
    }

    /**
     * Will retrieve an enumeration of toolbars for a specific class. <p/> The
     * check is made in the toolbar properties file. Key = class name Value =
     * toolbars, separated by comma delimitter.
     * 
     * @param classRef
     *            the reference to the given class.
     * @return the toolbars associated to the given class.
     */
    private Enumeration getToolBarsForClass(Class classRef) {
        String className = classRef.getName();

        // Return the toolbars if they are in the cache.
        if (this.toolBarsForClass.get(className) != null) {
            return ((Vector) this.toolBarsForClass.get(className)).elements();
        }

        Vector toolBars = new Vector();
        String toolBarSet = this.getProperty(this.toolBarProperties, className);
        if (toolBarSet != null) {
            // Create the toolbars and store them in the hash.
            StringTokenizer tokenizer = new StringTokenizer(toolBarSet, ",");
            String nextToken = null;
            while (tokenizer.hasMoreElements()) {
                nextToken = tokenizer.nextToken();
                if (nextToken != null) {
                    toolBars.add(this.toolBarHash.get(nextToken));
                }
            }
            this.toolBarsForClass.put(className, toolBars);
        }

        // Return the enumeration.
        return toolBars.elements();
    }

    /**
     * Store the keywords from the property file.
     */
    private void storeKeywords() {
        String keywordList = this.getProperty(this.toolBarProperties, KEY_WORD_LIST);
        if (keywordList != null) {
            StringTokenizer tokenizer = new StringTokenizer(keywordList, ",");
            String token = null;
            while (tokenizer.hasMoreElements()) {
                token = tokenizer.nextToken();
                this.keywords.add(token);
            }
        }
    }

    /**
     * Will get the list of toolbars for each keyword in the properties, that
     * matches a substring of the given text.
     * 
     * @param text
     *            the text to which its full set of substrings will be sought
     *            for keyword match.
     * @return an enumeration of the toolbars.
     */
    private Enumeration getToolBarsForKeywords(String keyword) {
        // Return the toolbars if they are in the cache.
        if (this.toolBarsForKeyword.get(keyword) != null) {
            return ((Vector) this.toolBarsForKeyword.get(keyword)).elements();
        }

        Vector toolBars = new Vector();
        String toolBarList = this.getProperty(this.toolBarProperties, keyword);
        if (toolBarList != null) {
            StringTokenizer tokenizer = new StringTokenizer(toolBarList, ",");
            String token = null;
            while (tokenizer.hasMoreElements()) {
                token = tokenizer.nextToken();
                if (token != null) {
                    toolBars.add(this.toolBarHash.get(token));
                }
            }
            this.toolBarsForKeyword.put(keyword, toolBars);
        }

        // Return the enumeration.
        return toolBars.elements();
    }

    /**
     * Will return an enumeration of toolbars that have an assocation with the
     * given status.
     * 
     * @param toolBarStatus
     *            the status of the toolbars to be returned.
     * @return the enumeration of toolbars.
     */
    private Enumeration getToolBarsForStatus(int toolBarStatus) {
        // Get the toolbar list value.
        Vector toolBars = new Vector();
        String toolBarId = null;
        String toolBarStatusStr = null;
        Enumeration toolBarList = this.toolBarHash.keys();
        while (toolBarList.hasMoreElements()) {
            toolBarId = (String) toolBarList.nextElement();
            toolBarStatusStr = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");
            if (toolBarStatusStr != null && (Integer.parseInt(toolBarStatusStr) == toolBarStatus)) {
                toolBars.add(this.toolBarHash.get(toolBarId));
            }
        }

        // Return the enumeration.
        return toolBars.elements();
    }

    /**
     * Set the current status to a specific toolbar.
     * 
     * @param toolBarId
     *            the property id of the toolbar.
     * @param status
     *            the status to be set to the toolbar.
     */
    private void setToolBarStatus(String toolBarId, int status) {
        this.setProperty(this.toolBarStatusProperties, toolBarId + ".status", "" + status);
    }

    /**
     * Turn the toolbar on or off
     * 
     * @param toolBarId
     *            the property id of the toolbar.
     * @param activate
     *            the status to be set to the toolbar.
     */
    public void activateToolbar(String toolBarId, boolean activate) {
        int newStatus;
        if (activate) {
            // If the toolbar is normally displayed in this window
            // set to ON_APPLICATION, otherwise ON_PERSISTANT
            if (isToolbarDisplayInWindow(toolBarId)) {
                newStatus = TOOLBAR_ON_APPLICATION;
            } else {
                newStatus = TOOLBAR_ON_PERSISTENT;
            }
        } else {
            // If the toolbar is normally displayed in this window
            // set to USER_OFF, otherwise APPLICATION_OFF
            if (isToolbarDisplayInWindow(toolBarId)) {
                newStatus = TOOLBAR_USER_OFF;
            } else {
                newStatus = TOOLBAR_APPLICATION_OFF;
            }
        }
        // Save the new status
        setToolBarStatus(toolBarId, newStatus);
    }

    /**
     * Find out if the toolbar passed in is displayed by the application when
     * the current body panel is loaded.
     * 
     * @param toolBarId
     *            toolbarId required to be checked
     * @return true if toolbar is loaded
     */
    private boolean isToolbarDisplayInWindow(String toolBarId) {
        boolean found = false;
        boolean hasCourtLog = false;
        JToolBar toolBar = null;
        String thisToolBarId = null;

        if (this.xhibitAppController.getBodyPanel() == null)
            return false;

        Class classRef = this.xhibitAppController.getBodyPanel().getClass();
        Enumeration toolBars = this.getToolBarsForClass(classRef);
        while (!found && toolBars.hasMoreElements()) {
            toolBar = (JToolBar) toolBars.nextElement();
            thisToolBarId = (String) toolBar.getClientProperty("id");

            if (thisToolBarId.equals(this.COURT_LOG_ID))
                hasCourtLog = true;
            if (toolBarId.equals(thisToolBarId))
                found = true;
        }

        // If this page displays the court log toolbar and
        // we haven't found the toolbar yet, check hearing
        // specific toolbars
        if (hasCourtLog && !found) {
            String keyword = null;
            for (int i = 0; !found && i < this.keywords.size(); i++) {
                keyword = (String) keywords.get(i);
                if (keywordInHearingType(keyword)) {
                    Enumeration e = getToolBarsForKeywords(keyword);
                    while (!found && e.hasMoreElements()) {
                        toolBar = (JToolBar) e.nextElement();
                        thisToolBarId = (String) toolBar.getClientProperty("id");
                        if (toolBarId.equals(thisToolBarId))
                            found = true;
                    }
                }
            }
        }
        return found;
    }

    /**
     * Will refresh the toolbars for just a particular state.
     * 
     * @param toolBarState
     *            the state to which the associated toolbars will be refreshed.
     */
    public void refreshToolBarState(int toolBarState) {
        JToolBar toolBar = null;
        String toolBarId = null;
        String status = null;
        Iterator toolBars = this.toolBarHash.values().iterator();

        while (toolBars.hasNext()) {
            toolBar = (JToolBar) toolBars.next();
            if (toolBar == null)
                continue;

            toolBarId = (String) toolBar.getClientProperty("id");
            status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");
            toolBar.setVisible((status != null) && (Integer.parseInt(status) >= TOOLBAR_ON_APPLICATION
            // Integer.parseInt(status) >=
                    // TOOLBAR_ON_PERSISTENT
                    || Integer.parseInt(status) == toolBarState || applicationToolBarsList.contains(toolBarId)));

        }
        // Will also add the visible toolbars to the controller.
        this.xhibitAppController.addGenericToolBars();
    }

    /**
     * This method will traverse the toolbars in the properties and display the
     * ones who have status TOOLBAR_ON_PERSISTENT or TOOLBAR_ON_APPLICATION.
     */
    public void refreshToolBarState() {
        JToolBar toolBar = null;
        String toolBarId = null;
        Iterator toolBars = this.toolBarHash.values().iterator();
        while (toolBars.hasNext()) {
            toolBar = (JToolBar) toolBars.next();
            if (toolBar == null)
                continue;
            toolBarId = (String) toolBar.getClientProperty("id");
            refreshToolBarState(toolBar, toolBarId);
        }
        // Will also add the visible toolbars to the controller.
        this.xhibitAppController.addGenericToolBars();
    }

    private void refreshToolBarState(JToolBar toolBar, String toolBarId) throws NumberFormatException {
        String status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");
        // if ( (status != null) && (
        // (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT)
        // // || (Integer.parseInt(status) == TOOLBAR_ON_APPLICATION)
        // ||
        // ( applicationToolBarsList.contains(toolBarId)
        // && Integer.parseInt(status) != TOOLBAR_APPLICATION_OFF )
        // ))
        if ((status != null) && Integer.parseInt(status) >= TOOLBAR_ON_APPLICATION)
        // || (applicationToolBarsList.contains(toolBarId)) )
        {
            toolBar.setVisible(true);
        } else {
            toolBar.setVisible(false);
        }
    }

    public void refreshToolBarState(String selectedToolBarId) {
        JToolBar toolBar = null;
        String toolBarId = null;
        String status = null;
        Iterator toolBars = this.toolBarHash.values().iterator();
        while (toolBars.hasNext()) {
            toolBar = (JToolBar) toolBars.next();
            if (toolBar == null)
                continue;
            toolBarId = (String) toolBar.getClientProperty("id");
            if (selectedToolBarId.equals(toolBarId)) {
                refreshToolBarState(toolBar, toolBarId);
                break;
            }
        }
        // Will also add the visible toolbars to the controller.
        this.xhibitAppController.addGenericToolBars();
    }

    /**
     * Will refresh the status of the toolbars on a load of a specific class.
     * 
     * @param classRef
     *            the class that is 'loaded'.
     */
    public void refreshToolBarOnLoad(Class classRef) {

        /**
         * @todo Call to This code works but there may be a neater way to do
         *       this.
         */
        refreshCourtLogApplicationSetting(classRef);

        // Refresh.
        this.refreshToolBarState(TOOLBAR_ON_PERSISTENT);

        JToolBar toolBar = null;
        Enumeration toolBars = this.getToolBarsForClass(classRef);
        String status = null;
        String toolBarId = null;
        JCheckBoxMenuItem checkBoxMenuItem = null;

        while (toolBars.hasMoreElements()) {
            toolBar = (JToolBar) toolBars.nextElement();
            if (toolBar == null)
                continue;
            toolBarId = (String) toolBar.getClientProperty("id");
            status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");

            checkBoxMenuItem = (JCheckBoxMenuItem) this.toolBarCheckBoxs.get(toolBarId);

            if (toolBarId.equals(this.COURT_LOG_ID)) {
                setCourtLogToolBarsDisplay(true);
            }

            // If the status is persistent or user de-activated,
            // then we don't care - toolbar remains in that state,
            // so just continue through the loop.
            if ((status != null)
                    && (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT || Integer.parseInt(status) == TOOLBAR_USER_OFF)) {
                if (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT) {
                    checkBoxMenuItem.setSelected(true);
                }
                continue;
            }

            // Set the status to be on during the lifetime of the
            // application.
            this.setProperty(this.toolBarStatusProperties, toolBarId + ".status", "" + TOOLBAR_ON_APPLICATION);

            applicationToolBarsList.add(toolBarId);

            // Check the menu checkbox as selected.
            if (checkBoxMenuItem != null) {
                checkBoxMenuItem.setSelected(true);
                toolBar.setVisible(true);
            }
        }

        refreshToolbarCheckboxes();

        this.xhibitAppController.addGenericToolBars();
    }

    /**
     * Will refresh the toolbars on an 'unload' of a class.
     * 
     * @param classRef
     *            the class that is unloaded.
     */
    public void refreshToolBarOnUnload(Class classRef) {
        JToolBar toolBar = null;
        Enumeration toolBars = this.getToolBarsForClass(classRef);
        String status = null;
        String toolBarId = null;
        JCheckBoxMenuItem checkBoxMenuItem = null;

        // If the class invoking this method is courtlogcontroller, then also
        // unload the related
        // toolbars for hearing type.
        if (classRef.getName().equals("uk.gov.courtservice.xhibit.client.courtlog.CourtLogController")) {
            setCourtLogToolBarsDisplay(false);
        }

        while (toolBars.hasMoreElements()) {
            toolBar = (JToolBar) toolBars.nextElement();
            if (toolBar == null)
                continue;
            toolBarId = (String) toolBar.getClientProperty("id");
            status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");

            checkBoxMenuItem = (JCheckBoxMenuItem) this.toolBarCheckBoxs.get(toolBarId);

            // Persistent is always on, continue.
            // If the status is persistent or user de-activated,
            // then we don't care - toolbar remains in that state,
            // so just continue through the loop.
            if ((status != null)
                    && (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT || Integer.parseInt(status) == TOOLBAR_USER_OFF)) {
                // Ensure checkbox is set to false(>1 window)
                continue;
            }

            // Class is unloaded, so toolbar must be set off.
            this.setProperty(this.toolBarStatusProperties, toolBarId + ".status", "" + TOOLBAR_APPLICATION_OFF);
            applicationToolBarsList.remove(toolBarId);

            // Check the checbox as unselected.
            if (checkBoxMenuItem != null) {
                checkBoxMenuItem.setSelected(false);
                toolBar.setVisible(false);
            }
        }

        // Refresh
        this.refreshToolBarState(TOOLBAR_ON_PERSISTENT);
        refreshToolbarCheckboxes();
    }

    /**
     * Store the status properties to its associated file.
     */
    public void storeProperties() {
        try {
            Enumeration toolBarIdList = this.toolBarHash.keys();
            String status = null;
            String toolBarId = null;
            while (toolBarIdList.hasMoreElements()) {
                toolBarId = (String) toolBarIdList.nextElement();
                status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");

                if ((status == null) || (Integer.parseInt(status) == TOOLBAR_ON_APPLICATION)) {
                    this.setProperty(this.toolBarStatusProperties, toolBarId + ".status", "" + TOOLBAR_APPLICATION_OFF);
                }
            }
            PropertyHelper.storeUserHomeProperties(this.toolBarStatusProperties, LOCAL_TOOLBAR_PROPERTIES);
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    /**
     * Sets the 'value' property for the given value. Will return true is
     * operation performed successfully.
     * <P>
     * 
     * @param key
     *            the key attribute of the property
     * @param value
     *            the value to be stored for the associated key
     * @return whether the operation was successful.
     */
    private synchronized boolean setProperty(Properties properties, String key, String value) {
        boolean operationPerformance = false;
        try {
            properties.setProperty(key, value);
            operationPerformance = true;
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }
        return operationPerformance;
    }

    /**
     * Get the value for the corresponding key. Will return null if there is no
     * key within the property.
     * 
     * @param key
     *            the key from which the value will be returned.
     * @return the value - or null if the operation was unsuccessful.
     */
    private synchronized String getProperty(Properties properties, String key) {
        Object value = properties.get(key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * Makes a check to see if there is a match with the given keyword and any
     * of the hearing types substrings - where substring length = keyword
     * length.
     * 
     * @param keyword
     *            the keyword to check to see if there is a match.
     * @return whether a match was found.
     */
    private boolean keywordInHearingType(String keyword) {
        // Always return false if there is a null hearing type.
        if (hearingType == null) {
            return false;
        }

        int index = 0;
        int length = keyword.length();
        int hearingTypeLength = this.hearingType.length();
        String subHearingString = null;
        while (index <= hearingTypeLength) {
            // Extra check to prevent indexoutofbounds exception.
            if ((index + length) > hearingTypeLength) {
                break;
            }

            // Reference the new substring
            subHearingString = this.hearingType.substring(index, (index + length));

            // Return true if there is a match with the keyword.
            if (subHearingString.equalsIgnoreCase(keyword)) {
                return true;
            }

            index++;
        }
        return false;
    }

    /**
     * This method will collate all the associated toolbars to the keyword from
     * the properties file in config. The visibility of each toolbar becomes set
     * to 'display'.
     * 
     * @param keyword
     *            the keyword to get from the properties file and its associated
     *            toolbars.
     * @param display
     *            the visibility attribute to be set to all the toolbars.
     */
    private void displayToolBarsForKeyword(String keyword, boolean display) {
        JToolBar toolBar = null;
        String toolBarId = null;
        String status = null;

        Enumeration toolBars = this.getToolBarsForKeywords(keyword);
        while (toolBars.hasMoreElements()) {
            toolBar = (JToolBar) toolBars.nextElement();
            toolBarId = (String) toolBar.getClientProperty("id");
            status = this.getProperty(this.toolBarStatusProperties, toolBarId + ".status");

            // If the status is persistent or user de-activated,
            // then we don't care - toolbar remains in that state,
            // so just continue through the loop.
            if ((status != null)
                    && (Integer.parseInt(status) == TOOLBAR_ON_PERSISTENT || Integer.parseInt(status) == TOOLBAR_USER_OFF)) {
                continue;
            }

            if (display) {
                // Check if in ArrayList -If it isn't then add toolBarId to
                // ArrayList
                if (!applicationToolBarsList.contains(toolBarId)) {
                    applicationToolBarsList.add(toolBarId);
                }
            } else {
                setToolBarStatus(toolBarId, this.TOOLBAR_APPLICATION_OFF);

                // Check if in ArrayList - If it is then remove toolBarId from
                // ArrayList
                if (applicationToolBarsList.contains(toolBarId)) {
                    applicationToolBarsList.remove(toolBarId);
                }
            }
            toolBar.setVisible(display);
            ((JCheckBoxMenuItem) this.toolBarCheckBoxs.get(toolBar.getClientProperty("id"))).setSelected(display);
        }
    }

    /**
     * Will set the visibility of toolbars related to the hearing type to
     * 'display'. Related toolbars are determined by the keywords defined in the
     * properties in config.
     * 
     * @param display
     *            the visibilty attribute to be set to the courtlogcontroller
     *            related toolbars.
     */
    private void setCourtLogToolBarsDisplay(boolean display) {
        String keyword = null;
        for (int i = 0; i < this.keywords.size(); i++) {
            keyword = (String) keywords.get(i);
            if (keywordInHearingType(keyword)) {
                displayToolBarsForKeyword(keyword, display);
            }
        }
    }

    /**
     * ComponentListener methods.
     */
    public void componentHidden(java.awt.event.ComponentEvent e) {
    }

    public void componentMoved(java.awt.event.ComponentEvent e) {
    }

    public void componentResized(java.awt.event.ComponentEvent e) {
    }

    /**
     * Whenever a toolbar is made visible, a check will be made to see if its an
     * instance of a courtlogcontroller toolbar. If so, then also set visible
     * the other related toolbars with the associated hearing type.
     * 
     * @param e
     *            the component event, where source is always of instance
     *            JToolBar.
     */
    public void componentShown(java.awt.event.ComponentEvent e) {
        JToolBar toolBar = (JToolBar) e.getSource();
        if (toolBar.getClientProperty("id").equals(this.COURT_LOG_ID)) {
            setCourtLogToolBarsDisplay(true);
            this.xhibitAppController.addGenericToolBars();
        }
    }

    /**
     * All toolbars status is being checked to ensure the appropriate checkbox
     * menu items are being selected
     */
    private void refreshToolbarCheckboxes() {
        JCheckBoxMenuItem checkBoxMenuItem = null;
        JToolBar toolBar = null;
        String toolBarId = null;
        Iterator toolBarList = this.toolBarHash.values().iterator();

        while (toolBarList.hasNext()) {
            toolBar = (JToolBar) toolBarList.next();
            if (toolBar == null)
                continue;
            toolBarId = (String) toolBar.getClientProperty("id");

            // retrieve checkbox and set to selected
            checkBoxMenuItem = (JCheckBoxMenuItem) this.toolBarCheckBoxs.get(toolBar.getClientProperty("id"));

            if (checkBoxMenuItem != null) {
                // Set the checkbox as selected, if its status is on persistent.
                String status = this.getProperty(this.toolBarStatusProperties, toolBar.getClientProperty("id")
                        + ".status");
                if ((status != null) && (Integer.parseInt(status) == this.TOOLBAR_ON_PERSISTENT)) {
                    checkBoxMenuItem.setSelected(true);
                }
                // set the checkbox as unselected if the status is user or
                // application off
                // OR the applicationToolBarList array does not contain toolbar.
                else if ((status != null) && (Integer.parseInt(status) <= this.TOOLBAR_APPLICATION_OFF)
                        && (!applicationToolBarsList.contains(toolBarId))) {
                    checkBoxMenuItem.setSelected(false);
                }
            }
        }
    }

    /**
     * This method checks the CourtLogToolbar status. If it has a status of
     * TOOLBAR_ON_APPLICATION on then this is set to TOOLBAR_APPLICATION_OFF.
     * The code prevents the courtlog toolbar appearing on TodaysSchedule if
     * there is more than one window open with differing main panels. Having the
     * CourtLogController displayed in one window sets the CourtLog Toolbar to
     * TOOLBAR_ON_APPLICATION. If the second window contains a
     * TodaysScheduleController(or blank panel), any refreshing (loading
     * TodaySchedule again) results in displaying the CourtLog toolbar because
     * the refreshToolBarOnUnload method fired before refreshToolBarOnLoad does
     * not set the CourtLog ToolBar to TOOLBAR_APPLICATION_OFF.
     */
    private void refreshCourtLogApplicationSetting(Class classRef) {
        if (classRef.getName().equals("uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController")
                || classRef.getName().equals("javax.swing.JPanel")) {
            String currentStatus = this.getProperty(this.toolBarStatusProperties, COURT_LOG_ID + ".status");
            if (currentStatus != null) {
                if (Integer.parseInt(currentStatus) == this.TOOLBAR_ON_APPLICATION) {
                    // Reset Courtlog to TOOLBAR_APPLICATION_OFF is it is ON
                    setToolBarStatus(COURT_LOG_ID, this.TOOLBAR_APPLICATION_OFF);
                }
            }
        }
    }
}