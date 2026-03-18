package uk.gov.courtservice.xhibit.client.util.text;

/**
 * <p>
 * Title: LineLengthValidatingDocumentDecorator
 * </p>
 * <p>
 * Description: Document to validate line length
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra, Will Fardell
 * @version 1.0
 */

public class LineLengthValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    private int maxLineLength;

    public LineLengthValidatingDocumentDecorator(int maxLineLength) {
        if (maxLineLength < 0) {
            throw new IllegalArgumentException("maxLineLength: " + maxLineLength);
        }

        this.maxLineLength = maxLineLength;
    }

    public boolean validate(String candidate) {
        char[] chars = candidate.toCharArray();
        int lineLength = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                lineLength = 0;
            } else {
                lineLength += 1;
            }

            if (lineLength > maxLineLength) {
                return false;
            }
        }
        return true;
    }
}