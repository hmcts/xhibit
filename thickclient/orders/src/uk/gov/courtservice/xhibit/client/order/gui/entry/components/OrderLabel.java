package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderLabel
 * </p>
 * <p>
 * Description: Class to construct a text label
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class OrderLabel extends AbstractOrderComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(OrderLabel.class);

    protected OrderPanel panel;

    private GridBagConstraints constraints;
    
    private JLabel label;

    /**
     * Constructor
     */
    public OrderLabel() {
        this.constraints = getDefaultConstraints();
    }

    /**
     * Create order label - organise in a GridBagLayout
     */
    public void initComponent() {
        log.debug("Create Label");

        panel = new OrderPanel();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(panel, constraints);
        
        String labelText = getHelper().getAttribute("label");
        label = new JLabel(labelText != null ? labelText : "");
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        add(label, constraints);
        	  	
        panel.setVisible(true);
        panel.setEnabled(true);
       
        setVisualComponent(this);
    }
    
     public boolean isLabelled() {
        return false;
    }
        
    public Frame getFrame() {
    	return getParentFrame(getParent());
    }

    /**
     * Get the component that is displayed
     * 
     * @return The JPanel that contains the visual components.
     */
    public JComponent getVisualLeafComponent() {
        return panel;
    }

    public void setEnabled(boolean enable) {
        if (isInitialised()) {
        	label.setEnabled(enable);
        	panel.setEnabled(enable);
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// No action required
	}
}
