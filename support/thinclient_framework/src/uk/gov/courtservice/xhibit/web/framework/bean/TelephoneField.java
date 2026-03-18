package uk.gov.courtservice.xhibit.web.framework.bean;

import org.exolab.castor.util.RegExpEvaluator;

/**
 * <p>
 * Title: TelephoneField
 * </p>
 * <p>
 * Description: Used for evaluating a Telephone/Fax Number
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 */
public class TelephoneField extends RegExpField {
    /**
     * construct the Regular Expression Evaluator
     */
    private static final RegExpEvaluator EVALUATOR = createEvaluator("[0-9 -]{1,20}");

    /**
     * error code resource name
     */
    private static final String TELEPHONE_ERROR = "telephone.error";

    public TelephoneField(String value) {
        super(value);
    }

    public TelephoneField(String value, boolean nullable) {
        super(value, nullable);
    }

    /**
     * Return the resources for postcode error
     * 
     * @return postcode error
     */
    public String getErrorKey() {
        return TELEPHONE_ERROR;
    }

    /**
     * Return the constructed Regular Expression Evaluator
     */
    public RegExpEvaluator getEvaluator() {
        return EVALUATOR;
    }
}
