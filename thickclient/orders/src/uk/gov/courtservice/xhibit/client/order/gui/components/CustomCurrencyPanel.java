package uk.gov.courtservice.xhibit.client.order.gui.components;

import javax.swing.JTextField;

/**
 * <p>
 * Title: CustomCurrencyPanel
 * </p>
 * <p>
 * Description: A currency widget to allow user to enter currency amounts. The
 * currency format is dependant on the current locale.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */
public class CustomCurrencyPanel extends OrderPanel {
    // JTextField with limited character support to only those
    // suitable for numeric input.
    private ValidCurrencyJTextField signText;

    private ValidCurrencyJTextField amountText;
    
    private boolean inputLocked = false;

    /**
     * Contstruct a CustomCurrencyPanel.
     * 
     * @param signText
     *            Currency code used to display the correct currency symbol.
     * @param value
     *            default numeric currency.
     * @param signLength
     *            length of field to display the currency symbol.
     * @param valueLength
     *            length of field to display the currency.
     * @param max
     *            max number of characters allowed by currency field.
     * @param type
     *            content type of currency field, i.e int or float.
     */
    public CustomCurrencyPanel(String signText, String value, String signLength, String valueLength, String max,
            String type) {
        setUp(signText, signLength, value, valueLength, max, type);
    }
    
    /**
     * @param enabled whether to enable or disable the panel
     */
    public void setEnabled(boolean enabled)
    {
    	if(isInputLocked())
    		enabled = false;
    	this.amountText.setEnabled(enabled);
  
    }

    /**
     * Set up a CustomCurrencyPanel.
     * 
     * @param sign
     *            Currency code used to display the correct currency symbol.
     * @param signLength
     *            length of field to display the currency symbol.
     * @param value
     *            default numeric currency.
     * @param valueLength
     *            length of field to display the currency.
     * @param max
     *            max number of characters allowed by currency field.
     * @param type
     *            content type of currency field, i.e int or float.
     */
    private void setUp(String sign, String signLength, String value, String valueLength, String max, String type) {
        this.signText = new ValidCurrencyJTextField(getSymbol(sign), Integer.parseInt(signLength), Integer
                .parseInt(max), type);
        signText.setHorizontalAlignment(JTextField.CENTER);
        signText.setEditable(false);
        signText.setEnabled(false);
        this.amountText = new ValidCurrencyJTextField(value, Integer.parseInt(valueLength), Integer.parseInt(max), type);
        this.amountText.setHorizontalAlignment(JTextField.LEFT);
        
        
        
        this.add(signText);
        this.add(amountText);
        
    }

    /**
     * Return the currency symbol given the currency symbol.
     * 
     * @param sign
     *            currency code.
     * @return currency symbol.
     */
    public String getSymbol(String sign) {
        return CurrencySymbolFactory.getCurrencySymbol(sign);
    }

    /**
     * Returns the JTextField containing the currency.
     * 
     * @return
     * @see JTextField
     */
    public JTextField getAmount() {
        return this.amountText;
    }

	public boolean isInputLocked() {
		return inputLocked;
	}

	public void setInputLocked(boolean inputLocked) {
		this.inputLocked = inputLocked;
	}

}
