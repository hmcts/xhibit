package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: ValidationDisposalLineCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed DisposalLine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic validation Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class ValidationDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final String validation;

    /**
     * Construct a new object to score the specifed criteria
     */
    public ValidationDisposalLineCriteria(String validation) {
        this.validation = validation;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return equals(validation, line.getValidation()) ? VALIDATION_MATCH_SCORE : 0;
    }
}
