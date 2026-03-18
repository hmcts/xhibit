package uk.gov.courtservice.framework.services.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author pznwc5
 * 
 * Default error handler
 */
public class ErrorChecker implements ErrorHandler {
    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void warning(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXParseException {
        throw e;
    }
}
