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
 * @version $Revision: 1.4 $
 */
public class SeperatorDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * AbstractDisposalLineCriteria Implementaion, a line is a seperator if it
     * starts or ends with an '*'
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        if (line.isInput()) {
            return 0;
        }

        String data = line.getData();
        if (data == null || data.length() < 2 || data.charAt(0) != '*' || data.charAt(data.length() - 1) != '*') {
            return 0;
        }

        return STRONGEST_MATCH_SCORE;
    }
}