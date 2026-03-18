package uk.gov.courtservice.xhibit.client.im.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class IMKeyListener implements KeyListener {
    private int keyCode;

    public IMKeyListener() {
    }

    public void keyPressed(KeyEvent e) {
        this.keyCode = e.getKeyCode();
    }

    public void keyTyped(KeyEvent e) {
        char text = e.getKeyChar();

        // if (text != 8) {
        // e.consume();
        // }

    }

    public void keyReleased(KeyEvent e) {
        // no implementation
    }

}