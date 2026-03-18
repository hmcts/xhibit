package uk.gov.courtservice.xhibit.client.crestformsbf.swing.table;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.client.crestformsbf.swing.icon.IconFactory;

/**
 * A table model which allows rows to be sorted
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */
public class SortTableHeaderRenderer extends JButton implements TableCellRenderer {

    /**
     * The margin to use
     */
    private static final Insets margin = new Insets(2, 2, 2, 2);

    /**
     * The model used by the table
     */
    private final SortTableHeader header;

    public SortTableHeaderRenderer(SortTableHeader header) {
        this.header = header;
        this.setHorizontalTextPosition(SwingConstants.LEFT);
        setMargin(margin);
    }

    /**
     * Returns the sort table cell renderer.
     * 
     * @param table
     *            the <code>JTable</code>
     * @param value
     *            the value to assign to the cell at <code>[row, column]</code>
     * @param isSelected
     *            true if cell is selected
     * @param isFocus
     *            true if cell has focus
     * @param row
     *            the row of the cell to render
     * @param column
     *            the column of the cell to render
     * @return the default table cell renderer
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        setText(value == null ? "" : value.toString());

        ButtonModel model = getModel();
        model.setPressed(header.isPressedColumn(column));
        model.setArmed(header.isArmedColumn(column));

        if (header.isSortColumn(column)) {
            if (header.isSortAscending()) {
                setIcon(IconFactory.getIcon(IconFactory.DOWN_ARROW_UNPRESSED));
                setPressedIcon(IconFactory.getIcon(IconFactory.DOWN_ARROW_PRESSED));
            } else {
                setIcon(IconFactory.getIcon(IconFactory.UP_ARROW_UNPRESSED));
                setPressedIcon(IconFactory.getIcon(IconFactory.UP_ARROW_PRESSED));
            }
        } else {
            setIcon(null);
            setPressedIcon(null);
        }

        return this;
    }

}
