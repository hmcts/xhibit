package uk.gov.courtservice.xhibit.client.listdistribution;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;

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
 * @version $Id: DistributeListLettersTableModel.java,v 1.3 2005/02/22 09:25:33
 *          bzjrnl Exp $
 */
public class DistributeListLettersTableModel extends AbstractListDistributionTableModel {

    // Column Names
    private static String[] COLUMN_NAMES = new String[] {
            getResource("distributelistletterstablemodel.columnname.listtitle"),
            getResource("distributelistletterstablemodel.columnname.liststatus"),
            getResource("distributelistletterstablemodel.columnname.letterscount"),
            getResource("distributelistletterstablemodel.columnname.lettersemail"),
            getResource("distributelistletterstablemodel.columnname.lettersfax"),
            getResource("distributelistletterstablemodel.columnname.letterspost") };

    // Column Classes
    private static Class[] COLUMN_CLASSES = new Class[] { String.class, String.class, Integer.class, Double.class,
            Double.class, Double.class, };

    // List Statuses
    private static Map LIST_STATUS_MAP = new HashMap(9);
    static {
        LIST_STATUS_MAP.put("AR", getResource("distributelistletterstablemodel.liststatus.AR"));
        LIST_STATUS_MAP.put("PL", getResource("distributelistletterstablemodel.liststatus.PL"));
        LIST_STATUS_MAP.put("PR", getResource("distributelistletterstablemodel.liststatus.PR"));
        LIST_STATUS_MAP.put("PC", getResource("distributelistletterstablemodel.liststatus.PC"));
        LIST_STATUS_MAP.put("LE", getResource("distributelistletterstablemodel.liststatus.LE"));
    }

    /**
     * Contruct a new model with no data
     */
    public DistributeListLettersTableModel() {
        this(null);
    }

    /**
     * Contruct a new model with the specified data
     */
    public DistributeListLettersTableModel(WllControlComplexValue[] data) {
        super(data, COLUMN_NAMES, COLUMN_CLASSES);
    }

    /**
     * AbstractTableModel Implementation
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return getTitle(rowIndex);
        case 1:
            return getStatus(rowIndex);
        case 2:
            return getLetterCount(rowIndex);
        case 3:
            return getLetterEmailPercentage(rowIndex);
        case 4:
            return getLetterFaxPercentage(rowIndex);
        case 5:
            return getLetterPostPercentage(rowIndex);
        default:
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }
    }

    private String getTitle(int rowIndex) {
        return getWllControlComplexValue(rowIndex).getDocumentTitle();
    }

    private String getStatus(int rowIndex) {
        return (String) LIST_STATUS_MAP.get(getWllControlComplexValue(rowIndex).getListStatus());
    }

    private Integer getLetterCount(int rowIndex) {
        return getWllControlComplexValue(rowIndex).getLetterCount();
    }

    private Double getLetterEmailPercentage(int rowIndex) {
        return getWllControlComplexValue(rowIndex).getLetterEmailPercentage();
    }

    private Double getLetterFaxPercentage(int rowIndex) {
        return getWllControlComplexValue(rowIndex).getLetterFaxPercentage();
    }

    private Double getLetterPostPercentage(int rowIndex) {
        return getWllControlComplexValue(rowIndex).getLetterPostPercentage();
    }

    /**
     * Get the value at the specified row.
     */
    public WllControlComplexValue getWllControlComplexValue(int rowIndex) {
        return (WllControlComplexValue) getDataAt(rowIndex);
    }

}
