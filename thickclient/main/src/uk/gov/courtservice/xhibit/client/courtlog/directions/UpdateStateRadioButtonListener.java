package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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
 * @version 1.0
 */

public class UpdateStateRadioButtonListener implements ActionListener {
    JPanel component;

    public UpdateStateRadioButtonListener(JPanel component) {
        this.component = component;
    }

    public void actionPerformed(ActionEvent e) {
        if (component.getParent() instanceof XPanel) {
            try {
                ((XPanel) component.getParent()).stepUpdateViewState();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }
}