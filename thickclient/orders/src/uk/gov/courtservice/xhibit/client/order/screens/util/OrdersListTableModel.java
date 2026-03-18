package uk.gov.courtservice.xhibit.client.order.screens.util;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;

/**
 * <p>
 * Title: Xhibit2 OrdersListTableModel
 * </p>
 * <p>
 * Description: Table model for the list of orders displayed either in view
 * mode, or if the user tries to create an existing order type
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrdersListTableModel extends AbstractTableModel {
    private XhbOrderValue[] data;

    private String[] columnNames;

    /**
     * 
     * @return
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 9:
            return (data[rowIndex].getOrderId() == null) ? "" : (Object) data[rowIndex].getOrderId();
        case 0:
            return (data[rowIndex].getXhbOrderStatus().getCode() == null) ? "" : (Object) data[rowIndex]
                    .getXhbOrderStatus().getCode();
        case 1:
            return (data[rowIndex].getXhbOrderTemplate().getXhbOrderType().getCode() == null) ? ""
                    : (Object) data[rowIndex].getXhbOrderTemplate().getXhbOrderType().getCode();
        case 2:
            return (data[rowIndex].getDescription() == null) ? "" : (Object) data[rowIndex].getDescription();
        default:
            return null;

        }
    }

    /**
     * If any records are held in data return the length, otherwise return 0
     * 
     * @return int
     */
    public int getRowCount() {
        if (data != null) {
            return data.length;
        } else {
            return 0;
        }
    }

    /**
     * 
     * @param v
     */
    public void setData(XhbOrderValue[] v) {
        data = v;
    }

    /**
     * 
     * @param v
     */
    public void setColumnNames(String[] v) {
        columnNames = v;
    }

    /**
     * 
     * @param c
     * @return
     */
    public String getColumnName(int c) {
        return columnNames[c];
    }
}