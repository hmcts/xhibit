package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: SelectDefendantsToRemoveModel
 * </p>
 * <p>
 * Description: The table model for Remove Defendants
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Krishna Pokala
 */

public class SelectDefendantsToRemoveModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;

    public static final int DEFENDANT_NAME = 0;

    public static final int REMOVE_FROM_COUNT = 1;

    public static final int DEFENDANT_ON_OFFENCE_ID = 2;

    public static final int DEFENDANT_ID = 3;

    /**
     * Set up that will bring out the header information from resource bundles.
     * 
     * @param Collection
     */
    private void setup(Collection data) {
        setColumnNames(new String[] { lookupResource("removedefendants.table.columnname.defendantName"),
                lookupResource("removedefendants.table.columnname.removefromcount") });
        this.setData(data);
    }

    public SelectDefendantsToRemoveModel() {
        super();
        Collection noDataYet = new Vector();
        setup(noDataYet);
    }

    public SelectDefendantsToRemoveModel(Collection param) {
        super();
        setup(param);
    }

    public boolean isCellEditable(int r, int c) {
        return true;

    }

    public Object getValueAt(int r, int c) {
        SelectDefendantsToRemoveRowModel myVO = (SelectDefendantsToRemoveRowModel) getData().elementAt(r);
        switch (c) {
        case DEFENDANT_ID:
            return myVO.getDefendantId();
        case DEFENDANT_ON_OFFENCE_ID:
            return myVO.getDefendantOnOffenceId();
        case DEFENDANT_NAME:
            return myVO.getDefendantName();
        case REMOVE_FROM_COUNT:
            return myVO.isRemoveFromCount();
        default:
            return "";
        }
    }

    public void setValueAt(Object value, int r, int c) {
        if (c == REMOVE_FROM_COUNT) {
            ((SelectDefendantsToRemoveRowModel) getData().elementAt(r)).setRemoveFromCount(((Boolean) value));
            fireTableDataChanged();
        }
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case DEFENDANT_ID:
            return Integer.class;
        case DEFENDANT_ON_OFFENCE_ID:
            return Integer.class;
        case DEFENDANT_NAME:
            return String.class;
        case REMOVE_FROM_COUNT:
            return Boolean.class;
        default:
            return Object.class;
        }
    }

    private String lookupResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources, key);
    }
}
