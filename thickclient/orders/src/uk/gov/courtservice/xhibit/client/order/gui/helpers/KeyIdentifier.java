package uk.gov.courtservice.xhibit.client.order.gui.helpers;

import java.awt.event.KeyEvent;

/**
 * <p>
 * Title: Simple utility class to check key pressed values.
 * </p>
 * <p>
 * Description: Contains static methods for checking valid key presses, carriage
 * return key presses and backspace key presses. Valid keys are defined to be
 * undefined characters and control keys. This is particularly valid when
 * responding to key presses in the OrderComboBox component.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David & Des
 * @version 1.0
 */
public class KeyIdentifier {

    /**
     * Returns true if key pressed is not a valid character.
     * 
     * @param e
     *            a key event
     * @return true or false
     */
    public static boolean isValidKey(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(e.getKeyChar())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns true if key pressed was backspace.
     * 
     * @param e
     *            a key event.
     * @return true or false.
     */
    public static boolean isKeyBackspace(KeyEvent e) {
        if (e.getKeyCode() == 8) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if key pressed was a carriage return.
     * 
     * @param e
     *            a key event
     * @return true or false
     */
    public static boolean isKeyReturn(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if key pressed was a left arrow return.
     * 
     * @param e
     *            a key event
     * @return true or false
     */
    // DJ SCR 53113 - Start
    public static boolean isKeyLeftArrow(KeyEvent e) {
        if (e.getKeyCode() == 37) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if key pressed was a right arrow return.
     * 
     * @param e
     *            a key event
     * @return true or false
     */
    public static boolean isKeyRighttArrow(KeyEvent e) {
        if (e.getKeyCode() == 39) {
            return true;
        } else {
            return false;
        }
    }

    // DJ SCR 53113 - End

    /**
     * Returns true if key pressed was escape.
     * 
     * @param e
     *            a key event
     * @return true or false
     */
    public static boolean isKeyEscape(KeyEvent e) {
        if (e.getKeyCode() == 27) {
            return true;
        } else {
            return false;
        }
    }
}
