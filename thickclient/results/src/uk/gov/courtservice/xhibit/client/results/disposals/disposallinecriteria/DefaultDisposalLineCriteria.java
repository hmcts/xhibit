package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DefaultDisposalLineCriteria
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
public class DefaultDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * AbstractDisposalCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return WEAKEST_MATCH_SCORE;
    }
}
