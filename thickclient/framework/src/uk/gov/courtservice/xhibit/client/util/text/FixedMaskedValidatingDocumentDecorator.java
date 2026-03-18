package uk.gov.courtservice.xhibit.client.util.text;

import javax.swing.text.Document;
import javax.swing.text.Position;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: FixedMaskedValidatingDocumentDecorator
 * </p>
 * <p>
 * Description: Document decorator that only allows characters to be entered
 * that match a mask.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 28-07-2003 AW Daley Initial Version
 */
public class FixedMaskedValidatingDocumentDecorator extends ValidatingDocumentDecorator {
    /** Mask */
    private String mask = null;

    private final static MaskCharacters maskCharacters = new MaskCharacters();

    private final static Logger log = CSServices.getLogger(FixedMaskedValidatingDocumentDecorator.class);

    /**
     * Construct a FixedMaskedValidatingDocumentDecorator which will delegate to
     * an implementation of the Document interface with the regular expression.
     * 
     * @param mask
     *            the mask that the document must comply to.
     */
    public FixedMaskedValidatingDocumentDecorator(String mask) {
        super();
        this.mask = mask;
    }

    /**
     * Construct a FixedMaskedValidatingDocumentDecorator which delegates to the
     * specified Document and with the mask
     * 
     * @param wrappedDocument
     *            the Docuemnt to delegate to.
     * @param mask
     *            the mask the document must comply to
     */
    public FixedMaskedValidatingDocumentDecorator(Document wrappedDocument, String mask) {
        super(wrappedDocument);
        this.mask = mask;
    }

    /**
     * ValidatingDocumentDecorator Implementation returns true if document
     * matches the regular expression. Nedd to validate the string as the user
     * presses each key. This method splits the regular expression into
     * expressions. Determines which expression to use against the current key
     * pressed.
     * 
     * @param candidate
     *            the text to validate.
     * @return true if the candidate text matches the mask.
     */
    public boolean validate(String candidate) {
        // If mask is null then act as a standard text box
        if (mask == null)
            return true;

        // If length of candidate is greater than the mask then return invalid
        if (candidate.length() > mask.length())
            return false;

        // Gets position of character to be inserted into the document
        Position endPos = this.getEndPosition();
        if (endPos.getOffset() <= 0)
            return false;

        // Handles case when backspace is pressed
        if (endPos.getOffset() > candidate.length())
            return true;

        int charPos = endPos.getOffset() - 1;

        // gets the mask character corresponding to the position of the
        // character
        // entered
        String maskChar = this.mask.substring(charPos, charPos + 1);
        if (maskChar.equalsIgnoreCase(""))
            return false;

        // gets the character entered
        String charToValidate = candidate.substring(charPos, charPos + 1);
        if (charToValidate.equalsIgnoreCase(""))
            return false;

        // Validates entered character against the mask character
        return isValid(maskChar, charToValidate);
    }

    /**
     * Determines if the character is contained in a set of characters defined
     * by the mask character.
     * 
     * @param maskChar
     * @param charToValidate
     * @return
     */
    public boolean isValid(String maskChar, String charToValidate) {
        // Get valid characters for mask place holder. If no characters returned
        // then masked place holder is invalid.
        String validChars = (String) maskCharacters.get(maskChar);
        if (validChars == null) {
            log.error("Mask contains invalid character: " + maskChar);
            return false;
        }

        // Validate character against valid characters defined by the mask
        int charPos = validChars.indexOf(charToValidate);

        if (charPos == -1)
            return false;
        else
            return true;
    }

}