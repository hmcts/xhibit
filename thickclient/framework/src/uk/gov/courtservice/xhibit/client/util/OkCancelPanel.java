package uk.gov.courtservice.xhibit.client.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: An OK and Cancel Panel<br>
 * The class using this panel must implement OkCancelPanelConsumer to link
 * actions to the buttons.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche Updated by R.Lakhani - removed action
 *         listener and added XActions
 * @version 1.0
 */

public class OkCancelPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public JButton okButton;

    public JButton cancelButton;

    public static final int OK = 0;

    public static final int CANCEL = 1;

    protected XAction okAction = null;

    protected XAction cancelAction = null;

    protected JDialog myContainer;

    protected int defaultButton;

    public OkCancelPanel(JDialog containingPanel, int defaultButton) {
        try {
            this.myContainer = containingPanel;
            this.defaultButton = defaultButton;
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public OkCancelPanel(JDialog containingPanel) {
        this(containingPanel, OK);
    }

    public XAction getOkAction() {
        if (okAction == null) {
            okAction = new OkAction((OkCancelPanelConsumer) this.myContainer);
        }
        return okAction;
    }

    public XAction getCancelAction() {
        if (cancelAction == null) {
            cancelAction = new CancelAction((OkCancelPanelConsumer) this.myContainer);
        }
        return cancelAction;
    }

    protected void setDefaultButton() {
        this.myContainer.getRootPane().setDefaultButton(getDefaultButton());
    }

    protected void init() {
        addButtons();
        setDefaultButton();
    }

    private void addButton(JButton btn, GridBagConstraints gbc) {
        this.add(btn, gbc);
    }

    protected void addButtons() {
        this.setLayout(new GridBagLayout());
        this.add(new JLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        GridBagConstraints gbc1 = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
        GridBagConstraints gbc2 = new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

        okButton = new JButton();
        okButton.setAction(getOkAction());
        this.okButton.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                setDefaultButton();
            }

            public void focusGained(FocusEvent e) {
                // empty
            }
        });
        okButton.setMnemonic(getOkAction().getMnemonicKey().intValue());
        // this.add(this.okButton, gbc);

        cancelButton = new JButton();
        cancelButton.setVerifyInputWhenFocusTarget(false);
        cancelButton.setAction(getCancelAction());
        this.cancelButton.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                setDefaultButton();
            }

            public void focusGained(FocusEvent e) {
                // empty
            }
        });
        // this.add(this.cancelButton, gbc);

        switch (defaultButton) {
        case (OK):
            addButton(okButton, gbc1);
            addButton(cancelButton, gbc2);

            break;
        case (CANCEL):
            addButton(cancelButton, gbc2);
            addButton(okButton, gbc1);
            break;
        default:
            addButton(okButton, gbc1);
            addButton(cancelButton, gbc2);
        }
        setDefaultButton();
    }

    public JButton getDefaultButton() {
        JButton returnButton = null;
        switch (defaultButton) {
        case (OK):
            returnButton = okButton;
            break;
        case (CANCEL):
            returnButton = cancelButton;
            break;
        }
        return returnButton;
    }

    /**
     * Sets the text of the OK button
     * 
     * @param text
     */
    public void setOkText(String text) {
        this.okButton.setText(text);
    }

    /**
     * Sets a tool tip on the OK button
     * 
     * @param tooltip
     */
    public void setOkToolTip(String tooltip) {
        this.okButton.setToolTipText(tooltip);
    }

    /**
     * Sets the text of the Cancel button
     * 
     * @param text
     */
    public void setCancelText(String text) {
        this.cancelButton.setText(text);
    }

    /**
     * Sets a tool tip on the Cancel button
     * 
     * @param tooltip
     */
    public void setCancelToolTip(String tooltip) {
        this.cancelButton.setToolTipText(tooltip);
    }

    class OkAction extends XAction {

        private static final long serialVersionUID = 1L;
        OkCancelPanelConsumer myContainer;

        public OkAction(OkCancelPanelConsumer container) {
            populateFromBundle("btnOk");
            myContainer = container;
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
            myContainer.okClicked(ae);
        }
    }

    class CancelAction extends XAction {

        private static final long serialVersionUID = 1L;
        OkCancelPanelConsumer myContainer;

        public CancelAction(OkCancelPanelConsumer container) {
            populateFromBundle("btnCancel");
            myContainer = container;
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
            myContainer.cancelClicked(ae);
        }
    }
}