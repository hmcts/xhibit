package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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
 * @author Simon Gilmore EDS
 * @version 1.0 23/4/03 RL - Modified to remove defendant. defendant name will
 *          be displayed on panel
 */
public class BreachSummaryTableModel extends AbstractTableModel {
    private static final int TABLE_COLUMNS = 2;

    public static final int OFFENCE_COLUMN = 0;

    public static final int DESCRIPTION_COLUMN = 1;

    private String[] columnNames = new String[TABLE_COLUMNS];

    private OffenceValue offence;

    private List arrayTableCounts;

    private Collection colCountsOrOffences;

    private ChargeSummaryTableRow chargeSummaryTableRow;

    public BreachSummaryTableModel(ChargeValue chargeValue) {
        getColumnHeaders();

        arrayTableCounts = new ArrayList();

        if (chargeValue != null) {
            if (chargeValue.getOffenceValues() != null) {
                colCountsOrOffences = chargeValue.getOffenceValues();

                Iterator iterator = colCountsOrOffences.iterator();
                while (iterator.hasNext()) {
                    offence = (OffenceValue) iterator.next();
                    chargeSummaryTableRow = new ChargeSummaryTableRow(offence, null, offence.getCrestOffenceSeqNo()
                            .toString(), offence.getOffenceDescription(), "", "", null);
                    arrayTableCounts.add(chargeSummaryTableRow);
                }
                Sorter.sort(arrayTableCounts, new String[] { "sortCount" });
            }
        }
    }

    private void getColumnHeaders() {
        columnNames[0] = ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, "columnNumber");
        columnNames[1] = ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, "columnOffence");
    }

    public String getColumnName(int c) {
        return columnNames[c];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int r, int c) {
        String cellValue;

        if (arrayTableCounts.size() <= 0)
            return null;
        switch (c) {
        case 0:
            cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getCount();
            break;
        case 1:
            cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getOffenceDescription();
            break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }

    public int getRowCount() {
        return arrayTableCounts.size();
    }

    public ChargeSummaryTableRow getRow(int row) {
        return (ChargeSummaryTableRow) arrayTableCounts.get(row);
    }
}