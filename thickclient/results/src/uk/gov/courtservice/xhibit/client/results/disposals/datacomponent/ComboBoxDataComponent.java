package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.util.ArrayComboBoxModel;
import uk.gov.courtservice.xhibit.client.util.XColor;


/**
 * <p>
 * Title: DefaultDataComponent
 * </p>
 * <p>
 * Description: Use a label for the data
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @version $Id: ComboBoxDataComponent.java,v 1.1 2009/03/24 10:02:44 hewittm Exp $
 */

public abstract class ComboBoxDataComponent extends JComboBox implements DelegatorDataComponent {

    private static final long serialVersionUID = 1L;

    /**
     * The delegate responsible for doing the work
     */
    protected final DelegateDataComponent delegate = new DelegateDataComponent(this);

    /**
     * Construct a new instance
     */
    public ComboBoxDataComponent() {
        super();
        setRenderer(new ComboBoxCellRenderer());
        addActionListener(delegate);
    }

    protected void setModel(String[] options) {
        int selectedIndex = getSelectedIndex();
        setModel(new ArrayComboBoxModel(options));
        setSelectedIndex(selectedIndex);
    }

    /**
     * DataComponent Implementation
     */
    public Component getComponent() {
        return this;
    }

    /**
     * DataComponent Implementation
     */
    public boolean isFixedSize() {
        return true;
    }

    // Callbacks

    /**
     * DelegatorDataComponent Implementation
     */
    public Color getBackgroundImpl() {
        return getBackground();
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setBackgroundImpl(Color color) {
        ((ComboBoxCellRenderer) getRenderer()).setBackgroundImpl(color);
        repaint();
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String getDataImpl() {
        // PR57235: Check if the result is the blank item.
        String selectedItem = (String) getSelectedItem();
        if (selectedItem != null && selectedItem.trim().length() > 0) {
            return selectedItem;
        }
        return null;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setDataImpl(String data) {
        setSelectedIndex(getIndex(data));
    }

    abstract protected int getIndex(String data);

    /**
     * DelegatorDataComponent Implementation
     */
    public void paintImpl(Graphics g) {
        super.paint(g);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public Insets getPaintInsetsImpl() {
        return DEFAULT_PAINT_INSETS;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public boolean hasError() {
        return false;
    }

    // Delegate

    /**
     * Return true if the component should be displayed
     */
    public boolean isScreenPrint() {
        return delegate.isScreenPrint();
    }

    /**
     * Set to true to display the component
     */
    public void setScreenPrint(boolean screenPrint) {
        delegate.setScreenPrint(screenPrint);
    }

    /**
     * DataComponent Implementation
     */
    public void setData(String data) {
        delegate.setData(data);
    }

    /**
     * DataComponent Implementation
     */
    public String getData() {
        return delegate.getData();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isMandatory() {
        return delegate.isMandatory();
    }

    /**
     * DataComponent Implementation
     */
    public void setMandatory(boolean mandatory) {
        delegate.setMandatory(mandatory);
    }

    /**
     * DataComponent Implementation
     */
    public boolean isComplete() {
        return delegate.isComplete();
    }

    /**
     * DataComponent Implementation
     */
    public void setNameG1(String nameG1) {
        delegate.setNameG1(nameG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setNameG2(String nameG2) {
        delegate.setNameG2(nameG2);
    }

    /**
     * DataComponent Implementation
     */
    public String getNameG1() {
        return delegate.getNameG1();
    }

    /**
     * DataComponent Implementation
     */
    public String getNameG2() {
        return delegate.getNameG2();
    }

    /**
     * DataComponent Implementation
     */
    public void setColorG1(XColor colorG1) {
        delegate.setColorG1(colorG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setColorG2(XColor colorG2) {
        delegate.setColorG2(colorG2);
    }

    /**
     * DataComponent Implementation
     */
    public XColor getColorG1() {
        return delegate.getColorG1();
    }

    /**
     * DataComponent Implementation
     */
    public XColor getColorG2() {
        return delegate.getColorG2();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isDeletedG1() {
        return delegate.isDeletedG1();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isDeletedG2() {
        return delegate.isDeletedG2();
    }

    /**
     * DataComponent Implementation
     */
    public void setDeletedG1(boolean deletedG1) {
        delegate.setDeletedG1(deletedG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setDeletedG2(boolean deletedG2) {
        delegate.setDeletedG2(deletedG2);
    }

    /**
     * Add the listener
     */
    public void addDataComponentListener(DataComponentListener listener) {
        delegate.addDataComponentListener(listener);
    }

    /**
     * Remove the listener
     */
    public void removeDataComponentListener(DataComponentListener listener) {
        delegate.removeDataComponentListener(listener);
    }

    /**
     * DataComponent Implementation
     */
    public void setMaxChars(int maxChars) {
        delegate.setMaxChars(maxChars);
    }

    /**
     * DataComponent Implementation
     */
    public int getMaxChars() {
        return delegate.getMaxChars();
    }

    /**
     * DataComponent Implementation
     */
    public void paint(Graphics g) {
        delegate.paint(g);
    }

    /**
     * The following class is required to ensure the combo box is painted
     * correctly
     */
    private static class ComboBoxCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        private Color main;

        private boolean isSelected = false;

        private boolean isEnabled = false;

        public void setBackgroundImpl(Color main) {
            this.main = main;
        }

        public Color getBackground() {
            return (main == null || isSelected || !isEnabled) ? super.getBackground() : main;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            this.isSelected = isSelected;
            this.isEnabled = list.isEnabled();
            return component;
        }
    }
}
