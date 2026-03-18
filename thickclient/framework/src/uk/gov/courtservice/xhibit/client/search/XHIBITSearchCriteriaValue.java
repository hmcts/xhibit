package uk.gov.courtservice.xhibit.client.search;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Criteria Object
 * </p>
 * <p>
 * Description: Used by Criteria panel to display the input fields
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.3 $
 */
public class XHIBITSearchCriteriaValue {
    /**
     * The field name corresponds to the name defined in the interface in mid
     * tier criteria object
     */
    private String _fieldName;

    /**
     * The resource key is used for the label
     */
    private String _resourceKey;

    /**
     * The component is displayed next to the label for entering criteria. By
     * default a JTextField is supplied. A null component results in a hidden
     * field for passing criteria such as the court id, obs_ind etc.
     */
    private Object _component = null;

    /**
     * The visibility can be controlled using this parameter
     */
    private boolean _visiblity = true;

    /**
     * This is the default value the will be set in the JComponent. Currently
     * default values are only set for JTextComponent, JList and JComboBox
     */
    private String _defaultValue = "";

    /**
     * Creates a JTextField that is visible with no default value and a lable
     * corresponding to the resourceKey passed in
     * 
     * @param fieldName
     * @param resourceKey
     */
    public XHIBITSearchCriteriaValue(String fieldName, String resourceKey) {
        this(fieldName, resourceKey, true, "");
    }

    /**
     * Similar to full constructor but default to a JTextField so you don't have
     * to pass one in.
     * 
     * @param fieldName
     * @param resourceKey
     * @param visible
     * @param defaultValue
     */
    public XHIBITSearchCriteriaValue(String fieldName, String resourceKey, boolean visible, String defaultValue) {
        setFieldName(fieldName);
        setResourceKey(resourceKey);
        setVisible(visible);
        setDefaultValue(defaultValue);
        setComponent(createDefaultTextField());
        if (defaultValue != null)
            ((JTextField) getComponent()).setText(defaultValue);
        ((JTextField) getComponent()).setVisible(visible);
    }

    /**
     * Full constructor If you just want a hidden field with a single value,
     * pass a null for the JComponent and a simple value will be created Note: a
     * null for the JComponent will default to an invisible value
     * 
     * @param fieldName
     * @param resourceKey
     * @param component
     * @param visible
     * @param defaultValue
     */
    public XHIBITSearchCriteriaValue(String fieldName, String resourceKey, Object component, boolean visible,
            String defaultValue) {
        setFieldName(fieldName);
        setResourceKey(resourceKey);
        setVisible(visible);
        setDefaultValue(defaultValue);
        setComponent(component);
        if (getComponent() != null && getComponent() instanceof JComponent)
        	((JComponent) getComponent()).setVisible(visible);
        populateDefaultValue();
    }

    /**
     * Attempts to establish the type of the component and set the default value
     */
    private void populateDefaultValue() {
        if (getComponent() != null && getDefaultValue() != null) {

    		if (getComponent() instanceof JTextComponent) {
                ((JTextComponent) getComponent()).setText(getDefaultValue());
            } else if (getComponent() instanceof JList) {
                ((JList) getComponent()).setSelectedValue(getDefaultValue(), true);
            } else if (getComponent() instanceof JComboBox) {
                ((JComboBox) getComponent()).setSelectedItem(getDefaultValue());
            }

        }
    }

    private JTextField createDefaultTextField() {
        Dimension defaultDim = new Dimension(150, XHIBITConstant.getLineHeight());
        JTextField jtf = new JTextField();
        jtf.setMinimumSize(defaultDim);
        jtf.setPreferredSize(defaultDim);
        return jtf;
    }

    // default getters and setters

    public Object getComponent() {
        return _component;
    }

    public String getDefaultValue() {
        return _defaultValue;
    }

    public String getFieldName() {
        return _fieldName;
    }

    public String getResourceKey() {
        return _resourceKey;
    }

    public boolean isVisible() {
        return _visiblity;
    }

    public void setComponent(Object component) {
        _component = component;
    }

    public void setDefaultValue(String defaultValue) {
        _defaultValue = defaultValue;
    }

    public void setFieldName(String fieldName) {
        _fieldName = fieldName;
    }

    public void setResourceKey(String resourceKey) {
        _resourceKey = resourceKey;
    }

    public void setVisible(boolean visible) {
        _visiblity = visible;
    }
}