package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.Applications;
import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_court_default.XhbTerminalCourtDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;
import uk.gov.courtservice.xhibit.business.services.userterminal.TerminalNotFoundException;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.version.VersionControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.message_of_the_day.MessageOfTheDayControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.PollingValue;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;
import uk.gov.courtservice.xhibit.client.actions.common.ExitAction;
import uk.gov.courtservice.xhibit.client.actions.common.NewAction;
import uk.gov.courtservice.xhibit.client.actions.menu.RoamingTerminalHotSwitchAction;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingServicesFactory;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Run this to login and start a Xhibit Session
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0 <p/> 01.05.2003 C Davies Moved some code around in
 *          exitXhibitApplication so that if the exit is aborted then court log
 *          etc. still remain on screen
 * @editor Frederik Vandendriessche
 */

public class Xhibit extends Applications.Application implements XhibitInterface {
    private static final String JAAS_LOCATION = "java.security.auth.login.config";
    private static int STOP_POLLING=-1;
    private static int DEFAULT_POLL_INTERVAL=300000;
    private static int DEFAULT_CHECK_POLL_INTERVAL=600000;

    private static final Logger log = CSServices.getLogger(Xhibit.class);

    private static final boolean packFrame = false;
    
    private Thread timedLogoutMessageChecker;
    

    // Message Receiver for application. This need to be open for the whole
    // time
    // that Xhibit is running, and then closed once Xhibit, not Xac, is
    // closed
    private XAction messageReceiverAction;
    
    private PollingValue pollingValue;

    private int pollingInterval;
    
    private int pollingCheckInterval;
    
    private long pollingStartTime;
    
    private long checkIntervalStartTime;
    
    // attribute to establish whether polling should stop or continue
    private int stopPolling;
    
    public Xhibit() {
        super("XHIBIT 2 Client Application");
        XhibitSplash xs = null;
        pollingStartTime = Calendar.getInstance().getTimeInMillis();
        checkIntervalStartTime = Calendar.getInstance().getTimeInMillis();
        try {
            log.debug("Created a Logger instance.");
            log.debug("This client was checked out from CVS as $Name:  $.");

            setJaasLocation();

            try {
            	stopPolling=-1;
            	pollingValue = getPollingValue();
            	try {
            	     pollingInterval = (new Integer(pollingValue.getPollingInterval()).intValue());
                     pollingCheckInterval = (new Integer(pollingValue.getCheckPollingInterval()).intValue());
            	} catch (Exception exp) {
            		pollingInterval=DEFAULT_POLL_INTERVAL;
            		pollingCheckInterval=DEFAULT_CHECK_POLL_INTERVAL;
            	}
                boolean loginAllowed = checkForLockoutMessage();
                if (!loginAllowed) {
                    log.debug("Login has been disabled on the server");
                    System.exit(-5);
                }
            } catch (Exception ex) {
                XHIBITErrorHandler.handleError(ex);
                System.exit(-4);
            }
            
            xs = new XhibitSplash();
            xs.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            xs.setStatus(getResource("splashStatusStart"));

            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                log.error("XHIBIT: Thread.sleep(500) threw Exception", ex);
            }

            try {
                xs.setStatus(getResource("splashStatusCheckVersion"));
                checkVersion();
            } catch (Exception ex) {
                XHIBITErrorHandler.handleError(ex);
                // -3 means client version incorrect
                System.exit(-3);
            }
            
            

            xs.setStatus(getResource("splashStatusLogin"));

            CSUserSession csus = CSServices.getCSUserSession(new XhibitCallbackHandler());
            XhibitSingleton.getInstance().setUserSession(csus);

            log.debug("CSUserSession.getServer()=" + csus.getServer());

            boolean loginSuccessful = false;
            boolean terminalRegistered = true;
            while (XHIBITErrorHandler.allowLoginAttempt() && !loginSuccessful &&terminalRegistered) {
                try {
                    // expected to throw
                    // javax.security.auth.login.LoginException
                    // in cause of any difficulty to log on.
                    csus.login();
                    loginSuccessful = true;
                } catch (Exception e) {
                    loginSuccessful = false;
                    if (e.getClass().equals(TerminalNotFoundException.class)) {
                        terminalRegistered = false;
                    }
                    // dont change this -- this is what triggers the
                    // xhibitconstant to remember the no of attempts
                    XHIBITErrorHandler.handleError(e);
                }
            }

            // This required as login can be successful even if the user is
            // not
            // registered in the XHB_TERMINAL table.
            if (loginSuccessful) {
                String loginSuccessMessage = getResource("splashStatusLoginSuccessful");
                xs.setStatus(loginSuccessMessage);

                loginSuccessMessage += ": ";
                if (XhibitSingleton.getInstance().isTerminalRoaming()) {
                    xs.setStatus(loginSuccessMessage + getResource("splashStatusInitializeRoaming"));
                    RoamingTerminalSelectDialog dialog = new RoamingTerminalSelectDialog();
                    dialog.setVisible(true);
                    if (dialog.isCancelClicked())
                        System.exit(0);
                    // Set up roaming action singletons to have model
                    // references back to this class */
                    RoamingTerminalHotSwitchAction.getInstance().setController(this);
                }

                // Set up some singletons to have model references back to this
                // class to control new and exit */
                NewAction.getInstance().setController(this);
                ExitAction.getInstance().setController(this);

                xs.setStatus(loginSuccessMessage + getResource("splashStatusInitializeMessenger"));
                startUpMessaging();

                xs.setStatus(loginSuccessMessage + getResource("splashStatusCreateXhibitWindow"));
                newXhibitApplication();
                log.debug("Created a XhibitApplicationController instance.");

                xs.setStatus(loginSuccessMessage + getResource("splashStatusXhibitSuccess"));
            } else {
                // -2 means not registered in the XHB_TERMINAL table
                System.exit(-2);
            }
        } catch (Exception e) {
            try {
                String key = "gui.user.loginFailed";
                String msg = "Exception e in Xhibit() constructor.";
                // e.printStackTrace();
                log.error(msg);
                log.error(e);
                CSRecoverableException csre = new CSRecoverableException(key, msg, e);
                XHIBITErrorHandler.handleError(csre, this.getClass(), msg);
            } catch (Exception ee) {
                e.printStackTrace(System.err);
            }
            // XHIBITConstant now decides how to deal with the exception.
            // [rl] But still need to exit the app or it hangs around in
            // memory!
            finally {
                System.exit(-1);
            }
        } finally {
            if (xs != null) {
                xs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                xs.dispose();
            }
        }
    }
    
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, key);
    }

    private boolean checkForLogoutMessage(Frame frame) {
        //log.debug("Checking for a logout message.");
        String logoutMessage = null;
        log.debug("Checking For Logout Message");
        try {
            logoutMessage =
                MessageOfTheDayControllerBeanBusinessDelegate.DelegateFactory.getInstance().getLogoutMessage(XhibitSingleton.getInstance().getUserSession().getUserLoginId());
        } catch (Exception e) {
            log.error("Exception from getLogoutMessage: " + e);
            try {
                log.debug("Sleep for ten seconds - before retry");
                final int tenSeconds = 10 * 1000;
                Thread.sleep(tenSeconds); // sleep takes milliseconds
            } catch (Exception ex) {
                log.error("Exception from Thread.sleep(): " + ex);
            }
        }
        
        if (logoutMessage != null && !logoutMessage.equals("")) {
            XMessageBox.inform(
                    frame,
                    getResource("LogoutMessage.Dialog.Title"),
                    XMessageBox.ICONWARNING, 
                    logoutMessage);
            // logout message displayed
            return true;
        }
        
        return false;
    }
    
    private boolean checkForLockoutMessage() {
        String messageOfTheDay =
            MessageOfTheDayControllerBeanBusinessDelegate.DelegateFactory.getInstance().getMessageOfTheDay();
        
        if (messageOfTheDay != null && !messageOfTheDay.equals("")) {
            XMessageBox.inform(
                    getResource("MessageOfTheDay.Dialog.Title"),
                    XMessageBox.ICONINFORMATION, 
                    messageOfTheDay);
        }
        
        String lockoutMessage = 
            MessageOfTheDayControllerBeanBusinessDelegate.DelegateFactory.getInstance().getLockoutMessage();
        
        if (lockoutMessage != null && !lockoutMessage.equals("")) {
            XMessageBox.inform(
                    getResource("LockoutMessage.Dialog.Title"),
                    XMessageBox.ICONERROR, 
                    lockoutMessage);
            // Login disallowed
            return false;
        }
        // Login allowed
        return true;
    }
    
    /*
     * Method to establish whether it's time to retrieve the polling times from
     * the mid tier.
     * 
     *  @param interval the value in milliseconds which is used to determine whether
     *                  the client should call the server.
     */
    private boolean timeElapsed(long startTime, long interval) {
    	boolean elapsed=false;
    	long nowTime = Calendar.getInstance().getTimeInMillis();
    	long checkTime = startTime + interval;
    	if (nowTime>=checkTime) {
    		elapsed = true;
    	}
    	return elapsed;
    }
    
    private void checkVersion() {
        String clientVersion = PropertyHelper.getProperties(XhibitProperties.XhibitClientProject).getProperty(
                "full_version");
        boolean versionMatch = VersionControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                .checkVersionCompatibility(clientVersion, ComponentValue.THICK_CLIENT);
        if (!versionMatch) {
            throw new CSUnrecoverableException(new Message("version.component.wrongversion"), "Client version of "
                    + clientVersion + " does not match server version");
        }
    }
   

    /*
     * This retrieves the polling values from the mid tier
     * 
     * @return PollingValue Object with attributes containing the values
     */
    private PollingValue getPollingValue() {
    	PollingValue value = MessageOfTheDayControllerBeanBusinessDelegate.DelegateFactory.getInstance().getPollingInterval();
        return value;
    }
    
    /**
     * Get the location of the xhibit_jaas file from the class path and set into
     * the system property.
     */
    private void setJaasLocation() {
        String jaas = getClass().getResource("/config/xhibit_jaas.config").toString();
        System.setProperty(JAAS_LOCATION, jaas);
        log.debug("setProperty('java.security.auth.login.config', '" + jaas + "')");
    }

    private class CheckForLogoutMessage implements Runnable {
        
        private Frame frame;
        
        CheckForLogoutMessage(Frame frame) {
            this.frame = frame;
        }
     
        public void run() {
            int milliseconds = Integer.parseInt(getResource("logoutMessageCheckInterval"));
            final int seconds = Integer.parseInt(getResource("logoutMessageReminderInterval"));
            boolean logoutMessageFound=false;
            log.debug("logout message check interval (milliseconds): " + milliseconds);
            log.debug("logout message reminder interval (seconds): " + seconds);
            
            while (true) {
                try {
                	if (pollingInterval != stopPolling) {
                	     if (timeElapsed(pollingStartTime, pollingInterval)) {
                	    	 pollingStartTime = Calendar.getInstance().getTimeInMillis();
                             logoutMessageFound = checkForLogoutMessage(frame);
                	     }
                         if (logoutMessageFound) {
                        // If a logout message has been displayed we need to give
                        // the users a while to end what they are doing before 
                        // displaying the next logout reminder.
                           Thread.sleep(seconds * 1000);
                          } else {
                          Thread.sleep(milliseconds);
                       }
                	}
                	else
                		Thread.sleep(milliseconds);
                	if (timeElapsed(checkIntervalStartTime, pollingCheckInterval)) {
                	  checkIntervalStartTime = Calendar.getInstance().getTimeInMillis();
                	  try {
                	   	pollingValue = getPollingValue();
                    	pollingInterval = (new Integer(pollingValue.getPollingInterval()).intValue());
                        pollingCheckInterval = (new Integer(pollingValue.getCheckPollingInterval()).intValue());
                        if (pollingInterval>0)
                          milliseconds = pollingInterval;
                	  } catch (Exception exp){
                		  pollingInterval = DEFAULT_POLL_INTERVAL;
                		  pollingCheckInterval = DEFAULT_CHECK_POLL_INTERVAL;
                	  }
                	}
                	log.debug("Polling interval "+pollingInterval);
                	log.debug("Polling check interval "+pollingCheckInterval);
                } catch (Exception ex) {
                    log.error("XHIBIT: Thread.sleep() threw Exception", ex);
                }
            }
        }
    }
    
    public XhibitApplicationController newXhibitApplication() {
        XhibitApplicationController xac = startApplication();
        try {
            Properties p = getClientPropertyFile();
            int width = 0;
            int height = 0;
            // Have to set the starting values to -10 as a maximised window
            // has a negative screen location
            int top = -10;
            int left = -10;
            try {
                width = Integer.parseInt(p.getProperty("window.width"));
                height = Integer.parseInt(p.getProperty("window.height"));
                top = Integer.parseInt(p.getProperty("window.top"));
                left = Integer.parseInt(p.getProperty("window.left"));
            } catch (NumberFormatException ex) {
                log.error("Problem parsing one of the size elements of the XHIBIT window", ex);
            }

            if (width > 0 && height > 0)
                xac.setSize(width, height);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (top > -10 && left > -10 && top < screenSize.height - 50 /*
                                                                         * -50
                                                                         * to
                                                                         * estimate
                                                                         * size
                                                                         * of
                                                                         * toolbar
                                                                         */
                    && left < screenSize.width) {
                xac.setLocation(left, top);
            } else {
                centerFrame(xac);
            }
        } catch (CSRecoverableException ex) {
            log.error("Error occurred reading window size properties", ex);
        }
        XhibitSingleton.getInstance().registerApp(xac);
        XhibitSingleton.addXacToGroup(xac);
        xac.setVisible(true);
        timedLogoutMessageChecker = new Thread(new CheckForLogoutMessage(xac));
        timedLogoutMessageChecker.start();
        return xac;
    }

    public void exitXhibitApplication(Frame callingFrame) throws CSRecoverableException {
        if (callingFrame == null) {
            callingFrame = (XhibitApplicationController) XhibitSingleton.getInstance().getApplications().get(0);
        }
        // do you want to exit
        int rc = JOptionPane.showConfirmDialog(
                callingFrame, 
                getResource("ExitConfirm"), 
                getResource("ExitConfirmTitle"), 
                JOptionPane.YES_NO_OPTION);
        if (rc == JOptionPane.YES_OPTION) {
            // Save frame size for next start up
            saveWindowSizeAndPosition(callingFrame);

            shutDownMessaging();
            closeXhibitInstances();
            logoutXhibit();

            System.exit(0);
        } else {
            throw new UserCancelException();
        }
    }

    public void roamingTerminalHotSwitch(Frame callingFrame) throws CSRecoverableException {
        XhibitSingleton xs = XhibitSingleton.getInstance();
        if (callingFrame == null) {
            callingFrame = (XhibitApplicationController) xs.getApplications().get(0);
        }
        int rc = JOptionPane.showConfirmDialog(
                callingFrame, 
                getResource("RaomingChangeConfirm"), 
                getResource("RaomingChangeConfirmTitle"), 
                JOptionPane.YES_NO_OPTION);
        if (rc == JOptionPane.YES_OPTION) {
            // Close down existing windows and messaging
            closeXhibitInstances();
            // This will leave one instance of XAC hanging around (normally
            // only closed by Exit.
            // Remove this one as well.
            XhibitApplicationController lastXAC = (XhibitApplicationController) xs.getApplications().get(0);
            XhibitSingleton.removeXacFromGroup(lastXAC);
            lastXAC.dispose();
            xs.deregisterApp(lastXAC);
            xs.clearCourtDataCache();

            shutDownMessaging();
            // Get new terminal location
            RoamingTerminalSelectDialog dialog = new RoamingTerminalSelectDialog();
            dialog.setVisible(true);
            if (dialog.isCancelClicked()) {
                logoutXhibit();
                System.exit(0);
            }
            // Start new XHIBIT window
            try {
                startUpMessaging();
                newXhibitApplication();
            } catch (Exception ex) {
                XHIBITErrorHandler.handleError(new CSUnrecoverableException());
                System.exit(-3);
            }
        }
    }

    private void closeXhibitInstances() throws CSRecoverableException {
        int totalApps = XhibitSingleton.getInstance().getApplications().size();
        for (int i = 0; i < totalApps; i++) {
            closeXhibitApplication((XhibitApplicationController) XhibitSingleton.getInstance().getApplications().get(0));
        }
    }

    private void logoutXhibit() {
        try {
            XhibitSingleton.getInstance().exitApplication();
        } catch (LoginException ex) {
            // ignore
            log.fatal(ex);
        }
    }

    private void startUpMessaging() {
        // Neil Entwistle - Creating a MESSAGE receiver BEGIN
        // Create a new message Receiver - this will remain active as long
        // as Xhibit is open. The receiver listens for messages on a JMS queue
        try {
            log.debug("Creating a message reciever");
            Class newAction = Class.forName("uk.gov.courtservice.xhibit.client.im.actions.IMReceiverAction");
            messageReceiverAction = (XAction) newAction.newInstance();
            log.debug("Created a message reciever");
        } catch (ClassNotFoundException ex) {
            // Ignore - but no messages received
            log.error("Class Not Found Exception: uk.gov.courtservice.xhibit.client.im.actions.IMReceiverAction");
        } catch (Exception ex) {
            // Message receipt NOT AVAILABLE
            log.error("Name Not Found Exception: uk.gov.courtservice.xhibit.client.im.actions.IMReceiverAction: Check Court Name"
                            + ex.getMessage());
        }
        // Neil Entwistle - Creating a MESSAGE receiver END
    }

    private void shutDownMessaging() throws CSRecoverableException {
        // Setting the IMReceiverAction to enabled = false, will close all
        // JMS connections and clean up the subscribers
        if (messageReceiverAction != null) {
            messageReceiverAction.setEnabled(false);
            InstantMessagingServicesFactory.getInstance().cleanupIMS();
        }
    }

    private void saveWindowSizeAndPosition(Frame callingFrame) throws CSRecoverableException {
        Properties p = getClientPropertyFile();
        p.setProperty("window.width", String.valueOf(callingFrame.getSize().width));
        p.setProperty("window.height", String.valueOf(callingFrame.getSize().height));
        p.setProperty("window.top", String.valueOf((int) callingFrame.getLocation().getY()));
        p.setProperty("window.left", String.valueOf((int) callingFrame.getLocation().getX()));
        storeClientPropertyFile(p);
    }

    public static synchronized Properties getClientPropertyFile() throws CSRecoverableException {
        Properties p = PropertyHelper.getUserHomeProperties(XhibitProperties.XhibitClientProject + ".properties");
        if (p == null) {
            PropertyHelper.createUserHomeProperties(XhibitProperties.XhibitClientProject + ".properties",
                    "Generic User Properties");
            p = PropertyHelper.getUserHomeProperties(XhibitProperties.XhibitClientProject + ".properties");
        }
        return p;
    }

    public static synchronized void storeClientPropertyFile(Properties userProperties) throws CSRecoverableException {
        PropertyHelper.storeUserHomeProperties(userProperties, XhibitProperties.XhibitClientProject + ".properties");
    }

    public void closeXhibitApplication(XhibitApplicationController xac) throws CSRecoverableException {
        xac.close();
        XhibitSingleton xs = XhibitSingleton.getInstance();

        // If window is closing for application
        if (xs.closeWindow()) {
            // remove from Linked group
            XhibitSingleton.removeXacFromGroup(xac);
            xac.dispose();
            xs.deregisterApp(xac);
            xs.enableCloseAction();
        }
    }

    private XhibitApplicationController startApplication() {
        XhibitApplicationController xac = new XhibitApplicationControllerImpl();
        xac.setParentController(this);

        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info, e.g. from their
        // layout
        if (packFrame) {
            xac.pack();
        } else {
            xac.validate();
        }
        return xac;
    }

    private void centerFrame(XhibitApplicationController xac) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = xac.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        xac.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    // /////////////////////////////////////////////////////////////////////////
    // /////////// ///////////////
    // ////////// START THE APPLICATION INVOKING THE MAIN() ////////////////
    // ///////// /////////////////
    // /////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            @SuppressWarnings("unused")
            Xhibit xhibit1 = new Xhibit();
        } catch (Exception e) {
            try {
                log.error("XHIBIT: Exception in main(String[] args)", e);
                XHIBITErrorHandler.handleError(e);
            } catch (Exception ee) {
                e.printStackTrace(System.err);
            }
        }
    }
}