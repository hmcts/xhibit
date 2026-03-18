package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: MandatoryDisposalLineProcessor
 * </p>
 * <p>
 * Description: Set mandatory to the specified value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class MandatoryDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final boolean mandatory;

    /**
     * Construct a new processor to set mandatory as specified
     */
    public MandatoryDisposalLineProcessor(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Process implementation, line must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setMandatory(mandatory);
    }
}
