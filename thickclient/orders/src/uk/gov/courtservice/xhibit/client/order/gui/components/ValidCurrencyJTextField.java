package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * 
 * <p>
 * Title: ValidCurrencyJTextField
 * </p>
 * <p>
 * Description: Text Field that displays a valid currency symbol
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class ValidCurrencyJTextField extends JTextField implements KeyListener {
    private int limit;

    private String validChars;

    private int keyCode;

    /**
     * Constructor
     * 
     * @param initialText
     * @param length
     * @param limit
     * @param type
     */
    public ValidCurrencyJTextField(String initialText, int length, int limit, String type) {
        super(initialText, length);
        if (type.equals("int")) {
            validChars = "0123456789\b";
        } else if (type.equals("date")) {
            validChars = "0123456789-\b";
        } else {
            validChars = "0123456789.\b";
        }
        this.limit = limit;
        addKeyListener(this);
    }

    /**
     * Implemnetation of keyPressed
     * 
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    /**
     * Implementaion of keyTyped - check if key is valid
     * 
     * @param e
     */
    public void keyTyped(KeyEvent e) {
        String text = this.getText();

        if ((text.length() > limit) && (this.keyCode != 8)) {
            e.consume();
        } else {
            checkValidChar(e);
            checkDecimalPoint(e);
        }
    }

    /**
     * Only allow one decimal point to be entered
     * 
     * @param e
     */
    private void checkDecimalPoint(KeyEvent e) {
        String contents = this.getText();
        if (e.getKeyChar() == '.') {
            if (contents.indexOf(".") != -1) {
                e.consume();
            }
        }
    }

    /**
     * Checks that only valid characters are input
     * 
     * @param e
     */
    private void checkValidChar(KeyEvent e) {
        if ((validChars.indexOf(e.getKeyChar()) < 0)) // && (decimalPoint >
        // 1))
        {
            e.consume();
        }
    }

    /**
     * Implementation of keyReleased
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        // no implementation
    }
    
    public void setEnabled(boolean value)
    {
    	super.setEnabled(value);
    }

}
