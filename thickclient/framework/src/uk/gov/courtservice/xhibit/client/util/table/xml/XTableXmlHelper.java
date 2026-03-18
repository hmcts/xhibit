package uk.gov.courtservice.xhibit.client.util.table.xml;

import java.awt.Component;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XTableBodyElement;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XTableCellElement;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XTableElement;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XTableHeaderElement;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.XTableRowElement;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.TextAlignType;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */
public class XTableXmlHelper {
    private static final Logger log = CSServices.getLogger(XTableXmlHelper.class);

    /**
     * Stop unnecseary construciton of this helper class
     */
    private XTableXmlHelper() {
        // Change permisions
    }

    /**
     * Write the table data to the character output stream
     * 
     * @param table
     *            the table to write
     * @param writer
     *            the stream to write too
     */
    // public static void marshal(XTable table, String title, Writer writer)
    // {
    // if (log.isDebugEnabled())
    // {
    // long startTime = System.currentTimeMillis();
    // _marshal(table, title, writer);
    // log.debug("Rendering the table as XML took " +
    // (System.currentTimeMillis() - startTime) + "ms.");
    // }
    // else
    // {
    // _marshal(table, title, writer);
    // }
    // }
    public static void marshal(XTable table, String title, Writer writer) {
        if (table == null) {
            throw new IllegalArgumentException("table: null");
        }
        if (title == null) {
            throw new IllegalArgumentException("title: null");
        }
        if (writer == null) {
            throw new IllegalArgumentException("writer: null");
        }

        try {
            createXTableElement(table, title).marshal(writer);
        } catch (MarshalException e) {
            throw new XTableXmlException(e);
        } catch (ValidationException e) {
            throw new XTableXmlException(e);
        }
    }

    private static XTableElement createXTableElement(XTable table, String title) {
        XTableElement tableElement = new XTableElement();

        tableElement.setXTableTitleElement(title);
        tableElement.setXTableHeaderElement(createXTableHeaderElement(table));
        tableElement.setXTableBodyElement(createXTableBodyElement(table));

        return tableElement;
    }

    private static XTableBodyElement createXTableBodyElement(XTable table) {
        XTableBodyElement tableBodyElement = new XTableBodyElement();
        for (int i = 0, c = table.getRowCount(); i < c; i++) {
            tableBodyElement.addXTableRowElement(createXTableRowElement(table, i));
        }
        return tableBodyElement;
    }

    private static XTableHeaderElement createXTableHeaderElement(XTable table) {
        TableColumnModel tableColumnModel = table.getColumnModel();

        XTableHeaderElement tableHeaderElement = new XTableHeaderElement();

        XTableRowElement tableRowElement = new XTableRowElement();
        for (int i = 0, c = tableColumnModel.getColumnCount(); i < c; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            TableCellRenderer tableCellRenderer = tableColumn.getHeaderRenderer();
            Object value = tableColumn.getHeaderValue();
            tableRowElement.addXTableCellElement(createXTableCellElement(tableCellRenderer, table, value, -1, i));
        }
        tableHeaderElement.setXTableRowElement(tableRowElement);

        return tableHeaderElement;
    }

    private static XTableRowElement createXTableRowElement(XTable table, int row) {
        TableModel tableModel = table.getModel();

        XTableRowElement tableRowElement = new XTableRowElement();

        for (int i = 0, c = tableModel.getColumnCount(); i < c; i++) {
            TableCellRenderer tableCellRenderer = table.getCellRenderer(row, i);
            Object value = tableModel.getValueAt(row, i);
            tableRowElement.addXTableCellElement(createXTableCellElement(tableCellRenderer, table, value, row, i));
        }

        return tableRowElement;
    }

    private static XTableCellElement createXTableCellElement(TableCellRenderer renderer, XTable table, Object value,
            int row, int column) {
        return createXTableCellElement(renderer.getTableCellRendererComponent(table, value, false, false, row, column));
    }

    private static XTableCellElement createXTableCellElement(Component component) {
        if (component instanceof JLabel) {
            return createXTableCellElement((JLabel) component);
        }
        if (component instanceof JButton) {
            return createXTableCellElement((JButton) component);
        }
        if (component instanceof JTextArea) {
            return createXTableCellElement((JTextArea) component);
        }
        return createXTableCellElement();
    }

    private static XTableCellElement createXTableCellElement(JLabel label) {
        XTableCellElement tableCellElement = new XTableCellElement();
        // Defaults to left so only need to change if right.
        if (label.getHorizontalAlignment() == SwingConstants.RIGHT) {
            tableCellElement.setTextAlign(TextAlignType.RIGHT);
        }
        tableCellElement.setContent(label.getText());
        return tableCellElement;
    }

    private static XTableCellElement createXTableCellElement(JButton button) {
        XTableCellElement tableCellElement = new XTableCellElement();
        // Defaults to left so only need to change if right.
        if (button.getHorizontalAlignment() == SwingConstants.RIGHT) {
            tableCellElement.setTextAlign(TextAlignType.RIGHT);
        }
        tableCellElement.setContent(button.getText());
        return tableCellElement;
    }

    private static XTableCellElement createXTableCellElement(JTextArea area) {
        XTableCellElement tableCellElement = new XTableCellElement();
        tableCellElement.setContent(area.getText());
        return tableCellElement;
    }

    private static XTableCellElement createXTableCellElement() {
        XTableCellElement tableCellElement = new XTableCellElement();
        return tableCellElement;
    }
}