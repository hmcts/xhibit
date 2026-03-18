package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.List;

import javax.swing.ImageIcon;

import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValueHelper;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Revision: 1.19 $
 */
public class UnrelatedDisposalTableModel extends XHIBITDefaultTableModel {
    public static final int DEFENDANT_COLUMN = 0;

    public static final int DISPOSAL_TYPE_COLUMN = 1;

    public static final int DESCRIPTION_COLUMN = 2;

    public static final int ALTERED_COLUMN = 3;

    private String defendantName;

    private ResultsRowValue dataRow;

    public UnrelatedDisposalTableModel(List data) {
        super();
        setupTableHeaders();
        if (data != null) {
            super.setData(data);
        }
    }

    public Object getValueAt(int row, int col) {
        Object cellValue;
        boolean defendantDuplicate = false;

        // This logic needs to stay here as it's specific to table column data
        // display.
        if (_data.length <= 0)
            return null;

        dataRow = (ResultsRowValue) _data[row];

        if (row > 0) {
            if (dataRow.getDefendantValue() != null) {
                if (dataRow.getDefendantValue().getDefOnCaseBasicValue().getId().compareTo(
                        ((ResultsRowValue) _data[row - 1]).getDefendantValue().getDefOnCaseBasicValue().getId()) == 0) {
                    defendantDuplicate = true;
                }
            }
        }
        switch (col) {
        case DEFENDANT_COLUMN:
            defendantName = ResultsHelper.getName(dataRow.getDefendantValue());
            if (defendantDuplicate || defendantName == null) {
                cellValue = "";
            } else {
                cellValue = defendantName;

            }
            break;
        case DISPOSAL_TYPE_COLUMN:
            // Check for null
            cellValue = ResultsRowValueHelper.getDisposalCourtType(dataRow.getDisposalValue());
            break;
        case DESCRIPTION_COLUMN:
            // Check for null
            cellValue = dataRow.getDisposalDesc();
            break;
        case ALTERED_COLUMN:
            cellValue = dataRow.getRowAltered();
            break;
        default:
            cellValue = "";
            break;
        }

        return cellValue;
    }

    private void setupTableHeaders() {
        String[] columnNames = { getResource("columnDefendant"), getResource("columnType"),
                getResource("columnDisposal"), getResource("columnAltered") };
        super.setColumnNames(columnNames);
        Object[] lv = new Object[columnNames.length];
        for (int i = 0; i < lv.length; i++) {
            lv[i] = XTable.COLUMN_WIDTH_UNDEFINED;
        }
        setLongValues(lv);
    }

    /**
     * Get the resource string for the given key.
     * 
     * @param key
     *            the key to lookup in the resource bundle.
     * @return the resource String for the given key.
     */
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Disposals, key);
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
        case ALTERED_COLUMN:
            return ImageIcon.class;
        default:
            return super.getColumnClass(columnIndex);
        }
    }
}