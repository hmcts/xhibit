package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

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

public class CourtRoomListCellRenderer extends DefaultListCellRenderer {

    /**
     * Creates a CourtRoomListCellRenderer
     */
    public CourtRoomListCellRenderer() {
    }

    /**
     * Overrides method in DefaultListCellRenderer
     * 
     * @param list
     *            The JList we're painting.
     * @param value
     *            The value returned by list.getModel().getElementAt(index).
     * @param index
     *            The cells index.
     * @param selected
     *            True if the specified cell was selected.
     * @param cellHasFocus
     *            True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
            boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, cellHasFocus);
        label.setText(((XhbCourtRoomBasicValue) value).getDisplayName());
        return label;
    }

}