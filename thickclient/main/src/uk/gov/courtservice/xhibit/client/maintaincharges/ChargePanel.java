package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class ChargePanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JPanel titlePanel = null;

    private JPanel mainPanel = null;

    private JLabel titleLabel = null;

    private ChargesSelectionModel csm = null;

    public ChargePanel(ChargesSelectionModel csm) {
        try {
            this.csm = csm;
            jbInit();
        } catch (CSRecoverableException e) {
            XHIBITConstant.handleError(e);
        }
    }

    private void jbInit() throws CSRecoverableException {
        setLayout(gridBagLayout1);

        add(getTitlePanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        add(getMainPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        setEnabled(false);
    }

    private JPanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new JPanel();
            titlePanel.setLayout(gridBagLayout1);

            titlePanel.add(getTitleLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }
        return titlePanel;
    }

    protected JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
        }
        return titleLabel;
    }

    protected JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(gridBagLayout1);
        }
        return mainPanel;
    }

    public ChargesSelectionModel getChargeSelectionModel() {
        return csm;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            titleLabel.setForeground(SystemColor.activeCaptionText);
            titlePanel.setBackground(SystemColor.activeCaption);
        } else {
            titleLabel.setForeground(SystemColor.inactiveCaptionText);
            titlePanel.setBackground(SystemColor.inactiveCaption);
        }
    }
}