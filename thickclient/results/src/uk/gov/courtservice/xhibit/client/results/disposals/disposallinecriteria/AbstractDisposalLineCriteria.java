package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.client.results.disposals.DisposalLineCriteria;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: AbstractDisposalLineCriteria
 * </p>
 * <p>
 * Description: Provide common DisposalLineCriteria functionality
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
public abstract class AbstractDisposalLineCriteria implements DisposalLineCriteria {
    /**
     * DisposalLineCriteria implementation prov
     */
    public int score(DisposalLineReferenceValue line) {
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }
        return scoreImpl(line);
    }

    /**
     * Implementaion of the score logic line should never be null
     */
    protected abstract int scoreImpl(DisposalLineReferenceValue line);

    /**
     * Return the given score if the if the values are equal
     */
    protected static boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null) || (value1 != null && value1.equals(value2));
    }
}