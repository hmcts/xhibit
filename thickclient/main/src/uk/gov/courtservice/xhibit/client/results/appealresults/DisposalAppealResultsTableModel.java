package uk.gov.courtservice.xhibit.client.results.appealresults;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: DisposalAppealResultsTableModel
 * </p>
 * <p>
 * Description: table model for the criminal appeal general magistrates disposal
 * results table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.4 $
 */
public class DisposalAppealResultsTableModel extends XHIBITDefaultTableModel {
    public static final int COLUMN_DEFENDANT = 0;

    public static final int COLUMN_DISPOSAL = 1;

    public static final int COLUMN_APPEAL_RESULT_CODE = 2;

    public static final int COLUMN_APPEAL_RESULT = 3;

    public static final int COLUMN_ALTERED = 4;

    private static final Logger log = CSServices.getLogger(DisposalAppealResultsTableModel.class);

    public DisposalAppealResultsTableModel() {
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
            dataRow.setMagsRefAppResult();

            switch (col) {
            case COLUMN_DEFENDANT:
                cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
                break;
            case COLUMN_DISPOSAL:
                // cellValue = dataRow.getDisposalDesc();
                cellValue = dataRow.getDisposalReferenceValue().getDisposalText(dataRow.getDisposalValue());
                break;
            case COLUMN_APPEAL_RESULT_CODE:
                cellValue = dataRow.getAppealResultCode();
                break;
            case COLUMN_APPEAL_RESULT:
                cellValue = dataRow.getAppealResultDescription();
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

            // link this appeal result to its Magistrates Court General
            // Disposal which is stored in xhb_disposal2.
            dataRow.linkResultToMagsGeneralDisposal((RefAppResultBasicValue) obj);
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
        case COLUMN_DEFENDANT:
            return String.class;

        case COLUMN_ALTERED:
            return ImageIcon.class;

        default:
            return super.getColumnClass(columnIndex);
        }
    }

    private void setupTableHeaders() {
        String[] columnNames = new String[] { getResource("criminal.columnAppellant"),
                getResource("criminal.columnDisposal"), getResource("criminal.columnResultsCode"),
                getResource("criminal.columnResults"), getResource("criminal.columnAltered"), };
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