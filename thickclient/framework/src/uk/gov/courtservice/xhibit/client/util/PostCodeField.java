package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>Title: PostCodeField</p>
 * <p>Description: Used for evaluating a PostCode</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 *
 */
import org.exolab.castor.util.RegExpEvaluator;

public class PostCodeField extends RegExpField {

    /**
     * construct the Regular Expression Evaluator
     */
    private static final RegExpEvaluator EVALUATOR = createEvaluator("(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
            + "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})");

    /**
     * error code resource name
     */
    private static final String POSTCODE_ERROR = "postcode.error";

    public PostCodeField(String value) {
        super(value, false);
    }

    /**
     * Return the resources for postcode error
     * 
     * @return postcode error
     */
    public String getErrorKey() {
        return POSTCODE_ERROR;
    }

    /**
     * Return the constructed Regular Expression Evaluator
     */
    public RegExpEvaluator getEvaluator() {
        return EVALUATOR;
    }

    /**
     * convert the PostCode to upper case if valid
     * 
     * @param value
     *            PostCode to convert to uppercase
     * @return converted PostCode
     */
    protected String transformValue(String value) {
        return value.toUpperCase();
    }

}
