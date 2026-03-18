/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 * 
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that 
 *   makes use of this code and that some acknowedgement is given. Comments, questions and 
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.util.Arrays;
import java.util.List;

/**
 * A SpinnerModel that manages a list of objects. Any object can be used, the
 * default editor in MSpinner may be sufficient to render the object using the
 * toString() method, alternatively a custom editor could be provided. The
 * objects returned in the getValue(), getNextValue() and getPreviousValue()
 * methods is the object from the selected row embedded within a ListObject
 * object. MListSpinnerModel is used as follows
 * 
 * <PRE>
 * 
 * final MSpinner m3= new MSpinner(10); String[] names={"Hello", "World"};
 * SpinnerModel model3 = new MListSpinnerModel(names); m3.setModel(model3);
 * m3.addMChangeListener(new MChangeListener() { public void
 * valueChanged(MChangeEvent e) { int index =
 * ((MListSpinnerModel.ListObject)m3.getValue()).index; String name =
 * (String)((MListSpinnerModel.ListObject)m3.getValue()).object;
 * System.out.println(index+", "+name); } });
 * 
 * </PRE>
 */
public class MListSpinnerModel extends DefaultSpinnerModel {
    private List list;

    private int current = 0;

    private int top = 0;

    /**
     * Constructor
     * 
     * @param list
     *            the sequence of objects to display in the Spinner
     */
    public MListSpinnerModel(Object[] list) {
        setList(list);
    }

    /**
     * Constructor
     * 
     * @param list
     *            the sequence of objects to display in the Spinner
     */
    public MListSpinnerModel(List list) {
        setList(list);
    }

    /**
     * Default Constructor
     */
    public MListSpinnerModel() {
    }

    /**
     * Gives the MListSpinnerModel a sequence of objects to display
     * 
     * @param list
     *            the sequence of objects to display in the Spinner
     */
    public void setList(List list) {
        this.list = list;
        init();
    }

    /**
     * Gives the MListSpinnerModel a sequence of objects to display
     * 
     * @param list
     *            the sequence of objects to display in the Spinner
     */
    public void setList(Object[] list) {
        this.list = Arrays.asList(list);
        init();
    }

    private void init() {
        top = list.size() - 1;
    }

    /**
     * Doesn't need to do anything for Lists.
     */
    public void setStep(int step) {
    }

    /**
     * Returns the current value of the field
     * 
     * @return the current value of the field
     */
    public Object getValue() {
        ListObject lo = new ListObject(current, list.get(current));
        return lo;
    }

    /**
     * Sets a new value in the model
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(Object value) {
        int i = list.indexOf(value);
        if (i >= 0 && i <= top) {
            current = i;
        }
        notifyListeners();
    }

    /**
     * Move the sequence to the row with the index given
     * 
     * @param i
     *            the index to scroll to
     */
    public void setIndex(int i) {
        if (i >= 0 && i <= top) {
            current = i;
        }
        notifyListeners();
    }

    /**
     * Return the index of the selected object
     * 
     * @return the index
     */
    public int getIndex() {
        return current;
    }

    /**
     * Advance and return the previous value in the sequence
     * 
     * @return the previous value
     */
    public Object getPreviousValue() {
        if (current > 0) {
            current--;
        } else {
            current = top;
        }
        notifyListeners();
        return getValue();
    }

    /**
     * Advance and return the next value in the sequence
     * 
     * @return the next value
     */
    public Object getNextValue() {
        if (current < top) {
            current++;
        } else {
            current = 0;
        }
        notifyListeners();
        return getValue();
    }

    /**
     * Simple wrapper class to return the object selected in the model
     * accompanied by it's index. Objects of this type are returned by
     * getValue(), getNextValue(), getPreviousValue() methods in
     * MListSpinnerModel and therefore in MSpinner when a MListSpinnerModel is
     * used.
     */
    public class ListObject {
        /** The index that the object had in the List */
        public int index;

        /** The object in the List */
        public Object object;

        public ListObject(int index, Object value) {
            this.index = index;
            this.object = value;
        }

        /**
         * Return the index in the List that the embedded object represented
         * 
         * @return the index
         */
        public int index() {
            return index;
        }

        /**
         * Return the embedded object
         * 
         * @return the embedded object
         */
        public Object object() {
            return object;
        }

        /**
         * Returns a string representation of the embedded object
         * 
         * @return a string representation of the embedded object
         */
        public String toString() {
            return object.toString();
        }
    }
}
