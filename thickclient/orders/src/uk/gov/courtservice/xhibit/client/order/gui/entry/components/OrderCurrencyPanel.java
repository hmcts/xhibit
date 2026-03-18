/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 16, 2003
 * Time: 8:38:50 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomCurrencyPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * @author David Duncan
 */
public class OrderCurrencyPanel extends AbstractOrderComponent implements FocusListener {
    private static final Logger log = CSServices.getLogger(OrderCurrencyPanel.class);

    private static final String ORDER_CURRENCY_FORMAT_STORE = "order.currency.format.store";

    private static final String ORDER_CURRENCY_FORMAT_DISPLAY = "order.currency.format.display";

    private static DecimalFormat orderCurrencyStoreFormat;

    private static DecimalFormat orderCurrencyDisplayFormat;

    private CustomCurrencyPanel currency;

    private String type = "float";

    private String signLength = "3";

    private String valueLength = "10";

    private String maxLength = "6";

    private String minVal = "0";

    private String required = "true";

    private String curr;

    /*
     * Utility Methods
     */

    /**
     * When we save the currency amount, any commas must be removed, otherwise
     * the XMLValidator will throw an exception. The currency should be
     * reformatted for display.
     * 
     * @param s
     *            The string from which the commas are to be removed.
     * @return The stripped string
     */
    private static String removeCommas(String s) {
        log.debug("removeCommas s = |" + s + "|");
        char[] c = s.toCharArray();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < c.length; i++) {
            if (c[i] != ',') {
                buf.append(c[i]);
            }
        }
        log.debug("removeCommas buf.toString()= |" + buf.toString() + "|");
        return buf.toString();
    }
    
    public void setEnabled(boolean setEnabled)
    {
    	
    	if(isInputLocked())
    		currency.setInputLocked(true);
    	
    	currency.setEnabled(setEnabled);
    }

    /**
     * Return the amount entered in the currency field
     * 
     * @param text
     *            The textfield that holds the data
     * @return float representing the amount
     */
    private static float getAmount(JTextField text) {
        return Float.parseFloat(removeCommas(text.getText()));
    }

    /**
     * Returns the format for currency storage
     * 
     * @return The appropriate DecimalFormat
     */
    private static DecimalFormat getOrderCurrencyStoreFormat() {
        if (orderCurrencyStoreFormat == null) {
            orderCurrencyStoreFormat = new DecimalFormat(getResource(ORDER_CURRENCY_FORMAT_STORE));
        }
        return orderCurrencyStoreFormat;
    }

    /**
     * Returns the format for currency display
     * 
     * @return The appropriate Decimal Format
     */
    private static DecimalFormat getOrderCurrencyDisplayFormat() {
        if (orderCurrencyDisplayFormat == null) {
            orderCurrencyDisplayFormat = new DecimalFormat(getResource(ORDER_CURRENCY_FORMAT_DISPLAY));
        }
        return orderCurrencyDisplayFormat;
    }

    /**
     * Get the resouirce
     * 
     * @param resourceName
     * @return
     */
    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.OrdersClient, resourceName);
    }

    public void initComponent() {
        setUpValidationAttributes();
        String defaultValue = "0.00";
        if (getHelper().getValue() != null) {
        	defaultValue = getHelper().getValue();
        	
        }
        currency = new CustomCurrencyPanel(getCurrency(), getOrderCurrencyDisplayFormat().format(
                Double.parseDouble(defaultValue)), signLength, valueLength, maxLength, type);
       
        String readOnlyAttrib = getHelper().getAttribute("readonly");
        if(isD20Order() && (readOnlyAttrib == null || "true".equalsIgnoreCase(readOnlyAttrib)))
        {
        	currency.setEnabled(false);
        }
        this.setVisualComponent(currency);
        this.setEnabled(false);
        currency.getAmount().addFocusListener(this);
        

    }
    
    
    
    

    private void setUpValidationAttributes() {
        if (getHelper().getAttribute("type") != null) {
            this.type = getHelper().getAttribute("type");
        }
        if (getHelper().getAttribute("signLength") != null) {
            this.signLength = getHelper().getAttribute("signLength");
        }
        if (getHelper().getAttribute("length") != null) {
            this.valueLength = getHelper().getAttribute("length");
        }
        if (getHelper().getAttribute("maxLength") != null) {
            this.maxLength = getHelper().getAttribute("maxLength");
        }
        if (getHelper().getAttribute("required") != null) {
            this.required = getHelper().getAttribute("required");
        }
        if (getHelper().getAttribute("minVal") != null) {
            this.minVal = getHelper().getAttribute("minVal");
        }

        getValidator().setMinVal(minVal);
        getValidator().setRequired(required.equalsIgnoreCase("true"));

    }

    /**
     * Returns a string representing the curreny
     * 
     * @return e.g GBP, EURO
     */
    public String getCurrency() {
        String ref = getHelper().getOrderDataReference();
        String newRef = ref.substring(0, ref.lastIndexOf("/")) + "/ord:Currency";
        log.debug("NEWREF ****************" + newRef);
        curr = getHelper().getValue(newRef);
        if (curr == null) {
        	// Set default to be £ sign
        	curr = "GBP";
        }
        log.debug("CURR ****************" + curr);
        return curr;
    }

    /**
     * Validates the currency field when the focus is lost
     * 
     * @param event
     */
    public void focusLost(FocusEvent event) {
        if (!event.isTemporary()) {
            JTextField text = ((CustomCurrencyPanel) getVisualComponent()).getAmount();
            
            float amount = getAmount(text);
            
            String oldValue = getOrderCurrencyDisplayFormat().format(Double.parseDouble(getHelper().getValue()));            
            String value = getOrderCurrencyStoreFormat().format(amount);
            
            // Value changed
            if (!value.equals(oldValue)) {
            	if (isInitialised() && isReadOnly()) {
            		if (showConfirmOverrideMsg((Frame)getParent())) {
			            setValue(text, value);
            		} else {
            			revertValue(text, oldValue);
            		}
            	} else {
            		setValue(text, value);
            	}
            }
        }
    }

    private void setValue(JTextField text, String value) {
    	if (getValidator().isValid(value)) {
            if (text.getText().length() == 0) {
                text.setText("0");
            }
            getHelper().setValue(value);
            text.setBorder(UIManager.getBorder("TextField.border"));
        } else {
            text.setBorder(BorderFactory.createLineBorder(Color.red));
            text.requestFocus();
        }
    }
    
    private void revertValue(JTextField text, String value) {
    	text.setText(value);
    }
    
    /**
     * Sets the caret position at the start of the field when focus is gained
     * 
     * @param event
     */
    public void focusGained(FocusEvent event) {
        currency.getAmount().setCaretPosition(0);
    }

}
