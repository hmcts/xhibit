package uk.gov.courtservice.xhibit.business.database.results.processor;

// XHIBIT
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: RefDisposalTypeProcessor
 * </p>
 * <p>
 * Description: Process the disposal type reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class RefDisposalTypeProcessor extends AbstractRefDisposalTypeProcessor {

    /**
     * List of of type items keyed by typeItemId
     */
    private DisposalReferenceValue type;

    /**
     * Process the value
     * 
     * @param value
     *            the value to process
     */
    public void processValue(DisposalReferenceValue value) {
        if (type != null) {
            throw new IllegalStateException("type: " + type);
        }
        type = value;
    }

    /**
     * Access the type List
     * 
     * @return the type list
     */
    public DisposalReferenceValue getType() {
        return type;
    }
}