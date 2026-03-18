package uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DefaultDisposalCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed Disposal
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic prompt Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class DefaultDisposalCriteria extends AbstractDisposalCriteria {
    /**
     * AbstractDisposalCriteria Implementaion
     */
    protected int scoreImpl(DisposalReferenceValue disposal) {
        return WEAKEST_MATCH_SCORE;
    }
}
