package uk.gov.courtservice.xhibit.client.viewinfopages.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Radio Button Action Listener
 * </p>
 * <p>
 * Description: Default Listener to be added to radio buttons (or other
 * components that use action listeners) that calls the stepUpdateViewState of
 * the parent component, if the component is an XPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RadioButtonAction.java,v 1.3 2006/06/05 12:32:08 bzjrnl Exp $
 */
public class RadioButtonAction implements ActionListener {
    JPanel component;

    public RadioButtonAction(JPanel component) {
        this.component = component;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (component instanceof XPanel) {
                ((XPanel) component).stepUpdateViewState();
            }
        } catch (CSRecoverableException ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }
}