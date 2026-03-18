package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.util.XPanel;

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
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ItemChangeListener implements ItemListener {
    JPanel component;

    public ItemChangeListener(JPanel component) {
        this.component = component;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
            if (component instanceof XPanel) {
                ((XPanel) component).modified();
            }
        }
    }
}