package uk.gov.courtservice.xhibit.client.listdistribution;

import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

/**
 * <p>
 * Title: DefaultMaintainListLetterRecipientsTableModel
 * </p>
 * <p>
 * Description: This table model is used for displaying recipients with no
 * delivery method specified.
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
 * @version $Id: DefaultMaintainListLetterRecipientsTableModel.java,v 1.2
 *          2005/02/22 09:25:32 bzjrnl Exp $
 */
public class DefaultMaintainListLetterRecipientsTableModel extends AbstractMaintainListLetterRecipientsTableModel {
    // Column Names
    private static String[] COLUMN_NAMES = new String[] {
            getResource("distributelistletterstablemodel.columnname.type"),
            getResource("distributelistletterstablemodel.columnname.name"),
            getResource("distributelistletterstablemodel.columnname.address"), };

    // Column Classes
    private static Class[] COLUMN_CLASSES = new Class[] { String.class, String.class, String.class, };

    /**
     * Contruct a new model with the specified data
     */
    public DefaultMaintainListLetterRecipientsTableModel(WllRecipientComplexValue[] data) {
        super(data, COLUMN_NAMES, COLUMN_CLASSES);
    }

    /**
     * AbstractTableModel Implementation
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return getRecipientType(rowIndex);
        case 1:
            return getName(rowIndex);
        case 2:
            return getAddress(rowIndex);
        default:
            throw new IllegalArgumentException("columnIndex: " + columnIndex);
        }
    }

}
