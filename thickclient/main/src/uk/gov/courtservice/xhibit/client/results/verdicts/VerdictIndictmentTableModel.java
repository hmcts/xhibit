package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellComponent;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: Table model for displaying/editing verdicts for indictments
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
 */
public class VerdictIndictmentTableModel extends XHIBITDefaultTableModel {
    public static final int COLUMN_NO = 0;

    public static final int COLUMN_OFFFENCE = 1;

    public static final int COLUMN_DEFENDANT = 2;

    public static final int COLUMN_VERDICT_CODE = 3;

    public static final int COLUMN_VERDICT_DESC = 4;

    public static final int COLUMN_JUROR_ASSENT_DISSENT = 5;

    public static final int COLUMN_ADDITIONAL_INFO = 6;

    public static final int COLUMN_DATE = 7;

    public static final int COLUMN_ALTERED = 8;

    private static final Logger log = Logger.getLogger(VerdictIndictmentTableModel.class);

    private final VerdictControllerModel model;

    private final static String emptyStr = "";

    /**
     * Creates a VerdictIndictmentTableModel.
     * 
     * @param tableRows
     *            the data for the table.
     * @param vcm
     *            the VerdictControllerModel.
     */
    public VerdictIndictmentTableModel(Object[] tableRows, VerdictControllerModel vcm) {
        super();
        setupTableHeaders();
        super.setData(tableRows);
        this.model = vcm;
    }

    /**
     * TableModel implementation to get the value for the cell at col and row.
     * 
     * @param row
     *            the row whose value is to be queried.
     * @param col
     *            the column whose value is to be queried.
     * @return the value Object at the specified cell.
     */
    public Object getValueAt(int row, int col) {
        Object cellValue = null;

        if (_data.length <= 0)
            return null;

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // Endure Error is reset on cell change
        model.setDateRangeError(false);

        switch (col) {
        case COLUMN_NO:
            cellValue = dataRow.getChargeSequenceNumber();
            break;
        case COLUMN_OFFFENCE:
            cellValue = dataRow.getCountDescription();
            break;
        case COLUMN_DEFENDANT:
            /**
             * @todo Get from RRV or ResultsHelper/Look at PleaTableModel too
             */
            cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
            break;
        case COLUMN_VERDICT_CODE:
            cellValue = dataRow.getVerdictCode();
            break;
        case COLUMN_VERDICT_DESC:
            cellValue = dataRow.getVerdictDesc();
            break;
        case COLUMN_JUROR_ASSENT_DISSENT:
            cellValue = dataRow.getJurorsAssenting();
            break;
        case COLUMN_ADDITIONAL_INFO:
            cellValue = dataRow.getVerdictAdditionalInformation();
            break;
        case COLUMN_DATE:
            cellValue = dataRow.getVerdictDate();
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

    /**
     * Sets up the table column headers.
     */
    private void setupTableHeaders() {
        String[] columnNames = {
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.indictment"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.count"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.defendant"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.verdictcode"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.verdictdescription"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                        "indictment.table.column.jurorsassentingdessenting"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.additionalInfo"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.date"),
                ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "indictment.table.column.altered") };
        super.setColumnNames(columnNames);
    }

    /**
     * Overrides AbstractTableModel implentation to set the value of a table
     * cell
     * 
     * @param obj
     *            value to assign to the cell
     * @param row
     *            the row whose value is to be set.
     * @param col
     *            the column whose value is to be set.
     */
    public void setValueAt(Object obj, int row, int col) {
        log.debug("setValueAt: row = " + row + "; col = " + col + "; obj = " + obj);
        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // If the object is null OR the values are unchanged, return
        if (obj == null || obj.equals(getValueAt(row, col)))
            return;

        if (escapeDate(obj, row, col)) {
            return;
        }

        // Invalid Verdict Code changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_VERDICT_CODE
                && obj != null
                && !obj.equals(emptyStr)
                && !VerdictHelper.isVerdictCodeValid(ResultsHelper.checkNull((String) obj).toUpperCase(), dataRow
                        .getPleaValue())) {
            return;
        }

        // Out of range date changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_DATE && obj != null && !obj.equals(emptyStr)
                && !VerdictHelper.isVerdictDateValid((java.util.Date) obj, dataRow)) {
            model.setDateRangeError(true);
            return;
        }

        dataRow.setOperationalVerdict(model.getVerdictDate());

        switch (col) {
        case COLUMN_VERDICT_CODE:
            dataRow.processVerdictCode(obj);
            break;
        case COLUMN_VERDICT_DESC:
            dataRow.processVerdictDesc(obj);
            break;
        case COLUMN_JUROR_ASSENT_DISSENT:
            dataRow.setJurorsAssentingDessenting(obj);
            break;
        case COLUMN_ADDITIONAL_INFO:
            dataRow.processVerdictAdditionalInfo(obj);
            break;
        case COLUMN_DATE:
            dataRow.setVerdictDate(obj);
            break;
        default:
            break;
        }

        dataRow.setModified(true);
        // fireTableDataChanged();
        fireTableRowsUpdated(row, row);
    }

    /**
     * Overrides AbstractTableModel implementaion to see if the cell at the
     * given row and column is editable.
     * 
     * @param rowIndex
     *            the row whose value is to be queried.
     * @param columnIndex
     *            the column whose value is to be queried.
     * @return true if the cell is editable.
     */
    public boolean isCellEditable(int row, int col) {
        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        if (!dataRow.isVerdictRequired()) {
            return false;
        }

        boolean isEditable = model.getVerdictDate() != null;

        if (isEditable) {
            switch (col) {
            case COLUMN_VERDICT_CODE:
                isEditable = true;
                break;
            case COLUMN_VERDICT_DESC:
                isEditable = true;
                break;
            case COLUMN_JUROR_ASSENT_DISSENT:
                isEditable = dataRow.isJurorAssentingDissentingEditable();
                break;
            case COLUMN_ADDITIONAL_INFO:
                isEditable = ((AdditionalInfoTableCellComponent) getValueAt(row, col)).isCellEditable();
                break;
            case COLUMN_DATE:
                isEditable = true;
                break;
            default:
                isEditable = false;
                break;
            }
        }
        return isEditable;
    }

    /**
     * Overrides AbstractTableModel implementation to get the column Class.
     * 
     * @param columnIndex
     *            the column being queried.
     * @return the Class of the given column.
     */
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case COLUMN_VERDICT_CODE:
            return String.class;
        case COLUMN_DATE:
            // This is actually a Date, but the initColumnSizes method of
            // XTable throws a date formatting error. Don't know why !?
            // Returning a calendar tricks the method to use a text renderer
            // instead of a date renderer.
            return Calendar.class;
        case COLUMN_ALTERED:
            return ImageIcon.class;
        default:
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
        if (col == COLUMN_DATE)
            format = XDateFormat.DATEFORMAT;

        if (obj instanceof Date) {
            // If value is not a Date don't do anything
            if (!(getValueAt(row, col) instanceof Date)) {
                return true;
            }
            if (XDateFormat.format((Date) obj, format).equals(XDateFormat.format((Date) getValueAt(row, col), format)))
                return true;
        }

        if (obj instanceof Calendar) {
            // If value is not a Calendar don't do anything
            if (!(getValueAt(row, col) instanceof Calendar)) {
                return true;
            }

            if (XDateFormat.format((Calendar) obj, format).equals(
                    XDateFormat.format((Calendar) getValueAt(row, col), format)))
                return true;
        }

        return false;
    }
}