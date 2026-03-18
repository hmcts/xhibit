package uk.gov.courtservice.xhibit.client.util;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public abstract class XHIBITValueObjectSpec extends XHIBITSearchArgumentSpec {

    protected Class valueObjectClass;

    public XHIBITValueObjectSpec(XHIBITSearch xs) {
        super(xs);
    }

    // Search implementors must implement this method
    public abstract Class getValueObjectClass();

    public class XHIBITValueObjectAttribute {
        public String name;

        public String label;

        public JComponent widget;

        public XHIBITValueObjectAttribute(String attribName, String attribLabel) {
            this(attribName, attribLabel, new JTextField());
        }

        public XHIBITValueObjectAttribute(String attribName, String attribLabel, JComponent uiWidget) {
            this.name = attribName;
            this.label = attribLabel;
            this.widget = uiWidget;
        }
    }

}