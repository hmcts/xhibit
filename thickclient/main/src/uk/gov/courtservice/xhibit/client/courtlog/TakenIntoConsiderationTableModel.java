package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

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
 * @author David Crossland
 * @version 1.0
 * 
 * This class has a unit test. Please update in line with any changes.
 */
public class TakenIntoConsiderationTableModel extends XHIBITDefaultTableModel {
    // for logging
    private static final Logger log = Logger.getLogger(TakenIntoConsiderationTableModel.class);

    // constants
    public static final int DEFENDANT_NAME_COLUMN = 0;

    public static final int TIC_COLUMN = 1;

    private static final String appellant = "A";

    private String caseType;

    private final String DefendantLabel = "lblDefendantName";

    private final String AppellantLabel = "lblAppellantName";

    private final String TICSLabel = "lblTIC";

    private String resources = XhibitBundles.TakenIntoConsideration;

    /**
     * Creates a new TakenIntoConsiderationTableModel using the specified
     * Collection of <code>TakenIntoConsiderationTableRowModel</code>'s as
     * its underlying data source
     */
    public TakenIntoConsiderationTableModel(Collection param, String caseType) {
        XHIBITConstant.debug("TakenIntoConsiderationTableModel( Collection ) Constructor entered");
        setData(param);
        this.caseType = caseType;
        setColumnNames(getColumnNames(resources, DefendantLabel, AppellantLabel));
    }

    //
    // the following override parent class methods
    //
    public boolean isCellEditable(int r, int c) {
        return (c == TIC_COLUMN);
    }

    public Class getColumnClass(int c) {
        return String.class;
    }

    public Object getValueAt(int r, int c) {
        switch (c) {
        case DEFENDANT_NAME_COLUMN:
            return getRowModel(r).getFullName();
        case TIC_COLUMN:
            return getRowModel(r).getNoOfTics();
        default:
            return "";
        }
    }

    // JP : changed table model type of TIC column from Integer to string
    // to make is easier to validate
    public void setValueAt(Object value, int r, int c) {
        if (c == TIC_COLUMN) {
            String ticString = (String) value;
            if (ticString.length() == 0) {
                getRowModel(r).setNoOfTics(null);
            } else {
                getRowModel(r).setNoOfTics(new Integer(ticString));
            }
            fireTableCellUpdated(r, c);
        }
    }

    // end of AbstractTableModel methods

    public Object[] getDefendantArray() {
        return _data;
    }

    public Collection getDefendantCollection() {
        Collection coll = new Vector();

        for (int x = 0; x < _data.length; x++) {
            coll.add(_data[x]);
        }

        return coll;
    }

    private TakenIntoConsiderationTableRowModel getRowModel(int r) {
        return (TakenIntoConsiderationTableRowModel) _data[r];
    }

    //
    // private methods
    //
    private String[] getColumnNames(String resource, String defLabel, String appLabel) {

        String temp[] = new String[2];
        if (caseType.equals(appellant)) {
            temp[0] = XHIBITConstant.getResource(resource, appLabel);
        } else {
            temp[0] = XHIBITConstant.getResource(resource, defLabel);

        }
        temp[1] = XHIBITConstant.getResource(resources, TICSLabel);
        return temp;
    }
}
