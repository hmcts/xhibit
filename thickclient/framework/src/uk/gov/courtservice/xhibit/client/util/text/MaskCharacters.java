package uk.gov.courtservice.xhibit.client.util.text;

import java.util.HashMap;
import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: MaskCharacters
 * </p>
 * <p>
 * Description: Defines characters that can be used to define an input mask
 * follows Microsofts convention for defining a mask. Only basic masks defined.
 * The mask place holder characters and the charcaters the place holders
 * represent are read from a resource bundle
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
 * 29-07-2003 AW Daley Initial Version
 */
public class MaskCharacters extends HashMap {

    private final static String DIGIT_PLACE_HOLDER_KEY = "digitPlaceHolder";

    private final static String VALID_DIGITS_KEY = "validDigits";

    private final static String ALPHANUMERIC_PLACE_HOLDER_KEY = "alphanumericPlaceHolder";

    private final static String VALID_ALPHANUMERICS_KEY = "validAlphanumerics";

    private final static String CHARACTER_PLACE_HOLDER_KEY = "characterPlaceHolder";

    private final static String VALID_CHARACTERS_KEY = "validCharacters";

    private final static String LETTER_PLACE_HOLDER_KEY = "letterPlaceHolder";

    private final static String VALID_LETTERS_KEY = "validLetters";

    public String DIGIT_PLACE_HOLDER;

    public String VALID_DIGITS;

    public String ALPHANUMERIC_PLACE_HOLDER;

    public String VALID_ALPHANUMERICS;

    public String CHARACTER_PLACE_HOLDER;

    public String VALID_CHARACTERS;

    public String LETTER_PLACE_HOLDER;

    public String VALID_LETTERS;

    public MaskCharacters() {
        super();

        // Loads the mask place holder characters and the charcaters the place
        // holders represent from a resource bundle.
        loadMaskCharacters();

        // Populates the hash map
        this.put(DIGIT_PLACE_HOLDER, VALID_DIGITS);
        this.put(ALPHANUMERIC_PLACE_HOLDER, VALID_ALPHANUMERICS);
        this.put(CHARACTER_PLACE_HOLDER, VALID_CHARACTERS);
        this.put(LETTER_PLACE_HOLDER, VALID_LETTERS);
    }

    /**
     * Loads the mask place holder characters and the charcaters the place
     * holders represent from a resource bundle.
     */
    private void loadMaskCharacters() {
        ResourceBundle maskCharactersResources = XHIBITConstant.getResourceBundle(XhibitBundles.MaskCharacters);

        DIGIT_PLACE_HOLDER = XHIBITConstant.getResource(maskCharactersResources, DIGIT_PLACE_HOLDER_KEY);

        VALID_DIGITS = XHIBITConstant.getResource(maskCharactersResources, VALID_DIGITS_KEY);

        ALPHANUMERIC_PLACE_HOLDER = XHIBITConstant.getResource(maskCharactersResources, ALPHANUMERIC_PLACE_HOLDER_KEY);

        VALID_ALPHANUMERICS = XHIBITConstant.getResource(maskCharactersResources, VALID_ALPHANUMERICS_KEY);

        CHARACTER_PLACE_HOLDER = XHIBITConstant.getResource(maskCharactersResources, CHARACTER_PLACE_HOLDER_KEY);

        VALID_CHARACTERS = XHIBITConstant.getResource(maskCharactersResources, VALID_CHARACTERS_KEY);

        LETTER_PLACE_HOLDER = XHIBITConstant.getResource(maskCharactersResources, LETTER_PLACE_HOLDER_KEY);

        VALID_LETTERS = XHIBITConstant.getResource(maskCharactersResources, VALID_LETTERS_KEY);
    }
}