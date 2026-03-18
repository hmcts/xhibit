/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 *
 *   Thanks go to David M Karr who, with permission, kindly supplied the code in the form
 *   of his DateTimeEditor, on which this component is based.
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

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import mseries.Calendar.MFieldListener;

/**
 * A single line input field that lets the user select a number or object from
 * an ordered set by clicking either the up or down button or pushing the
 * up/down keys on the keyboard.
 * <P>
 * The value is displayed by the <i>editor</i> and is encapsulated by a model
 * of a sequence of objects called a <strong>SpinnerModel</strong>. Thus the
 * editor must be able to render the values supplied by the model.
 * <P>
 * MChangeListeners can be added to recieve changes when ever the value changes
 * or MFieldListeners when the component recieves and loses the keyboard focus.
 * <P>
 * For effective presentation the preferred height should be an even number, try
 * it to see what I mean ! Spinner field are much more effective if they are not
 * editable so the user is forced to spin the field to find his desired value.
 * This restricts them to the set of valid values which is surely the point of
 * using a spinner over a text field.
 * 
 * @see mseries.ui.SpinnerModel
 * @see mseries.ui.SpinnerEditor
 */
public class MSpinner extends JComponent {
    private static final String uiClassID = "mSpinnerUI";

    private boolean editable = false;

    /**
     * The textfield that renders the value, this is usually replaced by one
     * supplied by the editor once it is installed
     */
    public JTextField display;

    private Vector listeners = new Vector();

    private Vector fieldListeners = new Vector();

    private Font font;

    /** The installed editor */
    protected SpinnerEditor editor;

    /** The installed model */
    protected SpinnerModel model;

    /** The default textfield size */
    protected int size = 6;

    /**
     * Default Constructor
     */
    public MSpinner() {
        init(size);
    }

    /**
     * Constructor
     * 
     * @param size
     *            the size of the display field, not the number of characters
     */
    public MSpinner(int size) {
        init(size);
    }

    private void init(int size) {
        editor = new DefaultSpinnerEditor();
        font = editor.getTextField().getFont();
        model = new DefaultSpinnerModel();
        setModel(model);
        setEditor(editor);
        this.size = size;

        updateUI();
    }

    /**
     * The textfield is the component that renders the value in the component.
     * It is usually supplied by the insalled editor.
     * 
     * @return the textfield
     * @see mseries.ui.SpinnerEditor#getTextField
     */
    public JTextField getTextField() {
        return display;
    }

    /**
     * The model is the object that supplies the sequence of values to the
     * spinner component.
     * 
     * @param model
     *            that supplies the values
     */
    public void setModel(SpinnerModel model) {
        this.model = model;
        editor.setModel(model);
    }

    /**
     * @return the currently installed SpinnerModel
     */
    public SpinnerModel getModel() {
        return this.model;
    }

    /**
     * The editor is the object that is able to manage the user input and render
     * the value in the spinner.
     * 
     * @param editor
     *            the new editor to use
     */
    public void setEditor(SpinnerEditor editor) {
        this.editor = editor;
        editor.setModel(model);
        display = editor.getTextField();
        display.setColumns(size);
        display.setFont(font);
        display.addFocusListener(new FocusManager(this));
        updateUI();
    }

    /**
     * Returns the current value of the field
     * 
     * @return the current value of the field
     */
    public Object getValue() {
        return editor.getValue();
    }

    /**
     * Sets the font of the display
     */
    public void setFont(Font font) {
        this.font = font;
        display.setFont(font);
    }

    public Font getFont() {
        return font;
    }

    /**
     * Sets the value, respecting the maximum & minimum
     * 
     * @param value
     *            the new value
     */
    public void setValue(Object value) {
        editor.setValue(value);
        model.setValue(value);
    }

    /**
     * The interface into the model. The model delivers the next value in the
     * sequence when this method is called, usually by the Look & Feel delegate.
     * 
     * @return the next value in the sequence
     */
    public Object getNextValue() {
        model.setStep(editor.getStep());
        if (editable) {
            model.setValue(editor.getValue());
        }
        Object x = model.getNextValue();
        notifyListeners(new MChangeEvent(this, x, MChangeEvent.CHANGE));
        return x;
    }

    /**
     * The interface into the model. The model delivers the previous value in
     * the sequence when this method is called, usually be the Look & Feel
     * delegate.
     * 
     * @return the previous value in the sequence
     */
    public Object getPreviousValue() {
        model.setStep(editor.getStep());
        if (editable) {
            model.setValue(editor.getValue());
        }
        Object x = model.getPreviousValue();
        notifyListeners(new MChangeEvent(this, x, MChangeEvent.CHANGE));
        return x;
    }

    /**
     * Disable changing the date using the keyboard, use setEnabled(false) if
     * the date is not to be changed at all.
     * 
     * @param editable
     *            true if the field can be changed by typing.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        editor.setEditable(editable);
    }

    /**
     * Can the date be changed using the keyboard ?
     * 
     * @return is the date changeble using the keyboard ?
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Registers the listeners of the field changes. Fired when the spinner
     * buttons are pushed.
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMChangeListener(MChangeListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMChangeListener(MChangeListener listener) {
        listeners.removeElement(listener);

    }

    private void notifyListeners(MChangeEvent event) {
        // Pass these events on to the registered listener

        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MChangeListener l = (MChangeListener) listeners.elementAt(i);
            l.valueChanged(event);
        }
    }

    /**
     * Simply delgates to super.removeFocusListener(),
     * 
     * @deprecated use removeMFieldListener instead, it is much more reliable
     * @param listener
     */
    public void removeFocusListener(FocusListener listener) {
        super.removeFocusListener(listener);
    }

    /**
     * Simply delgates to super.addFocusListener(),
     * 
     * @deprecated use addMFieldListener instead, it is much more reliable
     * @param listener
     */
    public void addFocusListener(FocusListener listener) {
        super.addFocusListener(listener);
    }

    /**
     * Registers the listeners field getting and losing focus
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMFieldListener(MFieldListener listener) {
        fieldListeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMFieldListener(MFieldListener listener) {
        fieldListeners.removeElement(listener);
    }

    private void notifyListeners(FocusEvent e) {
        int type = e.getID();
        Vector list = (Vector) fieldListeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MFieldListener l = (MFieldListener) list.elementAt(i);
            if (type == FocusEvent.FOCUS_GAINED) {
                l.fieldEntered(new FocusEvent(this, type));
            } else {
                l.fieldExited(new FocusEvent(this, type));
            }
        }
    }

    public void setToolTipText(String text) {
        display.setToolTipText(text);
    }

    public String getToolTipText() {
        return display.getToolTipText();
    }

    /**
     * Set focus on the receiving component if isRequestFocusEnabled returns
     * true
     */
    public void requestFocus() {
        display.requestFocus();
    }

    /** Used to install UI Delegate */
    public void updateUI() {
        setUI(registerUIDelegate());
    }

    /**
     * Used to automatically install the UIDelagate for Windows & Metal Look &
     * Feels. Any other Look & Feel will get the basic look and feel unless a
     * UIDelegate is provided and set in the look and feel class. Custom look
     * and feel delegates can be provided in the normal way (!). Since the only
     * real way to set a look and feel delegate is by means of the LookAndFeel
     * class, this class can only set the user defaults which override the Look
     * and Feel ones. The effect of this is that changing the Look and Feel of
     * the application on the fly still uses the user defaults and ignores the
     * look and feel change.
     * 
     * @see #getUIClassID
     */
    protected ComponentUI registerUIDelegate() {
        ComponentUI compUI = (ComponentUI) UIManager.get(uiClassID);
        if (compUI == null) {
            String uiDelegateClassName = "mseries.plaf.basic.BasicSpinnerUI";
            String lafName = UIManager.getLookAndFeel().getID();
            /*
             * There is no UI Delegate for this component so try to install one
             * of the defaults
             */
            if (lafName.equals("Windows")) {
                uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "SpinnerUI";
            }
            if (lafName.equals("Metal")) {
                uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "SpinnerUI";
            }
            if (lafName.equals("Motif")) {
                uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "SpinnerUI";
            }
            if (lafName.equals("Mac")) {
                uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "SpinnerUI";
            }
            try {
                compUI = (ComponentUI) (Class.forName(uiDelegateClassName)).newInstance();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return compUI;
    }

    /**
     * This method gives the UI Manager a constant to use to look up in the UI
     * Defaults table to find the class name of the UI Delegate for the
     * installed L&F.
     * 
     * @return string "SpinnerUI"
     */
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Simple class to be a focus listener for the MDateSpinner. Removes the
     * need for the spinner itself to be a FocusListener and hides the required
     * methods
     */
    class FocusManager implements FocusListener {
        MSpinner parent;

        public FocusManager(MSpinner parent) {
            this.parent = parent;
        }

        public void focusLost(FocusEvent e) {
            parent.notifyListeners(e);
        }

        public void focusGained(FocusEvent e) {
            parent.notifyListeners(e);
        }
    }

    /*
     * public static void main(String[] argv) { try {
     * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
     * catch(Exception e) { } JFrame f = new JFrame("Demo");
     * 
     * MSpinner m1 = new MSpinner(10); MIntegerSpinnerModel model1=new
     * MIntegerSpinnerModel(40, 100, 0, 1, true); SpinnerEditor ed = new
     * IntegerEditor(); m1.setModel(model1); m1.setEditor(ed);
     * m1.setEditable(true); model1.setMaximum(new Integer(50));
     * 
     * final MSpinner m2 = new MSpinner(10); SpinnerModel model2 = new
     * MDateSpinnerModel(); SpinnerEditor ed2=new DateEditor("MMMMM");
     * m2.setEditor(ed2); m2.setModel(model2); m2.setEditable(false);
     * m2.addMChangeListener(new MChangeListener() { public void
     * valueChanged(MChangeEvent e) { System.out.println("Changed
     * "+m2.getValue()); } });
     * 
     * m1.addMFieldListener(new MFieldListener(){ public void
     * fieldEntered(FocusEvent e) { System.out.println("m1:Entered"); } public
     * void fieldExited(FocusEvent e) { System.out.println("m1:Exited"); } });
     * m2.addMFieldListener(new MFieldListener(){ public void
     * fieldEntered(FocusEvent e) { System.out.println("m2:Entered"); } public
     * void fieldExited(FocusEvent e) { System.out.println("m2:Exited"); } });
     * 
     * final MSpinner m3= new MSpinner(10); String[] names={"Hello", "World"};
     * SpinnerModel model3 = new MListSpinnerModel(names); m3.setModel(model3);
     * m3.addMChangeListener(new MChangeListener() { public void
     * valueChanged(MChangeEvent e) { int index =
     * ((MListSpinnerModel.ListObject)m3.getValue()).index; String name =
     * (String)((MListSpinnerModel.ListObject)m3.getValue()).object;
     * System.out.println(index+", "+name); } });
     * 
     * MSpinner m4 = new MSpinner(4); MFloatSpinnerModel model4=new
     * MFloatSpinnerModel(10, 100, 0, (float)0.5, true); FloatEditor ed4 = new
     * FloatEditor(); m4.setModel(model4); m4.setEditor(ed4);
     * 
     * FlowLayout fl = new FlowLayout(); f.getContentPane().setLayout(fl);
     * f.getContentPane().add(m1); f.getContentPane().add(m2);
     * f.getContentPane().add(m3); f.getContentPane().add(m4);
     * 
     * f.pack(); f.show(); }
     */
}

// $Log: MSpinner.java,v $
// Revision 1.3  2006/06/05 12:31:54  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.2 2006/05/31 14:26:09 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.1 2004/04/02 15:44:08 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.6 2003/01/15 21:47:34 martin
// *** empty log message ***
//
// Revision 1.5 2002/12/21 22:53:16 martin
// *** empty log message ***
//
// Revision 1.4 2002/03/09 14:21:37 martin
// Added setFont & getFont methods
//
// Revision 1.3 2002/02/24 11:29:01 martin
// Focus Events have correct source.
//
