package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: FooterDisposalLineCriteria
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
public class FooterDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * AbstractDisposalLineCriteria Implementaion The line is a footer if it is
     * all equals
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        if (line.isInput()) {
            return 0;
        }

        String data = line.getData();
        if (data == null) {
            return 0;
        }

        for (int i = 0, l = data.length(); i < l; i++) {
            if (data.charAt(i) != '=') {
                return 0;
            }
        }

        return STRONGEST_MATCH_SCORE;
    }
}