package uk.gov.courtservice.framework.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Allow multiple content handlers to process the same stream of events
 * 
 * @author Will Fardell
 */
public class ForkContentHandler implements ContentHandler {
    /**
     * The first handler to recieve sax events
     */
    private final ContentHandler handler1;

    /**
     * The second handler to recieve sax events
     */
    private final ContentHandler handler2;

    /**
     * Construct a new ForkContentHandler for the handlers
     */
    public ForkContentHandler(ContentHandler handler1, ContentHandler handler2) {
        if (handler1 == null) {
            throw new IllegalArgumentException("handler1: null");
        }
        if (handler2 == null) {
            throw new IllegalArgumentException("handler2: null");
        }
        this.handler1 = handler1;
        this.handler2 = handler2;
    }

    /**
     * ContentHandler implementation: Set the document loacator for the content
     * handlers
     */
    public void setDocumentLocator(Locator locator) {
        handler1.setDocumentLocator(locator);
        handler2.setDocumentLocator(locator);
    }

    /**
     * ContentHandler implementation: Receive notification of the beginning of a
     * document.
     */
    public void startDocument() throws SAXException {
        handler1.startDocument();
        handler2.startDocument();
    }

    /**
     * ContentHandler implementation: Receive notification of the end of a
     * document.
     */
    public void endDocument() throws SAXException {
        handler1.endDocument();
        handler2.endDocument();
    }

    /**
     * ContentHandler implementation: Begin the scope of a prefix-URI Namespace
     * mapping.
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        handler1.startPrefixMapping(prefix, uri);
        handler2.startPrefixMapping(prefix, uri);
    }

    /**
     * ContentHandler implementation: End the scope of a prefix-URI mapping.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        handler1.endPrefixMapping(prefix);
        handler2.endPrefixMapping(prefix);
    }

    /**
     * ContentHandler implementation: Receive notification of the beginning of
     * an element.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        handler1.startElement(namespaceURI, localName, qName, atts);
        handler2.startElement(namespaceURI, localName, qName, atts);
    }

    /**
     * ContentHandler implementation: Receive notification of the end of an
     * element.
     */
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        handler1.endElement(namespaceURI, localName, qName);
        handler2.endElement(namespaceURI, localName, qName);
    }

    /**
     * ContentHandler implementation: Receive notification of character data.
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        handler1.characters(ch, start, length);
        handler2.characters(ch, start, length);
    }

    /**
     * ContentHandler implementation: Receive notification of ignorable
     * whitespace in element content.
     */
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        handler1.ignorableWhitespace(ch, start, length);
        handler2.ignorableWhitespace(ch, start, length);
    }

    /**
     * ContentHandler implementation: Receive notification of a processing
     * instruction.
     */
    public void processingInstruction(String target, String data) throws SAXException {
        handler1.processingInstruction(target, data);
        handler2.processingInstruction(target, data);
    }

    /**
     * ContentHandler implementation: Receive notification of a skipped entity.
     */
    public void skippedEntity(String name) throws SAXException {
        handler1.skippedEntity(name);
        handler2.skippedEntity(name);
    }
}
