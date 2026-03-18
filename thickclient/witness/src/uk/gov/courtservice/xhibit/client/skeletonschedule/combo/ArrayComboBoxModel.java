package uk.gov.courtservice.xhibit.client.skeletonschedule.combo;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Combo Box Model That stores its data in an array
 * </p>
 * <p>
 * Description: A model that wraps an array as thinly as possible
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */
public class ArrayComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private static final Logger log = CSServices.getLogger(ArrayComboBoxModel.class);

    private final Object[] data;

    private int selectedIndex;

    public ArrayComboBoxModel(Object[] newData) {
        log.debug("ArrayComboBoxModel(" + newData + ")");
        data = newData == null ? new Object[0] : newData;
        selectedIndex = data.length == 0 ? -1 : 0;
    }

    public ArrayComboBoxModel(Object[] newData, Object selectedItem) {
        log.debug("ArrayComboBoxModel(" + newData + ", " + selectedItem + ")");
        data = newData == null ? new Object[0] : newData;
        selectedIndex = indexOf(selectedItem);
    }

    /*
     * AbstractListModel Implementation
     */

    public int getSize() {
        return data.length;
    }

    public Object getElementAt(int index) {
        return index >= 0 && index < data.length ? data[index] : null;
    }

    /*
     * ComboBoxModel Implementation
     */

    public void setSelectedItem(Object item) {
        int itemIndex = indexOf(item);
        if (itemIndex != selectedIndex) {
            selectedIndex = itemIndex;
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return getElementAt(selectedIndex);
    }

    /*
     * Implementation
     */

    public int indexOf(Object item) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

}
