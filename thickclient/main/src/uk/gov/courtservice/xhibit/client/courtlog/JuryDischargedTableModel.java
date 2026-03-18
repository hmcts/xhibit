package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */

public class JuryDischargedTableModel extends XHIBITDefaultTableModel {
    public static final int ID_COLUMN = 0;

    public static final int NAME_COLUMN = 1;

    public static final int JUROR_NO_COLUMN = 2;

    public JuryDischargedTableModel(Juror[] arrayJuror) {
        setData(arrayJuror);
        String[] cols = {
                " ", // XHIBITConstant.getResource(XhibitBundles.JuryDischarged,
                // "colId"),
                ResourceBundleHelper.getResource(XhibitBundles.JuryDischarged, "colName"),
                ResourceBundleHelper.getResource(XhibitBundles.JuryDischarged, "colJurorNo") };
        setColumnNames(cols);
    }

    // public Juror[] getArrayJuror() {
    // Juror[] juror = new Juror[_data.length];
    // for (int i = 0; i < _data.length; i++)
    // {
    // juror[i] = (Juror)_data[i];
    // }
    // return juror;
    // }

    public Object getValueAt(int r, int c) {

        String cellValue;
        Juror jurorCell = (Juror) _data[r];
        switch (c) {
        case 0:
            cellValue = "" + (r + 1);
            break;
        case 1:
            cellValue = (jurorCell == null || jurorCell.getName() == null) ? "" : jurorCell.getName();
            break;
        case 2:
            cellValue = (jurorCell == null || jurorCell.getJurorId() == null) ? "" : jurorCell.getJurorId();
            break;
        default:
            cellValue = null;
            break;
        }
        return cellValue;
    }

    public void setValueAt(Object value, int r, int c) {
        Juror jurorCell = (Juror) _data[r];
        switch (c) {
        case 0:
            break;
        case 1:
            jurorCell.setName(value == null ? "" : value.toString());
            fireTableCellUpdated(r, c);
            break;
        case 2:
            jurorCell.setJurorId(value == null ? "" : value.toString());
            fireTableCellUpdated(r, c);
            break;
        }
    }

    public boolean isCellEditable(int r, int c) {
        return (c > 0);
    }

    public Class getColumnClass(int c) {
        return String.class;
    }
}
