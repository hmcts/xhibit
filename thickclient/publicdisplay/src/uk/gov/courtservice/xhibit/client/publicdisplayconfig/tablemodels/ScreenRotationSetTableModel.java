package uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayBasicValueSortAdapter;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ScreenRotationSetTableModel.java,v 1.4 2004/01/30 16:26:21
 *          sz0t7n Exp $
 */

public class ScreenRotationSetTableModel extends AbstractSelectRowTableModel {
    private static final String TABLECOL_KEY1 = "pd.tablecol.screensDisplayingRotationSet";

    private XTable dataTable;

    public ScreenRotationSetTableModel(XTable rotationSetTable) {
        dataTable = rotationSetTable;
        setColumnNames(new String[] { PublicDisplayUtils.getResource(TABLECOL_KEY1) });
    }

    /**
     * Get the XhbDisplayBasicValue for the selected row in the table model
     * passed in and extracts the description
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (getSelectedRow() < 0)
            return "";
        DisplayBasicValueSortAdapter[] displays = getDisplays();
        return displays[rowIndex];
    }

    /**
     * @return The number of screens using the selected rotation set
     */
    public int getRowCount() {
        if (getSelectedRow() < 0) {
            return 0;
        } else {
            return getDisplays().length;
        }
    }

    /**
     * Return a VO for required position
     * 
     * @param row
     *            required item from array
     * @return XhbDisplayBasicValue
     */
    public Object getDataAt(int row) {
        DisplayBasicValueSortAdapter[] displays = getDisplays();
        if (row > displays.length)
            throw new CSUnrecoverableException("The selected row " + row
                    + " is not available in the list (ArrayIndexOutOfBounds)");
        return displays[row];
    }

    /**
     * Get the array of displays for the selected rotation set
     * 
     * @return Array of XhbDisplayBasicValue
     */
    private DisplayBasicValueSortAdapter[] getDisplays() {
        RotationSetComplexValue rsComplex = (RotationSetComplexValue) ((XHIBITTableModelInterface) dataTable.getModel())
                .getDataAt(getSelectedRow());
        DisplayBasicValueSortAdapter[] displays = rsComplex.getDisplayBasicValues();
        return displays;
    }

}