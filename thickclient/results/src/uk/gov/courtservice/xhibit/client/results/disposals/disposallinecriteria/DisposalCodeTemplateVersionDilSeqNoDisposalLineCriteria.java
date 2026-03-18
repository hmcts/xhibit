package uk.gov.courtservice.xhibit.client.results.disposals.disposallinecriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DisposalCodeTemplateVersionDilSeqNoDisposalLineCriteria
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
public class DisposalCodeTemplateVersionDilSeqNoDisposalLineCriteria extends AbstractDisposalLineCriteria {
    /**
     * The criteria to match
     */
    private final String disposalCode;

    /**
     * The criteria to match
     */
    private final int templateVersion;

    /**
     * The criteria to match
     */
    private final int dilSeqNo;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DisposalCodeTemplateVersionDilSeqNoDisposalLineCriteria(String disposalCode, int templateVersion,
            int dilSeqNo) {
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion;
        this.dilSeqNo = dilSeqNo;
    }

    /**
     * AbstractDisposalLineCriteria Implementaion
     */
    protected int scoreImpl(DisposalLineReferenceValue line) {
        return (equals(disposalCode, line.getDisposalCode()) && templateVersion == line.getTemplateVersion() && dilSeqNo == line
                .getDilSeqNo()) ? DISPOSAL_CODE_MATCH_SCORE + TEMPLATE_VERSION_MATCH_SCORE + DIL_SEQ_NO_MATCH_SCORE : 0;
    }
}
