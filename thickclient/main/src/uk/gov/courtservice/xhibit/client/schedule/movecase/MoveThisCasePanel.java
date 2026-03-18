package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.schedule.CourtRoomListCellRenderer;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;

/**
 * <p>
 * Title: XHIBIT Prototype
 * </p>
 * <p>
 * Description: Show Move This Case selections panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public class MoveThisCasePanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JRadioButton adjournedRb;

    private JRadioButton newTimeTodayRb;

    private JPanel fillerPanel = new JPanel();

    private JComboBox courtCb;

    private JLabel courtLabel;

    private JLabel timeLabel;

    private XTimePanel xTimePanel;

    private JPanel timeCourtPanel;

    private TitledBorder titledBorder1;

    private ButtonGroup moveTypeGroup;

    private ResourceBundle myResource;

    private XPanel parentPanel;

    public MoveThisCasePanel(MoveCaseWiz1 parentPanel, ResourceBundle resource) {
        this.parentPanel = parentPanel;
        myResource = resource;
        jbInit();

        // set ButtonGroup
        moveTypeGroup = new ButtonGroup();
        moveTypeGroup.add(getAdjournedRb());
        moveTypeGroup.add(getNewTimeTodayRb());
    }

    void jbInit() {
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)),
                "Move This Case");
        this.setLayout(gridBagLayout1);
        this.setBorder(titledBorder1);
        // new time RB
        this.add(fillerPanel, new GridBagConstraints(0, 1, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 3));
        this.add(getNewTimeTodayRb(), new GridBagConstraints(1, 1, 1, 1, 0.6, 0.3, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 20, 0));

        // time court
        this.add(fillerPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 3));
        this.add(getTimeCourtPanel(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 3));

        // adjourn RB
        this.add(fillerPanel, new GridBagConstraints(0, 4, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 3));
        this.add(getAdjournedRb(), new GridBagConstraints(1, 4, 1, 1, 0.3, 0.3, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 20, 0));

        // this.add(fillerPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        // ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
        // 0, 0), 0, 3));
        // this.add(fillerPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        // ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
        // 0, 0), 0, 0));
    }

    public JPanel getTimeCourtPanel() {
        if (timeCourtPanel == null) {
            timeCourtPanel = new JPanel();
            timeCourtPanel.setLayout(gridBagLayout1);
            timeCourtPanel.add(getTimeLabel(), new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
            timeCourtPanel.add(getXTimePanel(), new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
            timeCourtPanel.add(getCourtLabel(), new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
            timeCourtPanel.add(getCourtCb(), new GridBagConstraints(3, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        }
        return timeCourtPanel;
    }

    private JLabel getTimeLabel() {
        if (timeLabel == null) {
            timeLabel = new JLabel(XHIBITConstant.getResource(myResource, "time"));
        }
        return timeLabel;
    }

    public XTimePanel getXTimePanel() {
        if (xTimePanel == null) {
            xTimePanel = new XTimePanel(parentPanel);
            xTimePanel.setToolTipText(XHIBITConstant.getResource(myResource, "ttTimePanel"));
        }
        return xTimePanel;
    }

    private JLabel getCourtLabel() {
        if (courtLabel == null) {
            courtLabel = new JLabel(XHIBITConstant.getResource(myResource, "moveCaseCourtRoom"));
        }
        return courtLabel;
    }

    public JComboBox getCourtCb() {
        if (courtCb == null) {
            courtCb = new JComboBox();
            // courtCb.setMinimumSize(new Dimension(80,
            // XHIBITConstant.getLineHeight()));
            courtCb.setToolTipText(XHIBITConstant.getResource(myResource, "ttCourtRoomCb"));
            courtCb.setRenderer(new CourtRoomListCellRenderer());

            courtCb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    try {
                        parentPanel.stepUpdateViewState();
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            });
        }
        return courtCb;
    }

    public JRadioButton getNewTimeTodayRb()// Button Group
    {
        if (newTimeTodayRb == null) {
            newTimeTodayRb = new JRadioButton(XHIBITConstant.getResource(myResource, "newTimeToday"));
            newTimeTodayRb.setToolTipText(XHIBITConstant.getResource(myResource, "ttNewTimeTodayRb"));
            newTimeTodayRb.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    try {
                        parentPanel.stepUpdateViewState();
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            });
        }
        return newTimeTodayRb;
    }

    public JRadioButton getAdjournedRb() // Button Group
    {
        if (adjournedRb == null) {
            adjournedRb = new JRadioButton(XHIBITConstant.getResource(myResource, "adjourned"));
            adjournedRb.setToolTipText(XHIBITConstant.getResource(myResource, "ttAdjournedRb"));
            adjournedRb.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    try {
                        parentPanel.stepUpdateViewState();
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            });
        }
        return adjournedRb;
    }
}
