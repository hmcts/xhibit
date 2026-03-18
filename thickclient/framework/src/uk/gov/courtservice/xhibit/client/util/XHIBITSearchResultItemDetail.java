package uk.gov.courtservice.xhibit.client.util;

import java.util.Vector;

import javax.swing.JComponent;

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

public abstract class XHIBITSearchResultItemDetail extends XHIBITValueObjectSpec {
    protected Vector displayAttributes;

    public XHIBITSearchResultItemDetail(XHIBITSearch xs) {
        super(xs);
        this.displayAttributes = new Vector();
    }

    // must be implemented to set the attributes to be displaed asn their UI
    // widget, as with criteria
    public abstract void setValueObjectDisplay();

    protected void addXHIBITValueObjectAttribute(String name, String label) {
        XHIBITValueObjectAttribute xHIBITValueObjectAttribute = new XHIBITValueObjectAttribute(name, label);
        this.displayAttributes.add(xHIBITValueObjectAttribute);
    }

    protected void addXHIBITValueObjectAttribute(String name) {
        String label = XHIBITConstant.getResource(xs.rsc, "xs.gen." + xs.SearchResultsItemDetailsCardID + ".label");
        try {
            label = XHIBITConstant.getResource(xs.rsc, xs.getComponentResourceKey() + "." + name);
        } catch (Exception e) {
            xs.debug("addXHIBITValueObjectAttribute could not find a customezied label from the atribute " + name
                    + " for card  " + xs.SearchResultsItemDetailsCardID);
        }
        addXHIBITValueObjectAttribute(name, label);
    }

    protected void addXHIBITValueObjectAttribute(String name, String label, JComponent widget) {
        XHIBITValueObjectAttribute xHIBITValueObjectAttribute = new XHIBITValueObjectAttribute(name, label, widget);
        this.displayAttributes.add(xHIBITValueObjectAttribute);
    }
}