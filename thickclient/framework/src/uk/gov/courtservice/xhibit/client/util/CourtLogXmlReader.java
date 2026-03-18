package uk.gov.courtservice.xhibit.client.util;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: Xhibit 2 Client Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CourtLogXmlReader {
    public boolean debug = false;

    private InputStream xmlFile;

    private boolean validFile;

    private Hashtable xmlHandlers;

    public Document document;

    public CourtLogXmlReader() throws Exception {
        throw new Exception("Do not instantiate CourtLogXmlReader without the req. string attrib xmlFileName!");
    }

    public CourtLogXmlReader(InputStream fileName) {
        this.xmlFile = fileName;
    }

    public void initialize() {
        this.validFile = false;
        this.xmlHandlers = new Hashtable();
        this.openDocument();
    }

    public void openDocument() {
        try {
            // DOMBuilder builder = new DOMBuilder(false);
            // builder.setValidation(false);
            // this.document = builder.build(this.xmlFile);

            SAXBuilder builder = new SAXBuilder(false);
            this.document = builder.build(this.xmlFile);
            this.validFile = true;
        } catch (Exception e) {
            XHIBITConstant.debug("Exception opening " + xmlFile);
            XHIBITConstant.handleError(e);
        }
    }

    public void parseXML() {
        if (!this.validFile) {
            this.openDocument();
        }
        this.parseXMLElementsIn(this.document);
    }

    public void parseXMLElementsIn(Document d) {
        tryHandling(d.getRootElement());
        this.parseXMLElementsIn(d.getRootElement());
    }

    public void parseXMLElementsIn(Element e) {
        List children = e.getChildren();
        Iterator childIterator = children.iterator();
        while (childIterator.hasNext()) {
            Element element = (Element) childIterator.next();
            this.tryHandling(element);
            this.parseXMLElementsIn(element);
        }
    }

    private void tryHandling(Element e) {
        try {
            Object o = this.xmlHandlers.get(e.getName());
            if (o == null) {
                if (debug)
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant
                            .debug("CourtLogXMLReader: No CourtLogXMLHandler fournd for element " + e.getName());
            } else {
                CourtLogXMLHandler clXMLH = (CourtLogXMLHandler) o;
                if (clXMLH == null) {
                    if (debug)
                        uk.gov.courtservice.xhibit.client.util.XHIBITConstant
                                .debug("CourtLogXMLReaer: Trouble instantiating CourtLogXMLHandler for element "
                                        + e.getName());
                } else {
                    Object[] args = { e };
                    try {
                        clXMLH.m.invoke(clXMLH.o, args);
                    } catch (Exception ee) {
                        if (debug)
                            uk.gov.courtservice.xhibit.client.util.XHIBITConstant
                                    .debug("CourtLogXMLReader: Error invoking xml handling method for element "
                                            + e.getName());
                        ee.printStackTrace(System.err);
                    }
                }
            }
        } catch (Exception eee) {
            XHIBITConstant.debug("CourtLogXMLReader: Exception in tryHandling(Element e)");
            XHIBITConstant.handleError(eee);
        }
    }

    // for now Namespace independent - bad.
    public void executeWhen(String elementName, Object o, Method m) {
        this.xmlHandlers.put(elementName, new CourtLogXMLHandler(o, m));
    }

    private class CourtLogXMLHandler {
        public Object o;

        public Method m;

        public CourtLogXMLHandler(Object o, Method m) {
            this.o = o;
            this.m = m;
        }
    }
}