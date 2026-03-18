package uk.gov.courtservice.xhibit.client.util;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
import uk.gov.courtservice.xhibit.client.util.helpers.ShieldHelper;
import uk.gov.courtservice.xhibit.client.util.listeners.HelpKeyListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>Title: XHIBIT 2</p>
 * <p>Description: </p>
 * <P>Extend this class instead of JDialog.<br>It automatically gives you OK/Cancel, Yes/No or Apply/OK/Cancel, set a default button and
 * links the escape key to cancel.
 * <p>The Yes/No buttons are the implemented using the Ok Cancel panel overwriting the Text that is displayed on the button<br>
 * so the code will still read OK and Cancel, but the user will see Yes/No.
 * <P>For full flexibility, in your constructor call super passing Frame (can be null), A title (String), Whether it is to be modal (boolean),
 * <br>The buttons you want: OKCANCEL or APPLYOKCANCEL (static int's)
 * <br>and the default button: DEFAULTOK, DEFAULTCANCEL or DEFAULTAPPLY (static int's)
 * <p>You will need to ensure that the lifecycle methods in your XPanel are implemented.
 * All three buttons will call StepValidate, StepDeactive and StepDeinitialise (this will have true for Apply and OK, false for Cancel).
 *
 * <p>A limited construtor can call super passing only a title (string) and modality (boolean) to receive a dialog with OK/Cancel buttons, with OK as the default.
 * <p>By default the default button will have screen focus. If you want another item to have focus, override the processWindowEvent(WindowEvent e)
 * <p>Example code:<br>
 * <code>
 * protected void processWindowEvent(WindowEvent e) {<br>
 * &nbsp;&nbsp; super.processWindowEvent( e );<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;     if ( e.getID() == WindowEvent.WINDOW_OPENED ) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         getNameTextBox().requestFocus();<br>
 * &nbsp;&nbsp;     }<br>
 * }<br>
 * </code>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Rakesh Lakhani
 * @version 1.2
 */
/**
 * Ref Date Author Description
 * 
 * 17,52136 26-02-2003 AW Daley Adding the button panel to the dialog deferred
 * until the body panel is added. This ensures that the first field on the body
 * panel will receive focus and not the buttons on the button pane.
 */

public class XDialog extends JDialog implements ApplyOkCancelPanelConsumer, ShieldInterface {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constant that identifies an escape key
     */
    private static final String ESCAPE_ACTION = "escape";

    protected JPanel mainPanel = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    protected OkCancelPanel buttonPanel;

    // Used to ensure that the button panel is added once and only once.
    private boolean buttonsAdded = false;

    protected XPanel bodyPanel = null;

    private Frame parentFrame;

    protected int buttonClicked = -1;

    public static final int DEFAULTOK = ApplyOkCancelPanel.OK;

    public static final int DEFAULTCANCEL = ApplyOkCancelPanel.CANCEL;

    public static final int DEFAULTYES = ApplyOkCancelPanel.OK;

    public static final int DEFAULTNO = ApplyOkCancelPanel.CANCEL;

    public static final int DEFAULTAPPLY = ApplyOkCancelPanel.APPLY;

    public static final int OKCANCEL = 0;

    public static final int YESNO = 1;

    public static final int APPLYOKCANCEL = 2;

    public static final int CANCEL = 3;
    
    public static final int OK_ONLY = 4;
    
    public static final int CUSTOM = 5;

    public static boolean internalDebug = false;

    private final ShieldHelper shieldHelper = new ShieldHelper(this);

    static {
        try {
            XHIBITConstant.debug("Started static initialisation of XDialog.");
            internalDebug = XHIBITConstant.isInternalDebug("XDialog");

        } catch (Exception e) {
            XHIBITConstant.error("Exception during Static initialisation of XDialog");
            XHIBITConstant.error(e);
        } finally {
            XHIBITConstant.debug("Static initialisation XDialog finished.");
        }
    }

    /**
     * A reference to the button panel
     * 
     * @return OkCancelPanel
     */
    public OkCancelPanel getButtonPanel() {
        return this.buttonPanel;
    }

    /**
     * Allows you to specify a new action for the OK / Yes button
     * 
     * @param okAction
     */
    public void setOkAction(XAction okAction) {
        this.buttonPanel.okButton.setAction(okAction);
    }

    /**
     * Allows you to enable/disable the OK / Yes button
     * 
     * @param enabled
     */
    public void setOkEnabled(boolean enabled) {
    	this.buttonPanel.okButton.setEnabled(enabled);
    }
    
    /**
     * Allows you to specify a new action for the Cancel / No button
     * 
     * @param cancelAction
     */
    public void setCancelAction(XAction cancelAction) {
        this.buttonPanel.cancelButton.setAction(cancelAction);
    }

    /**
     * Allows you to enable/disable the Cancel / No button
     * 
     * @param enabled
     */
    public void setCancelEnabled(boolean enabled) {
    	this.buttonPanel.cancelButton.setEnabled(enabled);
    }

    /**
     * Allows you to set the verifyInputWhenFocusTarget for the Cancel / No button
     * 
     * @param enabled
     */
    public void setCancelVerifyInputWhenFocusTarget(boolean verifyInputWhenFocusTarget) {
    	this.buttonPanel.cancelButton.setVerifyInputWhenFocusTarget(verifyInputWhenFocusTarget);
    }

    /**
     * Allow you to specify a new action for the Apply button
     * 
     * @param applyAction
     */
    public void setApplyAction(XAction applyAction) {
        ((ApplyOkCancelPanel) this.buttonPanel).applyButton.setAction(applyAction);
    }

    /**
     * Allows you to enable/disable the Apply button
     * 
     * @param enabled
     */
    public void setApplyEnabled(boolean enabled) {
    	((ApplyOkCancelPanel) this.buttonPanel).applyButton.setEnabled(enabled);
    }

    /**
     * Full flexibility Constructor<br>
     * <p>
     * See doco above for options available for all parameters.
     * 
     * @param frame
     * @param title
     * @param modal
     * @param buttons
     * @param defaultButton
     */
    public XDialog(Frame frame, String title, boolean modal, int buttons, int defaultButton) {
        super(frame, title, modal);
        parentFrame = frame;
        setContents(buttons, defaultButton);
    }

    /**
     * Sets the contents of the pane, adds the buttons and help and escape
     * listeners
     * 
     * @param buttons
     * @param defaultButton
     */
    private void setContents(int buttons, int defaultButton) {
        mainPanel.setLayout(borderLayout1);
        getContentPane().add(mainPanel);
        addButtonsPanel(buttons, defaultButton);
        this.addKeyListener(new HelpKeyListener());

        // React when the user presses Escape.
        this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESCAPE_ACTION);
        this.getRootPane().getActionMap().put(ESCAPE_ACTION, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                getButtonPanel().cancelButton.doClick();
            }
        });
    }

    // private static Frame findActiveFrame(Frame frameIn) {
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

    /**
     * Simple constructor Call super(frame, title, modal) in your code.
     * OK(default) and Cancel buttons added.<br>
     * 
     * @param frame
     * @param title
     * @param modal
     */
    public XDialog(Frame frame, String title, boolean modal) {
        this(frame, title, modal, OKCANCEL, DEFAULTOK);
    }

    /**
     * @deprecated replaced by <code>XDialog(Frame, title, modal)</code>,
     *             where frame is a reference to XhibitApplicationController You
     *             can then use getParentFrame to get a handle on XAC.
     */
    public XDialog(String title, boolean modal) {
        this((Frame) null, title, modal, OKCANCEL, DEFAULTOK);
    }

    /**
     * Use this construct where the parent of this dialog is another dialog.
     * 
     * @param dialogParent
     *            the dialog parent of this dialog.
     * @param title
     *            the title of this dialog
     * @param modal
     *            the modality of this dialog.
     * @param button
     *            for this idalog to display by default
     * @param defaultButton
     */
    public XDialog(Dialog dialogParent, String title, boolean modal, int buttons, int defaultButton) {
        super(dialogParent, title, modal);
        setContents(buttons, defaultButton);
    }

    /**
     * Simple constructor Call super(dialog, title, modal) in your code.
     * OK(default) and Cancel buttons added.<br>
     * 
     * @param frame
     * @param title
     * @param modal
     */
    public XDialog(Dialog dialogParent, String title, boolean modal) {
        this(dialogParent, title, modal, OKCANCEL, DEFAULTOK);
    }

    /**
     * This will return the frame that called the dialog. If used correctly this
     * will return the XhibitApplicationController.
     * 
     * @return
     */
    public Frame getParentFrame() {
        return parentFrame;
    }

    /**
     * This method centres the dialog on the screen.<br>
     * This method is automatically called by the pack() method.
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
    
    /**
     * Add the relevant buttons to the SOUTH border<br>
     * By default OK/Cancel will be added.
     * 
     * @param panelType
     * @param defaultButton
     */
    private void addButtonsPanel(int panelType, int defaultButton) {
        // create the button panel
        buttonPanel = createButtonPanel(panelType, defaultButton);

        // Add default listeners
        XhibitListeners.setDefaultListeners(buttonPanel);

        // Set listener for escape
        this.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent k) {
                // empty
            }

            public void keyTyped(KeyEvent k) {
                // empty
            }

            public void keyReleased(KeyEvent k) {
                if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    getButtonPanel().cancelButton.doClick();
                }
            }
        });
        // Defer adding the button panel to the dialog until the body panel
        // is added. This ensures that the first field on the body panel will
        // receive focus and not the buttons on the button panel
        // mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        buttonPanel.cancelButton.setVerifyInputWhenFocusTarget(false);
    }

    /**
     * Added as a hook to change construction of button panel
     * 
     * @param panelType
     * @param defaultButton
     * @return the new button panel
     */
    protected OkCancelPanel createButtonPanel(int panelType, int defaultButton) {
        String yesText = "Yes";
        String noText = "No";
        char yesMnemonic = 'Y';
        char noMnemonic = 'N';

        switch (panelType) {
        case (OKCANCEL):
            return new OkCancelPanel(this, defaultButton);
        case (OK_ONLY): {
                OkCancelPanel buttonPanel = new OkCancelPanel(this, defaultButton);
                buttonPanel.cancelButton.setVisible(false);
                return buttonPanel; 
            }
        case (YESNO):
            OkCancelPanel buttonPanel = new OkCancelPanel(this, defaultButton);
            try {
                ResourceBundle myResources = XHIBITConstant
                        .getResourceBundle(XhibitBundles.XhibitClientDefaultResources);
                yesText = myResources.getString("btnYesName");
                noText = myResources.getString("btnNoName");
                yesMnemonic = myResources.getString("btnYesMnemonic").charAt(0);
                noMnemonic = myResources.getString("btnNoMnemonic").charAt(0);
            } catch (java.util.MissingResourceException ex) {
                XHIBITConstant.error("ResourceBundle could not be found for " + Locale.getDefault());
                XHIBITConstant.error(ex);
            }
            buttonPanel.okButton.setText(yesText);
            buttonPanel.cancelButton.setText(noText);
            buttonPanel.okButton.setMnemonic(yesMnemonic);
            buttonPanel.cancelButton.setMnemonic(noMnemonic);
            return buttonPanel;
        case (APPLYOKCANCEL):
            return new ApplyOkCancelPanel(this, defaultButton);
        case (CUSTOM):
            return new CustomButtonPanel(this, defaultButton);
        default:
            return new OkCancelPanel(this, defaultButton);
        }
    }

    /**
     * Closes the dialog.
     */
    protected void closeDialog() {
        clearStatusBarScreenCode();
        this.dispose();
    }

    /**
     * Used by buttons to call the final three life cycle methods on the XPanel.
     * <p>
     * If any of the methods are performing actions against business delegates,
     * <br>
     * be sure to throw the exception back to this method otherwise the next
     * step in the chain will still run!
     * 
     * @param save
     */
    private void performCloseLifeCycle(boolean save, boolean closeWindow, ActionEvent ae) throws Exception {
        if (internalDebug && ae != null)
            XHIBITConstant.debug("performCloseLifeCycle: " + ae.toString());
        
        if (bodyPanel != null) {
        	Object source = (ae != null) ? ae.getSource() : null;
        	bodyPanel.stepPreDeinitialise(source);
            if (save) {
                bodyPanel.stepValidate();
                bodyPanel.stepDeactivate();
            }
            bodyPanel.stepDeinitialise(save);
            if (closeWindow) {
            	closeDialog();
            }
        }
        // NOT POSSIBLE TO USE A SYNC ACTION HERE AS MANY STEPDEINITIALISE
        // METHODS
        // TRAP THE ERROR AND CALL THE ERROR HANDLER DIRECTLY, WHICH CAUSES THE
        // ERROR WINDOW TO NO BE ACCESSBILE
        // DialogCloseAction dcAction = new DialogCloseAction(save,
        // closeWindow);
        // dcAction.actionPerformed(ae);
    }

    class DialogCloseAction extends SynchXAction {

        private static final long serialVersionUID = 1L;

        boolean save;

        boolean closeWindow;

        public DialogCloseAction(boolean save, boolean closeWindow) {
            this.save = save;
            this.closeWindow = closeWindow;
        }

        public void preSynchActionPerformed(ActionEvent parm1) throws CSRecoverableException {
            if (save) {
                bodyPanel.stepValidate();
                bodyPanel.stepDeactivate();
            }
        }

        public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
            bodyPanel.stepDeinitialise(save);
        }

        public void postSynchActionPerformed(ActionEvent parm1) throws CSRecoverableException {
            if (closeWindow) {
                clearStatusBarScreenCode();
                dispose();
            }
        }
    }

    public void okClicked(ActionEvent ae) throws Exception {
        if (internalDebug)
            XHIBITConstant.debug("XDialog OK Clicked");
        buttonClicked = DEFAULTOK;
        performCloseLifeCycle(true, true, ae);
    }

    public void cancelClicked(ActionEvent ae) throws Exception {
        if (internalDebug)
            XHIBITConstant.debug("XDialog Cancel Clicked");
        buttonClicked = DEFAULTCANCEL;
        performCloseLifeCycle(false, true, ae);
    }

    public void applyClicked(ActionEvent ae) throws Exception {
        if (internalDebug)
            XHIBITConstant.debug("XDialog Apply Clicked");
        buttonClicked = DEFAULTAPPLY;
        performCloseLifeCycle(true, false, ae);
    }

    public void customClicked(boolean save, boolean closeWindow, ActionEvent ae) throws Exception {
        if (internalDebug)
            XHIBITConstant.debug("XDialog Custom Clicked");
        buttonClicked = -1;
        performCloseLifeCycle(save, closeWindow, ae);
    }

    /**
     * Find out if the OK button was clicked
     * 
     * @return
     */
    public boolean isOkClicked() {
        return (buttonClicked == DEFAULTOK);
    }

    /**
     * Find out if the Cancel button was clicked
     * 
     * @return
     */
    public boolean isCancelClicked() {
        return (buttonClicked == DEFAULTCANCEL);
    }

    /**
     * Find out if the Apply button was clicked
     * 
     * @return
     */
    public boolean isApplyClicked() {
        return (buttonClicked == DEFAULTAPPLY);
    }

    /**
     * Use this method to add your XPanel to the centre portion of the dialog<br>
     * This method must be used otherwise the buttons will not be able to call
     * the lifecycle methods.
     * 
     * @param xpanel
     */
    public void addBodyPanel(XPanel xpanel) {
        if (bodyPanel != null)
            mainPanel.remove(bodyPanel);
        bodyPanel = xpanel;
        XhibitListeners.setDefaultListeners(bodyPanel);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        // Defer adding the button panel to the dialog until the body panel
        // is added. This ensures that the first field on the body panel will
        // receive focus and not the buttons on the button pane
        if (!buttonsAdded) {
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            buttonsAdded = true;
        }
    }
    
    // Duplicate method to allow insets for buttonPanel
    public void addBodyPanel(XPanel xpanel, Insets insets) {
        if (bodyPanel != null)
            mainPanel.remove(bodyPanel);
        bodyPanel = xpanel;
        XhibitListeners.setDefaultListeners(bodyPanel);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        // Defer adding the button panel to the dialog until the body panel
        // is added. This ensures that the first field on the body panel will
        // receive focus and not the buttons on the button pane
        if (!buttonsAdded) {
        	buttonPanel.setBorder(new EmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            buttonsAdded = true;
        }
    }

    /**
     * Overriding the super class pack so that the dialog is centred.
     */
    public void pack() {
        super.pack();
        centreDialog();
    }

    /**
     * Resize the dialog to the available screen, i.e. taking into
     * account the space the taskbar uses. Must be used instead of
     * the above pack method to take effect.
     */
    public void fullScreen() {
    	// Get the bounds of the screen and the size of the taskbar
        GraphicsConfiguration graphicsConfig = getGraphicsConfiguration();
        Rectangle screenBounds = graphicsConfig.getBounds();
        Insets screenInsets = getToolkit().getScreenInsets(graphicsConfig);
        
        // Create bounds with the effective working area of the screen
        Rectangle effectiveBounds = new Rectangle();
        effectiveBounds.x = screenBounds.x + screenInsets.left;
        effectiveBounds.y = screenBounds.y + screenInsets.top;
        effectiveBounds.height = screenBounds.height - screenInsets.top - screenInsets.bottom;
        effectiveBounds.width = screenBounds.width - screenInsets.left - screenInsets.right;
        
        // Resize the dialog to the effective screen size
        setBounds(effectiveBounds);
    }
    
    public void stepActivate() throws CSRecoverableException {
        if (this.parentFrame instanceof XhibitApplicationController) {
            XhibitApplicationController xac = (XhibitApplicationController) this.parentFrame;
            xac.setScreenLabel(getScreenCode());
            if (internalDebug)
                XHIBITConstant.debug("XDialog - stepActivate - set Screen Code: " + getScreenCode());
        }
    }

    public void stepDeactivate() throws CSRecoverableException {
        // empty
    }

    @Override
    public void setVisible(boolean isVisible) {
        try {
            if (bodyPanel != null) {
                bodyPanel.setVisible(isVisible);
            }
            if (isVisible) {
                if (internalDebug)
                    XHIBITConstant.debug("setVisible triggers stepActivate(); !!!");
                stepActivate();
            } else {
                if (internalDebug)
                    XHIBITConstant.debug("setVisible triggers stepDeactivate(); !!!");
                stepDeactivate();
            }
        } catch (Exception re) {
            XHIBITConstant.error(re);
            XHIBITConstant.handleError(re);
        }
        super.setVisible(isVisible);
    }

    // Trap closing using Alt-F4 and x
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            getButtonPanel().cancelButton.doClick();
        } else {
            super.processWindowEvent(e);
        }
    }

    // either all sub class xdialog implementors override this method,
    // setting their dialog code, or all calls must be removed from the
    // code.
    public String getScreenCode() {
        String screenCode = XHIBITConstant.getResource(XhibitBundles.XhibitDialogCodes, this.getClass().getName());
        if (internalDebug)
            XHIBITConstant.debug(this.getClass().getName() + ".getScreenCode() returns  XDialog Code '" + screenCode
                    + "'.");
        return screenCode;
    }

    public void clearStatusBarScreenCode() {
        try {
            if (this.parentFrame instanceof XhibitApplicationController) {
                if (internalDebug)
                    XHIBITConstant.debug("stepDeactivate in XDIALOG resetting status now");
                XhibitApplicationController xac = (XhibitApplicationController) this.parentFrame;
                xac.setScreenLabel("");
                if (internalDebug)
                    XHIBITConstant.debug("XDialog - stepDeactivate - clearing statusbar");
            } else {
                XHIBITConstant
                        .debug("XDialog - stepDeactivate - parentFrame not instanceof XhibitApplicationController");
            }
        } catch (Exception e) {
            XHIBITConstant.error("XDialog - stepDeactivate() - Exception caught:");
            XHIBITConstant.error(e);
        }
    }

    /**
     * Raise a glass pane infront of the dialog so nothing can be clicked on
     * allowing multi threaded call without worrying about the user clicking on
     * anything
     */
    public void shield() {
        shieldHelper.shield();
    }

    /**
     * Remove glass pane allowing application to continue as normal
     */
    public void unshield() {
        shieldHelper.unshield();
    }
}