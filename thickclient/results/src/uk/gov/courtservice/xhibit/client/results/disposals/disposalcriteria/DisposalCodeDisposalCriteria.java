package uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DisposalCodeDisposalCriteria
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
public class DisposalCodeDisposalCriteria extends AbstractDisposalCriteria {
    /**
     * The criteria to match
     */
    private final String disposalCode;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DisposalCodeDisposalCriteria(String disposalCode) {
        this.disposalCode = disposalCode;
    }

    /**
     * AbstractDisposalCriteria Implementaion
     */
    protected int scoreImpl(DisposalReferenceValue disposal) {
        return equals(disposalCode, disposal.getDisposalCode()) ? DISPOSAL_CODE_MATCH_SCORE : 0;
    }
}
