package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalCodeTemplateVersionDisposalLineCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed DisposalLine
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
public class DisposalCodeDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final String disposalCode;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DisposalCodeDisposalLineCriteria(String disposalCode) {
        this.disposalCode = disposalCode;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return equals(disposalCode, line.getDisposalCode()) ? DISPOSAL_CODE_MATCH_SCORE : 0;
    }
}
