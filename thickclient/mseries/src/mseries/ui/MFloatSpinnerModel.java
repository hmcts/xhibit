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
public class MFloatSpinnerModel extends DefaultSpinnerModel {
    private float value = 0;

    private double val = 0;

    /** The default minimum value */
    protected float min = Float.MIN_VALUE;

    /** The default maximum value */
    protected float max = Float.MAX_VALUE;

    private boolean hasMin;

    private boolean hasMax;

    protected float step = 1;

    private boolean roll = false;

    public MFloatSpinnerModel() {
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
    public MFloatSpinnerModel(Float start, Comparable max, Comparable min, float step, boolean roll) {
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
    public MFloatSpinnerModel(float start, float max, float min, float step, boolean roll) {
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
        return new Float(value);
    }

    /**
     * Sets the value
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(float newValue) {
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
        setValue(((Float) newValue).floatValue());
    }

    /**
     * Advances and returns the current value in the sequence according to the
     * step, maximum value and roll attribute
     * 
     * @return the next value
     */
    public Object getNextValue() {
        val = ((Float) getValue()).floatValue();
        Double l = new Double(val + step);
        Float m = (Float) getMaximum();
        Double ml = new Double(m.doubleValue());
        if (ml.compareTo(l) < 0) {
            if (roll) {
                val = min;
            }
        } else {
            val += step;
        }
        setValue(new Float((float) val));
        return getValue();
    }

    /**
     * Retracts and returns the current value in the sequence according to the
     * step, minimum value and roll attribute
     * 
     * @return the next value
     */
    public Object getPreviousValue() {
        val = ((Float) getValue()).floatValue();

        Double l = new Double(val - step);
        Float m = (Float) getMinimum();
        Double ml = new Double(m.doubleValue());
        if (ml.compareTo(l) > 0) {
            if (roll) {
                val = max;
            }
        } else {
            val -= step;
        }
        setValue(new Float((float) val));
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
        if (min instanceof java.lang.Float) {
            hasMin = true;
            Float x = (Float) min;
            this.min = x.floatValue();
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
    public void setMinimum(float min) {
        setMinimum(new Float(min));
    }

    /**
     * Returns the current minimum value
     * 
     * @return The current minimum value
     */
    public Comparable getMinimum() {
        return new Float(this.min);
    }

    /**
     * Used to force a maximum value when the field is incremented using the up
     * button
     * 
     * @param max
     *            the maximum value
     */
    public void setMaximum(Comparable max) {
        if (max instanceof java.lang.Float) {
            hasMax = true;
            Float x = (Float) max;
            this.max = x.floatValue();
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
    public void setMaximum(float max) {
        setMaximum(new Float(max));
    }

    /**
     * Returns the current maximum value
     * 
     * @return The current maximum value
     */
    public Comparable getMaximum() {
        return new Float(this.max);
    }
}
