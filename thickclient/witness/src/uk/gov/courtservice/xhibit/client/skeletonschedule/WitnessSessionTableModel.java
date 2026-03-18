package uk.gov.courtservice.xhibit.client.skeletonschedule;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.WitnessSessionSorter;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: WitnessSessionTableModel
 * </p>
 * <p>
 * Description: The witness session table model
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version $Id: WitnessSessionTableModel.java,v 1.7 2006/02/08 16:22:50 xztnfq
 *          Exp $
 */
public class WitnessSessionTableModel extends XHIBITDefaultTableModel // SortableTableModel
{
    /*
     * Resource Key Constants
     */

    private static final String[] COLUMN_NAME_KEYS = new String[] { "skeletonschedule.table.day.column",
            "skeletonschedule.table.session.column", "skeletonschedule.table.name.column",
            "skeletonschedule.table.status.column", "skeletonschedule.table.type.column",
            "skeletonschedule.table.expected.column", };

    public static final int DAY_NUMBER_COL = 0;

    public static final int SESSION_TYPE_COL = 1;

    public static final int NAME_COL = 2;

    public static final int STATUS_COL = 3;

    public static final int TYPE_COL = 4;

    public static final int EXPECTED_COL = 5;

    /*
     * Logger
     */

    private static final Logger log = CSServices.getLogger(SkeletonSchedulePanel.class);

    /*
     * Implementation
     */

    public WitnessSessionTableModel() {
        this(new WitnessSession[0]);
    }

    public WitnessSessionTableModel(WitnessSession[] newData) {
        setData(WitnessSessionSorter.sort(newData));
        setColumnNames(getColumnNamesArray());
    }

    /**
     * Override super method so that data is sorted and checked for null before
     * being added to model
     * 
     * @param newData
     */
    public void setData(Object[] newData) {
        WitnessSession[] ws = (newData == null ? new WitnessSession[0] : (WitnessSession[]) newData);
        super.setData(WitnessSessionSorter.sort(ws));
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        WitnessSession ws = (WitnessSession) _data[rowIndex];
        switch (columnIndex) {
        case DAY_NUMBER_COL:
            return new Integer(ws.getDayNumber());
        case SESSION_TYPE_COL:
            // Cosmetic Bug X53677
            return getResource("skeletonschedule.session." + ws.getSessionType());
        case NAME_COL:
            return ws.getName();
        case STATUS_COL:
            return ws.getStatus();
        case TYPE_COL:
            return ws.getType();
        case EXPECTED_COL:
            return ws.getExpected();
        default:
            return "";
        }
    }

    /*
     * Utility Methods
     */

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }

    private static final String[] getColumnNamesArray() {
        String[] columnNames;
        columnNames = new String[COLUMN_NAME_KEYS.length];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = getResource(COLUMN_NAME_KEYS[i]);
        }
        return columnNames;
    }

}
