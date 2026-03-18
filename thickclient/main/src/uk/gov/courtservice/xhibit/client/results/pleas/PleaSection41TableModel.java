package uk.gov.courtservice.xhibit.client.results.pleas;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: PleaSection41TableModel
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
public class PleaSection41TableModel extends XHIBITDefaultTableModel {
    public static final int COLUMN_OFFFENCE = 0;

    public static final int COLUMN_DEFENDANT = 1;

    public static final int COLUMN_PLEA_CODE = 2;

    public static final int COLUMN_PLEA_DESC = 3;

    public static final int COLUMN_ALTERED = 4;

    private static final Logger log = CSServices.getLogger(PleaSection41TableModel.class);

    private String emptyStr = "";

    public PleaSection41TableModel(Object[] tableRows) {
        super();
        setupTableHeaders();
        super.setData(tableRows);
    }

    public Object getValueAt(int row, int col) {
        Object cellValue;

        if (_data.length <= 0)
            return null;

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        switch (col) {
        case COLUMN_OFFFENCE:
            cellValue = dataRow.getOffenceDescription();
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
        String[] columnNames = { getResource("section41.table.column.offence"),
                getResource("section41.table.column.defendant"), getResource("section41.table.column.pleacode"),
                getResource("section41.table.column.pleadescription"), getResource("section41.table.column.altered") };
        super.setColumnNames(columnNames);
    }

    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Pleas, key);
    }

    // Override setValueAt method of AbstractTableModel
    public void setValueAt(Object obj, int row, int col) {
        log.debug("[PleaSection41TableModel] setValueAt: row = " + row + "; col = " + col + "; obj = " + obj);

        // value has not changed therefore do nothing.
        if (obj != null && obj.equals(getValueAt(row, col)))
            return;

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];

        // Invalid Plea Code changes needs to be checked before
        // setOperationalPlea is called
        if (col == COLUMN_PLEA_CODE
                && obj != null
                && !obj.equals(emptyStr)
                && !PleaHelper.isPleaCodeValid(ResultsHelper.checkNull((String) obj).toUpperCase(), PleaHelper
                        .getPleaS41RefData())) {
            return;
        }

        dataRow.setOperationalPlea();

        switch (col) {
        case COLUMN_PLEA_CODE:
            dataRow.processRefS41PleaCode(obj);
            break;
        case COLUMN_PLEA_DESC:
            dataRow.processRefS41PleaDesc(obj);
            break;
        default:
            break;
        }

        fireTableCellUpdated(row, col);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean isEditable = false;

        switch (columnIndex) {
        case COLUMN_PLEA_CODE:
            isEditable = true;
            break;
        case COLUMN_PLEA_DESC:
            isEditable = true;
            break;
        default:
            isEditable = false;
            break;
        }
        return isEditable;
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == COLUMN_PLEA_CODE) {
            return String.class;
        } else if (columnIndex == COLUMN_ALTERED) {
            return ImageIcon.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

}