package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;


/**
 * <p>
 * Title: Xhibit2 OrderSentPanel
 * </p>
 * <p>
 * Description: Captures order sented data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderSentConfirmedPanel  extends uk.gov.courtservice.xhibit.client.util.XPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(OrderSentConfirmedPanel.class);

    private static final int screenWidth = 100;

    private JLabel lblMessage = new JLabel();

    protected Dimension d = new Dimension(screenWidth, 330);

    private Color foregroundColorWhite = Color.white;


    private JPanel containingPanel;

    public OrderSentConfirmedPanel() {
        containingPanel = new JPanel(new GridBagLayout()) {

            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g); // paint background
            }

        };
        GridBagConstraints gbc;

        setLayout(new GridBagLayout());

        // Messages
        gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                XHIBITConstant.rootContainerInsets, 0, 0);
        lblMessage.setForeground(foregroundColorWhite);
        containingPanel.add(lblMessage, gbc);

        containingPanel.setPreferredSize(d);
        this.add(containingPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        setVisible(true);
    }

 
    public OrderSentConfirmedPanel(XDialog d) {
        this();
        //((ApplyOkCancelPanel) d.getButtonPanel()).applyButton.setAction(new ShowMoreAction(d));
    }

    public void setStatus(String message) {
        lblMessage.setText(message);
    }


    // Empty life cycle methods
    public void stepInitialise() {
        // empty
    }

    public void stepActivate() {
        // empty
    }

    public void stepUpdateViewState() {
        // empty
    }

    public void stepValidate() {
        // empty
    }

    public void stepDeactivate() {
        // empty
    }

    public void stepDeinitialise(boolean boolean0) {
        // empty
    }

}