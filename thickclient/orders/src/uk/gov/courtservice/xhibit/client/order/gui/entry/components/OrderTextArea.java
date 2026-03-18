package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomScrollPane;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomTextArea;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;

/**
 * <p>
 * Title: OrderTextArea
 * </p>
 * <p>
 * Description: Class to utilise a CustomTextArea to provide a multi-lined view
 * of plain text, supporting tab traversal.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan, Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class OrderTextArea extends AbstractOrderComponent implements KeyListener, FocusListener {
    private static final int DEFAULT_DOC_LIMIT = 1000;

    private CustomTextArea textArea;

    private boolean labelled = false;

    private String defaultValue = "NOVAL";

    /**
     * Constructs a new CustomTextArea with the specified text and number of
     * rows and columns. These attributes are obtained via the
     * OrderComponentHelper class. The text area always has a vertical scroll
     * bar.
     */
    public void initComponent() {
        // if no maxlength has been set in the template xml, set to default
    	
    	if(getHelper().getValue()!=null)
    	{
    		defaultValue = (String) getHelper().getValue();
    	}
        int xmlLimit = getHelper().getAttributeAsInt("maxlength");
        int textLimit = xmlLimit == 0 ? DEFAULT_DOC_LIMIT : xmlLimit;
        if(getHelper().getValue()!=null)
    	{
	        textArea = new CustomTextArea(new LimitedTextDocument(textLimit), defaultValue, getHelper()
	                .getAttributeAsInt("rows"), getHelper().getAttributeAsInt("cols"));
    	}
        else 
        {
        	 textArea = new CustomTextArea(new LimitedTextDocument(textLimit), "", getHelper()
 	                .getAttributeAsInt("rows"), getHelper().getAttributeAsInt("cols"));
        }
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true); // Des Johnston SCR 52686
        textArea.setBorder(BorderFactory.createLineBorder(Color.gray));
        CustomScrollPane scroll = new CustomScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints constraints = getDefaultConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        String label = getHelper().getAttribute("topLabel");
        if (label == null) {
            labelled = true;
            scroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        } else {
            add(new JLabel(label), constraints);
            constraints.gridy++;
        }
        add(scroll, constraints);
        setVisualComponent(this);
        
        textArea.addFocusListener(this);
        textArea.addKeyListener(this);
        
        String readOnlyAttrib = getHelper().getAttribute("readonly");
        if (readOnlyAttrib != null) {
        	textArea.setEditable(!"true".equalsIgnoreCase(readOnlyAttrib));
        }else if(isD20Order())
        {
        	textArea.setEditable(false);
        }
    }

    /**
     * Invoked when a component loses the keyboard focus and changes the value
     * of the name/value pair corresponding to the text area.
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusLost(FocusEvent event) {
        getHelper().setValue(getValueOrDefault(textArea.getText()));
    }

    /**
     * Empty implementation
     * 
     * @param event
     */
    public void keyTyped(KeyEvent event) {
        // No implementation
    }

    /**
     * Empty implementation
     * 
     * @param event
     */
    public void keyPressed(KeyEvent event) {
        // No implementation
    }

    /**
     * Empty implementation
     * 
     * @param event
     */
    public void keyReleased(KeyEvent event) {
        // No implementation
    }

    /**
     * OrderTextAreas have the label defined in the XML. We do not want any
     * additional lebels for these components.
     * 
     * @return boolean
     */
    public boolean isLabelled() {
        return labelled;
    }

    /**
     * Sets the caret position to the first (left) character
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusGained(FocusEvent event) {
        this.textArea.setCaretPosition(0);
    }

    /**
     * Check if the default value has been deleted - if so restore
     * 
     * @param value
     *            the string to be saved
     * @return the chnaged string or the default value
     */
    private String getValueOrDefault(String value) {
        return isEmptyString(value) ? this.defaultValue : value;
    }

    /**
     * Check if the string is empty
     * 
     * @param s
     *            the string to check
     * @return true if string is empty
     */
    private boolean isEmptyString(String s) {
        return s.length() == 0;
    }

}
