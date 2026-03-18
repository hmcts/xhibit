package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.text.ParseException;
import java.util.Date;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.util.XColor;
import uk.gov.courtservice.xhibit.client.util.XDateField;

/**
 * <p>
 * Title: DateFieldDataComponent
 * </p>
 * <p>
 * Description: Use a label for the data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DateFieldDataComponent.java,v 1.12 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public class DateFieldDataComponent extends XDateField implements DelegatorDataComponent {
    private static final String DATE_FORMAT = "dd-MMM-yyyy";

    /**
     * The delegate responsible for doing the work
     */
    private final DelegateDataComponent delegate = new DelegateDataComponent(this);

    /**
     * Construct a new instance
     */
    public DateFieldDataComponent() {
        super(DATE_FORMAT);
        addActionListener(delegate);
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

    // Delegate Callbacks

    /**
     * DelegatorDataComponent Implementation
     */
    public Color getBackgroundImpl() {
        return display.getBackground();
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setBackgroundImpl(Color color) {
        display.setBackground(color);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setDataImpl(String text) {
        setValue(valueOf(text));
    }

    private Date valueOf(String text) {
        if (text != null) {
            try {
                return parse(text);
            } catch (ParseException pe) {
                // Fall through
            }
        }
        return new Date();
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String getDataImpl() {
        try {
            Date value = getValue();
            if (value != null) {
                return format(value);
            }
        } catch (ParseException pe) {
            // Fall through
        }
        return null;
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
        return !isDateFormatValid();
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
