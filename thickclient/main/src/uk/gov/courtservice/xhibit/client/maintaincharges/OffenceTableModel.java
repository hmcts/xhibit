package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
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
 * @authorSimon Gilmore EDS
 * @version 1.0
 */
public class OffenceTableModel extends XHIBITDefaultTableModel {
    public static final int DEFENDANT_COLUMN = 0;

    public static final int OFFENCE_COLUMN = 1;

    public static final int DESCRIPTION_COLUMN = 2;

    private List arrayTableCounts;

    public OffenceTableModel(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue) {
        // Holders for collections
        Collection colOffences;
        Collection colDefendants;

        // Holders for value objects
        // ChargeValue chargeValue;
        OffenceValue offenceValue;
        DefendantValue defendant;
        ChargeSummaryTableRow chargeSummaryTableRow;

        // this.chargeValue = chargeValue;

        getColumnHeaders();

        arrayTableCounts = new ArrayList();

        // get offences
        if (chargeValue != null) {
            if (chargeValue.getOffenceValues() != null) {
                colOffences = chargeValue.getOffenceValues();
                Iterator offenceIterator = colOffences.iterator();
                while (offenceIterator.hasNext()) {
                    offenceValue = (OffenceValue) offenceIterator.next();
                    colDefendants = offenceValue.getDefendantValues();

                    if (colDefendants != null && colDefendants.size() > 0) {
                        Iterator defendantIterator = colDefendants.iterator();
                        while (defendantIterator.hasNext()) {
                            defendant = (DefendantValue) defendantIterator.next();
                            String crestOffenceSeqNo = offenceValue.getCrestOffenceSeqNo().toString();
                            String offenceDescription = offenceValue.getOffenceDescription();
                            String defendantName = defendant.getFirstName() + " " + defendant.getMiddleName() + " "
                                    + defendant.getSurName();
                            chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, defendant,
                                    crestOffenceSeqNo, offenceDescription, defendantName, "status Stay/LOF",null);
                            arrayTableCounts.add(chargeSummaryTableRow);
                        }
                    } else {
                        // No defendants for this offence.
                        String crestOffenceSeqNo = offenceValue.getCrestOffenceSeqNo().toString();
                        String offenceDescription = offenceValue.getOffenceDescription();
                        chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, null, crestOffenceSeqNo,
                                offenceDescription, "", "",null);
                        /** @todo Status column - Iteration 2 */
                        arrayTableCounts.add(chargeSummaryTableRow);
                    }
                }
                Sorter.sort(arrayTableCounts, new String[] { "defendant", "sortCount" });
            }
        }
        setData(arrayTableCounts.toArray());
    }

    private void getColumnHeaders() {
        String[] cols = { XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnDefendant"),
                XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnNumber"),
                XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnOffence") };
        setColumnNames(cols);
    }

    public Object getValueAt(int r, int c) {
        String cellValue;

        if (_data.length <= 0)
            return null;

        // ChargeSummaryTableRow cell = (ChargeSummaryTableRow)
        // arrayTableCounts.get(r);
        ChargeSummaryTableRow cell = (ChargeSummaryTableRow) _data[r];
        switch (c) {
        case 0:
            cellValue = cell.getDefendant();
            break;
        case 1:
            cellValue = cell.getCount();
            break;
        case 2:
            cellValue = cell.getOffenceDescription();
            break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }

    public ChargeSummaryTableRow getRow(int row) {
        return (ChargeSummaryTableRow) arrayTableCounts.get(row);
    }

}
