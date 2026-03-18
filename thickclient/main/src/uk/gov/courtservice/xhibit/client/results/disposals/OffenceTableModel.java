package uk.gov.courtservice.xhibit.client.results.disposals;

import javax.swing.ImageIcon;

import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValueHelper;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
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
 */
public class OffenceTableModel extends XHIBITDefaultTableModel {
    private static final long serialVersionUID = 101L;
    
    public static final int CHARGE_COLUMN = 0;
    public static final int DESCRIPTION_COLUMN = 1;
    public static final int DEFENDANT_COLUMN = 2;
    public static final int APPEAL_AGAINST_COLUMN = 3;
    public static final int COURTTYPE_COLUMN = 4;
    public static final int DISPOSAL_COLUMN = 5;
    public static final int ALTERED_COLUMN = 6;
    
    private ResultsRowValue dataRow;

    private boolean isBreach = false;
    private boolean isBailAct = false;

    public OffenceTableModel(java.util.List data) {
        super();
        if (data != null) {
            if (data.size() > 0) {
                isBreach = ((ResultsRowValue) data.get(0)).getChargeType().equals(ChargeTypes.BREACH.getChargeType());
            }
            if (data.size() > 0) {
                isBailAct = ((ResultsRowValue) data.get(0)).getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType());
            }
            super.setData(data);
        }
        setupTableHeaders();
    }

    public Object getValueAt(int row, int col) {
        Object cellValue;
        boolean offenceDefendantDuplicate = false;
        boolean chargeDuplicate = false;

        // This logic needs to stay here as it's specific to table column data
        // display.
        if (_data.length <= 0)
            return null;

        dataRow = (ResultsRowValue) _data[row];

        // set offenceDefendantDuplicate offence/defendant pair boolean
        if (row > 0) {
            if (dataRow.getChargeSequenceNumber() != null) {
                if (dataRow.getChargeSequenceNumber().intValue() == ((ResultsRowValue) _data[row - 1])
                        .getChargeSequenceNumber().intValue()) {
                    if (dataRow.getDefendantOnChargeId() != null) {
                        if (dataRow.getDefendantOnChargeId().intValue() == ((ResultsRowValue) _data[row - 1])
                                .getDefendantOnChargeId().intValue()) {
                            chargeDuplicate = true;
                        } else {
                            chargeDuplicate = false;
                        }
                    } else {
                        chargeDuplicate = true;
                    }
                }
            }
            if (dataRow.getOffenceValue() != null) {
                if (dataRow.getOffenceValue().getOffenceDescription().equalsIgnoreCase(
                        ((ResultsRowValue) _data[row - 1]).getOffenceValue().getOffenceDescription())
                        && dataRow.getDefendantOnOffenceId().intValue() == ((ResultsRowValue) _data[row - 1])
                                .getDefendantOnOffenceId().intValue()) {
                    offenceDefendantDuplicate = true;
                }
            }
        }

        switch (col) {
        case CHARGE_COLUMN:
            // This logic needs to stay here as it's specific to table
            // column data display.
            if (chargeDuplicate) {
                cellValue = "";
            } else {
                cellValue = dataRow.getDisposalChargeText();
            }
            break;

        case DESCRIPTION_COLUMN:
            // This logic needs to stay here as it's specific to table
            // column data display.
            // description for breach, offence for other charges.
            /* Existing EDS cludge needs to be removed. Currently for breaches;
             *   defendant info is in the  DESCRIPTION_COLUMN:
             *   and
             *   Offence text is in in the DEFENDANT_COLUMN:
             */
            if (dataRow.isBreach()) {
                if (chargeDuplicate)
                    cellValue = "";
                else
                    cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
            } 
            else {
                if (offenceDefendantDuplicate)
                    cellValue = "";
                else
                    cellValue = dataRow.getDisposalOffenceText();
            }
            break;

        case DEFENDANT_COLUMN:
            // This logic needs to stay here as it's specific to table
            // column data display.
            // offence for breach, description for other charges
            if (dataRow.isBreach()) {
                if (offenceDefendantDuplicate)
                    cellValue = "";
                else
                    if (isBailAct){
                        cellValue = dataRow.getBailActDisposalOffenceText();
                    } else {
                        cellValue = dataRow.getDisposalOffenceText();
                    }
            } else {
                if (offenceDefendantDuplicate)
                    cellValue = "";
                else
                    cellValue = ResultsHelper.getName(dataRow.getDefendantValue());
            }
            break;

        case APPEAL_AGAINST_COLUMN:
            cellValue = dataRow.getDisposalAppealAgainst();
            break;
        case COURTTYPE_COLUMN:
            cellValue = ResultsRowValueHelper.getDisposalCourtType(dataRow.getDisposalValue());
            break;

        case DISPOSAL_COLUMN:
            cellValue = dataRow.getDisposalDesc();
            break;

        case ALTERED_COLUMN:
            cellValue = dataRow.getRowAltered();
            break;
        default:
            cellValue = "";
        }
        return cellValue;
    }

    private void setupTableHeaders() {
        String resources = XhibitBundles.Disposals;
        String[] columnNames;
        if (isBreach || isBailAct) {
            columnNames = new String[] { XHIBITConstant.getResource(resources, "columnNumber"),
                    XHIBITConstant.getResource(resources, "columnDefendant"),
                    XHIBITConstant.getResource(resources, "columnOffence"),
                    XHIBITConstant.getResource(resources, "columnAppealAgainst"),
                    XHIBITConstant.getResource(resources, "columnType"),
                    XHIBITConstant.getResource(resources, "columnDisposal"),
                    XHIBITConstant.getResource(resources, "columnAltered") };
        } else {
            columnNames = new String[] { XHIBITConstant.getResource(resources, "columnNumber"),
                    XHIBITConstant.getResource(resources, "columnOffence"),
                    XHIBITConstant.getResource(resources, "columnDefendant"),
                    XHIBITConstant.getResource(resources, "columnAppealAgainst"),
                    XHIBITConstant.getResource(resources, "columnType"),
                    XHIBITConstant.getResource(resources, "columnDisposal"),
                    XHIBITConstant.getResource(resources, "columnAltered") };
        }
        super.setColumnNames(columnNames);

        String firstColumn = isBreach ? "BreachDescription" : "No";
        Object[] lv = new Object[] { firstColumn, XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED,
                XTable.COLUMN_WIDTH_UNDEFINED, XTable.COLUMN_WIDTH_UNDEFINED, "Tick" };
        setLongValues(lv);
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

    public String getNameForPropertyFile() {
        String chargeType = "";
        if (_data.length != 0) {
            chargeType = ((ResultsRowValue) _data[0]).getChargeType();
        }
        return super.getNameForPropertyFile() + chargeType;
    }

}