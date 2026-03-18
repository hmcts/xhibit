package uk.gov.courtservice.xhibit.client.results.appealresults;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellComponent;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: AppealResultsTableModel
 * </p>
 * <p>
 * Description: the table model for the criminal appeal results table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version $Revision: 1.40 $
 */
public class AppealResultsTableModel extends XHIBITDefaultTableModel {
    public static final int COLUMN_OFFFENCE = 0;

    public static final int COLUMN_DEFENDANT = 1;

    public static final int COLUMN_APPEAL_AGAINST = 2;

    public static final int COLUMN_APPEAL_RESULT_CODE = 3;

    public static final int COLUMN_APPEAL_RESULT = 4;

    public static final int COLUMN_ADDITIONAL_INFO = 5;

    public static final int COLUMN_ALTERED = 6;

    private static final Logger log = CSServices.getLogger(AppealResultsTableModel.class);

    public AppealResultsTableModel() {
        super();
        setupTableHeaders();
    }

    /**
     * gets the value for the required row & column combination
     * 
     * @param row
     *            the row whose value is to be queried.
     * @param col
     *            the column whose value is to be queried.
     * @return the value Object at the specified cell.
     */
    public Object getValueAt(int row, int col) {
        Object cellValue = null;

        if (_data.length > 0) {
            ResultsRowValue dataRow = (ResultsRowValue) _data[row];
            dataRow.setRefAppResult();

            switch (col) {
            case COLUMN_OFFFENCE:
                cellValue = dataRow.getCountDescription();
                break;
            case COLUMN_DEFENDANT:
                cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
                break;
            case COLUMN_APPEAL_AGAINST:
                cellValue = dataRow.getAppealAgainst();
                break;
            case COLUMN_APPEAL_RESULT_CODE:
                cellValue = dataRow.getAppealResultCode();
                break;
            case COLUMN_APPEAL_RESULT:
                cellValue = dataRow.getAppealResultDescription();
                break;
            case COLUMN_ADDITIONAL_INFO:
                cellValue = dataRow.getAppealResultAdditionalInfo();
                break;
            case COLUMN_ALTERED:
                cellValue = dataRow.getRowAltered();
                break;
            default:
                break;
            }
        }

        return cellValue;
    }

    /**
     * find out for a given row & col combination if the cell should be editable
     * 
     * @param row
     *            the row whose value is to be queried.
     * @param col
     *            the column whose value is to be queried.
     * @return whether or not the cell should be editable
     */
    public boolean isCellEditable(int row, int col) {
        boolean isEditable = false;

        switch (col) {
        case COLUMN_APPEAL_RESULT:
            isEditable = true;
            break;
        case COLUMN_ADDITIONAL_INFO:
            isEditable = ((AdditionalInfoTableCellComponent) getValueAt(row, col)).isCellEditable();
            break;
        default:
            break;
        }
        return isEditable;
    }

    /**
     * Sets the value in the cell at row and col to obj.
     * 
     * @param obj
     *            the new value.
     * @param row
     *            the row whose value is to be changed.
     * @param col
     *            the column whose value is to be changed.
     */
    public void setValueAt(Object obj, int row, int col) {
        log.debug("setValueAt: row = " + row + "; col = " + col + "; obj = " + obj);

        // Verify that the data has changed.
        if (obj == null)
            return;
        Object origValue = getValueAt(row, col);
        if (col == COLUMN_APPEAL_RESULT) {
            if (AppealResultsHelper.getAppealResultDescription((RefAppResultBasicValue) obj).equals(origValue)) {
                return;
            }
        } else {
            if (obj.equals(origValue))
                return;
        }

        ResultsRowValue dataRow = (ResultsRowValue) _data[row];
        dataRow.setOperationalAppealResult();

        switch (col) {
        case COLUMN_APPEAL_RESULT:
            dataRow.processAppealResult((RefAppResultBasicValue) obj, true);
            break;

        case COLUMN_ADDITIONAL_INFO:
            dataRow.processAppealAdditionalInfo(obj);
            break;

        default:
            break;
        }

        fireTableDataChanged();
    }

    /**
     * Gets the class of the given column.
     * 
     * @param columnIndex
     *            the column whose class is to be queried.
     * @return the Class of the given column.
     */
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case COLUMN_OFFFENCE:
            return String.class;

        case COLUMN_DEFENDANT:
            return String.class;

        case COLUMN_ALTERED:
            return ImageIcon.class;

        default:
            return super.getColumnClass(columnIndex);
        }
    }

    private void setupTableHeaders() {
        String[] columnNames = new String[] { getResource("criminal.columnCount"),
                getResource("criminal.columnAppellant"), getResource("criminal.columnAppealAgainst"),
                getResource("criminal.columnResultsCode"), getResource("criminal.columnResults"),
                getResource("criminal.columnLesserOffence"), getResource("criminal.columnAltered"), };
        super.setColumnNames(columnNames);
    }

    /**
     * Gets a resource string from the Appeal Result bundle.
     * 
     * @param key
     *            the key to lookup in the resource bundle.
     * @return the resource string for the given key.
     */
    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, key);
    }
}