package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.AbstractSelectorPanel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.models.SortableListModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * @version $Id: SelectorJListSortableView.java,v 1.2 2004/01/15 16:57:48 sz0t7n
 *          Exp $
 */

public class SelectorJListSortableView extends AbstractSelectorPanel {
    private final SortableListModel model;

    private final JList list;

    /**
     * Creates a view that can be used in the Selector Panel
     * 
     * @param comparator
     *            used for sorting items that are added to the list
     * @param renderer
     *            For renderering the display. If null, the toString() method on
     *            the object will be used
     * @param title
     *            To display above the list.
     */
    public SelectorJListSortableView(Comparator comparator, ListCellRenderer renderer, String title) {
        this.setLayout(new GridBagLayout());
        model = new SortableListModel(comparator);
        list = new JList(model);
        Dimension dim = new Dimension(200, 300);
        if (renderer != null)
            list.setCellRenderer(renderer);

        this.add(new JLabel(title), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(PublicDisplayUtils.getDefaultScrollPane(list, dim), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    public void removeElementAt(int objectToRemoveLocationId) {
        model.remove(objectToRemoveLocationId);
    }

    public void addElement(Object objectToAdd) {
        model.addElement(objectToAdd);
    }

    public Object getElementAt(int index) {
        return model.getElementAt(index);
    }

    public int getModelSize() {
        return model.getSize();
    }

    public void removeAllElements() {
        model.removeAllElements();
    }

    public int[] getSelectedIndeces() {
        return list.getSelectedIndices();
    }

    public Object[] getData() {
        return model.toArray();
    }

    public void addListSelectionListener(ListSelectionListener listen) {
        list.addListSelectionListener(listen);
    }
}