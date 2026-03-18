package uk.gov.courtservice.xhibit.client.im.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>
 * Title: IMNumericKeyListener
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

public class IMNumericKeyListener implements KeyListener {
    private int keyCode;

    private static final String numChars = "+ 0123456789\t\b";

    public IMNumericKeyListener() {
    }

    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    public void keyTyped(KeyEvent e) {
        char text = e.getKeyChar();

        if (!isNumeric(text)) {
            e.consume();
        }

    }

    public void keyReleased(KeyEvent e) {
        // no implementation
    }

    private boolean isNumeric(char key) {
        return (numChars.indexOf(key) > -1);

    }

}