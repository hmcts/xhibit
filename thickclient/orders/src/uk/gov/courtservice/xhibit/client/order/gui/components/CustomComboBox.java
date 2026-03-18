package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListFactory;

/**
 * <p>
 * Title: CustomComboBox
 * </p>
 * <p>
 * Description: A custom ComboBox class that contains court names
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */

public class CustomComboBox extends JComboBox {
    private CourtList courtNames;

    private Dimension courtSize = new Dimension();
    
    private boolean inputLocked=false;

    /**
     * Creates an editable CustomComboBox to display court names
     * 
     * @param list
     *            the object that contains court names
     */
    public CustomComboBox(CourtList list) {
        super(list.getOrderTypes());
        this.setEditable(true);
        this.courtNames = list;
        courtSize.setSize(250, 22);
        this.setPreferredSize(courtSize);
        
        
    }

    /**
     * Creates a CustomComboBox and instantiates the courtlist object
     */
    public CustomComboBox() {
        this(CourtListFactory.createCourtList());
    }
    
    

    /**
     * Method to return court names from the courtlist object
     * 
     * @return courtlist
     */
    public CourtList getCourtNames() {
        return this.courtNames;
    }

    /**
     * Method to set the values for the courtlist object
     * 
     * @param list
     *            the object that contains court names
     */
    public void setCourtNames(CourtList list) {
        this.courtNames = list;
    }

    /**
     * Method that checks if the combobox is a order type, if so then resize the
     * combox, i.e. make it smaller when displaying order types.
     * 
     * @param orderTypeValue
     *            the object that contains court names
     */
    public void checkOrderType(String orderTypeValue) {
        if (orderTypeValue.equals("true")) {
            this.courtSize.setSize(85, 22);
            this.setPreferredSize(courtSize);
        }
    }

	public boolean isInputLocked() {
		return inputLocked;
	}

	public void setInputLocked(boolean inputLocked) {
		this.inputLocked = inputLocked;
	}
	
	public void setEnabled(boolean enabled){
		if(inputLocked)
			enabled = false;
		
		super.setEnabled(enabled);
	}
    

}
