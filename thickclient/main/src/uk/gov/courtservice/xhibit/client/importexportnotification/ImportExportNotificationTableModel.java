package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: Import/Export Notification Table Model
 * </p>
 * <p>
 * Description: This class provides access to the data displayed in the table in
 * the case details tab of the import/export notification screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class ImportExportNotificationTableModel extends XHIBITDefaultTableModel {
    public static final int TRANSACTION = 0;

    public static final int STATUS = 1;

    public static final int DATE = 2;

    public static final int DESCRIPTION = 3;

    public static final int DEFENDANT = 4;

    private int _tabType;

    private String[] _columnNames;

    // private ImportExportNotificationHelper helper = new
    // ImportExportNotificationHelper( );

    public ImportExportNotificationTableModel() {
        super();
        _tabType = ImportExportNotificationHelper.COURT_TAB;
        Collection noDataYet = (Collection) new Vector();
        setup(noDataYet);
    }

    public ImportExportNotificationTableModel(int tabType) {
        super();
        _tabType = tabType;
        Collection noDataYet = (Collection) new Vector();
        setup(noDataYet);
    }

    public ImportExportNotificationTableModel(Collection param, int tabType) {
        super();
        _tabType = tabType;
        setup(param);
    }

    private void setup(Collection data) {
        if (_tabType == ImportExportNotificationHelper.CASE_TAB) {
            _columnNames = new String[5];
            setDefaultColumns();
            _columnNames[4] = XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "colDefendant");
        } else {
            _columnNames = new String[4];
            setDefaultColumns();
        }

        setColumnNames(_columnNames);
        setData(data);
    }

    private void setDefaultColumns() {
        _columnNames[0] = XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "colTransaction");
        _columnNames[1] = XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "colStatus");
        _columnNames[2] = XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "colDate");
        _columnNames[3] = XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "colDescription");
    }

    /**
     * Indicates whether or not a particular cell may be edited.
     * 
     * @param r
     * @param c
     * @return boolean
     */
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    /**
     * Returns the data for a specific cell.
     * 
     * @param r
     * @param c
     * @return Object
     */
    public Object getValueAt(int r, int c) {
        ImportExportNotificationTableRowModel myVO = (ImportExportNotificationTableRowModel) getDataAt(r);

        switch (c) {
        case TRANSACTION:
            return XHIBITConstant
                    .getResource(XhibitBundles.ImportExportNotification, "trxCode" + myVO.getTransaction());
        case STATUS:
            return XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "status" + myVO.getStatus());
        case DATE:
            // return( myVO.getEndDate( ) == null ?
            // XDateFormat.format( myVO.getStartDate( ),
            // XDateFormat.DATETIMEINSECSFORMAT ) :
            // XDateFormat.format( myVO.getEndDate( ) ,
            // XDateFormat.DATETIMEINSECSFORMAT )
            // );
            return (myVO.getEndDate() == null ? myVO.getStartDate() : myVO.getEndDate());
        case DESCRIPTION:
            StringBuffer buf = new StringBuffer();

            if (myVO.getDescription().indexOf(":") > -1) {
                String errorCode = myVO.getDescription().substring(0, myVO.getDescription().indexOf(":"));

                if (ResourceBundleHelper.isResourceAvailable(XhibitBundles.ErrorText, errorCode)) {
                    buf.append(XHIBITConstant.getResource(XhibitBundles.ErrorText, myVO.getDescription().substring(0,
                            myVO.getDescription().indexOf(":"))));
                    if (myVO.getDescription().substring(myVO.getDescription().indexOf(":") + 1).length() > 0) {
                        buf.append("\n\n");
                        buf.append(myVO.getDescription().substring(myVO.getDescription().indexOf(":") + 1));
                    }
                } else {
                    buf.append(myVO.getDescription());
                }
            } else {
                buf.append(myVO.getDescription());
            }

            return buf.toString();
        case DEFENDANT:
            return myVO.getDefendantName();
        default:
            return "";
        }
    }

    /**
     * Returns the class of a specific column.
     * 
     * @param c
     * @return Class
     */
    public Class getColumnClass(int c) {
        switch (c) {
        case TRANSACTION:
        case STATUS:
            return String.class;
        case DATE:
            return java.util.Date.class;
        case DESCRIPTION:
            return String.class;
        default:
            return Object.class;
        }
    }
}
