package uk.gov.courtservice.xhibit.client.util.listeners;

// jdk
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: RadioButtonListener
 * </p>
 * <p>
 * Description: ActionListener for radio buttons which calls the
 * stepUpdateViewState() method of the XPanel when the radio button is changed.
 * The functionality can be extended in future to look for parent XPanels and
 * call the stepUpdateViewState() method on this instead where required.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: RadioButtonListener.java,v 1.3 2006/06/05 12:30:43 bzjrnl Exp $
 */
public class RadioButtonListener implements ActionListener {
    JPanel component;

    public RadioButtonListener(JPanel component) {
        this.component = component;
    }

    public void actionPerformed(ActionEvent e) {
        if (component instanceof XPanel) {
            try {
                ((XPanel) component).stepUpdateViewState();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }
}