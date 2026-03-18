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
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Useful implementation of SpinnerEditor that manages the model-editor
 * relationship and the listeners. The only methods that require overloading are
 * 
 * <PRE>
 * 
 * getValue() setValue(Object newValue)
 * 
 * </PRE>
 */
public class DefaultSpinnerEditor implements SpinnerEditor {

    protected Object value;

    /**
     * The text field that is used to render the values. This value is public so
     * that applications using the component can access it directly for testing
     * focus or adding keyboard actions for example. Caution should be exercised
     * when doing this.
     */
    public JTextField display = new JTextField(6) {
        public void setUI(TextUI ui) {
            super.setUI(ui);
            setBorder(null);
        }
    };

    private boolean editable = false;

    protected boolean setting = false;

    SpinnerModel model;

    public DefaultSpinnerEditor() {
        display.setDocument(getCustomDocument());
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object v) {
        setting = true;
        this.value = v;
        display.setText(v.toString());
        setting = false;
    }

    public JTextField getTextField() {
        return display;
    }

    public int getStep() {
        return 0;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setModel(SpinnerModel model) {
        if (this.model != null) {
            this.model.removeChangeListener(this);
        }
        this.model = model;
        setValue(model.getValue());
        this.model.addChangeListener(this);
    }

    public SpinnerModel getModel() {
        return this.model;
    }

    public void stateChanged(ChangeEvent e) {
        setValue(model.getValue());
    }

    /**
     * Oerride this method to supply the custom document to the textfield. The
     * document is used to restrict the characters that can be typed into the
     * field.
     * 
     * @return the custom document
     */
    public Document getCustomDocument() {
        return new ReadOnlyDocument();
    }

    /**
     * The custom document that prevents editing of he field yet allows the
     * curser to be landed in the field.
     */
    protected class ReadOnlyDocument extends PlainDocument {
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (isEditable() || setting) {
                super.insertString(offset, str, a);
            }
        }

        public void remove(int offset, int len) throws BadLocationException {
            if (isEditable() || setting) {
                super.remove(offset, len);
            }
        }
    }
}
