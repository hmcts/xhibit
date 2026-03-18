package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseAccessException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseClosedException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseLoadFailedException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseLoadingException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseLockedException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseNotLoadedException;
import uk.gov.courtservice.xhibit.business.services.caze.CasePartiallyLoadedException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseRemovedException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseResyncFailException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseRetrievalIntControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;

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
 * @author unascribed
 * @version 1.0
 */

public class OpenCaseDialog extends JDialog {
    // allow us to log directly from class
    private static final Logger log = Logger.getLogger(OpenCaseDialog.class);

    public static final int NO_ERROR = 0;

    public static final int CASE_OPEN_CANCEL = 1;

    // public static final int CASE_ALREADY_OPEN = 2;
    public static final int CASE_NOT_FOUND = 3;

    public static final int CHARGE_ERROR = 4;

    public static final int CASE_OPEN_ERROR = 5;

    public static final int CASE_OPEN_UNKNOWN_ERROR = 6;

    /** The width of this dialog. */
    protected static final int DIALOG_WIDTH = 270;

    /** The height of this dialog. */
    protected static final int DIALOG_HEIGHT = 150;

    /** The image name of the image for display. */
    protected static final String IMAGE_NAME = "gavel2.gif";

    protected int errorType = NO_ERROR;

    /**
     * Notification message used to display the current status of the open case
     * operation.
     */
    private JLabel notificationMessage = null;

    /** The icon for display. */
    private JLabel iconLabel;

    /** Thread used to open the case. */
    private OpenCaseThread openCaseThread = null;

    private Thread t = null;

    /** Set to true if the user cancels the process */
    private boolean stopThread = false;

    private JoinderIndictmentModel model = null;

    // Case Related attributes.
    private Integer caseId = null;

    private CaseAccessValue caseAccessValue = null;

    private ResourceBundle joinderResources = null;

    private String errorTitle = "";

    private String errorMsg = "";

    /**
     * Creates an open case dialog.
     *
     * @param parent
     *            the dialog that opened this.
     */
    public OpenCaseDialog(final JDialog parent) {
        super(parent, "Open Case", true);
        init();
    }

    /**
     * Initialises the components of this dialog.
     */
    private void init() {
        // Instantiate the panels.
        JPanel iconPanel = new JPanel(new BorderLayout());
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Instantiate the labels.
        this.notificationMessage = new JLabel();

        ImageIcon i = null;
        URL url = getClass().getClassLoader().getResource(XHIBITConstant.imageRoot + IMAGE_NAME);
        if (url == null) {
            i = new ImageIcon(XHIBITConstant.imageRoot + IMAGE_NAME);
        } else {
            i = new ImageIcon(url);
        }
        this.iconLabel = new JLabel(i);

        // Add the components.
        iconPanel.add(this.iconLabel, BorderLayout.CENTER);
        iconPanel.setBorder(BorderFactory.createEtchedBorder());
        messagePanel.add(this.notificationMessage, BorderLayout.CENTER);
        messagePanel.setPreferredSize(new Dimension(DIALOG_WIDTH, (int) (DIALOG_HEIGHT * 0.2)));
        messagePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(iconPanel, BorderLayout.CENTER);
        this.getContentPane().add(messagePanel, BorderLayout.SOUTH);

        // Dialog specifics
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        this.centreDialog();
    }

    /**
     * Overrides the method of the superclass to always return false.
     *
     * @return false - this dialog is not resizable.
     */
    public boolean getResizable() {
        return false;
    }

    /**
     * Sets the location of the dialog in the centre of the screen.
     */
    public void centreDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private ResourceBundle getJoinderBundle() {
        if (joinderResources == null) {
            joinderResources = XHIBITConstant.getResourceBundle(XhibitBundles.JoinderResources);
        }
        return joinderResources;
    }

    /**
     * Update the message stored in the dialog, the update will always be made
     * on the event-dispatching thread.
     *
     * @param message
     *            the message to be set.
     */
    private void updateMessageLabel(final String message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                notificationMessage.setText(message);
                notificationMessage.revalidate();
                notificationMessage.repaint();
            }
        });
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cleanUp();
            errorType = CASE_OPEN_CANCEL;
            errorTitle = "";
            errorMsg = "";
        }
    }

    public int getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMsg;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    private void cleanUp() {
        if (openCaseThread != null)
            openCaseThread.requestStop();
        if (t != null) {
            t.interrupt();
        }
        this.t = null;

        caseId = null;
        caseAccessValue = null;
        dispose();
    }

    /**
     * Will show the dialog as it performs it open case process.
     *
     * @param model
     *            the model to have its case related attributes set.
     * @throws CSRecoverableException
     *             if an error occured.
     */
    public void openCase(JoinderIndictmentModel model) throws CSRecoverableException {
        // Reset the error type and thread control values.
        errorType = NO_ERROR;
        stopThread = false;

        this.model = model;

        openCaseThread = new OpenCaseThread();
        t = new Thread(openCaseThread);
        t.start();

        this.setVisible(true);

        log.debug("openCase after setVisible(true) - errorType = " + errorType);
    }

    /**
     *
     * <p>
     * Title: Open Case Thread
     * </p>
     * <p>
     * Description: Responsible for invoking the process of opening a case.
     * </p>
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
    private class OpenCaseThread implements Runnable {
        public OpenCaseThread() {
        }

        public void run() {
            try {
                if (!model.isCaseJoined(model.getSelectedCaseTypeNumber())) {
                    updateMessageLabel("Searching for case...");
                    log.debug("Searching for case: " + model.getSelectedCaseTypeNumber());

                    caseId = XhibitDelegateHelper.getCaseDelegate().findCaseId(model.getSelectedCaseType(),
                            model.getSelectedCaseNumber(), model.getJoinderCourtId());

                    if (stopThread)
                        return;

                    log.debug("CASE FOUND");
                    updateMessageLabel("Opening case...");

                    try {
                        // Now open the case from the given case id.
                        caseAccessValue = XhibitDelegateHelper.getCaseDelegate().openCase(caseId);

                        // Validate - if null, then problem with opening case
                        if (caseAccessValue == null) {
                            throw new CaseLoadingException();
                        }
                    } catch (CaseLoadingException e) {
                        log.debug("Case Loading Exception FIRED");                
                    }

                    if (stopThread)
                        return;
                }

                setModelAttributes(model, caseAccessValue);
            } catch (CaseAccessException cae) {
                log.error("[OpenCaseThread] run - caught CaseAccessException", cae);
                errorType = CASE_OPEN_ERROR;              
            } catch (CaseControllerException cce) {
                log.error("[OpenCaseThread] run - caught CaseControllerException", cce);
                // This exception is thrown if the case is not found.
                errorType = CASE_NOT_FOUND;
            } catch (Exception e) {
                log.error("[OpenCaseThread] run - caught Exception", e);
                errorType = CASE_OPEN_UNKNOWN_ERROR;
                errorTitle = getJoinderBundle().getString("openCaseDialog.error.title");
                errorMsg = getJoinderBundle().getString("openCaseDialog.error.msg");
            } finally {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        OpenCaseDialog.this.setVisible(false);
                        OpenCaseDialog.this.cleanUp();
                    }
                });
            }
        }

        /**
         * Called to stop the OpenCaseThread as soon as possible.
         */
        public void requestStop() {
            stopThread = true;
        }
        

        /**
         * Stores case information in the model.
         *
         * @param model
         *            the model to have its attributes set.
         * @param caseAccessValue
         *            the case value object, containing all the other case
         *            information.
         */
        private void setModelAttributes(JoinderIndictmentModel model, CaseAccessValue caseAccessValue) {
            try {
                ChargeCompositeValue chargeCompValue = model.getChargeCompositeValue(model.getSelectedCaseTypeNumber());

                if (chargeCompValue == null) {
                    model.setSelectedCaseId(caseAccessValue.getCaseId());

                    updateMessageLabel("Searching for indictments...");

                    // Retrieve the defendants and charges and store them in
                    // the model. We don't require the Charge Amendment log.
                    log.debug("About to call getCharges(" + model.getSelectedCaseId() + ", false)");

                    chargeCompValue = XhibitDelegateHelper.getChargeDelegate().getCharges(model.getSelectedCaseId(),
                            false);

                    log.debug("Done getCharges(caseId, false)");
                } else {
                    model.setSelectedCaseId(chargeCompValue.getCaseBasicValue().getCaseId());
                }

                model.setSelectedChargeCompValue(chargeCompValue);
                model.addCaseId(model.getSelectedCaseId());
                model.addJoinderCase(chargeCompValue.getCaseBasicValue());
            } catch (ChargeControllerException cce) {
                log.error("ChargeControllerException in setModelAttributes", cce);
                errorType = CHARGE_ERROR;
            } catch (Exception e) {
                log.error("Exception in setModelAttributes", e);
                errorType = CHARGE_ERROR;
            }
        }
    }
}