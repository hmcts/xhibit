package uk.gov.courtservice.xhibit.client.util;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
import uk.gov.courtservice.xhibit.client.util.listeners.HelpKeyListener;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <P>
 * Extend this class instead of JDialog.<br>
 * It automatically gives you Back/Next/Finish/Cancel, and links the escape key
 * to cancel.
 * <p>
 * Call addBodyPanels to add the cards in the wizard.
 * <P>
 * getButtonPanel will return a reference to the buttons.
 * <p>
 * The actions have been set up to use the default lifecyle methods As the user
 * steps through the panels various lifecycle methods are called. See the next,
 * prev and finish methods for details
 * 
 * <p>
 * By default the default button will have screen focus. If you want another
 * item to have focus, override the processWindowEvent(WindowEvent e)
 * <p>
 * Example code:<br>
 * <code>
 * protected void processWindowEvent(WindowEvent e) {<br>
 * &nbsp;&nbsp; super.processWindowEvent( e );<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;     if ( e.getID() == WindowEvent.WINDOW_OPENED ) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         getNameTextBox().requestFocus();<br>
 * &nbsp;&nbsp;     }<br>
 * }<br>
 * </code>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XWizardDialog extends JDialog {
    /**
     * Constant that identifies an escape key
     */
    private static final String ESCAPE_ACTION = "escape";

    /**
     * Constant that identifies whether the last event generated from the main
     * button pane was a 'prev' action.
     */
    public static final int PREV_EVENT = 0;

    /**
     * Constant that identifies whether the last event generated from the main
     * button panel was a 'next' action.
     */
    public static final int NEXT_EVENT = 1;

    /**
     * Constant that identifies whether the last event generated from the main
     * button panel was a 'cancel' action.
     */
    public static final int CANCEL_EVENT = 2;

    /**
     * Constant that identifies whether the last event generated from the main
     * button pane was a 'finish' action.
     */
    public static final int FINISH_EVENT = 3;

    /**
     * Constant that indentifies no event was generated from the main button
     * panel during the lifescope of the wizard dialog.
     */
    public static final int NO_EVENT = 4;

    /**
     * This references the last event that was generated from main button panel
     * of the wizard dialog. The event can be of any of the following types:
     * <p>
     * <code>XWizardDialog.PREV_EVENT</code><br>
     * <code>XWizardDialog.NEXT_EVENT</code><br>
     * <code>XWizardDialog.CANCEL_EVENT</code><br>
     * <code>XWizardDialog.FINISH_EVENT</code><br>
     * <code>XWizardDialog.NO_EVENT</code>
     */
    private int latestEvent = NO_EVENT;

    private JPanel mainPanel = new JPanel();

    private JPanel bodyPanel = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    private WizardButtonPanel btnPanel = new WizardButtonPanel();

    protected CardLayout cl = new CardLayout();

    protected int currentPanel = 0;

    protected int numPanels;

    protected Object[] xPanels = null;

    private Frame parentFrame;

    /**
     * When passing in the frame, pass in the XhibitApplicationController.<br>
     * This will be required so that actions can be retrieved if required
     * 
     * @param frame
     *            Must be the XhibitApplicationController
     * @param title
     * @param modal
     */
    public XWizardDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        parentFrame = frame;
        jbInit();
        this.addKeyListener(new HelpKeyListener());
        // React when the user presses Escape.
        this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESCAPE_ACTION);
        this.getRootPane().getActionMap().put(ESCAPE_ACTION, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ((XAction) btnPanel.getCancel().getAction()).actionPerformed(new ActionEvent(this, 0, ""));
            }
        });
    }

    private void jbInit() {
        mainPanel.setLayout(borderLayout1);
        getContentPane().add(mainPanel);
        addButtonsPanel();
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
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
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
     * Call this method to add a image to the wizard dialog.
     * 
     * @param imageName
     */
    public void setWizardPanelImage(String imageName) {
        JPanel jp = new JPanel();
        JLabel jl = new JLabel();
        ImageIcon i;
        java.net.URL url = getClass().getClassLoader().getResource(XHIBITConstant.imageRoot + imageName);
        if (url == null) {
            i = new ImageIcon(XHIBITConstant.imageRoot + imageName);
        } else {
            i = new ImageIcon(url);
        }
        jl.setIcon(i);
        jp.add(jl);
        mainPanel.add(jp, BorderLayout.WEST);
    }

    private void addButtonsPanel() {
        btnPanel = new WizardButtonPanel();
        btnPanel.getBack().setAction(new backAction(this));
        btnPanel.getNext().setAction(new nextAction(this));
        btnPanel.getCancel().setAction(new cancelAction(this));
        btnPanel.getFinish().setAction(new finishAction(this));

        // Set listener for escape
        this.addKeyListener(new EscapeListener((XAction) btnPanel.getCancel().getAction()));

        mainPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void setButtonEnabled() {
        if (currentPanel == 0) {
            btnPanel.getBack().getAction().setEnabled(false);
        } else {
            btnPanel.getBack().getAction().setEnabled(true);
        }
        if (currentPanel == numPanels) {
            btnPanel.getNext().getAction().setEnabled(false);
        }
        validate();
        // else btnPanel.getNext().getAction().setEnabled(true);
    }

    /**
     * @return the latest event generated from the main button panel of the
     *         wizard dialog.
     */
    public int getLatestEvent() {
        return this.latestEvent;
    }

    /**
     * Will set the latest event generated from the main button panel of the
     * wizard dialog. This method should not be called be any other external
     * class.
     * 
     * @param event
     *            the last event generated from the main button panel.
     */
    protected void setLatestEvent(int event) {
        this.latestEvent = event;
    }

    /**
     * Move to the next panel in card stack If the current panel is an XPanel it
     * will call<br>
     * <code>stepValidate()<br>stepDeactivate()</code><br>
     * Once the next panel is displayed, if it is an XPanel it will call<br>
     * <code>stepActivate()<br>stepUpdateViewState()</code>
     * 
     * @throws CSRecoverableException
     */
    public void next() throws CSRecoverableException {
        setLatestEvent(XWizardDialog.NEXT_EVENT);
        if (currentPanel == numPanels)
            throw new CSRecoverableException("gui.xwizard.nomorepanels", "Attempting to move next, but no more panels");
        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepValidate();
            xp.stepDeactivate();
        }
        currentPanel++;
        cl.next(bodyPanel);
        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepActivate();
            xp.stepUpdateViewState();
        }
        setButtonEnabled();
    }

    /**
     * Move to the previous card in the stact No life cycle steps will be called
     * on the current panel.<br>
     * Once the previous panel is displayed, if it is an XPanel it will call<br>
     * <code>stepUpdateViewState()</code>
     * 
     * @throws CSRecoverableException
     */
    public void prev() throws CSRecoverableException {
        setLatestEvent(XWizardDialog.PREV_EVENT);
        if (currentPanel == 0)
            throw new CSRecoverableException("gui.xwizard.noprevpanels",
                    "Attempting to move prev, but a the first panel");

        // Do not do anything to current panel if going backwards.
        // if (xPanels[currentPanel] instanceof XPanel) {
        // XPanel xp = (XPanel)xPanels[currentPanel];
        // xp.stepValidate();
        // xp.stepDeactivate();
        // }
        currentPanel--;
        cl.previous(bodyPanel);
        // On a previous do you want activate called again???
        // For now I am assuming NO.
        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            // xp.stepActivate();
            xp.stepUpdateViewState();
        }
        setButtonEnabled();
    }

    /**
     * Finish the wizard. If the current panel is an XPanel it will call<br>
     * <code>stepValidate()<br>stepDeactivate()<br>stepDeinitialise()</code>
     * 
     * @throws CSRecoverableException
     */
    public void finish() throws CSRecoverableException {
        setLatestEvent(XWizardDialog.FINISH_EVENT);
        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepValidate();
            xp.stepDeactivate();
            xp.stepDeinitialise(true);
        }
    }

    /**
     * Return true if the cancel button should alert the user
     */
    public boolean showCancelAlert() {
        return true;
    }

    /**
     * Use this method to add your XPanel to the centre portion of the dialog<br>
     * This method must be used otherwise the buttons will not be able to call
     * the lifecycle methods.
     * 
     * @param xpanel
     */
    public void addBodyPanels(Collection xpanels) {
        xPanels = xpanels.toArray();
        int count = 0;
        bodyPanel = new JPanel();
        bodyPanel.setLayout(cl);
        Iterator iter = xpanels.iterator();
        while (iter.hasNext()) {
            JPanel item = (JPanel) iter.next();
            XhibitListeners.setDefaultListeners(item);
            bodyPanel.add(item, Integer.toString(count));
            count++;
        }
        numPanels = xpanels.size() - 1;
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        setButtonEnabled();
    }

    public void setPanelIndex(int panelIndex, int latestEvent) throws CSRecoverableException {
        if (panelIndex < 0 || panelIndex >= xPanels.length)
            throw new CSRecoverableException();

        this.latestEvent = latestEvent;

        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepValidate();
            xp.stepDeactivate();
        }

        cl.first(bodyPanel);
        currentPanel = panelIndex;

        if (panelIndex == 0) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepActivate();
            xp.stepUpdateViewState();
        } else {
            for (int i = 0; i < xPanels.length; i++) {
                if (i == currentPanel) {
                    XPanel xp = (XPanel) xPanels[currentPanel];
                    xp.stepActivate();
                    xp.stepUpdateViewState();
                    break;
                }
                cl.next(bodyPanel);
            }
        }
        setButtonEnabled();
    }

    /**
     * Overriding the super class pack so that the dialog is centred.
     */
    public void pack() {
        super.pack();
        centreDialog();
    }

    /**
     * Returns a references to the Wizard button panel
     * 
     * @return
     */
    public WizardButtonPanel getButtonPanel() {
        return btnPanel;
    }

    /**
     * Overrides set visible so that stepActivate and stepUpdateViewState are
     * called when the dialog is displayed.
     * 
     * @param b
     */
    public void setVisible(boolean b) {
        super.setVisible(b);

        // SG - Commented out because the super.setVisible(b); call above will
        // display the dialog and the following code is not executed until
        // the dialog closes, which does not make sense!
        //
        // if (xPanels[currentPanel] instanceof XPanel) {
        // XPanel xp = (XPanel)xPanels[currentPanel];
        // try
        // {
        // xp.stepActivate();
        // xp.stepUpdateViewState();
        // }
        // catch (CSRecoverableException ex)
        // {
        // XHIBITConstant.handleError(ex);
        // }
        // }
    }

    // Trap closing using Alt-F4 and x
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            btnPanel.getCancel().doClick();
        } else {
            super.processWindowEvent(e);
        }
    }
}

class EscapeListener implements KeyListener {
    private XAction xa;

    public EscapeListener(XAction eventToFire) {
        xa = eventToFire;
    }

    public void keyPressed(KeyEvent k) {
    }

    public void keyTyped(KeyEvent k) {
    }

    public void keyReleased(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
            xa.actionPerformed(new ActionEvent(this, 0, ""));
        }
    }
}

class backAction extends XAction {
    XWizardDialog xwd;

    public backAction(XWizardDialog parent) {
        populateFromBundle("WizBack");
        xwd = parent;
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xwd.prev();
    }
}

class nextAction extends XAction {
    XWizardDialog xwd;

    public nextAction(XWizardDialog parent) {
        populateFromBundle("WizNext");
        xwd = parent;
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xwd.next();
    }
}

class finishAction extends XAction {
    XWizardDialog xwd;

    public finishAction(XWizardDialog parent) {
        populateFromBundle("WizFinish");
        xwd = parent;
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xwd.finish();
        xwd.dispose();
    }
}

class cancelAction extends XAction {
    XWizardDialog xwd;

    static ResourceBundle rb = null;

    public cancelAction(XWizardDialog parent) {
        populateFromBundle("btnCancel");
        xwd = parent;
    }

    private ResourceBundle getClientBundle() {
        if (rb == null) {
            rb = XHIBITConstant.getResourceBundle(XhibitBundles.XhibitClientDefaultResources);
        }
        return rb;
    }

    public void xActionPerformed(ActionEvent e) throws UserCancelException, CSRecoverableException {
        xwd.setLatestEvent(XWizardDialog.CANCEL_EVENT);
        if (xwd.showCancelAlert()
                && !XMessageBox.alert(xwd, getClientBundle().getString("CancelConfirmTitle"), true,
                        XMessageBox.ICONQUESTION, getClientBundle().getString("CancelConfirm"), XMessageBox.YESNO,
                        XMessageBox.DEFAULTNO)) {
            throw new UserCancelException();
        }
        try {
            if (xwd.xPanels[xwd.currentPanel] instanceof XPanel) {
                XPanel xp = (XPanel) xwd.xPanels[xwd.currentPanel];
                xp.stepDeinitialise(false);
            }
        } catch (CSRecoverableException ex) {
            throw ex;
        } finally {
            xwd.dispose();
        }
    }
}
