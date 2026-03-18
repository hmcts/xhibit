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

import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

/**
 * The SpinnerEditor interface defines editors that can be used to render and
 * edit the display field in the MSPinner. The encapsulate a JTextField which is
 * the actual component that gets placed in the MSpinner. The editor configures
 * the textfield to parse the users input and format the model values.
 */
public interface SpinnerEditor extends ChangeListener {
    /**
     * Gets the current value out of the editor. This may or may not be valid as
     * far as the sequence being managed by the model is concerned as the user
     * may be able to type a value directly into the field
     */
    public Object getValue();

    /**
     * Puts a new value in the editor
     */
    public void setValue(Object v);

    /**
     * Gives a JTextField for placing on the GUI. This component is configured
     * to accept only input specific to the kind of value being spun. E.g.
     * Integers, Dates etc
     */
    public JTextField getTextField();

    /**
     * The step size can be variable as it may depend on the caret position so
     * the editor must be able to say what the step size is.
     */
    public int getStep();

    /**
     * Sets the current SpinnerModel whose values are being rendered by this
     * editor
     */
    public void setModel(SpinnerModel model);

    /**
     * Makes the textfield editable, i.e. allows the user to type into the
     * field. A Spinner is most effective when the field is not editable as
     * valid values can be ensured.
     */
    public void setEditable(boolean editable);

    /** Is the field editable ? */
    public boolean isEditable();
}
