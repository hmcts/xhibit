package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: Property change listener for listening to Screen changes
 * </p>
 * <p>
 * Description: If the screen rotation set has changed, refresh the data in the
 * maintain rotation set panel to reflect the new assigned to court rooms data.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: MaintainScreenChangeListener.java,v 1.3 2004/03/11 16:42:22
 *          qzd3k3 Exp $
 */

public class MaintainScreenChangeListener implements PropertyChangeListener {
    MaintainRotationSetPanel _parent;

    /**
     * Store a link to the Maintain Rotation set panel
     * 
     * @param parent
     *            The panel who's data is to be refreshed
     */
    public MaintainScreenChangeListener(MaintainRotationSetPanel parent) {
        _parent = parent;
    }

    /**
     * Refresh the rotation set table data : - Get new data - Store in the
     * rotation set table model - sort the table (if necessary) - reselect row
     * to return to the original state.
     * 
     * @param evt
     *            The change event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // Start with changed as true
        // If we can't establish that the rotation set has changed
        // then we have to assume it has.
        boolean changed = true;

        if (evt.getOldValue() != null && evt.getNewValue() != null) {
            DisplayConfiguration oldConfig = (DisplayConfiguration) evt.getOldValue();
            DisplayConfiguration newConfig = (DisplayConfiguration) evt.getNewValue();
            if (oldConfig.getRotationSetId().equals(newConfig.getRotationSetId())) {
                changed = true;
            }
        }

        if (changed) {
            try {
                _parent.stepInitialise();
                XTable table = _parent.getRotationSetTable();
                int selectedRow = table.getSelectedRow();
                XHIBITTableModelInterface model = (XHIBITTableModelInterface) table.getModel();
                model.setData(_parent.getData());
                if (model instanceof XSortableTableModel) {
                    ((XSortableTableModel) model).sort(model);
                }
                if (selectedRow >= 0) {
                    table.setRowSelectionInterval(selectedRow, selectedRow);
                }
            } catch (CSRecoverableException ex) {
                XHIBITErrorHandler.handleError(ex);
            }
        }
    }
}