package uk.gov.courtservice.xhibit.business.services.validation;


/**
 * Simple Service for Validating XML.
 * @author William Fardell 
 */       
public interface ValidationService { 
        public ValidationResult validate(final String xml, final String schemaName)
            throws ValidationException;
}
