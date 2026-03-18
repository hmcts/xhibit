package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * This panel is used by XWizardDialog
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

public class WizardButtonPanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JButton btnNext = new JButton();

    private JButton btnBack = new JButton();

    private JButton btnFinish = new JButton();

    private JButton btnCancel = new JButton();

    private Insets inset = uk.gov.courtservice.xhibit.client.util.XHIBITConstant.nonContainerInsets;

    private JPanel etchedLine = new JPanel();

    public WizardButtonPanel() {
        jbInit();
    }

    void jbInit() {
        this.setLayout(gridBagLayout1);
        // Create text, but should be over-riden by actions
        btnNext.setText("Next >");
        btnBack.setText("< Back");
        btnFinish.setText("Finish");
        btnCancel.setText("Cancel");
        btnCancel.setVerifyInputWhenFocusTarget(false);

        // Create an etched line between panel and buttons.
        etchedLine.setBorder(BorderFactory.createEtchedBorder());
        etchedLine.setPreferredSize(new Dimension(2, 2));
        etchedLine.setMinimumSize(new Dimension(0, 0));

        this.add(etchedLine, new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, inset, 0, 0));
        this.add(uk.gov.courtservice.xhibit.client.util.XHIBITConstant.getSpacer(), new GridBagConstraints(0, 0, 1, 1,
                1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, inset, 0, 0));
        this.add(new JPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, inset, 0, 0));
        this.add(btnNext, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, inset, 0, 0));
        this.add(btnFinish, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, inset, 0, 0));
        this.add(btnBack, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, inset, 0, 0));
        this.add(btnCancel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, inset, 0, 0));
    }

    /**
     * Get a reference to the back button
     * 
     * @return JButton
     */
    public JButton getBack() {
        return btnBack;
    }

    /**
     * Get a reference to the Cancel button
     * 
     * @return JButton
     */
    public JButton getCancel() {
        return btnCancel;
    }

    /**
     * Get a reference to the Finish button
     * 
     * @return JButton
     */
    public JButton getFinish() {
        return btnFinish;
    }

    /**
     * Get a reference to the Next button
     * 
     * @return JButton
     */
    public JButton getNext() {
        return btnNext;
    }

}