package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.results.CrestSuccessRefreshResyncException;
import uk.gov.courtservice.xhibit.business.services.results.CrestUnknownRefreshResyncException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.common.CopyTextComponentToClipboardAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Default Error Handler
 * </p>
 * <p>
 * Description: Extracted from XHIBIT Constant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XHIBITErrorHandler.java,v 1.15 2009/06/02 10:33:59 powellja Exp $
 */
public class XHIBITErrorHandler {
    private static final int maxFailedLoginTrials = 3;

    private static int failedLoginTrials = 0;

    private static boolean internalDebug = false;

    private static boolean testTeamReport = false;

    private static Logger log = CSServices.getLogger(XHIBITErrorHandler.class);

    // titles for the error display message box
    private static String alertTitle;

    private static String confirmTitle;

    private XHIBITErrorHandler() {
    }

    // HANDLING of Exceptions
    private static boolean isExceptionOfKind(Exception e, Class c) {
        boolean isExceptionOfKind = false;
        if (e.getClass() == c) {
            if (internalDebug)
                log.debug(". . . .Exception e (" + e + ") is assignable from Class (" + c + ").");
            isExceptionOfKind = true;
        }
        return isExceptionOfKind;
    }

    public static void handleError(Exception t, XAction xaction, ActionEvent ae) {
        handleError(t, XHIBITConstant.class,
                "Exception reported by XHIBITConstant.handleError(Exception t, XAction xaction, ActionEvent ae)",
                xaction, ae);
    }

    public static void handleError(Exception t) {
        handleError(t, XHIBITConstant.class, "Exception reported by XHIBITConstant.handleError(Exception t)");
    }

    public static void handleError(Exception t, Class c) {
        handleError(t, c, "Exception reported by XHIBITConstant.handleError(Exception t, Class c)");
    }

    public static void handleError(Exception t, Class c, String msg) {
        handleError(t, c, msg, null, null);
    }

    private static void processLoginError(Exception t, Message msg) {
        // only if msg != null try to find a new one,
        // else just display the message and o the error counting.
        Message message;
        failedLoginTrials++;
        if (failedLoginTrials < maxFailedLoginTrials) {
            message = msg;
        } else {
            message = new Message("gui.user.loginFailedFinal");
        }
        handleError(new CSRecoverableException(message.getKey(), message.getMessage(), t));
        if (failedLoginTrials == maxFailedLoginTrials)
            System.exit(1);
    }

    public static Throwable getCause(Throwable e) {
        if (e instanceof CSException) {
            return ((CSException) e).getCause();
        } else if (e instanceof java.rmi.RemoteException) {
            return ((java.rmi.RemoteException) e).detail;
        } else if (e instanceof javax.ejb.EJBException) {
            return ((javax.ejb.EJBException) e).getCausedByException();
        } else {
            return null;
        }
    }

    public static boolean isOptimisticLockError(Throwable ex) {
        boolean isLock = false;
        if (ex instanceof OptimisticLockException)
            isLock = true;
        else {
            while (getCause(ex) != null) {
                ex = (Throwable) getCause(ex);
                if (ex instanceof OptimisticLockException) {
                    isLock = true;
                    break;
                }
            }
        }
        return isLock;
    }

    private static boolean isAccessException(Throwable ex) {
        boolean isAE = false;
        if (ex instanceof AccessException) {
            isAE = true;
        } else {
            while (ex.getCause() != null) {
                ex = ex.getCause();
                if (ex instanceof AccessException) {
                    isAE = true;
                    break;
                }
            }
        }
        return isAE;
    }

    /*
     * This handleError method is used by all others (whom forward their
     * invokation to this one)
     * 
     * This handleError method will produce an alertbox for CSRecoverable and
     * CSUnrecoverable exceptions. Where the exception is CSRecoverableException
     * and an Action and ActionEvent are passed in, the alertbox will ask the
     * users whether or not to retry the action. In all other cases, the alert
     * box is for confirmation (ok only)
     * 
     * All other exceptions than CSRecoverable and CSUnRecoverable are wrapped
     * in an CS(Un)Recoverable Exception and handleError() is exctured again.
     */

    public static void handleError(Exception t, Class c, String msg, XAction xaction, ActionEvent ae) {
        log.error(msg, t);
        int i = 1;
        for (Throwable cause = getCause(t); cause != null; cause = getCause(cause)) {
            log.error("    Cause " + i++ + ":", cause);
        }

        if (internalDebug) {
            log.error("handleError\tException=[" + t + "]");
            log.error("handleError\tException.getClass=[" + t.getClass() + "]");
            log.error("handleError\tClass=[" + c + "]");
            log.error("handleError\tString=[ " + msg + "]");
            log.error("handleError\tXAction=[ " + xaction + "]");
            log.error("handleError\tActionEvent =[ " + ae + "]");
        }

        Window parentWindow = null;
        if (ae != null) {
            if (ae.getSource() instanceof Component) {
                if (XSwingUtilities.getWindowAncestor((Component) ae.getSource()) instanceof Window) {
                    parentWindow = XSwingUtilities.getWindowAncestor((Component) ae.getSource());
                }
            }
        }
        // if (parentWindow == null) parentWindow = findActiveFrame(null);

        Message userMessage;
        Message message;
        /* =============== */
        /* Java Exceptions */
        /* ================ */
        if (isExceptionOfKind(t, javax.security.auth.login.CredentialExpiredException.class)) {

            message = new Message("gui.user.loginFailedCredential");
            processLoginError(t, message);
            // handleError(new
            // CSRecoverableException(message.getKey(),message.getMessage(),t));
        } else if (isExceptionOfKind(t, javax.security.auth.login.AccountExpiredException.class)) {
            message = new Message("gui.user.loginFailedAccount");
            handleError(new CSRecoverableException(message.getKey(), message.getMessage(), t), xaction, ae);
        } else if (isExceptionOfKind(t, javax.security.auth.login.FailedLoginException.class)) {
            message = new Message("gui.user.loginFailedPassword");
            handleError(new CSRecoverableException(message.getKey(), message.getMessage(), t), xaction, ae);
            // processLoginError(t);?
        }

        // any and all CSUserSession login / logout trouble are throw this one:
        else if (isExceptionOfKind(t, javax.security.auth.login.LoginException.class)) {
            if (getStackTrace(t).indexOf("java.net.UnknownHostException") != -1) {
                message = new Message("gui.user.loginFailedServer");
            } else {
                message = new Message("gui.user.loginFailed");
            }
            processLoginError(t, message);
        } else if (isExceptionOfKind(t, java.lang.SecurityException.class)) {
            message = new Message("gui.log.java.lang.SecurityException");
            handleError(new CSRecoverableException(message.getKey(), message.getMessage(), t), xaction, ae);
        } else if (isExceptionOfKind(t, javax.naming.ServiceUnavailableException.class)) {
            message = new Message("gui.user.loginFailedServer");
            processLoginError(t, message);
        }
        /***********************************************************************
         * ==================== xhibit 2 exceptions * ====================
         */
        else if (isExceptionOfKind(t, uk.gov.courtservice.xhibit.client.util.UserCancelException.class)) {
            // Don't need to do anything with User Cancel exception.
            // It is intended to be used to only stop the flow on the
            // screen. I.e when the user hits cancel!
        } else if (isExceptionOfKind(t, CrestSuccessRefreshResyncException.class)
                || isExceptionOfKind(t, CrestUnknownRefreshResyncException.class)) {
            String crestMessage = "";
            if (parentWindow == null && !(parentWindow instanceof XhibitApplicationController)) {
                handleError(new CSUnrecoverableException(new Message("results.saveError.notParentWindow")));
            } else {
                if (t instanceof CrestSuccessRefreshResyncException) {
                    crestMessage = getResource(XhibitBundles.ErrorText, "results.saveError.crestSuccess");
                } else {
                    crestMessage = getResource(XhibitBundles.ErrorText, "results.saveError.crestUnknown");
                }
                JOptionPane.showMessageDialog(parentWindow, crestMessage, getResource(XhibitBundles.ErrorText,
                        "results.saveError.title"), JOptionPane.ERROR_MESSAGE);

                XhibitApplicationController xac = (XhibitApplicationController) parentWindow;
                // set the body panels modify flag to false so it doesn't ask
                // you to save.
                xac.getBodyPanel().setModified(false);
                XAction courtLog = XhibitActions.getAction(xac, XhibitActions.ViewCourtLog);
                ActionEvent courtLogActionEvent = new ActionEvent(xac, 0, "ReloadLog");
                courtLog.actionPerformed(courtLogActionEvent);
            }
        } else if (isExceptionOfKind(t,
                uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException.class)) {
            userMessage = new Message("gui.user.DefendantControllerText");
            message = new Message("gui.log.DefendantControllerText");
            CSRecoverableException newCSue = new CSRecoverableException(userMessage.getKey(), message.getMessage(), t);

            handleError(newCSue, c, msg, xaction, ae);
        } else if (isExceptionOfKind(t, java.lang.reflect.InvocationTargetException.class)) {
            userMessage = new Message("gui.user.InvocationTargetException");
            message = new Message("gui.log.InvocationTargetException");
            CSRecoverableException newCSue = new CSRecoverableException(userMessage.getMessage(), message.getMessage(),
                    t);
            handleError(newCSue, xaction, ae);
        } else if (isExceptionOfKind(t, uk.gov.courtservice.framework.services.CSServicesException.class)) {
            userMessage = new Message("gui.user.ErrorInServicesText");
            message = new Message("gui.log.ErrorInServicesText");
            CSRecoverableException newCSue = new CSRecoverableException(userMessage.getMessage(), message.getMessage(),
                    t);
            handleError(newCSue, xaction, ae);

        } else if (isExceptionOfKind(t, uk.gov.courtservice.framework.exception.CSConfigurationException.class)) {
            userMessage = new Message("gui.user.ErrorInServicesText");
            message = new Message("gui.log.ErrorInServicesText");
            CSRecoverableException newCSue = new CSRecoverableException(userMessage.getMessage(), message.getMessage(),
                    t);
            handleError(newCSue, xaction, ae);
        } else if (isExceptionOfKind(t, CSValidationException.class)) {
            JOptionPane.showMessageDialog(parentWindow, buildValidationMessageText((CSValidationException) t, false),
                    getValidationTitle(), JOptionPane.ERROR_MESSAGE);
        } else if (t instanceof uk.gov.courtservice.framework.exception.CSRecoverableException) {
            // log.error(buildErrorMessageTexts((CSException) t, true));

            if (isOptimisticLockError((CSRecoverableException) t)) {
                // message to tell user to reopen screen and make changes again
                String currMessage = getResource(XhibitBundles.ErrorText, "xhibit.error.optimisticlock");
                JOptionPane.showMessageDialog(parentWindow, currMessage, getFatalErrorTitle(),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                userMessage = new Message("gui.user.ErrorIntroductionText");
                message = new Message("gui.log.ErrorIntroductionText");

                String currMessage = buildErrorMessageTexts((CSException) t, false);
                if (currMessage.trim().length() == 0) {
                    userMessage = new Message("gui.user.ErrorIntroductionText");
                    message = new Message("gui.log.ErrorIntroductionText");
                    CSUnrecoverableException newCSue = new CSUnrecoverableException(userMessage.getMessage(), t);
                    currMessage = buildErrorMessageTexts((CSException) newCSue, false);
                }

                if (ae != null) {
                    if (internalDebug)
                        log.debug("RECOVERABLE Error");

                    showTestTeamReport("CSRecoverableException with action and actionEvent", t);

                    JOptionPane
                            .showMessageDialog(parentWindow, currMessage, getAlertTitle(), JOptionPane.ERROR_MESSAGE);
                } else { // no action, actionEvent couple arguments
                    // available, so no retry offered by default.
                    showTestTeamReport("CSRecoverableException without action and actionEvent", t);
                    JOptionPane
                            .showMessageDialog(parentWindow, currMessage, getAlertTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (t instanceof uk.gov.courtservice.framework.exception.CSUnrecoverableException) {
            if (isOptimisticLockError(t)) {
                // message to tell user to reopen screen and make changes again
                String currMessage = getResource(XhibitBundles.ErrorText, "xhibit.error.optimisticlock");
                JOptionPane.showMessageDialog(parentWindow, currMessage, getFatalErrorTitle(),
                        JOptionPane.ERROR_MESSAGE);
            } else if (isAccessException(t)) {
                // message to tell the user that they do not have the required
                // Weblogic security access
                String currMessage = getResource(XhibitBundles.ErrorText, "xhibit.error.AccessException");
                JOptionPane.showMessageDialog(parentWindow, currMessage, getFatalErrorTitle(),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                if (internalDebug) {
                    log.debug("UNRECOVERABLE Error");
                    log.debug("Caught uk.gov.courtservice.framework.exception.CSUnrecoverableException from GUI");
                }

                // Need to check if the Unrecoverable exception is wrapping an
                // invocation or remote exception.
                t = checkForUnderlyingException((CSUnrecoverableException) t);

                String currMessage = buildErrorMessageTexts((CSException) t, false);
                // If the exception did not contain any Message objects
                // build an empy message.
                if (currMessage.trim().length() == 0) {
                    userMessage = new Message("gui.user.ErrorIntroductionText");
                    message = new Message("gui.log.ErrorIntroductionText");
                    CSUnrecoverableException newCSue = new CSUnrecoverableException(userMessage.getMessage(), t);
                    currMessage = buildErrorMessageTexts((CSException) newCSue, false);
                }
                showTestTeamReport("CSUnrecoverableException", t);
                JOptionPane.showMessageDialog(parentWindow, currMessage, getFatalErrorTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (isExceptionOfKind(t, java.lang.RuntimeException.class)) {
            message = new Message("gui.log.ErrorIntroductionText");
            handleError(new CSRecoverableException(message.getKey(), message.getMessage(), t), xaction, ae);
        } else {
            if (internalDebug)
                log
                        .debug("Dispatching non CSRecoverableEception from GUI: CSServices.getDefaultErrorHandler().handleError(t, c, msg);");
            CSServices.getDefaultErrorHandler().handleError(t, XHIBITErrorHandler.class,
                    "XHIBITConstant reporting on Exception caught");
            showTestTeamReport("java.lang.RuntimeException", t);
            JOptionPane.showMessageDialog(parentWindow,
                    getResource(XhibitBundles.ErrorText, "xhibit.error.unexpected"), getFatalErrorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Exception checkForUnderlyingException(CSUnrecoverableException cse) {
        log.error("checkForUnderlyingException - BEGIN");
        Throwable e = cse.getCause();
        log.error("checkForUnderlyingException e = " + e);

        if (e == null || e instanceof CSException) {
            // if the underlying exception is already a CSException or null
            // then return the original exception
            return cse;
        }

        while (e != null && !(e instanceof CSException)) {
            if (e instanceof InvocationTargetException) {
                e = ((InvocationTargetException) e).getTargetException();
            } else if (e instanceof RemoteException) {
                e = ((RemoteException) e).detail;
            } else {
                // don't have any other exceptions that we have special handling
                // for so set it back to the original exception and continue
                e = cse;
            }
        }
        if (e instanceof Exception) {
            return (Exception) e;
        } else {
            return cse;
        }
    }

    // private static Window findActiveFrame(Window frameIn) {
    // System.out.println("FIND FRAME - Input=" + frameIn);
    // if (frameIn != null) return frameIn;
    // Frame[] frames = JFrame.getFrames();
    // System.out.println("FIND FRAME - frames count=" + frames.length);
    // for (int i = 0; i < frames.length; i++) {
    // Frame frame = frames[i];
    // System.out.println("FIND FRAME - Visible=" + frame.isVisible());
    // if (frame.isVisible()) {
    // return frame;
    // }
    // }
    // return null;
    // }

    private static void showTestTeamReport(String inTitle, Throwable t) {
        if (testTeamReport) {
            String title = "Report For Test purposes only - " + inTitle + " - "
                    + XDateFormat.format(Calendar.getInstance(), XDateFormat.DATETIMEFORMAT);
            String stringMessage = title + "\n";// +buildErrorMessageTexts((CSException)t,true);
            try {
                stringMessage = stringMessage.concat("\nServer:"
                        + XhibitSingleton.getInstance().getUserSession().getServer());
            } catch (Exception e) {
                log.debug("Trouble accessing user session to get server for exceptoin report");
            }
            try {
                stringMessage = stringMessage.concat("\nServer:"
                        + XhibitSingleton.getInstance().getUserSession().getServer());
            } catch (Exception e) {
                log.debug("Trouble accessing user session to get server for exceptoin report");
            }
            stringMessage = appendStringWithStackTraces(stringMessage, t);
            GridBagLayout gbLayout = new GridBagLayout();
            JPanel jPanelMessage = new JPanel(gbLayout);
            JTextArea jta = new JTextArea(stringMessage);
            JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jsp.setPreferredSize(new Dimension(500, 200));
            jPanelMessage.add(jsp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
            JButton button = new JButton();
            CopyTextComponentToClipboardAction action = new CopyTextComponentToClipboardAction();
            action.setModel(jta);
            action.setSelectAllIfNoSelection(true);
            action.setName(getResource(XhibitBundles.XhibitActionResources, "CopyName"));
            button.setAction(action);
            jPanelMessage.add(button);
            jta.setCaretPosition(0);

            JFrame jFrame = new JFrame(title);
            jFrame.getContentPane().add(jPanelMessage);
            jFrame.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = jFrame.getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            jFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            jFrame.show();
        }
    }

    /**
     * This method tries to cast the Trowable T to a CSException (in order to
     * recurse it) or to an Exception (if it's not a CSException) to get the
     * message and stack trace.
     * 
     * @param in
     *            the error report built so far
     * @param t
     *            an exception
     * @return string with the error report (message + stack trace)
     */
    private static String appendStringWithStackTraces(String in, Throwable t) {
        String out = in;
        String newText = "";
        try {
            CSException cse = (CSException) t;
            newText = newText.concat(">>");
            newText = newText.concat(cse.getUserMessage());
            newText = cse.getMessage() == null ? newText : newText.concat(cse.getMessage());
            newText = newText.concat(getStackTrace((Exception) cse));
            if (cse.getCause() != null) {
                newText = newText.concat(getStackTrace((Exception) cse.getCause()));
                newText = newText.concat(appendStringWithStackTraces(out, cse.getCause()));
            }
            newText = newText.concat("<<");
        } catch (ClassCastException cse) {
            // cse.printStackTrace();
            Exception e = (Exception) t;
            newText = newText.concat("...Internal ClassCastException");
            newText = e.getMessage() == null ? newText : newText.concat(e.getMessage());
            newText = newText.concat(getStackTrace(e));
        } catch (Exception e) {
            log.debug("Exception whlist appendStringWithStactTrace(in , t)");
            log.debug(e);
        }
        return out.concat(newText);
    }

    private static String buildValidationMessageText(CSValidationException cv, boolean appendInternalTrace) {
        StringBuffer messageText = new StringBuffer();
        messageText.append(cv.getUserMessage());
        if (cv.getUserValidationErrorList() != null && !cv.getUserValidationErrorList().isEmpty()) {
            Iterator iter = cv.getUserValidationErrorList().iterator();
            while (iter.hasNext()) {
                Message item = (Message) iter.next();
                messageText.append("\n" + item.getMessage());
            }
        }
        return messageText.toString();
    }

    private static String buildErrorMessageTexts(CSException CSe, boolean appendInternalTrace) {
        StringBuffer messageText = new StringBuffer(); // "";
        try {
            try {
                String[] messages = CSe.getUserMessages();
                log.debug("CSe Message length=" + messages.length);
                for (int i = 0; i < messages.length; i++) {
                    log.debug("Message text[" + i + "]=" + messages[i]);
                    messageText.append(messages[i]);
                    messageText.append("\n");
                }
                // messageText.append(CSe.getUserMessageAsMessage().getMessage()
                // + "\n");
                if (internalDebug)
                    log.debug("_____added [" + CSe.getUserMessageAsMessage().getMessage().replace('\n', ' ') + "] for "
                            + CSe);
            } catch (Exception ee) {
                // this error should not be handled.
                log.error("minor exception whlist building an exception report from CSException " + ee
                        + " ... using CSe.getUserMessageAsMessage().getMessage()");
                log.error("original error=", CSe.getCause());
            }
            if (CSe.getCause() != null && CSe.getCause() instanceof CSException) {
                // RL 18-7-03 : Added a check to see if the error message is the
                // same as the previous
                // If it is do not append.
                String newText = buildErrorMessageTexts((CSException) CSe.getCause(), appendInternalTrace);
                if (!newText.equals(messageText.toString())) {
                    messageText.append(newText);
                }
            } else {
                if (appendInternalTrace) {
                    messageText.append(getStackTrace((Exception) CSe.getCause()));
                }
            }
        } catch (ClassCastException eee) {
            if (internalDebug) {
                if (appendInternalTrace) {
                    messageText = messageText.append(getStackTrace((Exception) CSe));
                }
            }
        } catch (Exception eee) {
            // this error must not be handled.
            log.error("Minor exception whlist building an exception report from CSException " + eee);
            log.error("original error=", CSe.getCause());
        }
        return messageText.toString();
    }

    private static String getStackTrace(Exception e) {
        StringWriter s = new StringWriter();
        PrintWriter p = new PrintWriter(s);
        e.printStackTrace(p); // must to a error(e) or log.debug(e), but I
        // assume this line was put in for development
        // purposes only?
        // log.error( s.toString() );
        return s.toString();
    }

    public static boolean allowLoginAttempt() {
        return failedLoginTrials < maxFailedLoginTrials;
    }

    private static String getValidationTitle() {
        return getResource("exception.validation.title");
    }

    private static String getFatalErrorTitle() {
        return getResource("exception.fatalerror.title");
    }

    /*
     * implmentation method used in construction of error/dialog message boxes
     */
    private static String getAlertTitle() {
        if (alertTitle == null) {
            alertTitle = getResource("exception.alert.title");
        }
        return alertTitle;
    }

    /*
     * implmentation method used in construction of error/dialog message boxes
     */
    private static String getConfirmTitle() {
        if (confirmTitle == null) {
            confirmTitle = getResource("exception.alert.title");
        }

        return confirmTitle;
    }

    private static String getResource(String resourceKey) {
        return XHIBITConstant.getResource(XhibitBundles.XhibitConstant, resourceKey);
    }

    private static String getResource(String bundle, String resourceKey) {
        return XHIBITConstant.getResource(bundle, resourceKey);
    }
}