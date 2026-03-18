package uk.gov.courtservice.xhibit.client.results.disposals.disposalprocessor;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalProcessor;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalProcessor
 * </p>
 * <p>
 * Description: Provides common processing functionality
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public abstract class AbstractDisposalProcessor implements DisposalProcessor {
    /**
     * DisposalProcessor Implementation
     */
    public void process(DisposalReferenceValue disposal) {
        if (disposal == null) {
            throw new IllegalArgumentException("disposal: null");
        }
        processImpl(disposal);
    }

    /**
     * Process implementation, must not be null
     */
    protected abstract void processImpl(DisposalReferenceValue disposal);
}