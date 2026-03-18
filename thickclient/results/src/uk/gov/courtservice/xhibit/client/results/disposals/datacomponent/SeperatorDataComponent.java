package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: SeperatorDataComponent
 * </p>
 * <p>
 * Description: Use a label and lines to seperate the screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: SeperatorDataComponent.java,v 1.9 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public class SeperatorDataComponent extends JPanel implements DelegatorDataComponent {

    // Constraints
    private static final GridBagConstraints createLeadingSeperatorConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 0, 4, 0);
        return constraints;
    }

    private static final GridBagConstraints createLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(4, 4, 4, 4);
        return constraints;
    }

    private static final GridBagConstraints createTrailingSeperatorConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 0.9;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 0, 4, 0);
        return constraints;
    }

    // Components
    private final JSeparator leadingSeperator = new JSeparator();

    private final JLabel label = new JLabel();

    private final JSeparator trailingSeperator = new JSeparator();

    /*
     * public void doLayout() { System.out.println("Before: leadingSeperator: " +
     * leadingSeperator.getSize()); System.out.println("Before: label: " +
     * label.getSize()); System.out.println("Before: trailingSeperator: " +
     * trailingSeperator.getSize());
     * 
     * super.doLayout();
     * 
     * System.out.println("After: leadingSeperator: " +
     * leadingSeperator.getSize()); System.out.println("After: label: " +
     * label.getSize()); System.out.println("After: trailingSeperator: " +
     * trailingSeperator.getSize()); }
     */

    /**
     * The delegate responsible for doing the work
     */
    private final DelegateDataComponent delegate = new DelegateDataComponent(this);

    /**
     * Create a new instance
     */
    public SeperatorDataComponent() {
        super(new GridBagLayout());
        add(leadingSeperator, createLeadingSeperatorConstraints());
        label.setForeground(DATA_LABEL_COLOR);
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
        return false;
    }

    // Delegate Callbacks

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
        setBackground(color);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String getDataImpl() {
        return parseData(label.getText());
    }

    /**
     * Strip the * and whitespace of the start and end of the data
     */
    private String parseData(String data) {
        if (data != null) {
            int startIndex = data.length();
            for (int i = 0, l = data.length(); i < l; i++) {
                char c = data.charAt(i);
                if (c != '*' && !Character.isWhitespace(c)) {
                    startIndex = i;
                    break;
                }
            }

            int endIndex = 0;
            for (int i = data.length() - 1; i >= startIndex; i--) {
                char c = data.charAt(i);
                if (c != '*' && !Character.isWhitespace(c)) {
                    endIndex = i + 1;
                    break;
                }
            }

            if (startIndex < endIndex) {
                return data.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setDataImpl(String data) {
        String oldData = getData();
        String newData = parseData(data);

        if (newData != null) {
            if (!newData.equals(oldData)) {
                removeAll();
                label.setText(newData);
                add(leadingSeperator, createLeadingSeperatorConstraints());
                add(label, createLabelConstraints());
                add(trailingSeperator, createTrailingSeperatorConstraints());
                doLayout();
                delegate.fireDataChanged();
            }
        } else {
            if (oldData != null) {
                removeAll();
                label.setText("");
                add(leadingSeperator, createLeadingSeperatorConstraints());
                doLayout();
                delegate.fireDataChanged();
            }
        }
    }

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
    public String createToolTipTextImpl() {
        return null;
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
     * Set the previous data component.
     */
    public void setPreviousDataComponent(DataComponent previousDataComponent) {
        // Dont need to access the previous component
    }

}
