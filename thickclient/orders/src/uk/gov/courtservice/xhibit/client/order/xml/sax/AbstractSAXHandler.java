package uk.gov.courtservice.xhibit.client.order.xml.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * 
 * <p>
 * Title: AbstractSAXHandler
 * </p>
 * <p>
 * Description: Abstract SAX handler class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis
 * @version 1.0
 */
public class AbstractSAXHandler {

    /**
     * Retruns a char[] of characters
     * 
     * @param chars
     * @param i
     * @param i1
     * @throws SAXException
     */
    public void characters(char[] chars, int i, int i1) throws SAXException {
    }

    /**
     * Implmentation of endPrefixMapping
     * 
     * @param s
     * @throws SAXException
     */
    public void endPrefixMapping(String s) throws SAXException {
    }

    /**
     * Implmentation of ignorableWhitespace
     * 
     * @param chars
     * @param i
     * @param i1
     * @throws SAXException
     */
    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {
    }

    /**
     * Implmentation of processingInstruction
     * 
     * @param s
     * @param s1
     * @throws SAXException
     */
    public void processingInstruction(String s, String s1) throws SAXException {
    }

    /**
     * Implmentation of setDocumentLocator
     * 
     * @param locator
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Implmentation of skippedEntity
     * 
     * @param s
     * @throws SAXException
     */
    public void skippedEntity(String s) throws SAXException {
    }
}
