package uk.gov.courtservice.xhibit.client.xhibitapplication.tablemodel;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: Table for displaying versions of XHIBIT components
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: VersionTableModel.java,v 1.4 2006/06/05 12:31:43 bzjrnl Exp $
 */

public class VersionTableModel extends XHIBITDefaultTableModel {
    public static final int COL_DISPLAY_NAME = 0;

    public static final int COL_VERSION = 1;

    public static final int COL_UPDATE_DATE = 2;

    public VersionTableModel() {
        setColumnNames(new String[] { "Application Component", "Version", "Date" });
        setLongValues(new Object[] { "Application Component", "Version", new Date() });
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        VersionValue vv = (VersionValue) _data[rowIndex];
        switch (columnIndex) {
        case COL_DISPLAY_NAME:
            return vv.getDisplayName();
        case COL_VERSION:
            return vv.getSchemaVersion();
        case COL_UPDATE_DATE:
            return vv.getLastUpdateDate();
        }
        return "";
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case COL_DISPLAY_NAME:
        case COL_VERSION:
            return String.class;
        case COL_UPDATE_DATE:
            return java.util.Date.class;
        default:
            return super.getColumnClass(col);
        }

    }
}