package uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalCriteria;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalCriteria
 * </p>
 * <p>
 * Description: Provide common DisposalCriteria functionality
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
public abstract class AbstractDisposalCriteria implements DisposalCriteria {
    /**
     * DisposalCriteria implementation prov
     */
    public int score(DisposalReferenceValue disposal) {
        if (disposal == null) {
            throw new IllegalArgumentException("disposal: null");
        }
        return scoreImpl(disposal);
    }

    /**
     * Implementaion of the score logic should never be null
     */
    protected abstract int scoreImpl(DisposalReferenceValue disposal);

    /**
     * Return the given score if the if the values are equal
     */
    protected static boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null) || (value1 != null && value1.equals(value2));
    }
}