package uk.gov.courtservice.xhibit.client.util.text;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani, Will Fardell
 * @version 1.0
 */

public class AlphaNumericValidatingDocumentDecorator extends ValidatingDocumentDecorator {

    private static final Logger log = CSServices.getLogger(AlphaNumericValidatingDocumentDecorator.class);

    public AlphaNumericValidatingDocumentDecorator() {
        super();
        log.debug("AlphaNumericDocument()");
    }

    public boolean validate(String candidate) {
        boolean rc = true;
        if (candidate.length() > 0) {
            char[] toTest = candidate.toCharArray();
            for (int i = 0; i < toTest.length; i++) {
                if (!Character.isDigit(toTest[i]) && !Character.isLetter(toTest[i]) && toTest[i] != ' ') {
                    rc = false;
                    break;
                }
            }
        }
        return rc;
    }
}