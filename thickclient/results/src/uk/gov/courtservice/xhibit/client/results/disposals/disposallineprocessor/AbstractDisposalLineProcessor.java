package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineProcessor;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalLineProcessor
 * </p>
 * <p>
 * Description: Provides common line processing functionality
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
public abstract class AbstractDisposalLineProcessor implements DisposalLineProcessor {
    /**
     * DisposalLineProcessor Implementation
     */
    public void process(DisposalLineReferenceValue line) {
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }
        processImpl(line);
    }

    /**
     * Process implementation, line must not be null
     */
    protected abstract void processImpl(DisposalLineReferenceValue line);
}