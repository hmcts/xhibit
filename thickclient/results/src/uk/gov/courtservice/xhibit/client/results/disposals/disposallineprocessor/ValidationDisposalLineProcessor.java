package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: ValidationDisposalLineProcessor
 * </p>
 * <p>
 * Description: Set validation to the specified value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.3 $
 */
public class ValidationDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final String validation;

    /**
     * Construct a new processor to set the prompt as specified
     */
    public ValidationDisposalLineProcessor(String validation) {
        this.validation = validation;
    }

    /**
     * Process implementation, input must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setValidation(validation);
    }
}
