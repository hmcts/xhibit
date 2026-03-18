package uk.gov.courtservice.xhibit.client.util.table.model;

import javax.swing.table.AbstractTableModel;

/**
 * <p>
 * Title: XHIBIT Table Model Interface
 * </p>
 * <p>
 * Description: This interface is intended to be used by any table model that
 * wishes to extend the functionality of XHIBIT Default Table Model. This
 * follows the principles of the decorator pattern, though possibly not a true
 * implementation of this pattern.
 * 
 * When getting the model from a table it can be casted to this class type so
 * that the standard get/set methods will be available regardless of what the
 * table model has been decorated with.
 * 
 * Examples of use is un the sorting model. The intension is that the methods in
 * this interface as those available through the standard
 * XHIBITDefaultTableModel. The class implementing this will not store any data,
 * but carry out some pre-processing before passing the call onto the underlying
 * XHIBITTableModel which is typically stored in a local variable.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XHIBITTableModelInterface.java,v 1.2 2005/02/11 16:42:00 sz0t7n
 *          Exp $
 */

public abstract class XHIBITTableModelInterface extends AbstractTableModel {
    // Column Names
    /**
     * Used to display the column headers. Used by getColumnName for a cleaner
     * implementation
     * 
     * @return String array of column names
     */
    public abstract String[] getColumnNames();

    /**
     * @param x
     *            String array of column names
     */
    public abstract void setColumnNames(String[] columnNames);

    /**
     * The values used to initially size the columns The coder should use this
     * call when calling initColumnSizes
     * 
     * @return
     */
    public abstract Object[] getLongValues();

    /**
     * Pass the values that should be used to initially size the columsn
     * 
     * @param x
     */
    public abstract void setLongValues(Object[] x);

    /**
     * The method is typically used to return a value object which would
     * correspond to a row in the table. If this functionality is not required
     * it should throw an unsuppored operation so that it is not used
     * inadvertantly.
     * 
     * @param row
     * @return
     */
    public abstract Object getDataAt(int row);

    /**
     * Returns the data stored in the table model
     * 
     * @return
     */
    // public abstract Object[] getData();
    /**
     * Optionally use to store the data in the table model. The get/setValueAt
     * can refer to other models if required.
     * 
     * @param x
     */
    public abstract void setData(Object[] x);

    public abstract void setData(java.util.Collection x);
}