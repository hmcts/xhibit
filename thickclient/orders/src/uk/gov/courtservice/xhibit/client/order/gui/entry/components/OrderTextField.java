package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.TextLimitedJTextField;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers.OrderInputVerifier;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers.OrderVerifierFactory;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListMidTier;

/**
 * <p>
 * Title: OrderTextArea
 * </p>
 * <p>
 * Description: Class utilising a JTextField for single-line editing of plain
 * text.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan, Desmon Johnston, Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class OrderTextField extends AbstractOrderComponent {
    private static final Logger log = CSServices.getLogger(OrderTextField.class);

    private TextLimitedJTextField textFieldText;

    private String numeric = null;
    String value = "NOVAL";

    /**
     * Constructs a new JTextField with text and length defined from xml
     * parameter accessible from the OrderComponentHelper class.
     */
    public void initComponent() {
    	if(getHelper().getValue()!=null)
    	{
    		value = (String) getHelper().getValue();
    	}
    	
        log.debug("[initComponent]");
        String s = getHelper().getAttribute("length");
        String maxLength = getHelper().getAttribute("maxLength");
        numeric = getHelper().getAttribute("numeric");
        
        
        
        if (getHelper().getValue() != null) {
            if (getHelper().getValue().trim().length() == 0) {
                getHelper().setValue("");
            }
        }

        if ((s != null) && (maxLength != null)) {
            // Determine if Text field is for numeric text entry/display
            if (numeric != null) {
                textFieldText = new TextLimitedJTextField(getHelper().getValue(), getHelper().getAttributeAsInt(
                        "length"), getHelper().getAttribute("maxLength"), getHelper().getAttribute("numeric"));
            } else {
                textFieldText = new TextLimitedJTextField(getHelper().getValue(), getHelper().getAttributeAsInt(
                        "length"), getHelper().getAttribute("maxLength"));
            }
        } else {
            // Determine if Text field is for numeric text entry/display
            if (numeric != null) {
                textFieldText = new TextLimitedJTextField((String) getHelper().getValue(), 10, "10", getHelper().getAttribute("numeric"));
            } else {
                textFieldText = new TextLimitedJTextField((String) getHelper().getValue(), 10, "10");
            }
        }
        
        

        setVisualComponent(textFieldText);

        OrderInputVerifier inputVerifier = setupVerifier(OrderVerifierFactory.createVerifier(getHelper().getAttribute("type")));
        if (getHelper().getAttribute("minLength") != null && new Integer(getHelper().getAttribute("minLength")).intValue() > 0) {
            inputVerifier.setRequired(true);
            inputVerifier.setMinVal(getHelper().getAttribute("minLength"));
        } else {
            inputVerifier.setRequired(false);
        }
        textFieldText.setInputVerifier(new OverridenInputVerifier(inputVerifier) {
        	
        	private boolean validateOverride() {
        		String oldValue = getHelper().getValue();
        		String newValue = textFieldText.getText();
        		if (!newValue.equals(oldValue)) {
	        		if (isInitialised() && isReadOnly()) {
	    	    		if (!showConfirmOverrideMsg(getFrame())) {
	    	    			revertValue();
	    	    			return false;
	    	    		}
	    	    	}
        		}
        		return true;
        	}
        	
        	@Override
            public boolean verify(JComponent input) {
            	if (!validateOverride()) {
    	    		return false;
    	    	}
            	return super.verify(input);
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
            	if (!validateOverride()) {
    	    		return false;
    	    	}
            	return super.shouldYieldFocus(input);
            }
        });
        
        String readOnlyAttrib = getHelper().getAttribute("readonly");
        if(isD20Order()) {
        	textFieldText.setEditable(!isInputLocked());
        } else if (readOnlyAttrib != null) {
        	textFieldText.setEditable(!"true".equalsIgnoreCase(readOnlyAttrib));
        }
        
       
    }

    /**
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public void focusGained(FocusEvent event) {
        this.textFieldText.setCaretPosition(0);
    }

    public Frame getFrame() {
    	return getParentFrame(textFieldText.getParent());
    }
    
    private void revertValue() {
    	textFieldText.setText(getHelper().getValue());
    }
    
    /**
     * Invoked when a component loses the keyboard focus and changes the value
     * of the name/value pair corresponding to the text field.
     * 
     * @param event
     *            a low-level event which indicates that a component has gained
     *            or lost the keyboard focus.
     */
    public OrderInputVerifier setupVerifier(OrderInputVerifier verifier) {
        String text = ((JTextField) getVisualComponent()).getText();

        if (numeric != null) {
            text = verifier.getIntAsString(text);
            verifier.setIsNumeric(numeric.equalsIgnoreCase("Y") ? true : false);
            ((JTextField) getVisualComponent()).setText(text);
        }
        getHelper().setValue(text);
        getVisualComponent().setBorder(UIManager.getBorder("TextField.border"));

        // Start SCR 52805 Used by COMY to force user to enter a value less than
        // 12
        String returnTerm = getHelper().getAttribute("returnTerm");

        if (returnTerm != null && returnTerm.equals("true")) {
            log.debug("The return period has been found *************** " + returnTerm);
            setRangeValues(verifier, "0", "12");
        }
        // End SCR 52805

        // Start SCR 53247 Used by CPO & CPRO - Check to ensure that value
        // within the required range
        String rangeValue = getHelper().getAttribute("valueRange");

        if (rangeValue != null && rangeValue.equals("true")) {
            setRangeValues(verifier);
        }
        // End SCR 53247
        
        // Start check to validate that a D20 order offence code is correct or if it is blank that is ok at this stage
        if (getHelper().getOrderDataReference().contains("ord:OffenceCode")) {
        	ArrayList<String> offenceCodesAL = CourtListMidTier.getInstance().getD20OffenceCodes();
        	
        	// Only set the verifier for the D20 elements if elements of the correct type have been returned
        	if (offenceCodesAL != null && offenceCodesAL.size()>0)  {
        		
        		if(!offenceCodesAL.contains(value))
            	{
        			offenceCodesAL.add(new String());
            	}
        		
        		verifier.setValidValuesList(offenceCodesAL);
        		verifier.setValidateInArray(true);
        	}
        	
        	
        }
        // End D20 offence code check

        verifier.setHelper(getHelper());

        return verifier;
    }

    private void setRangeValues(OrderInputVerifier verifier) {
        String minVal = getHelper().getAttribute("minVal");
        String maxVal = getHelper().getAttribute("maxVal");
        setRangeValues(verifier, minVal, maxVal);
    }

    private void setRangeValues(OrderInputVerifier verifier, String minVal, String maxVal) {
        verifier.setMinVal(minVal);
        verifier.setMaxVal(maxVal);
        verifier.setValidateRange(true);
        verifier.setIsNumeric(true);
    }

    public class OverridenInputVerifier extends InputVerifier {
        private OrderInputVerifier actualInputVerifier;

        public OverridenInputVerifier(OrderInputVerifier actualInputVerifier) {
            this.actualInputVerifier = actualInputVerifier;
        }

        @Override
        public boolean verify(JComponent input) {
        	return actualInputVerifier.verify(input);
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
        	return actualInputVerifier.shouldYieldFocus(input);
        }
    }
}