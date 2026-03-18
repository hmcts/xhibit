package uk.gov.courtservice.xhibit.client.util;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * <p>
 * Title: ArrayComboBoxModel
 * </p>
 * <p>
 * Description: Low cost implemenetation of a (non mutable) combo box model
 * bassed around an array of Objects
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment
 * @version 1.0
 */

public class ArrayComboBoxModel extends AbstractListModel implements ComboBoxModel {
    /**
     * The data to be displayed in the combo box
     */
    private final Object[] data;

    /**
     * The index of the currently selected item or -1;
     */
    private int selectedIndex;

    /**
     * Construct a model for the given data, selecting the first item if present
     */
    public ArrayComboBoxModel(Object[] data) {
        this.data = data == null ? new Object[0] : data;
        this.selectedIndex = 0 < data.length ? 0 : -1;
    }

    /**
     * ComboBoxModel Implementation
     * 
     * @see javax.swing.ComboBoxModel#getSize() ComboBoxModel
     */
    public int getSize() {
        return data.length;
    }

    /**
     * ComboBoxModel Implementation
     * 
     * @see javax.swing.ComboBoxModel#getElementAt(int) ComboBoxModel
     */
    public Object getElementAt(int index) {
        return data[index];
    }

    /**
     * ComboBoxModel Implementation
     * 
     * @see javax.swing.ComboBoxModel#setSelectedItem(Object) ComboBoxModel
     */
    public void setSelectedItem(Object anItem) {
        selectedIndex = indexOf(anItem);
    }

    /**
     * ComboBoxModel Implementation
     * 
     * @see javax.swing.ComboBoxModel#getSelectedItem() ComboBoxModel
     */
    public Object getSelectedItem() {
        return selectedIndex < 0 || selectedIndex >= data.length ? null : data[selectedIndex];
    }

    /**
     * Set the selected index
     * 
     * @param the
     *            new selected index
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Get the index of the selected item or -1 if none selected
     * 
     * @return the index of the selected item
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Get the index of the item.
     * 
     * @return the index of the item or -1 if not in data
     */
    public int indexOf(Object item) {
        if (item != null) {
            for (int i = 0; i < data.length; i++) {
                if (item.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

}
