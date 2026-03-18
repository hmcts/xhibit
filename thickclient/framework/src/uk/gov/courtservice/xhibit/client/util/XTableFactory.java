package uk.gov.courtservice.xhibit.client.util;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.style.StyleTableRenderer;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: A Factory object responsible for retrieving a JTable instance of
 * a specific type. Table types include the default table with normal JTable
 * functionality, as well as a Multi Line Table that wraps its text content
 * within a cell.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou, Frederik Vandendriessche
 * @version 1.2 Rakesh Lakhani Multiline table is deprecated and functionality
 *          moved into the renderer and the Multiline table helper. This now
 *          allows sortable multiline tables.
 */
public class XTableFactory extends Object {
    private static final Logger log = CSServices.getLogger(XTableFactory.class);

    public static final String COLUMN_WIDTH_UNDEFINED = "";

    /**
     * The XTableFactory instance.
     */
    private static XTableFactory tableFactory = null;

    /**
     * Default constructor.
     */
    private XTableFactory() {
        super();
    }

    /**
     * @return the XTableFactory instance.
     */
    public static synchronized XTableFactory getInstance() {
        if (tableFactory == null) {
            tableFactory = new XTableFactory();
        }
        return tableFactory;
    }

    /**
     * Will create and return the default table component used for the xhibit
     * application.
     * 
     * @param data
     *            the data to be stored in the table.
     * @param headers
     *            the column headers of the table.
     * @return the table component.
     */
    public XTable createDefaultTable(Object[][] data, Object[] headers) {
        XTable table = new XTable(data, headers);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    /**
     * Will create and return the default table component used for the xhibit
     * application. The data and headers are represented in Vectors.
     * 
     * @param data
     *            the data to be stored in the table.
     * @param headers
     *            the column headers of the table.
     * @return the table component.
     */
    public XTable createDefaultTable(Vector data, Vector headers) {
        XTable table = new XTable(data, headers);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    /**
     * Will create and return the default table component used for the xhibit
     * application from a given TableModel.
     * 
     * @param tableModel
     *            the table model used to create the table.
     * @return the table component.
     */
    public XTable createDefaultTable(TableModel tableModel) {
        XTable table = new XTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    /**
     * Will create and return a table that wraps its content within its cells.
     * 
     * @param data
     *            the data to be stored in the table.
     * @param headers
     *            the column headers of the table.
     * @return the multi-line table.
     */
    public XTable createMultiLineTable(Object[][] data, Object[] headers) {
        XTable table = createDefaultTable(data, headers);
        makeMultiline(table);
        return table;
    }

    /**
     * Will create and return a table that wraps its content within its cells.
     * 
     * @param model
     *            the table model for the table
     * @return the multi-line table.
     */
    public XTable createMultiLineTable(TableModel tableModel) {
        XTable table = createDefaultTable(tableModel);
        makeMultiline(table);
        return table;
    }

    /**
     * Sets up the table to be multiline - Set the renderers to be multi-line
     * 
     * @param table
     */
    private void makeMultiline(XTable table) {
        setTableComponents(table);
    }

    /**
     * Initialises the column sizes from given column masks for the table. If
     * there is a column that does not have a particular mask, then it must be
     * passed through as <code>XTableFactory.COLUMN_WIDTH_UNDEFINED</code>
     * 
     * @param table
     *            the table that the columns belong to
     * @param longValues
     *            the column masks to determine the column widths
     * @param tableViewableWidth
     *            the view width of the table.
     * @deprecated This method has now been deprecated, use initColumns
     *             (longvalues,tableviewableiwidth) in XTable.
     */
    public void initColumnSizes(XTable table, Object[] longValues, int tableViewableWidth) {
        TableColumn column = null;
        java.awt.Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        int columnCount = table.getColumnModel().getColumnCount();
        ArrayList undefinedIndexs = new ArrayList();

        // Represents the total width of all column widths that have a column
        // mask.
        int totalDefinedWidth = 0;

        for (int i = 0; i < longValues.length; i++) {
            // If the column mask is specified as undefined, then continue,
            // but
            // keep the index for later calculation
            if (longValues[i].equals(COLUMN_WIDTH_UNDEFINED)) {
                undefinedIndexs.add(new Integer(i));
                continue;
            }
            column = table.getColumnModel().getColumn(i);

            try {
                comp = column.getHeaderRenderer().getTableCellRendererComponent(null, column.getHeaderValue(), false,
                        false, 0, 0);
                headerWidth = comp.getPreferredSize().width;
            } catch (NullPointerException e) {
                // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Null
                // pointer exception!");
                // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("
                // getHeaderRenderer returns null in 1.3.");
                // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("
                // The replacement is getDefaultRenderer.");
                log.fatal(e, e);
                throw new CSUnrecoverableException(e);

            }

            if (table.getModel().getValueAt(0, i) == null) {
                continue;
            }

            comp = table.getDefaultRenderer(table.getModel().getColumnClass(i)).getTableCellRendererComponent(table,
                    longValues[i], false, false, 0, i);
            if (comp == null) {
                continue;
            }

            cellWidth = comp.getPreferredSize().width;

            // XXX: Before Swing 1.1 Beta 2, use setMinWidth instead.
            cellWidth = Math.max(headerWidth, cellWidth);
            column.setPreferredWidth(cellWidth);
            totalDefinedWidth += cellWidth;
        }

        if (undefinedIndexs.size() == 0) {
            return;
        }

        // Traverse the indexes of the undefined column masks and set equal
        // width to each column.
        int undefinedColWidth = (tableViewableWidth - totalDefinedWidth) / undefinedIndexs.size();
        for (int i = 0; i < undefinedIndexs.size(); i++) {
            int columnIndex = ((Integer) undefinedIndexs.get(i)).intValue();
            table.getColumnModel().getColumn(columnIndex).setPreferredWidth(undefinedColWidth);
        }
    }

    public TableCellRenderer getStyleTableRenderer(XTable table) {
        return new StyleTableRenderer(table, table.getMultiLineHelper());
    }

    public TableCellRenderer getMultiLineCellRenderer(XTable table) {
        return new MultiLineCellRenderer(table, table.getMultiLineHelper());
    }

    /**
     * Utility method to attach a MultiLineCell renderer/editor to each column
     * of the given table.
     * 
     * @param table
     *            the table to which the columns will have their renderer set.
     */
    private void setTableComponents(XTable table) {
        MultiLineCellRenderer multiLineCR = new MultiLineCellRenderer(table, table.getMultiLineHelper());
        table.setDefaultRenderer(Object.class, multiLineCR);
        // RL: Changed default state of editor from false to true as it
        // is an editor after all !
        // MultiLineCellEditor editor = new MultiLineCellEditor (table,true);
        // This sets all columns to be multi-line cells.
        // Need to change this to leave it to the user to set the renderer
        // in their code, so the default is a table without multi-line/
        // v. low priority.
        // TableColumnModel model = table.getColumnModel ();
        // for (int i=0; i<model.getColumnCount(); i++)
        // {
        // model.getColumn(i).setCellRenderer (multiLineCR);
        // model.getColumn(i).setCellEditor(editor);
        // }
        table.getTableHeader().setReorderingAllowed(false);
    }
}