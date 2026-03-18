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
 * Title:
 * </p>
 * <p>
 * Description: Value object to represent the document format code which is
 * stored in the database and the value which is displayed on screen. Used in
 * building combo boxes.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ComboBoxListener.java,v 1.3 2006/06/05 12:30:43 bzjrnl Exp $
 */

public class ComboBoxListener implements ActionListener {
    JPanel component;

    public ComboBoxListener(JPanel component) {
        this.component = component;
    }

    public void actionPerformed(ActionEvent parm1) {
        if (component instanceof XPanel) {
            try {
                ((XPanel) component).stepUpdateViewState();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }
}