package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 Client Application
 * </p>
 * <p>
 * Description: XHIBITConstant contains 'program constants' to be used by all
 * programmers throughout the application code, where and as appropriate
 * according to development guidelines.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0 $Log: XHIBITConstant.java,v $
 * @version 1.0 Revision 1.6  2006/06/05 12:30:39  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.5 2006/05/31 14:24:31 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standardise code formatting
 * @version 1.0 Revision 1.4 2005/02/11 16:40:18 sz0t7n Organise imports
 * 
 * Revision 1.3 2003/11/07 16:06:04 szn20z Build the judge's name from a
 * RefJudgeBasicValue
 * 
 * Revision 1.2 2003/10/14 12:19:38 sz0t7n refactor xhibit constant into small
 * classes
 * 
 * Revision 1.1 2003/10/08 16:49:23 sz0t7n splitting out the
 * thickclient_framework
 * 
 * Revision 1.50 2003/10/07 11:09:13 sz0t7n problem in class cast for optimistic
 * lock checking
 * 
 * Revision 1.49 2003/10/01 15:51:11 sz0t7n modifications for optimistic lock
 * handling
 * 
 * Revision 1.48 2003/08/27 13:51:05 sz0t7n no message
 * 
 * Revision 1.47 2003/08/15 15:01:32 szn20z Added method to determine if the
 * property is available in the specified resource file
 * 
 * Revision 1.46 2003/07/21 12:37:22 rzgbyh Error message fix.
 * 
 * Revision 1.45 2003/07/18 16:42:54 sz0t7n added comment
 * 
 * Revision 1.44 2003/07/18 15:47:46 sz0t7n If the error origintated from an
 * action, then no message was displayed!
 * 
 * Revision 1.43 2003/07/16 18:20:16 szfnvt The client has been changed so the
 * Yes/No option on the dialog is replaced by an OK. This is a genreric change
 * which affects the entire GUI. Whenever a CSRecoverableException is now thrown
 * from the mid-tier the GUI will display the message with only and OK button on
 * the dialog. This change has been triggered by Bug 53557.
 * 
 * Revision 1.42 2003/07/16 12:52:19 sz0t7n modified to display error message
 * from mid tier only once (if they have been wrapped multiple times) Note: this
 * is not tested as I could not produce any errors !
 * 
 * Revision 1.41 2003/07/08 16:27:13 sz0t7n optimise imports
 * 
 * Revision 1.40 2003/06/03 16:59:00 szn20z Statics for upper and lower limits
 * for defendant age
 * 
 * Revision 1.39 2003/05/15 16:27:52 sz0t7n to fix an earlier problem
 * 
 * Revision 1.38 2003/05/12 13:57:09 nz5zpz testTeamReport switch via properties
 * file
 * 
 * Revision 1.37 2003/05/12 13:49:07 nz5zpz docu
 * 
 * Revision 1.36 2003/05/11 15:41:47 nz5zpz Changes to debug info (turn on/off
 * using config file), including the TestTeamReport exception popup window.
 * Logon may now three times fail.
 * 
 * Revision 1.35 2003/04/30 10:38:46 nz5zpz added method for XML Escaping of
 * strings
 * 
 * Revision 1.34 2003/04/29 09:23:13 nz5zpz more selective debug info
 * 
 * Revision 1.33 2003/04/25 13:40:44 nz5zpz small change to locale aware
 * resource bundle loading and debugging
 * 
 */

public class XHIBITConstant {
    public static final String resourceNotFoundStringStart = ResourceBundleHelper.resourceNotFoundStringStart;

    public static final String propertyNotFoundStringStart = PropertyHelper.propertyNotFoundStringStart;

    private static final Logger log = CSServices.getLogger(XHIBITConstant.class);

    // the next three flags are used to determine the level of internal
    // debugging.
    // one may change the values (set all to true) by putting
    // 'XHIBITConstant.internalDebug = AnyValueReally' in
    // 'GUI.XhibitClientProject.properties'
    private static boolean internalDebug = false;
    // public static int Defendant_Age_Lower_Limit = 12;
    // public static int Defendant_Age_Upper_Limit = 70;

    // static init of the XHIBIT Constants - loading of Locale-dependent
    // ResourceBundle and non-Locale-dependent Properties.
    static {
        try {
            log.debug("Started static initialisation of XHIBITConstant - Created a Logger instance.");

            String internalDebugIndicator = PropertyHelper.getProperty(XhibitProperties.XhibitClientProject,
                    "XHIBITConstant.internalDebug");
            if ((internalDebugIndicator != null)
                    && (internalDebugIndicator.indexOf(PropertyHelper.propertyNotFoundStringStart) != -1)) {
                internalDebug = false;
                CaseTypeHelper.internalDebugCaseTypes = false;
            } else {
                internalDebug = true;
                CaseTypeHelper.internalDebugCaseTypes = true;
                log.debug("XHIBITConstant.internalDebug property key found in " + XhibitProperties.XhibitClientProject
                        + " property file - internal XHIBITConstant debug is turned on!");
            }
        } catch (Exception e) {
            log.error("Exception during Static initialisation of XHIBITConstant");
            log.error(e);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Unable to initialise XHIBIT 2.\nPossible cause: missing configuration and library files during deployment.\n",
                            "Fatal Exception", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } finally {
            log.debug("Static initialisation finished.");
        }
    }

    public static boolean isInternalDebug(String className) {
        String internalDebugIndicator = PropertyHelper.getProperty(XhibitProperties.XhibitClientProject, className
                + ".internalDebug");

        if ((internalDebugIndicator != null)
                && (internalDebugIndicator.indexOf(PropertyHelper.propertyNotFoundStringStart) != -1)) {
            if (internalDebug)
                XHIBITConstant.debug("XHIBITConstant.internalDebug property key not found in "
                        + XhibitProperties.XhibitClientProject
                        + " property file - internal XHIBITConstant debug is turned off.");

            return false;
        } else {
            if (internalDebug)
                XHIBITConstant.debug("XHIBITConstant.internalDebug property key found in "
                        + XhibitProperties.XhibitClientProject
                        + " property file - internal XHIBITConstant debug is turned on!");
            return true;
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    // /////////////// //////////////////////////////
    // /////////////// GUI FORMATTING CONSTANTS
    // //////////////////////////////
    // /////////////// ///////////////////////////////
    // //////////////////////////////////////////////////////////////////////////
    public static Insets nonContainerInsets = new Insets(4, 4, 4, 4);

    public static Insets containerInsets = new Insets(0, 0, 0, 0);

    public static Insets rootContainerInsets = new Insets(15, 15, 15, 15);

    public static Insets errorLabelInsets = new Insets(-3, 4, -3, 4);

    public static Dimension defaultSmallListDimension = new Dimension(275, 75);

    public static FlowLayout containerFlowLayout = getXHIBITFlowLayout(2);

    public static String imageRoot = "images/";

    public static final int TABLE_ROW_HEIGHT = 22;

    public static FlowLayout getXHIBITFlowLayout(int hgap) {
        FlowLayout x = new FlowLayout();
        x.setHgap(hgap);
        return x;
    }

    public static GridBagConstraints getDefaultGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbc.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = XHIBITConstant.nonContainerInsets;
        return gbc;
    }

    public static JPanel getSpacer() {
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(35, 35));
        return spacer;
    }

    /**
     * Returns a reference to the default font used in text fields
     * 
     * @return
     */
    public static Font getCurrentFont() {
        UIDefaults defaults = UIManager.getDefaults();
        Font f = defaults.getFont("TextField.font");
        return f;
    }

    /**
     * This is the default height for text fields.
     * 
     * @return
     */
    public static int getLineHeight() {
        return ((getCurrentFont().getSize()) + 10);
    }

    // //////////////////////////////////////////////////////////////////////////
    // ///////////// /////////////
    // //////////// GUI LOGGING / ERRORHANDLING CONTROL //////////////
    // /////////// ///////////////
    // //////////////////////////////////////////////////////////////////////////

    /**
     * These methods should not be used. Each class should create its own logger
     */
    // Logging of 'Info(rmation)' is done by using the Framework-provided
    // JLog configurable mechanism.
    public static void info(String msg) {
        info((Object) msg, null);
    }

    public static void info(Throwable t) {
        info((Object) null, t);
    }

    public static void info(Object o, Throwable t) {
        if (log.isInfoEnabled())
            log.info(o, t);
    }

    // Logging of 'Debug' information is done by using the
    // Framework-provided JLog configurable mechanism.
    public static void debug(String msg) {
        debug((Object) msg, null);
    }

    public static void debug(Object o) {
        debug(o, null);
    }

    public static void debug(Object o, Throwable t) {
        if (log.isDebugEnabled())
            log.debug(o, t);
    }

    // Logging of 'Errors' information&exceptions is done by using the
    // Framework-provided JLog configurable mechanism.
    public static void error(String msg) {
        error((Object) msg, null);
    }

    public static void error(Object o) {
        error(o, null);
    }

    public static void error(Object o, Throwable t) {
        log.error(o, t);
    }

    /**
     * Error Handling moved to helpers/PropertyHelper These methods are
     * deprecated They should be properly deprecated and deleted when the
     * project has time to edit all connected classes.
     */
    public static String getProperty(String propertiesName, String propertyKey) {
        return PropertyHelper.getProperty(propertiesName, propertyKey);
    }

    /**
     * Error Handling moved to helpers/PropertyHelper These methods are
     * deprecated They should be properly deprecated and deleted when the
     * project has time to edit all connected classes.
     */
    public static String getResource(ResourceBundle rsc, String resourceKey) {
        return ResourceBundleHelper.getResource(rsc, resourceKey);
    }

    public static String getResource(String resourcesName, String resourceKey) {
        return ResourceBundleHelper.getResource(resourcesName, resourceKey);
    }

    public static ResourceBundle getResourceBundle(String baseName) {
        return ResourceBundleHelper.getResourceBundle(baseName);
    }

    public static boolean isResourceAvailable(String resourcesName, String resourceKey) {
        return ResourceBundleHelper.isResourceAvailable(resourcesName, resourceKey);
    }
    
    /**
     * Error Handling moved to XHIBITErrorHandler These methods are deprecated
     * They should be properly deprecated and deleted when the project has time
     * to edit all connected classes.
     */
    public static void handleError(Exception t, XAction xaction, ActionEvent ae) {
        XHIBITErrorHandler.handleError(t, xaction, ae);
    }

    public static void handleError(Exception t) {
        XHIBITErrorHandler.handleError(t);
    }

    public static void handleError(Exception t, Class c) {
        XHIBITErrorHandler.handleError(t, c);
    }

    public static void handleError(Exception t, Class c, String msg) {
        XHIBITErrorHandler.handleError(t, c, msg);
    }

    public static boolean isOptimisticLockError(Throwable ex) {
        return XHIBITErrorHandler.isOptimisticLockError(ex);
    }

    /**
     * Case Type methods moved to CaseTypeHelper These methods are deprecated
     * They should be properly deprecated and deleted when the project has time
     * to edit all connected classes.
     */
    public static boolean isNormal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isNormal_CaseType(shv);
    }

    public static boolean isCriminalAppeal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isCriminalAppeal_CaseType(shv);
    }

    public static boolean isMiscelleanousAppeal_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isMiscelleanousAppeal_CaseType(shv);
    }

    public static boolean isSentence_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isSentence_CaseType(shv);
    }

    public static boolean isTrial_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isTrial_CaseType(shv);
    }

    public static boolean isBail_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isBail_CaseType(shv);
    }

    public static boolean isUndefined_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isUndefined_CaseType(shv);
    }

    public static boolean isCombinedCourt_CaseType(ScheduledHearingValue shv) throws UnknownCaseTypeException {
        return CaseTypeHelper.isUndefined_CaseType(shv);
    }

    /**
     * This will take the three pre-defined entities in XML 1.0 (used
     * specifically in XML elements) and convert their character representation
     * to the appropriate entity reference, suitable for XML element.
     * 
     * @param st
     *            the string to be escaped
     * @return the escaped string
     */
    public static String escapeElementEntities(String st) {
        if (internalDebug)
            debug("Escaping '" + st + "'.");
        StringBuffer buff = new StringBuffer();
        char[] block = st.toCharArray();
        String stEntity = null;
        int i, last;
        for (i = 0, last = 0; i < block.length; i++) {
            switch (block[i]) {
            case '<':
                stEntity = "&lt;";
                break;
            case '>':
                stEntity = "&gt;";
                break;
            case '&':
                stEntity = "&amp;";
                break;
            default:
                /* no-op */
                ;
            }
            if (stEntity != null) {
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }
        if (last < block.length)
            buff.append(block, last, i - last);

        if (internalDebug)
            debug("Escaped '" + st + "' to '" + buff.toString() + "'.");
        return buff.toString();
    }

    /**
     * Builds the judge's name depending pre-defined rules.
     * 
     * @param RefJudgeBasicValue -
     *            Details of the judge
     * @return String - the name to be displayed to the user
     */
    public static String buildJudgeName(RefJudgeBasicValue refJudgeBasicValue) {
        String judgeName = null;

        if (refJudgeBasicValue != null) {
            if (refJudgeBasicValue.getFullListTitle1() != null) {
                judgeName = refJudgeBasicValue.getFullListTitle1();
            } else if (refJudgeBasicValue.getSurname() != null) {
                judgeName = refJudgeBasicValue.getSurname();
            }
        }

        return (judgeName == null ? "" : judgeName);
    }
    
    public static Date getTomorrowsDate() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR,1);
		
		return cal.getTime();
	} 
}