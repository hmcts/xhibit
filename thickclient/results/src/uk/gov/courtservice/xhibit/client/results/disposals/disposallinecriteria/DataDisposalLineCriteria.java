package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DataDisposalLineCriteria
 * </p>
 * <p>
 * Description: A Criteria object to match the specifed DisposalLine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.7 $
 */
public class DataDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final boolean input;

    /**
     * The criteria to match
     */
    private final String data;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DataDisposalLineCriteria(String data) {
        this(data, false);
    }

    /**
     * Construct a new object to score the specifed criteria
     */
    public DataDisposalLineCriteria(String data, boolean input) {
        this.data = data;
        this.input = input;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return line.isInput() == input && equals(data, line.getData()) ? DATA_MATCH_SCORE : 0;
    }
}
