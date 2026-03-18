package uk.gov.courtservice.xhibit.client.results.util.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * A custom renderer to be used when it is required to display an additional
 * information column in a <code>JTable</code>, that can display an empty
 * cell, a text field containing an "other offence" or a text area for
 * displaying of "alternate offence" that can be editable or not.
 * 
 * @author Simon Gilmore
 * @author tz0d5m
 * @version $Revision: 1.6 $
 */
public class AdditionalInfoTableCellRenderer implements TableCellRenderer {
    private static final int PAD = 7;

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private MultiLineHelper _multiLineHelper;

    // only to be set in the constructor...
    private final AdditionalInfoTableCell panel;

    /**
     * Minimum width for column - see PR 57373
     */
    public static final int COLUMN_ADDITIONAL_INFO_MIN_WIDTH = 75;

    /**
     * Constructor to allow the offence text field to be non-editable.
     * 
     * @param xac
     */
    public AdditionalInfoTableCellRenderer(XhibitApplicationController xac, MultiLineHelper multiLineHelper) {
        this(xac, multiLineHelper, false);
    }

    /**
     * Constructor that allows the offence text field to be editable if the
     * passed in parameter is set to <i>true </i>, otherwise it is not-editable.
     * 
     * @param xac
     * @param offenceTextFieldEditable
     */
    public AdditionalInfoTableCellRenderer(XhibitApplicationController xac, MultiLineHelper multiLineHelper,
            boolean offenceTextFieldEditable) {
        panel = new AdditionalInfoTableCell(xac, offenceTextFieldEditable);
        panel.setBorder(noFocusBorder);
        _multiLineHelper = multiLineHelper;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        AdditionalInfoTableCellComponent tclc = (AdditionalInfoTableCellComponent) value;
        panel.show(tclc, isSelected);

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
            panel.setForeground(table.getSelectionForeground());
        } else {
            panel.setBackground(table.getBackground());
            panel.setForeground(table.getForeground());
        }

        if (hasFocus) {
            panel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            panel.setBorder(noFocusBorder);
        }

        _multiLineHelper.doRowHeights(row, column, panel.getPreferredSize().height + PAD);

        return panel;
    }

    protected AdditionalInfoTableCell getPanelTableCell() {
        return panel;
    }
}