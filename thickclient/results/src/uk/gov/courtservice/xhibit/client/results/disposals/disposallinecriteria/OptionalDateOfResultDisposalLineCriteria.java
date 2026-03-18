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
 * @version $Revision: 1.5 $
 */
public class OptionalDateOfResultDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return equals("D2", line.getDbDestin()) && !line.isMandatory() ? STRONGEST_MATCH_SCORE : 0;
    }
}
