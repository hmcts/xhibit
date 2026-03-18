package uk.gov.courtservice.xhibit.client.publicnotice;

import javax.swing.table.DefaultTableModel;

import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Model contains information on Public Notices displayed on
 * PublicNoticesPanel
 * </p>
 * <p>
 * Description: The PublicNoticesPanel contains a table which is populated by
 * this model
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 * 
 */

public class PublicNoticesTableModel extends DefaultTableModel {
    /**
     * int IS_ACTIVE
     */
    public static final int IS_ACTIVE = 0;

    /**
     * int DESCRIPTION
     */
    public static final int DESCRIPTION = 1;

    /**
     * int COLUMN_COUNT
     */
    private static final int COLUMN_COUNT = 2;

    /**
     * DisplayablePublicNoticeValue[] publicNoticeValues
     */
    private DisplayablePublicNoticeValue[] publicNoticeValues = new DisplayablePublicNoticeValue[] {};

    /**
     * <init>
     * 
     * @param publicNoticeValues
     *            parameter for <init>
     */
    public PublicNoticesTableModel(DisplayablePublicNoticeValue[] publicNoticeValues) {
        super();
        this.publicNoticeValues = publicNoticeValues;
        this.getColumnName(IS_ACTIVE);
        this.getColumnName(DESCRIPTION);
        filterNotices();
    }

    /**
     * getColumnCount
     * 
     * @return the returned int
     */
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    /**
     * getRowCount
     * 
     * @return the returned int
     */
    public int getRowCount() {
        if (publicNoticeValues == null) {
            return 0;
        } else {
            return publicNoticeValues.length;
        }
    }

    /**
     * getColumnClass
     * 
     * @param col
     *            parameter for getColumnClass
     * @return the returned Class
     */
    public Class getColumnClass(int col) {
        if (col == IS_ACTIVE) {
            return Boolean.class;
        } else if (col == DESCRIPTION) {
            return String.class;
        } else {
            return Object.class;
        }
    }

    /**
     * getColumnName
     * 
     * @param col
     *            parameter for getColumnName
     * @return the returned String
     */
    public String getColumnName(int col) {
        if (col == IS_ACTIVE) {
            return XHIBITConstant.getResource(XhibitBundles.PublicNotices, "selectColHeading");
        } else if (col == DESCRIPTION) {
            return XHIBITConstant.getResource(XhibitBundles.PublicNotices, "publicNoticeColHeading");
        } else {
            return "";
        }
    }

    /**
     * getValueAt
     * 
     * @param row
     *            parameter for getValueAt
     * @param col
     *            parameter for getValueAt
     * @return the returned Object
     */
    public Object getValueAt(int row, int col) {
        if (col == IS_ACTIVE) {
            return new Boolean(publicNoticeValues[row].getIsActive());
        } else if (col == DESCRIPTION) {
            return publicNoticeValues[row].getDesc();
        } else {
            return "";
        }
    }

    /**
     * setValueAt
     * 
     * @param value
     *            parameter for setValueAt
     * @param row
     *            parameter for setValueAt
     * @param col
     *            parameter for setValueAt
     */
    public void setValueAt(Object value, int row, int col) {
        if (col == IS_ACTIVE) {
            this.publicNoticeValues[row].setIsActive(((Boolean) value).booleanValue());
        } else if (col == DESCRIPTION) {
            this.publicNoticeValues[row].setDesc((String) value);
        }
    }

    /**
     * setData
     * 
     * @param publicNoticeValues
     *            parameter for setData
     */
    public void setData(DisplayablePublicNoticeValue[] publicNoticeValues) {
        this.publicNoticeValues = publicNoticeValues;
    }

    /**
     * getPublicNoticeValues
     * 
     * @return the returned DisplayablePublicNoticeValue[]
     */
    public DisplayablePublicNoticeValue[] getPublicNoticeValues() {
        return publicNoticeValues;
    }

    /**
     * filterNotices This only allows a maximum of 5 public notices to be
     * checked even though more than 5 may be considered active. The chosen 5
     * will be those of with a higher priority. If a high priority is unchecked
     * then subsequent loads will display the next active notice in the priority
     * ranks.
     */
    private void filterNotices() {
        int activeCount = 0;
        for (int i = 0; i < publicNoticeValues.length; i++) {
            if (activeCount == 5) {
                publicNoticeValues[i].setIsActive(false);
            } else if (publicNoticeValues[i].getIsActive()) {
                activeCount++;
            }
        }
    }
}