package uk.gov.courtservice.xhibit.client.util.text;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: FloatValidatingDocumentDecorator
 * </p>
 * <p>
 * Description: A document that can only contain a java primitive float or an
 * empty string
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class FloatValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    private static final Logger log = CSServices.getLogger(FloatValidatingDocumentDecorator.class);

    public FloatValidatingDocumentDecorator() {
        log.debug("FloatValidatingDocumentDecorator()");
    }

    public boolean validate(String candidate) {
        log.debug("validate(\"" + candidate + "\")");

        if (candidate.length() == 0) {
            return true;
        } else {
            try {
                Float.valueOf(candidate);
                return checkDigitPeriod(candidate);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
    }

    private boolean checkDigitPeriod(String candidate) {
        boolean rc = true;
        char[] toTest = candidate.toCharArray();
        for (int i = 0; i < toTest.length; i++) {
            if (!Character.isDigit(toTest[i]) && (toTest[i] != '.')) {
                rc = false;
                break;
            }
        }
        return rc;
    }
}