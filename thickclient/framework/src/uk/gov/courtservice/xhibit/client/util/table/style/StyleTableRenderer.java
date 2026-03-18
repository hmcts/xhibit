package uk.gov.courtservice.xhibit.client.util.table.style;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import uk.gov.courtservice.xhibit.client.util.DocumentAttributeSet;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: This renderer displays the <b>String</b> content in a text
 * area, wrapped.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
public class StyleTableRenderer extends JTextPane implements TableCellRenderer, Serializable {
    private MultiLineHelper _multiLineHelper;

    /** Standard no-focus border for a JTable cell. */
    protected static Border noFocusBorder = null;

    /** Standard focus border for a JTable cell. */
    protected static Border focusBorder = null;

    /** The foreground color of a cell when it is unselected. */
    private Color unselectedForeground;

    /** The backgruond color of a cell when it is unselected. */
    private Color unselectedBackground;

    /**
     * Default constructor - creates a text area renderer for the multi-line
     * table.
     * 
     * @param table
     *            the table that references this renderer
     */
    public StyleTableRenderer(JTable table, MultiLineHelper multiLineHelper) {
        super();
        focusBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        noFocusBorder = new EmptyBorder(focusBorder.getBorderInsets(this));

        setOpaque(true);
        setBorder(noFocusBorder);
        setDocument(new DefaultStyledDocument());

        _multiLineHelper = multiLineHelper;

        // Calling these methods help performance on table navigation.
        ToolTipManager.sharedInstance().unregisterComponent(table);
        ToolTipManager.sharedInstance().unregisterComponent(table.getTableHeader());
    }

    /**
     * Sets the foreground color to this component.
     */
    public void setForeground(Color c) {
        super.setForeground(c);
        unselectedForeground = c;
    }

    /**
     * Sets the background color to this component.
     */
    public void setBackground(Color c) {
        super.setBackground(c);
        unselectedBackground = c;
    }

    /**
     * Called by the component, remove the foreground and background colors on
     * update.
     */
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Set the cell colours
        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
        }
        // Set the value
        setValue(value, this.getForeground());

        // Set the size of the cell and the root view, forcing it to internally
        // recalculate the preferred size of the cell to fit all the text in.
        setSize(table.getColumnModel().getColumn(column).getWidth(), 0);// Integer.MAX_VALUE);
        getUI().getRootView(this).setSize(this.getWidth(), 0f);// Float.MAX_VALUE);

        if (hasFocus) {
            setBorder(focusBorder);
            if (table.isCellEditable(row, column)) {
                super.setForeground(UIManager.getColor("Table.focusCellForeground"));
                super.setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(noFocusBorder);
        }

        // Resize row height
        _multiLineHelper.doRowHeights(row, column, this.getPreferredSize().height);

        return this;
    }

    private void setValue(Object object, Color textColor) {
        if (object instanceof ArrayList) {
            try {
                Iterator iter = ((ArrayList) object).iterator();
                this.getDocument().remove(0, getDocument().getLength());
                while (iter.hasNext()) {
                    StyleTableCellValue item = (StyleTableCellValue) iter.next();
                    int offset = this.getDocument().getLength();
                    SimpleAttributeSet aSet = DocumentAttributeSet.getInstance().getAttibute(item.getAttribute());
                    StyleConstants.setForeground(aSet, textColor);
                    this.getDocument().insertString(offset, item.getText(), aSet);
                }
                this.setDocument(this.getDocument());
                revalidate();
                repaint();
            } catch (BadLocationException e) {
                // throw new CSUnrecoverableException
                // ("Style_Renderer.Document_Bad_Location");
            }
        }
    }
}