package uk.gov.courtservice.xhibit.client.widgetfactory;

import uk.gov.courtservice.xhibit.client.util.text.AlphaNumericValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.DocumentDecoratorAdapter;
import uk.gov.courtservice.xhibit.client.util.text.FixedMaskedValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.FloatValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.LongValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.TextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UTF8LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UnlimitedNumericValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UpperCaseTransformingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.YesOrNoDocumentDecorator;

/**
 * @author Meeraj
 * @title Capability The class models a capaibilty for a text field. The class
 *        enumerates a finite set of wellknown capabilities. The only reason the
 *        enumerations are implemnted as methods instead of fields is that some
 *        of the capabilities are parameterized.
 */
/*
 * Ref Date Author Description
 * 
 * 28-07-2003 AW Daley Masked Text capability added
 */
public final class Capability {
    // Document for the capability
    private DocumentDecoratorAdapter adapter;

    // -----------------------------------------------------------------------------
    /**
     * Creates a capability with backing adapter
     * 
     * @param Underlying
     *            adapter
     */
    private Capability(DocumentDecoratorAdapter newAdapter) {
        if (newAdapter == null)
            throw new IllegalArgumentException("newAdapter");
        adapter = newAdapter;
    }

    // -----------------------------------------------------------------------------
    /**
     * Sub classes should implement this method
     * 
     * @return
     */
    protected DocumentDecoratorAdapter getDocument() {
        return adapter;
    }

    // -----------------------------------------------------------------------------
    /**
     * Returns a Yes or No capability
     * 
     * @return Yes or No capability
     */
    public static Capability yesOrNo() {
        return new Capability(new YesOrNoDocumentDecorator());
    }

    // -----------------------------------------------------------------------------
    /**
     * Returns an upper case capability
     * 
     * @return Upper case capability
     */
    public static Capability upperCase() {
        return new Capability(new UpperCaseTransformingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------
    /**
     * Returns a numeric capability
     * 
     * @return Numeric capability
     */
    public static Capability numeric() {
        return new Capability(new NumericValidatingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------
    /**
     * Returns limitted text capability
     * 
     * @param Number
     *            of characters
     * @return Limitted Text Capability
     */
    public static Capability limitedText(final int numChars) {
        return new Capability(new LimitedTextValidatingDocumentDecorator(numChars));
    }

    // -----------------------------------------------------------------------------
    // -----------------------------------------------------------------------------
    /**
     * Returns masked text capability
     * 
     * @param mask
     * @return masked text Capability
     */
    public static Capability maskedText(final String mask) {
        return new Capability(new FixedMaskedValidatingDocumentDecorator(mask));
    }

    // -----------------------------------------------------------------------------

    /**
     * Returns long numeric capability
     * 
     * @param mask
     * @return long numeric Capability
     */
    public static Capability longNumeric() {
        return new Capability(new LongValidatingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------

    /**
     * Returns unlimited numeric capability
     * 
     * @return numeric Capability
     */
    public static Capability unlimitedNumeric() {
        return new Capability(new UnlimitedNumericValidatingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------
    
    /**
     * Returns float numeric capability
     * 
     * @param mask
     * @return float numeric Capability
     */
    public static Capability floatNumeric() {
        return new Capability(new FloatValidatingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------

    /**
     * Returns Alpha Numeric capability
     * 
     * @param mask
     * @return alpha numeric Capability
     */
    public static Capability alphaNumeric() {
        return new Capability(new AlphaNumericValidatingDocumentDecorator());
    }
    
    /**
     * Returns Text capability
     * 
     * @return text Capability
     */
    public static Capability plainText() {
        return new Capability(new TextValidatingDocumentDecorator());
    }

    // -----------------------------------------------------------------------------

    /**
     * Returns UTF8 Limited Text capability
     * 
     * @param mask
     * @return utf8LimitedTextCapability
     */
    public static Capability utf8LimitedTextCapability(final int numUTF8Chars) {
        return new Capability(new UTF8LimitedTextValidatingDocumentDecorator(numUTF8Chars));
    }

}
