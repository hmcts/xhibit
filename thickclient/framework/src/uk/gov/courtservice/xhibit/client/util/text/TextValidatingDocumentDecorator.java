package uk.gov.courtservice.xhibit.client.util.text;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * 
 * @author N Walters
 * @version 1.0
 */

public class TextValidatingDocumentDecorator extends ValidatingDocumentDecorator {

    private static final Logger log = CSServices.getLogger(TextValidatingDocumentDecorator.class);

    public TextValidatingDocumentDecorator() {
        super();
        log.debug("TextDocument()");
    }

    public boolean validate(String candidate) {
        boolean rc = true;
        if (candidate.length() > 0) {
            char[] toTest = candidate.toCharArray();
            for (int i = 0; i < toTest.length; i++) {
                if (!Character.isLetter(toTest[i]) && toTest[i] != ' ') {
                    rc = false;
                    break;
                }
            }
        }
        return rc;
    }
}