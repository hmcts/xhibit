package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomFixedList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderFixedList component to display all associated cases.
 * </p>
 * <p>
 * Description: Displays all associated cases using an OrderFixedList which is
 * an implementation of a JList component. Multiple selects are supported, and
 * each value is dynamically linked to the underlying order data via helper
 * methods from OrderComponentHelper class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David & Des
 * @version 1.0
 */
public class OrderFixedList extends AbstractOrderComponent implements ListSelectionListener {
    // Jlist component with extended functionality to return the number of
    // possible associated cases.
    private CustomFixedList cases;

    // Array of all xpath strings related to every associated case.
    private String[] newRef;

    // Collection of all attribute names of associated case.
    private HashMap attr = new HashMap();

    /**
     * Create the OrderFixList component, constructing new xpath strings to
     * allow for dynamic control over order data upon any selection changes.
     */
    public void initComponent() {
        cases = new CustomFixedList(getHelper().getChildReferences());
        cases.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(cases);
        scrollPane.getViewport().setView(cases);
        scrollPane.setPreferredSize(new Dimension(getHelper().getAttributeAsInt("rows"), getHelper().getAttributeAsInt(
                "cols")));
        setVisualComponent(scrollPane);
        newRef = constructNewReferences(getHelper().getOrderDataReference());
        setInitiallySelectedAtributes();

    }

    /**
     * Find all selected atttributes which are initially true and select those
     * options in the associated case component.
     */
    private void setInitiallySelectedAtributes() {
        Vector v = getInitiallySelected(newRef);
        Iterator it = v.iterator();
        // iterate through vector and set selected attributes.
        while (it.hasNext()) {
            getHelper().setValues(newRef[Integer.parseInt(it.next().toString())], "true");
        }

        cases.setSelectedIndices(getSelectedArray(v));
    }

    /**
     * Return a vector containing the selected indices of selected associated
     * cases.
     * 
     * @param ref
     *            xpath string used to locate the case attribute.
     * @return a vector.
     */
    private Vector getInitiallySelected(String[] ref) {
        Vector allSelected = new Vector();
        for (int i = 0; i < ref.length; i++) {
            String initialValue = getHelper().getValue(newRef[i]);
            attr.put("" + i, initialValue);
            if (initialValue.equals("true")) {
                allSelected.add("" + i);
            }

        }
        return allSelected;
    }

    /**
     * Return an array containing the selected indices of selected associated
     * cases.
     * 
     * @param v
     *            a vector.
     * @return integer array.
     */
    private int[] getSelectedArray(Vector v) {
        int[] selected = new int[v.size()];
        Iterator it = v.iterator();
        for (int i = 0; i < v.size(); i++) {
            selected[i] = Integer.parseInt(it.next().toString());
        }

        return selected;
    }

    /**
     * Invoked when value of selection changes, and sets the value of the
     * correct attribute.
     * 
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            setSelectedValues(newRef);

        }
    }

    /**
     * Construct an array of valid xpath strings which successively point to all
     * case selected attributes.
     * 
     * @param old
     *            old reference string provided from data template xml.
     * @return String array of all new xpath strings.
     */
    private String[] constructNewReferences(String old) {
        String[] newRef = new String[cases.getCaseCount()];

        for (int i = 0; i < cases.getCaseCount(); i++) {
            newRef[i] = old + "[" + (i + 1) + "]/@selected";
        }
        return newRef;
    }

    /**
     * Using new xpath strings, set the selected attributes values indicating
     * their selected state, true or false.
     * 
     * @param refs
     *            new xpath strings.
     */
    private void setSelectedValues(String[] refs) {
        int[] selected = cases.getSelectedIndices();
        for (int i = 0; i < refs.length; i++) {
            if (isSelected(i, selected)) {
                if (attr.get("" + i).equals("false")) {
                    getHelper().setValues(refs[i], "true");
                    attr.put("" + i, "true");
                }
            } else {
                if (attr.get("" + i).equals("true")) {
                    getHelper().setValues(refs[i], "false");
                    attr.put("" + i, "false");
                }
            }
        }
    }

    /**
     * Checks whether the associated case element is selected or not, by using
     * the selected indices returned by getSelectedIndices() from JList.
     * 
     * @param i
     *            integer value representing position of speficic associated
     *            case.
     * @param indices
     *            integer array of all indices of selected associated case.
     * @return true or false.
     */
    private boolean isSelected(int i, int[] indices) {
        boolean found = false;

        for (int k = 0; k < indices.length; k++) {
            if (indices[k] == i)
                found = true;
        }

        return found;
    }

}
