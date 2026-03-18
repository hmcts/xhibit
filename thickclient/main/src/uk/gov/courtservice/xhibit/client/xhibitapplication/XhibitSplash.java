package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JWindow;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Splash panel to show version and copyrigth<br>
 * Also setStatus(String s) will display loading messages
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @editor Frederik Vandendriessche
 * @version 1.0
 */

public class XhibitSplash extends JWindow {
    
    private static final long serialVersionUID = 1L;
    
    private XhibitSplashPanel sp;

    public XhibitSplash() {
        sp = new XhibitSplashPanel();
        this.getContentPane().add(sp);
        pack();
        centreDialog();
        setVisible(true);
    }

    public void setStatus(String message) {
        sp.setStatus(message);
    }

    private void centreDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
}
