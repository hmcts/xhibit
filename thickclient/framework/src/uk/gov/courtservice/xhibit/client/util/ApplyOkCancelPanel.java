package uk.gov.courtservice.xhibit.client.util;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: An Apply, OK and Cancel Panel<br>
 * The class using this panel must implement ApplyOkCancelPanelConsumer to link
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

public class ApplyOkCancelPanel extends OkCancelPanel {
    protected XAction applyAction = null;

    public JButton applyButton;

    public static final int APPLY = 2;

    /**
     * @param containingPanel -
     *            Supply the container that will have ApplyOkCancelPanelConsumer
     *            implemented
     * @param defaultButton -
     *            one of the static variables available
     */
    public ApplyOkCancelPanel(JDialog containingPanel, int defaultButton) {
        super(containingPanel, defaultButton);
    }

    /**
     * OK will be the defauly button
     * 
     * @param containingPanel -
     *            Supply the container that will have ApplyOkCancelPanelConsumer
     *            implemented
     */
    public ApplyOkCancelPanel(JDialog containingPanel) {
        this(containingPanel, OK);
    }

    public XAction getApplyAction() {
        if (applyAction == null) {
            applyAction = new ApplyAction((ApplyOkCancelPanelConsumer) this.myContainer);
        }
        return applyAction;
    }

    protected void setDefaultButton(int thisButton) {
        super.setDefaultButton();
        // setButtonState(applyButton, thisButton, APPLY);
    }

    protected void addButtons() {
        super.addButtons();
        GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
        this.applyButton = new JButton();
        this.applyButton.setAction(getApplyAction());

        this.applyButton.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                setDefaultButton(defaultButton);
            }

            public void focusGained(FocusEvent e) {
            }
        });
        applyButton.setMnemonic(getApplyAction().getMnemonicKey().intValue());
        this.add(this.applyButton, gbc);
    }

    public JButton getDefaultButton() {
        JButton returnButton = super.getDefaultButton();
        switch (defaultButton) {
        case (APPLY):
            returnButton = applyButton;
            break;
        }
        return returnButton;
    }

    /**
     * Sets a tool tip on the Apply button
     * 
     * @param tooltip
     */
    public void setApplyToolTip(String tooltip) {
        this.applyButton.setToolTipText(tooltip);
    }

    class ApplyAction extends XAction {
        ApplyOkCancelPanelConsumer myContainer;

        public ApplyAction(ApplyOkCancelPanelConsumer container) {
            populateFromBundle("btnApply");
            myContainer = container;
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
            myContainer.applyClicked(ae);
        }
    }
}