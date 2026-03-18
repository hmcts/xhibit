package uk.gov.courtservice.xhibit.client.results.pleas;

import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

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
 * @author Simon Gilmore
 * @version 1.0
 */
public class PleaBreachTableModel extends XHIBITDefaultTableModel {
    public static final int COLUMN_CHARGE = 0;

    public static final int COLUMN_OFFENCES = 1;

    public static final int COLUMN_DEFENDANT = 2;

    public static final int COLUMN_PLEA = 3;

    public static final int COLUMN_DATEPUT = 4;

    public static final int COLUMN_ALTERED = 5;

    private static final Logger log = CSServices.getLogger(PleaBreachTableModel.class);

    private String emptyStr = "";

    private ResultsRowValue dataRow;

    private PleaControllerModel model;

    public PleaBreachTableModel(Object[] tableRows, PleaControllerModel model) {
        super();
        setupTableHeaders();
        super.setData(tableRows);
        this.model = model;
    }

    public Object getValueAt(int row, int col) {
        Object cellValue;

        if (_data.length <= 0)
            return null;

        dataRow = (ResultsRowValue) _data[row];

        // Ensure Error is reset on cell change
        model.setDateRangeError(false);

        switch (col) {
        case COLUMN_CHARGE:
            cellValue = dataRow.getBreachChargeData();
            break;
        case COLUMN_OFFENCES:
            cellValue = dataRow.getBreachOffencesText();
            break;
        case COLUMN_DEFENDANT:
            cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
            break;
        case COLUMN_PLEA:
            cellValue = dataRow.getBreachPleaText();
            break;
        case COLUMN_DATEPUT:
            cellValue = dataRow.getBreachDatePut();
            break;
        case COLUMN_ALTERED:
            cellValue = dataRow.getRowAltered();
            break;
        default:
            cellValue = "";
            break;
        }
        return cellValue;
    }

    private void setupTableHeaders() {
        String[] columnNames = { getResource("breach.table.column.breachdesc"),
                getResource("breach.table.column.offencesdesc"), getResource("breach.table.column.defendant"),
                getResource("breach.table.column.pleadescription"), getResource("breach.table.column.dateput"),
                getResource("breach.table.column.altered") };
        super.setColumnNames(columnNames);
    }

    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Pleas, key);
    }

    // Override setValueAt method of AbstractTableModel
    public void setValueAt(Object obj, int row, int col) {
        log.debug("row = " + row + "; col = " + col + "; obj = " + obj);

        // If the value has not changed then do nothing.
        if (obj != null && obj.equals(getValueAt(row, col)))
            return;
        if (escapeDate(obj, row, col)) {
            return;
        }

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // Out of range date changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_DATEPUT) {
            // Prevent altering row if clicked in empty dateput.
            if (obj == null
                    && (dataRow.getPleaValue() == null || (dataRow.getPleaValue() != null && dataRow.getPleaValue()
                            .getDatePut() == null))) {
                return;
            }

            // Cannot have null Date Put if not using HO Proc code
            if (!PleaHelper.isDatePutValid(obj, dataRow)) {
                return;
            }

            if (obj != null && !obj.equals(emptyStr) && PleaHelper.isFutureDate((java.util.Date) obj)) {
                model.setDateRangeError(true);
                return;
            }
        }

        // Invalid plea needs to be checked before setOperationalPlea is called
        if (col == COLUMN_PLEA && !PleaHelper.isBreachPleaValid(obj, dataRow)) {
            return;
        }

        dataRow.setOperationalBreachPlea();

        try {
            switch (col) {
            case COLUMN_PLEA:
                dataRow.processBreachPlea(obj);
                break;
            case COLUMN_DATEPUT:
                dataRow.setBreachDatePut(obj);
                break;
            default:
                break;
            }

            // ** @todo Revisit onve pleaValue.getArraignmentDate starts
            // send back actual date */
            // fireTableCellUpdated(row, col); //Original
            // Temp methods called in effort to get model to refresh on
            // selecting breach null
            fireTableRowsUpdated(row, row);
        } catch (UserCancelException uce) {
            // Do not call fireTableRowsUpdated method
        }
        // fireTableDataChanged();
    }

    public boolean isCellEditable(int row, int column) {
        if (column == COLUMN_PLEA) {
            return true;
        } else if (column == COLUMN_DATEPUT) {
            return true;
        } else {
            return false;
        }
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == COLUMN_ALTERED) {
            return ImageIcon.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    /**
     * if the user clicks in then out of the date fields setValueAt is fired so
     * return if the new value is the same as the original value.
     * 
     * @param obj
     * @param row
     * @param col
     * @return
     */
    private boolean escapeDate(Object obj, int row, int col) {
        int format = XDateFormat.DATETIMEFORMAT;
        if (col == COLUMN_DATEPUT)
            format = XDateFormat.DATEFORMAT;

        Object oldObj = getValueAt(row, col);
        if (obj instanceof Date) {
            // If value is not a Date don't do anything
            if (oldObj == null) {
                return false;
            } else if (!(oldObj instanceof Date)) {
                return true;
            }
            if (XDateFormat.format((Date) obj, format).equals(XDateFormat.format((Date) getValueAt(row, col), format)))
                return true;
        }

        if (obj instanceof Calendar) {
            // If value is not a Calendar don't do anything
            if (oldObj == null) {
                return false;
            } else if (!(getValueAt(row, col) instanceof Calendar)) {
                return true;
            }

            if (XDateFormat.format((Calendar) obj, format).equals(
                    XDateFormat.format((Calendar) getValueAt(row, col), format)))
                return true;
        }

        return false;
    }
}
