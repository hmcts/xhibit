package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Key Listener
 * </p>
 * <p>
 * Description: Default Listener to be added to text fields that call the
 * stepUpdateViewState of the parent component, if the component is an XPanel
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

public class UpdateStateKeyListener implements KeyListener {
    protected JPanel component;

    public UpdateStateKeyListener(JPanel component) {
        this.component = component;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (component instanceof XPanel) {
            ((XPanel) component).modified();
        }
        maybeUpdateViewState();
    }

    protected void maybeUpdateViewState() {
        if (component.getParent() instanceof XPanel) {
            try {
                ((XPanel) component.getParent()).stepUpdateViewState();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }
}