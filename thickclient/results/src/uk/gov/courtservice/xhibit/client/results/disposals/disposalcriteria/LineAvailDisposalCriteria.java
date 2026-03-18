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
 * @version $Revision: 1.4 $
 */
public class LineAvailDisposalCriteria extends AbstractDisposalCriteria {
    /**
     * The criteria to match
     */
    private final int lineAvail;

    /**
     * Construct a new object to score the specifed criteria
     */
    public LineAvailDisposalCriteria(int lineAvail) {
        this.lineAvail = lineAvail;
    }

    /**
     * AbstractDisposalCriteria Implementaion
     */
    protected int scoreImpl(DisposalReferenceValue disposal) {
        return lineAvail == disposal.getLineAvail() ? LINE_AVAIL_MATCH_SCORE : 0;
    }
}
