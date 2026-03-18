package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2 - Defendant Offense Table Model
 * </p>
 * <p>
 * Description: Table model that holds defendant/offence specific data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class DefendantOffenceTableModel extends XHIBITDefaultTableModel {

    private static final long serialVersionUID = 1L;

    /** Column index of Column representing count number. */
    public static final int COUNT_NUMBER = 0;

    /** Column index representing the offence description. */
    public static final int OFFENCE_DESCRIPTION = 1;

    /**
     * Column index representing the list of defendants associated to its
     * offence.
     */
    public static final int DEFENDANTS = 2;

    /** The cache for storing the list of defendants against an offence. */
    private HashMap<Integer, String> defendantListCache;
    
    /**
     * The resources that are responsible for rendering the fixed text of this
     * model.
     */
    private String resources;

    /**
     * Default constructor. Will instantiate a non-editable model, that will
     * contain data specific to defendants and offences under a selected
     * indictment.
     */
    public DefendantOffenceTableModel() {
        super();
        this.defendantListCache = new HashMap<Integer, String>();
        this.resources = XhibitBundles.JoinderResources;
        setColumnNames(new String[] { 
                XHIBITConstant.getResource(this.resources, JoinderConstants.COUNT),
                XHIBITConstant.getResource(this.resources, JoinderConstants.OFFENCE_DESC),
                XHIBITConstant.getResource(this.resources, JoinderConstants.DEFENDANT) });
    }

    public void setData(ChargeValue indictmentValue) {
        super.setData(indictmentValue.getOffenceValues().toArray());
        this.setCache();
    }

    /**
     * Overrides the getValueAt() method of XTableModelHelper to return specific
     * content to the column.
     * 
     * @param row
     *            the row to which the value will be contained in
     * @param col
     *            the column to which the value will be contained in
     * 
     * @return the value specific to the column:
     * 
     * COLUMN 1 - will return the number of the count. COLUMN 2 - will return
     * the offence description COLUMN 3 - will return the list of defendants
     * associated to the offence.
     */
    public Object getValueAt(int row, int col) {
        if (row < 0 || row >= _data.length) {
            return "";
        }
        OffenceValue offenceValue = (OffenceValue) _data[row];
        switch (col) {
        case COUNT_NUMBER:
            return XHIBITConstant.getResource(resources, JoinderConstants.COUNT) + " "
            + offenceValue.getCrestOffenceSeqNo();
        case OFFENCE_DESCRIPTION:
            return offenceValue.getOffenceDescription();
            // Strictly speaking this should be in the renderer, maybe
            // refactor later..
        case DEFENDANTS:
            return this.defendantListCache.get(offenceValue.getOffenceID());
        default:
            return "";
        }
    }

    /**
     * Set the column names for the model.
     */
    protected void setColumnNames() {
        // empty
    }

    /**
     * Traverse the list of offences and retrieve all the defendants. Store the
     * defendants in a cache, and if there is more than one defendant for an
     * offence, then the defendants are separated via the "," delimiter.
     */
    private void setCache() {
        OffenceValue offenceValue = null;
        Iterator defendantValues = null;
        StringBuffer defendantList = new StringBuffer();
        for (int i = 0; i < _data.length; i++) {
            offenceValue = (OffenceValue) _data[i];

            if ((offenceValue == null) || (offenceValue.getDefendantValues() == null))
                continue;

            defendantValues = offenceValue.getDefendantValues().iterator();
            DefendantValue defendantValue = null;
            while (defendantValues.hasNext()) {
                defendantValue = (DefendantValue) defendantValues.next();
                defendantList.append(defendantValue.getFirstName() + " " + defendantValue.getSurName());

                // Add the delimiter if there other elements.
                if (defendantValues.hasNext()) {
                    defendantList.append(",");
                }
            }

            // Store the defendant list in its own cache.
            this.defendantListCache.put(offenceValue.getOffenceID(), defendantList.toString());

            // Reset the defendantList
            defendantList.delete(0, defendantList.length());
        }
    }

    public void moveUp(int rowIndex) {
        // If the row index is the first index, cannot move up 1, so return.
        if (rowIndex == 0) {
            return;
        }
        
        int seq = ((OffenceValue) _data[rowIndex]).getCrestOffenceSeqNo().intValue();
        ((OffenceValue) _data[rowIndex]).setCrestOffenceSeqNo(new Integer(seq - 1));
        int seq2 = ((OffenceValue) _data[rowIndex - 1]).getCrestOffenceSeqNo().intValue();
        ((OffenceValue) _data[rowIndex - 1]).setCrestOffenceSeqNo(new Integer(seq2 + 1));

        // Now Swap the rows.
        Object temp = _data[rowIndex - 1];
        _data[rowIndex - 1] = _data[rowIndex];
        _data[rowIndex] = temp;
    }

    public void moveDown(int rowIndex) {
        // If the row index is the last index, cannot move down 1, so return.
        if (rowIndex == (_data.length - 1)) {
            return;
        }
        
        int seq = ((OffenceValue) _data[rowIndex]).getCrestOffenceSeqNo().intValue();
        ((OffenceValue) _data[rowIndex]).setCrestOffenceSeqNo(new Integer(seq + 1));
        int seq2 = ((OffenceValue) _data[rowIndex + 1]).getCrestOffenceSeqNo().intValue();
        ((OffenceValue) _data[rowIndex + 1]).setCrestOffenceSeqNo(new Integer(seq2 - 1));

        // Now Swap the rows.
        Object temp = _data[rowIndex + 1];
        _data[rowIndex + 1] = _data[rowIndex];
        _data[rowIndex] = temp;
    }

    public Collection<OffenceValue> getOffences() {
        ArrayList<OffenceValue> offenceList = new ArrayList<OffenceValue>();
        for (int i = 0; i < _data.length; i++) {
            offenceList.add((OffenceValue)_data[i]);
        }
        return offenceList;
    }
}