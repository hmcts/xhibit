package uk.gov.courtservice.xhibit.business.services.validation.sax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.gov.courtservice.xhibit.business.services.validation.ValidationResult;

/**
 * SAX Error handler for collating and printing errors.
 * @author Fardellwi
 */
public class ErrorHandlerValidationResult implements ErrorHandler, ValidationResult {
    
    private final List<Error> errorList = new ArrayList<Error>();  

    public void error(final SAXParseException exception) throws SAXException {
        errorList.add(new Error(Error.Type.ERROR, exception));
    }

    public void fatalError(final SAXParseException exception) throws SAXException {
        errorList.add(new Error(Error.Type.FATAL, exception));       
    }

    public void warning(final SAXParseException exception) throws SAXException {
        errorList.add(new Error(Error.Type.WARN, exception));        
    }
    
    public boolean isValid() {
        for(Error error : errorList) {
            if(error.type != Error.Type.WARN) {
                return false;
            }
        }
        return true;
    }
    
    public String toString() {        
        Iterator<Error> errors = errorList.iterator();
        if(errors.hasNext()) {
            StringBuilder builder = new StringBuilder();
            builder.append(errors.next());
            while(errors.hasNext()) {
                builder.append("\n");
                builder.append(errors.next());    
            }
            return builder.toString();
        }
        return "";
    }
    
    private static class Error {
        private static enum Type {
            FATAL, ERROR, WARN 
        };
        
        private final Type type;
        private final SAXParseException exception;
        
        public Error(final Type pType, final SAXParseException pException ) {
            if(pType == null) {
                throw new IllegalArgumentException("pType: null");
            }
            if(pException == null) {
                throw new IllegalArgumentException("pException: null");
            }
            type = pType;
            exception = pException;
        }
        
        public String toString() {
            return type + " (" + exception.getLineNumber() + ":" + exception.getColumnNumber() + "): " + exception.getMessage();
        }
    }
}
