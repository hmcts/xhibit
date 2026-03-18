package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Renders Counts (offence values) to be displayed in a combo box.
 * </p>
 * <p>
 * Description: Renders Counts as the word Count followed by the crest offence
 * sequence number (i.e. the count number) e.g. Count 1
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class CountComboBoxRenderer extends JLabel implements ListCellRenderer {
    /**
     * The text to prefix the count number.
     */
    private final String COUNT_TEXT = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
            JoinderConstants.COUNT_SMALL);

    /**
     * The offence value (Count) being rendered.
     */
    private OffenceValue offenceValue;

    /**
     * Creates a CountComboBoxRenderer
     */
    public CountComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setPreferredSize(new Dimension(100, 20));
    }

    /**
     * Return this component that has been configured to display the specified
     * value. The component's paint method is then called to "render" the cell.
     * 
     * @param list
     *            The list we are painting
     * @param value
     *            The value returned by list.getModel().getElementAt(index).
     * @param index
     *            The cells index.
     * @param isSelected
     *            True if the specified cell was selected.
     * @param cellHasFocus
     *            True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        offenceValue = (OffenceValue) value;
        setHorizontalAlignment(LEFT);
        if (list.getSelectedValue() != null) {
            setText(COUNT_TEXT + offenceValue.getCrestOffenceSeqNo());
        }
        return this;
    }
}