package uk.gov.courtservice.xhibit.client.courtlog.directions;

import javax.swing.JPanel;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
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
 * @author unascribed
 * @version 1.0
 */

public class DateChangeListener implements MChangeListener {
    JPanel component;

    public DateChangeListener(JPanel component) {
        this.component = component;
    }

    public void valueChanged(MChangeEvent e) {
        if (e.getType() == MChangeEvent.CHANGE) {
            if (component instanceof XPanel) {
                ((XPanel) component).modified();
            }
        }
    }
}