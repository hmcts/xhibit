package uk.gov.courtservice.xhibit.business.services.validation;

public interface ValidationResult {
    /**
     * @return true if the document was valid
     */
    boolean isValid();
    /**
     * @return a String containg details of any errors
     */
    String toString(); 
}
