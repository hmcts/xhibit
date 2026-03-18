package uk.gov.courtservice.xhibit.client.results.pleas;

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
 * Title: Table model for PleasIndictmentPanel
 * </p>
 * <p>
 * Description: This class contains logic to determine whether to add, update or
 * delete plea depending on user actions on table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.68 $
 */
public class PleaIndictmentTableModel extends XHIBITDefaultTableModel {
    private static final Logger log = Logger.getLogger(PleaIndictmentTableModel.class);

    public static final int COLUMN_NO = 0;

    public static final int COLUMN_OFFENCE = 1;

    public static final int COLUMN_DEFENDANT = 2;

    public static final int COLUMN_PLEA_CODE = 3;

    public static final int COLUMN_PLEA_DESC = 4;

    public static final int COLUMN_ADDITIONAL_INFO = 5;

    public static final int COLUMN_DATE = 6;

    public static final int COLUMN_ALTERED = 7;

    private String emptyStr = "";

    private final PleaControllerModel model;

    public PleaIndictmentTableModel(Object[] tableRows, PleaControllerModel pcm) {
        super();
        setupTableHeaders();
        super.setData(tableRows);
        this.model = pcm;
    }

    public Object getValueAt(int row, int col) {
        Object cellValue;
        if (_data.length <= 0)
            return null;

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // Endure Error is reset on cell change
        model.setDateRangeError(false);

        switch (col) {
        case COLUMN_NO:
            cellValue = dataRow.getChargeSequenceNumber();
            break;
        case COLUMN_OFFENCE:
            cellValue = dataRow.getCountDescription();
            break;
        case COLUMN_DEFENDANT:
            cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
            break;
        case COLUMN_PLEA_CODE:
            cellValue = dataRow.getPleaCode();
            break;
        case COLUMN_PLEA_DESC:
            cellValue = dataRow.getPleaDesc();
            break;
        case COLUMN_ADDITIONAL_INFO:
            cellValue = dataRow.getPleaAdditionalInfo();
            break;
        case COLUMN_DATE:
            cellValue = dataRow.getPleaArraignmentDate();
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
        String[] columnNames = {
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.indictment"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.count"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.defendant"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.pleacode"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.pleadescription"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.additionalInfo"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.date"),
                ResourceBundleHelper.getResource(XhibitBundles.Pleas, "indictment.table.column.altered") };
        super.setColumnNames(columnNames);
    }

    // Override setValueAt method of AbstractTableModel
    public void setValueAt(Object obj, int row, int col) {
        log.debug("setValueAt: row = " + row + "; col = " + col + "; obj = " + obj);

        // If the object is null OR the values are unchanged, return
        if (obj == null || obj.equals(getValueAt(row, col)))
            return;

        if (escapeDate(obj, row, col)) {
            return;
        }

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // Invalid Plea Code changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_PLEA_CODE
                && obj != null
                && !obj.equals(emptyStr)
                && !PleaHelper.isPleaCodeValid(ResultsHelper.checkNull((String) obj).toUpperCase(), PleaHelper
                        .getPleaRefData())) {
            return;
        }

        // Out of range date changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_DATE && obj != null && !obj.equals(emptyStr)
                && !PleaHelper.isArraignmentDateValid((java.util.Date) obj, dataRow)) {
            model.setDateRangeError(true);
            return;
        }

        dataRow.setOperationalPlea(model.getArraignmentDate());
        switch (col) {
        case COLUMN_PLEA_CODE:
            dataRow.processRefIndictmentPleaCode(obj, model.getArraignmentDate());
            break;
        case COLUMN_PLEA_DESC:
            dataRow.processRefIndictmentPleaDesc(obj, model.getArraignmentDate());
            break;
        case COLUMN_ADDITIONAL_INFO:
            dataRow.processPleaAdditionalInfo(obj);
            break;
        case COLUMN_DATE:
            dataRow.setPleaArraignmentDate(obj);
            break;
        default:
            break;
        }
        fireTableDataChanged();
    }

    public boolean isCellEditable(int row, int col) {
        boolean isEditable = model.getArraignmentDate() != null;

        // model must have an arraignment date for the table to be editable.
        if (isEditable) {
            switch (col) {
            case COLUMN_PLEA_CODE:
                isEditable = true;
                break;
            case COLUMN_PLEA_DESC:
                isEditable = true;
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

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case COLUMN_PLEA_CODE:
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
