package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>
 * Title: NumericKeyListener
 * </p>
 * <p>
 * Description: Listens for numeric key inputs
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

public class NumericKeyListener implements KeyListener {
    private int keyCode;

    // numeric values, tab and back-delete
    private static final String NUMERIC_CHARS = "0123456789\t\b";

    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    /**
     * If the key pressed is not one of those defined, consume the key pressed
     * 
     * @param e
     */
    public void keyTyped(KeyEvent e) {
        char text = e.getKeyChar();

        if (!isNumeric(text)) {
            e.consume();
        }

    }

    public void keyReleased(KeyEvent e) {
        // no implementation
    }

    /**
     * Checks that the key pressed is numeric
     * 
     * @param key
     *            the key pressed
     * @return true if key pressed is numeric
     */
    private boolean isNumeric(char key) {
        return (NUMERIC_CHARS.indexOf(key) > -1);

    }

}