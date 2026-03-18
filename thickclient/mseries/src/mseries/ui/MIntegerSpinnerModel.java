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

/**
 * Model for MSpinner to manage integers. The range of parameters are expressed
 * in the constructors
 */
public class MIntegerSpinnerModel extends DefaultSpinnerModel {
    private int value = 0;

    private long val = 0;

    /** The default minimum value */
    protected int min = -2147483648;

    /** The default maximum value */
    protected int max = 2147483647;

    private boolean hasMin;

    private boolean hasMax;

    protected int step = 1;

    private boolean roll = false;

    public MIntegerSpinnerModel() {
    }

    /**
     * Constructor
     * 
     * @param start
     *            the initial value
     * @param max
     *            the maximum value
     * @param min
     *            the minimum value
     * @param step
     *            the amount to increment/decrement the value by when
     *            getNextValue/getPreviousValue are executed
     * @param roll
     *            true if the value rolls over the maximum back to the minimum
     */
    public MIntegerSpinnerModel(Integer start, Comparable max, Comparable min, int step, boolean roll) {
        setMaximum(max);
        setMinimum(min);
        this.step = step;
        this.roll = roll;
        setValue(start);
    }

    /**
     * Constructor
     * 
     * @param start
     *            the initial value
     * @param max
     *            the maximum value
     * @param min
     *            the minimum value
     * @param step
     *            the amount to increment/decrement the value by when
     *            getNextValue/getPreviousValue are executed
     * @param roll
     *            true if the value rolls over the maximum back to the minimum
     */
    public MIntegerSpinnerModel(int start, int max, int min, int step, boolean roll) {
        setMaximum(max);
        setMinimum(min);
        this.step = step;
        this.roll = roll;
        setValue(start);
    }

    /**
     * Doesn't need to do anything for Integers, the step is set when the model
     * is constructed.
     */
    public void setStep(int step) {
    }

    /**
     * Returns the current value of the field
     * 
     * @return the current value of the field
     */
    public Object getValue() {
        return new Integer(value);
    }

    /**
     * Sets the value
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(int newValue) {
        if (newValue >= min && newValue <= max) {
            value = newValue;
            notifyListeners();
        }
    }

    /**
     * Sets the value
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(Object newValue) {
        setValue(((Integer) newValue).intValue());
    }

    /**
     * Advances and returns the current value in the sequence according to the
     * step, maximum value and roll attribute
     * 
     * @return the next value
     */
    public Object getNextValue() {
        val = ((Integer) getValue()).longValue();
        Long l = new Long(val + step);
        Integer m = (Integer) getMaximum();
        Long ml = new Long(m.longValue());
        if (ml.compareTo(l) < 0) {
            if (roll) {
                val = min;
            }
        } else {
            val += step;
        }
        setValue(new Integer((int) val));
        return getValue();
    }

    /**
     * Retracts and returns the current value in the sequence according to the
     * step, minimum value and roll attribute
     * 
     * @return the next value
     */
    public Object getPreviousValue() {
        val = ((Integer) getValue()).longValue();

        Long l = new Long(val - step);
        Integer m = (Integer) getMinimum();
        Long ml = new Long(m.longValue());
        if (ml.compareTo(l) > 0) {
            if (roll) {
                val = max;
            }
        } else {
            val -= step;
        }
        setValue(new Integer((int) val));
        return getValue();
    }

    /**
     * Used to force a minimum value when the field is decremented using the
     * down button
     * 
     * @param min
     *            the minimum value
     */
    public void setMinimum(Comparable min) {
        if (min instanceof java.lang.Integer) {
            hasMin = true;
            Integer x = (Integer) min;
            this.min = x.intValue();
        }
        notifyListeners();
    }

    /**
     * Used to force a minimum value when the field is decremented using the
     * down button
     * 
     * @param min
     *            the minimum value
     */
    public void setMinimum(int min) {
        setMinimum(new Integer(min));
    }

    /**
     * Returns the current minimum value
     * 
     * @return The current minimum value
     */
    public Comparable getMinimum() {
        return new Integer(this.min);
    }

    /**
     * Used to force a maximum value when the field is incremented using the up
     * button
     * 
     * @param max
     *            the maximum value
     */
    public void setMaximum(Comparable max) {
        if (max instanceof java.lang.Integer) {
            hasMax = true;
            Integer x = (Integer) max;
            this.max = x.intValue();
        }
        notifyListeners();
    }

    /**
     * Used to force a maximum value when the field is incremented using the up
     * button
     * 
     * @param max
     *            the maximum value
     */
    public void setMaximum(int max) {
        setMaximum(new Integer(max));
    }

    /**
     * Returns the current maximum value
     * 
     * @return The current maximum value
     */
    public Comparable getMaximum() {
        return new Integer(this.max);
    }
}
