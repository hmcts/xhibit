package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderSection
 * </p>
 * <p>
 * Description: Class to construct a panel (dataentry) that allows the user to
 * change checkbox values
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil & Neil
 * @version 1.0
 */
public class OrderSection extends AbstractOrderComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(OrderSection.class);

    protected OrderPanel panel;

    private GridBagConstraints constraints;

    protected MultiLineCheckbox jCheckBoxCbx;

    protected TitledBorder border;

    /**
     * Return the check box
     * 
     * @return the check box
     */
    public JCheckBox getjCheckBoxCbx() {
        return jCheckBoxCbx;
    }

    /**
     * Constructor
     */
    public OrderSection() {
        this.constraints = getDefaultConstraints();
    }

    /**
     * Create order checkboxes and checkbox labels - organise in a GridBagLayout
     */
    public void initComponent() {
        log.debug("Create Section");
        String borderReq = getHelper().getAttribute("borderReq");
        if (borderReq == null) {
            labelBorder();
        }

        panel = new OrderPanel();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(panel, constraints);

        if (isSwitchable()) {
            // Assign label name to a string linked to xml attribute
            String switchLabel = getHelper().getAttribute("switchLabel");
            String value = getHelper().getValue();
            boolean bool = false;
            if (value != null) {
                // Set the checkbox depending on what is read from the xml
                bool = value.equalsIgnoreCase(getHelper().getAttribute("selectedValue"));
            } else {
                bool = getHelper().getAttribute("selectedValue").equalsIgnoreCase("true");
            }
            // Create a new label and checkbox
            jCheckBoxCbx = new MultiLineCheckbox(switchLabel != null ? switchLabel : "", bool);
            
            jCheckBoxCbx.addActionListener(this);
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.WEST;
            
            add(jCheckBoxCbx, constraints);
            panel.setEnabled(bool);
  
        } else { 
        	 
         	panel.setVisible(true);
         	
         	panel.setEnabled(true);
        }

       
        setVisualComponent(this);
    }

    public boolean isSwitchable() {
    	return "true".equalsIgnoreCase(getHelper().getAttribute("switchable"));
    }
    
    /**
     * Create outer and inner dataentry borders. Outer borders use
     * RaisedBevelLevel borders, whilst inner borders use EtchedBorders.
     */
    private void labelBorder() {
        String borderLabel = getHelper().getAttribute("borderLabel");
        String titleRaised = getHelper().getAttribute("borderRaised");
        if (borderLabel != null) {
            if ((titleRaised != null) && (titleRaised.equals("true"))) {
                border = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), borderLabel);
                this.setBorder(border);
            } else {
                border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), borderLabel);
                this.setBorder(border);
            }
        }
    }

    /**
     * Method returns false, indicating that all checkboxes are unselected
     * 
     * @return false to ensure buttons are initially unselected
     */

    public boolean isLabelled() {
        return false;
    }
        
    /**
     * Checks the state of the checkbox , i.e. true or false, and assigns the
     * value to xml placeholders
     * 
     * @param e
     */
    @Override
	public void actionPerformed(ActionEvent event) {
    	log.debug("State Changed");
    	boolean selected = getjCheckBoxCbx().isSelected();
        if (isInitialised() && isReadOnly()) {
    		if (showConfirmOverrideMsg(getFrame())) {
    			setValue(selected);
    		} else {
    			revertValue(!selected);
    		}
    	} else {
    		setValue(selected);
    	}
    }
    

    private void revertValue(boolean selected) {
    	getjCheckBoxCbx().setSelected(selected);
    }

    public Frame getFrame() {
    	return getParentFrame(getParent());
    }

    private void setValue(boolean selected) {
        panel.setEnabled(selected);
        this.validate();
        if (getHelper().getOrderDataReference() != null) {
            if (selected) {
                log.debug("Selected");
                String selectedValue = getHelper().getAttribute("selectedValue");
                getHelper().setValue(selectedValue);
            } else {
                log.debug("Unselected");
                String unselectedValue = getHelper().getAttribute("unselectedValue");
                getHelper().setValue(unselectedValue);
            }
        }

        if (this.jCheckBoxCbx.isSelected()) {
            log.debug("CB Selected");
            this.switchValidation(true);
        } else {
            log.debug("CB Unselected");
            this.switchValidation(false);
        }
    }
        
    /**
     * Get the component that is displayed
     * 
     * @return The JPanel that contains the visual components.
     */
    public JComponent getVisualLeafComponent() {
        return panel;
    }

    /**
     * Enable the components contained within the section
     * 
     * @param enable
     */
    public void setEnabled(boolean enable) {
    	if (isD20Order() && !isReadOnly()) {
        	enable = true;
        } else if(this.isInputLocked()) {
    		enable = false;
    	}
        if (isInitialised()) {
        	if (jCheckBoxCbx != null) {
                this.jCheckBoxCbx.setEnabled(enable);
                panel.setEnabled(enable  && this.jCheckBoxCbx.isSelected());
                setSelectedValue();
            } else {
                panel.setEnabled(enable);
                // Bit of a hack to make the border title look as though
                // it has been disabled.
                if (border != null) {
                    border.setTitleColor(enable ? Color.black : Color.gray);
                }
            }
        }
    }
    
    /**
     * Set the appropriate value on the dom
     */
    private void setSelectedValue() {
        if (getHelper().getOrderDataReference() != null) {
            if (this.jCheckBoxCbx.isSelected()) {
                log.debug("Selected");
                String selectedValue = getHelper().getAttribute("selectedValue");
                getHelper().setValue(selectedValue);
            } else {
                log.debug("Unselected");
                String unselectedValue = getHelper().getAttribute("unselectedValue");
                getHelper().setValue(unselectedValue);
            }
        }
    }
    
    public class MultiLineCheckbox extends JCheckBox {

		private static final long serialVersionUID = 1L;
		private String originalText;
    	
    	public MultiLineCheckbox(String text, boolean bool) {
    		super(text, bool);
    		originalText = text;
    		setMultilineText();
    	}
    	
    	@Override
    	public void setEnabled(boolean b) {
    		super.setEnabled(b);
    		setMultilineText();
    	}

    	private void setMultilineText() {
    		if (isMultilineText(originalText)) {
    			this.setText(addLineBreaks(originalText, isEnabled()));
    		}
    	}
    	
    }
}
