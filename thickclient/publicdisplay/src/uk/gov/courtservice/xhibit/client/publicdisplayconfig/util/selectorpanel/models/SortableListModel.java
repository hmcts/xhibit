package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.models;

import java.util.Comparator;

import javax.swing.DefaultListModel;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: SortableListModel.java,v 1.3 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class SortableListModel extends DefaultListModel {
    private final Comparator _comparator;

    public SortableListModel(Comparator comparator) {
        _comparator = comparator;
    }

    /**
     * Overrides the addElement to add in the correct place in the list to
     * ensure the list is sorted.
     * 
     * @param obj
     *            item to add
     */
    public void addElement(Object obj) {
        int index = findInsertPosition(obj);
        super.insertElementAt(obj, index);
    }

    /**
     * Establishes the insertion point
     * 
     * @param element
     *            object to insert
     * @return insertion point
     */
    private int findInsertPosition(Object element) {
        Object[] allArray = this.toArray();
        int i = 0;
        for (i = 0; i < allArray.length; i++) {
            if (_comparator.compare(allArray[i], element) > 0)
                return i;
        }
        return i;
    }
}