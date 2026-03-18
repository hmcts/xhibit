package uk.gov.courtservice.xhibit.client.util.text;

/**
 * <p>
 * Title: Document decorator that only allows positive numbers to be entered.
 * </p>
 * <p>
 * Description: Validates a numeric string without limiting by a Long.parse 
 * (as the NumericValidatingDocumentDecorator does at present - limiting to 19 chrs).
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class UnlimitedNumericValidatingDocumentDecorator extends NumericValidatingDocumentDecorator {

	@Override
	public boolean validate(String candidate) {
        boolean rc = true;
        if (candidate.length() > 0) {
            char[] toTest = candidate.toCharArray();
            for (int i = 0; i < toTest.length; i++) {
                if (!Character.isDigit(toTest[i])) {
                    rc = false;
                    break;
                }
            }
        }
        return rc;
    }
}