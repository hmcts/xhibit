package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.util.LimitedTextDocument;

/**
 * 
 * <p>
 * Title: TextLimitedJTextField
 * </p>
 * <p>
 * Description: JTextField that provides for input validation and also limited
 * text entry.
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
public class TextLimitedJTextField extends JTextField implements KeyListener {
    private static final int DEFAULT_LEN = 10;

    private static final Logger log = CSServices.getLogger(TextLimitedJTextField.class);

    private static final String NUMERICS = "0123456789.";

    private int maxLength;

    private int keyCode;

    private boolean numeric;

    /**
     * Constructor - Accepts numeric parameter used to determine if Right
     * justification required
     * 
     * @param s
     *            text to display
     * @param l
     *            columns
     * @param length
     *            maximum length of field
     */
    public TextLimitedJTextField(String s, int l, String length, String numeric) {
        // The LimitedTextDocument will prevent paste from going
        // over the text limit.
        super(new LimitedTextDocument(length == null ? DEFAULT_LEN : Integer.parseInt(length)), s, l);

        this.numeric = "Y".equalsIgnoreCase(numeric);
        if (this.numeric) {
            this.setHorizontalAlignment(JTextField.RIGHT);
        }

        maxLength = (length == null ? DEFAULT_LEN : Integer.parseInt(length));
        addKeyListener(this);
    }

    /**
     * Constructor
     * 
     * @param s
     *            text to display
     * @param l
     *            columns
     * @param length
     *            maximum length of field
     */
    public TextLimitedJTextField(String s, int l, String length) {
        // The LimitedTextDocument will prevent paste from going
        // over the text limit.
        super(new LimitedTextDocument(length == null ? DEFAULT_LEN : Integer.parseInt(length)), s, l);
        maxLength = (length == null ? DEFAULT_LEN : Integer.parseInt(length));
        addKeyListener(this);
    }

    /**
     * Constructor
     * 
     * @param s
     *            text to display
     * @param l
     *            columns
     */
    public TextLimitedJTextField(String s, int l) {
        this(s, l, "10");
    }

    /**
     * Constructor
     * 
     * @param s
     *            text to display
     */
    public TextLimitedJTextField(String s) {
        this(s, DEFAULT_LEN);
    }

    /**
     * Capture the key pressed
     * 
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    /**
     * Check if the maximum length of the text field has been exceeded and also
     * also if keyCode = 8 (Back Delete)
     * 
     * @param e
     */
    public void keyTyped(KeyEvent e) {
        String text = this.getText();

        if ((text.length() > maxLength) // > Max Length
                || // OR
                (this.keyCode != 8 // NOT Back Delete
                && // AND
                (numeric // numeric field
                && // AND
                NUMERICS.indexOf(e.getKeyChar()) < 0))) // Numeric character
        {
            e.consume();
        }
    }

    /**
     * Empty implementation of keyReleased
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        // no implementation
    }
}
