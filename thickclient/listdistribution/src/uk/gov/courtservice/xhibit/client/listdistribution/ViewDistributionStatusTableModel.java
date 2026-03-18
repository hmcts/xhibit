package uk.gov.courtservice.xhibit.client.listdistribution;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.vos.listdistribution.DistributionStatusComplexValue;

/**
 * <p>
 * Title: DistributeListLettersTableModel
 * </p>
 * <p>
 * Description: The table model used by the screen for distributing list
 * letters.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: Will Fardell, Xdevelopment (2004)
 * </p>
 * 
 * @version $Id: ViewDistributionStatusTableModel.java,v 1.1 2005/02/06 15:49:29
 *          bzjrnl Exp $
 */
public class ViewDistributionStatusTableModel extends AbstractListDistributionTableModel {
    // Column Names
    private static final String[] COLUMN_NAMES = new String[] {
            getResource("viewdistributionstatustablemodel.columnname.type"),
            getResource("viewdistributionstatustablemodel.columnname.datecreated"),
            getResource("viewdistributionstatustablemodel.columnname.datedistributed"),
            getResource("viewdistributionstatustablemodel.columnname.status"),
            getResource("viewdistributionstatustablemodel.columnname.distributionmethod"),
            getResource("viewdistributionstatustablemodel.columnname.recipientnames") };

    // Column Classes
    private static final Class[] COLUMN_CLASSES = new Class[] { String.class, Date.class, Date.class, String.class,
            String.class, String.class };

    // Distribution Type
    private static final Map DISTRIBUTION_TYPE_MAP = new HashMap(3);
    static {
        DISTRIBUTION_TYPE_MAP.put("EMAIL", getResource("viewdistributionstatustablemodel.distributiontype.EMAIL"));
        DISTRIBUTION_TYPE_MAP.put("POST", getResource("viewdistributionstatustablemodel.distributiontype.POST"));
        DISTRIBUTION_TYPE_MAP.put("FAX", getResource("viewdistributionstatustablemodel.distributiontype.FAX"));
        DISTRIBUTION_TYPE_MAP.put("FTP", getResource("viewdistributionstatustablemodel.distributiontype.FTP"));
    }

    // Status
    private static final Map STATUS_MAP = new HashMap(14);
    static {
        STATUS_MAP.put("PR", getResource("viewdistributionstatustablemodel.status.PR"));
        STATUS_MAP.put("PC", getResource("viewdistributionstatustablemodel.status.PC"));
        STATUS_MAP.put("RP", getResource("viewdistributionstatustablemodel.status.RP"));
        STATUS_MAP.put("ND", getResource("viewdistributionstatustablemodel.status.ND"));
        STATUS_MAP.put("FD", getResource("viewdistributionstatustablemodel.status.FD"));
        STATUS_MAP.put("DR", getResource("viewdistributionstatustablemodel.status.DR"));
        STATUS_MAP.put("FE", getResource("viewdistributionstatustablemodel.status.FE"));
        STATUS_MAP.put("FF", getResource("viewdistributionstatustablemodel.status.FF"));
        STATUS_MAP.put("DA", getResource("viewdistributionstatustablemodel.status.DA"));
        STATUS_MAP.put("RJ", getResource("viewdistributionstatustablemodel.status.RJ"));
        STATUS_MAP.put("FT", getResource("viewdistributionstatustablemodel.status.FT"));
        STATUS_MAP.put("SE", getResource("viewdistributionstatustablemodel.status.SE"));
        STATUS_MAP.put("SF", getResource("viewdistributionstatustablemodel.status.SF"));
        STATUS_MAP.put("SX", getResource("viewdistributionstatustablemodel.status.SX"));
        STATUS_MAP.put("EE", getResource("viewdistributionstatustablemodel.status.EE"));
        STATUS_MAP.put("EF", getResource("viewdistributionstatustablemodel.status.EF"));
    }

    // Type
    private static final Map TYPE_MAP = new HashMap(26);
    static {
        TYPE_MAP.put("DL", getResource("viewdistributionstatustablemodel.type.DL"));
        TYPE_MAP.put("RL", getResource("viewdistributionstatustablemodel.type.RL"));
        TYPE_MAP.put("WL", getResource("viewdistributionstatustablemodel.type.WL"));
        TYPE_MAP.put("FL", getResource("viewdistributionstatustablemodel.type.FL"));
        TYPE_MAP.put("IN", getResource("viewdistributionstatustablemodel.type.IN"));
        TYPE_MAP.put("SS", getResource("viewdistributionstatustablemodel.type.SS"));
        TYPE_MAP.put("RH", getResource("viewdistributionstatustablemodel.type.RH"));
        TYPE_MAP.put("PO", getResource("viewdistributionstatustablemodel.type.PO"));
        TYPE_MAP.put("PR", getResource("viewdistributionstatustablemodel.type.PR"));
        TYPE_MAP.put("RO", getResource("viewdistributionstatustablemodel.type.RO"));
        TYPE_MAP.put("BO", getResource("viewdistributionstatustablemodel.type.BO"));
        TYPE_MAP.put("IO", getResource("viewdistributionstatustablemodel.type.IO"));
        TYPE_MAP.put("YO", getResource("viewdistributionstatustablemodel.type.YO"));
        TYPE_MAP.put("CR", getResource("viewdistributionstatustablemodel.type.CR"));
        TYPE_MAP.put("AR", getResource("viewdistributionstatustablemodel.type.AR"));
        TYPE_MAP.put("TR", getResource("viewdistributionstatustablemodel.type.TR"));
        TYPE_MAP.put("WW", getResource("viewdistributionstatustablemodel.type.WW"));
        TYPE_MAP.put("MC", getResource("viewdistributionstatustablemodel.type.MC"));
        TYPE_MAP.put("NA", getResource("viewdistributionstatustablemodel.type.NA"));
        TYPE_MAP.put("BW", getResource("viewdistributionstatustablemodel.type.BW"));
        TYPE_MAP.put("DLP", getResource("viewdistributionstatustablemodel.type.DLP"));
        TYPE_MAP.put("WLD", getResource("viewdistributionstatustablemodel.type.WLD"));
        TYPE_MAP.put("WLL", getResource("viewdistributionstatustablemodel.type.WLL"));
        TYPE_MAP.put("FLD", getResource("viewdistributionstatustablemodel.type.FLD"));
        TYPE_MAP.put("FLL", getResource("viewdistributionstatustablemodel.type.FLL"));
        TYPE_MAP.put("DLD", getResource("viewdistributionstatustablemodel.type.DLD"));
        TYPE_MAP.put("DLL", getResource("viewdistributionstatustablemodel.type.DLL"));
        TYPE_MAP.put("IWP", getResource("viewdistributionstatustablemodel.type.IWP"));
    }

    /**
     * Contruct a new model with no data
     */
    public ViewDistributionStatusTableModel() {
        this(null);
    }

    /**
     * Contruct a new model with the specified data
     */
    public ViewDistributionStatusTableModel(DistributionStatusComplexValue[] data) {
        super(data, COLUMN_NAMES, COLUMN_CLASSES);
    }

    /**
     * AbstractTableModel Implementation
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return getType(rowIndex);
        case 1:
            return getCreationDate(rowIndex);
        case 2:
            return getDistributionDate(rowIndex);
        case 3:
            return getStatus(rowIndex);
        case 4:
            return getDistributionType(rowIndex);
        case 5:
            return getRecipientNames(rowIndex);
        default:
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }
    }

    /**
     * Get the index of the column to sort on by default
     */
    public int getDefaultSortColumnIndex() {
        return 1;
    }

    /**
     * Get the index of the column to sort on by default
     */
    public boolean getDefaultSortAsscending() {
        return false;
    }

    public static String getType(DistributionStatusComplexValue value) {
        return value == null ? null : (String) TYPE_MAP.get(value.getDocumentType());
    }

    public static Date getCreationDate(DistributionStatusComplexValue value) {
        return value.getCreationDate();
    }

    private String getType(int rowIndex) {
        return getType(getDistributionStatusComplexValue(rowIndex));
    }

    private Date getCreationDate(int rowIndex) {
        return getCreationDate(getDistributionStatusComplexValue(rowIndex));
    }

    private Date getDistributionDate(int rowIndex) {
        return getDistributionStatusComplexValue(rowIndex).getDistributionDate();
    }

    private String getStatus(int rowIndex) {
        return (String) STATUS_MAP.get(getDistributionStatusComplexValue(rowIndex).getStatus());
    }

    private String getDistributionType(int rowIndex) {
        return (String) DISTRIBUTION_TYPE_MAP.get(getDistributionStatusComplexValue(rowIndex).getDistributionType());
    }

    private String getRecipientNames(int rowIndex) {
        DistributionStatusComplexValue value = getDistributionStatusComplexValue(rowIndex);

        if (value.hasRecipientNames()) {
            int c = value.getRecipientCount();

            if (c == 1) // Optimised for most common
            {
                return value.getRecipientName(0);
            } else if (c > 1) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(value.getRecipientName(0));
                buffer.append(", ");
                buffer.append(value.getRecipientName(1));
                for (int i = 2; i < c; i++) {
                    buffer.append(", ");
                    buffer.append(value.getRecipientName(i));
                }
                return buffer.toString();
            }
        }

        return getResource("viewdistributionstatustablemodel.recipientcount", new Integer(value.getRecipientCount()));
    }

    /**
     * Get the value at the specified row.
     */
    public DistributionStatusComplexValue getDistributionStatusComplexValue(int rowIndex) {
        return (DistributionStatusComplexValue) getDataAt(rowIndex);
    }
}
