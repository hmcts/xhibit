package uk.gov.courtservice.xhibit.client.util.listeners;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Call stepUpdateViewState on the XPanel when a new row is
 * selected in the table/list
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

public class TableRowListener implements ListSelectionListener {
    private XPanel toUpdate;

    public TableRowListener(XPanel parent) {
        toUpdate = parent;
    }

    public void valueChanged(ListSelectionEvent e) {
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        // if (lsm.isSelectionEmpty()) {
        // //no rows are selected
        // } else {
        try {
            if (toUpdate != null)
                toUpdate.stepUpdateViewState();
        } catch (Exception ex) {
            XHIBITConstant.handleError(ex);
        }
        // }
    }
}