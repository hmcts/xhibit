package uk.gov.courtservice.xhibit.client.listdistribution;

import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: AbstractListDistributionTableModel
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
 * @version $Id: AbstractListDistributionTableModel.java,v 1.3 2005/02/22
 *          09:25:32 bzjrnl Exp $
 */
public abstract class AbstractListDistributionTableModel extends XHIBITDefaultTableModel {
    private Class[] columnClasses;

    /**
     * Contruct a new model with the specified data
     */
    public AbstractListDistributionTableModel(Object[] data, String[] columnNames, Class[] columnClasses) {
        setData(data);
        setColumnNames(columnNames);
        setColumnClasses(columnClasses);
    }

    /**
     * Set the column classes
     */
    public void setColumnClasses(Class[] columnClasses) {
        this.columnClasses = columnClasses;
    }

    /**
     * AbstractTableModel Implementation
     */
    public Class getColumnClass(int columnIndex) {
        return columnClasses == null ? super.getColumnClass(columnIndex) : columnClasses[columnIndex];
    }

    /**
     * Get the specifed resource from the list distribution resources
     */
    protected static String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key);
    }

    /**
     * Get the specifed resource from the list distribution resources replacing
     * a single parameter
     */
    public static String getResource(String key, Object parameter) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key, new Object[] { parameter });
    }

}
