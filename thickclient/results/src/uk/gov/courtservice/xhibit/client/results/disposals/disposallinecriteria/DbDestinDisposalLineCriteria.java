package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DbdestinDisposalLineCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed DisposalLine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic dbdestin Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.4 $
 */
public class DbDestinDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final String dbdestin;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DbDestinDisposalLineCriteria(String dbdestin) {
        this.dbdestin = dbdestin;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return equals(dbdestin, line.getDbDestin()) ? DBDESTIN_MATCH_SCORE : 0;
    }
}
