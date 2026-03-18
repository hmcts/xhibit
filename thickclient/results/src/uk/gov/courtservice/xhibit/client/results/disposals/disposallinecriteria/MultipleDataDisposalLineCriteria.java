package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: MultipleDataDisposalLineCriteria
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
 * @version $Revision: 1.4 $
 */
public class MultipleDataDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final boolean input;

    /**
     * The criteria to match
     */
    private final String[] data;

    /**
     * Construct a new object to score the specifed criteria
     */
    public MultipleDataDisposalLineCriteria(String[] data) {
        this(data, false);
    }

    /**
     * Construct a new object to score the specifed criteria
     */
    public MultipleDataDisposalLineCriteria(String[] data, boolean input) {
        if (data == null) {
            throw new IllegalArgumentException("data: null");
        }
        this.data = data;
        this.input = input;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        if (line.isInput() == input) {
            String lineData = line.getData();
            for (int i = 0; i < data.length; i++) {
                if (equals(data[i], lineData)) {
                    return DATA_MATCH_SCORE;
                }
            }
        }
        return 0;
    }
}
