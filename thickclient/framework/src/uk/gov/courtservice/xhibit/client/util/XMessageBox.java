package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: This is a helper utility that will allow the quick creation of a
 * message box to deliver user messages
 * </p>
 * <p>
 * It is still recommended that you use JOptionPane, however there is a problem
 * with setting a default button other than the OK,<br>
 * so this helper class was created.
 * <p>
 * Use the static alert() methods to display the message box.
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
 */

public class XMessageBox extends XDialog {

    private static final long serialVersionUID = 1L;

    private BodyPanel mainBody;

    // public static final int OKCANCEL = OKCANCEL;
    // public static final int YESNO = YESNO;
    public static final int DEFAULTOK = OkCancelPanel.OK;

    public static final int DEFAULTCANCEL = OkCancelPanel.CANCEL;

    public static final int ICONNONE = 0;

    public static final int ICONINFORMATION = 1;

    public static final int ICONWARNING = 2;

    public static final int ICONERROR = 3;

    public static final int ICONQUESTION = 4;

    private int buttonClicked;

    public XMessageBox(Frame frame, String title, boolean modal, int icon, JComponent message, int buttonType,
            int defaultButton) {
        super(frame, title, modal, buttonType, defaultButton);
        jbInit(icon, message);
        displayDialog();
    }

    // replaced by contructor above.
    public XMessageBox(Frame frame, String title, boolean modal, int icon, JComponent message, int defaultButton) {
        this(frame, title, modal, icon, message, OKCANCEL, defaultButton);
    }

    private void init(int icon, String message) {
        // Format string into a text area.
        JTextArea jt = new JTextArea(message);
        jt.setEditable(false);
        jt.setEnabled(false);
        jt.setDisabledTextColor(SystemColor.controlText);
        jt.setBackground(SystemColor.control);
        jt.setRequestFocusEnabled(false);

        UIDefaults defaults = UIManager.getDefaults();
        Font f = defaults.getFont("OptionPane.font");
        jt.setFont(f);

        jbInit(icon, jt);
        displayDialog();
    }

    public XMessageBox(Dialog dialog, String title, boolean modal, int icon, String message, int buttonType,
            int defaultButton) {
        super(dialog, title, modal, buttonType, defaultButton);
        init(icon, message);
    }

    public XMessageBox(Frame frame, String title, boolean modal, int icon, String message, int buttonType,
            int defaultButton) {
        super(frame, title, modal, buttonType, defaultButton);
        init(icon, message);
    }

    // replaced by contructor above.
    public XMessageBox(Frame frame, String title, boolean modal, int icon, String message, int defaultButton) {
        this(frame, title, modal, icon, message, OKCANCEL, defaultButton);
    }

    /**
     * This method offers the maximum flexibility to the message box.
     * 
     * @param frame
     *            The name of the containing dialog, can be null
     * @param title
     *            The title to appear on the dialog box
     * @param modal
     *            Whether or not you want a modal window
     * @param icon
     *            The icon to display. Use MessageBox.ICON....
     * @param message
     *            A JComponent to be displayed in the message area
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked. Note only works for
     *         Modal displays.
     */
    public static boolean alert(Frame frame, String title, boolean modal, int icon, JComponent message, int buttonType,
            int defaultButton) {
        XMessageBox m = new XMessageBox(frame, title, modal, icon, message, buttonType, defaultButton);
        return m.whatWasClicked();
    }

    /**
     * This method offers the maximum flexibility to the message box.
     * 
     * @param frame
     *            The name of the containing frame, can be null
     * @param title
     *            The title to appear on the dialog box
     * @param modal
     *            Whether or not you want a modal window
     * @param icon
     *            The icon to display. Use MessageBox.ICON....
     * @param message
     *            A JComponent to be displayed in the message area
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked. Note only works for
     *         Modal displays.
     */
    public static boolean alert(Frame frame, String title, boolean modal, int icon, JComponent message,
            int defaultButton) {
        XMessageBox m = new XMessageBox(frame, title, modal, icon, message, OKCANCEL, defaultButton);
        return m.whatWasClicked();
    }

    /**
     * This method offers the second most flexibile access to the message box.
     * 
     * @param dialog
     *            The name of the containing dialog, can be null
     * @param title
     *            The title to appear on the dialog box
     * @param modal
     *            Whether or not you want a modal window
     * @param icon
     *            The icon to display. Use MessageBox.ICON....
     * @param message
     *            A string for the message to display. Use "\n" for new lines
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked. Note only works for
     *         Modal displays.
     */
    public static boolean alert(Dialog dialog, String title, boolean modal, int icon, String message, int buttonType,
            int defaultButton) {
        XMessageBox m = new XMessageBox(dialog, title, modal, icon, message, buttonType, defaultButton);
        return m.whatWasClicked();
    }

    /**
     * This method offers the second most flexibile access to the message box.
     * 
     * @param frame
     *            The name of the containing dialog, can be null
     * @param title
     *            The title to appear on the dialog box
     * @param modal
     *            Whether or not you want a modal window
     * @param icon
     *            The icon to display. Use MessageBox.ICON....
     * @param message
     *            A string for the message to display. Use "\n" for new lines
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked. Note only works for
     *         Modal displays.
     */
    public static boolean alert(Frame frame, String title, boolean modal, int icon, String message, int buttonType,
            int defaultButton) {
        XMessageBox m = new XMessageBox(frame, title, modal, icon, message, buttonType, defaultButton);
        m.setAlwaysOnTop(true);
        return m.whatWasClicked();
    }

    /**
     * This method offers the second most flexibile access to the message box.
     * 
     * @param frame
     *            The name of the containing frame, can be null
     * @param title
     *            The title to appear on the dialog box
     * @param modal
     *            Whether or not you want a modal window
     * @param icon
     *            The icon to display. Use MessageBox.ICON....
     * @param message
     *            A string for the message to display. Use "\n" for new lines
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked. Note only works for
     *         Modal displays.
     */
    public static boolean alert(Frame frame, String title, boolean modal, int icon, String message, int defaultButton) {
        XMessageBox m = new XMessageBox(frame, title, modal, icon, message, OKCANCEL, defaultButton);
        return m.whatWasClicked();
    }

    /**
     * A simple interface to the message box allowing the user to specify only
     * the message and the default button. <BR>
     * The title for the window will be "Please Confirm Your Action" and it will
     * be a modal window. <BR>
     * A warning icon will be displayed.
     * 
     * @param message
     *            A string for the message to display. Use "\n" for new lines
     * @param defaultButton
     *            The default button. Use MessageBox.DEFAULTOK OR
     *            MessageBox.DEFAULTCANCEL
     * @return True if OK clicked, False if cancel clicked.
     */
    public static boolean alert(String message, int defaultButton) {
        Frame frame = null;
        XMessageBox m = new XMessageBox(frame, "Please Confirm Your Action", true, ICONWARNING, message, OKCANCEL,
                defaultButton);
        return m.whatWasClicked();
    }

    /**
     * A simple interface to the message box allowing the user to specify only
     * the message to be displayed. <BR>
     * The title for the window will be "Please Confirm Your Action", it will be
     * a modal window<BR>
     * and OK will be the default button. <BR>
     * A warning icon will be displayed.
     * 
     * @param message
     *            A string for the message to display. Use "\n" for new lines
     * @return True if OK clicked, False if cancel clicked.
     */
    public static boolean alert(String message) {
        Frame frame = null;
        XMessageBox m = new XMessageBox(frame, "Please Confirm Your Action", true, ICONWARNING, message, OKCANCEL,
                DEFAULTOK);
        return m.whatWasClicked();
    }

    public static boolean alert(String title, int icon, String message) {
        Frame frame = null;
        XMessageBox m = new XMessageBox(frame, title, true, icon, message, OKCANCEL, DEFAULTOK);
        return m.whatWasClicked();
    }
    
    public static boolean inform(Frame frame, String title, int icon, String message) {
        XMessageBox m = new XMessageBox(frame, title, true, icon, message, OK_ONLY, DEFAULTOK);
        return m.whatWasClicked();
    }
    
    public static boolean inform(String title, int icon, String message) {
        Frame frame = null;
        return inform(frame, title, icon, message);
    }

    private boolean whatWasClicked() {
        return mainBody.getButtonClicked();
    }

    private void displayDialog() {
        mainPanel.setMinimumSize(new Dimension(500, 920));
        pack();
        centreDialog();
        this.setVisible(true);
        this.validate();
    }

    private void jbInit(int icon, JComponent message) {
        mainBody = new BodyPanel();
        mainBody.setLayout(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 10, 10);
        GridBagConstraints gbc2 = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 10, 10);
        mainBody.add(getIcon(icon), gbc1);
        mainBody.add(message, gbc2);
        addBodyPanel(mainBody);
    }
 
    private JLabel getIcon(int icon) {
        Icon thisImage = null;
        JLabel jl;
        UIDefaults defaults = UIManager.getDefaults();

        switch (icon) {
        case (0):
            break;
        case (1):
            thisImage = defaults.getIcon("OptionPane.informationIcon");
            break;
        case (2):
            thisImage = defaults.getIcon("OptionPane.warningIcon");
            break;
        case (3):
            thisImage = defaults.getIcon("OptionPane.errorIcon");
            break;
        case (4):
            thisImage = defaults.getIcon("OptionPane.questionIcon");
            break;
        default:
            break;
        }
        if (thisImage != null) {
            jl = new JLabel(thisImage);
        } else {
            jl = new JLabel();
        }
        return jl;
    }

    public void okClicked(ActionEvent ae) throws Exception {
        setButtonClicked(OkCancelPanel.OK);
        super.okClicked(ae);
    }

    public void cancelClicked(ActionEvent ae) throws Exception {
        setButtonClicked(OkCancelPanel.CANCEL);
        super.cancelClicked(ae);
    }

    public void applyClicked(ActionEvent ae) {
        // Not required for this implementation
    }

    private void setButtonClicked(int buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    public int getButtonClicked() {
        return buttonClicked;
    }

    class BodyPanel extends XPanel {

        private static final long serialVersionUID = 1L;
        protected boolean buttonClicked;

        protected boolean getButtonClicked() {
            return buttonClicked;
        }

        public void stepInitialise() {
            // empty
        }

        public void stepActivate() {
            // empty
        }

        public void stepUpdateViewState() {
            // empty
        }

        public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException {
            // empty
        }

        public void stepDeactivate() {
            // empty
        }

        public void stepDeinitialise(boolean update) {
            buttonClicked = update;
        }
    }
}