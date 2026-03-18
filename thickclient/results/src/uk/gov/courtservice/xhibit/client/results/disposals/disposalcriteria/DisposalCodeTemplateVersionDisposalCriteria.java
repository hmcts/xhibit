package uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: DisposalCodeTemplateVersionDisposalCriteria
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
 * @version $Revision: 1.6 $
 */
public class DisposalCodeTemplateVersionDisposalCriteria extends AbstractDisposalCriteria {
    /**
     * The criteria to match
     */
    private final String disposalCode;

    /**
     * The criteria to match
     */
    private final int templateVersion;

    /**
     * Construct a new object to score the specifed criteria
     */
    public DisposalCodeTemplateVersionDisposalCriteria(String disposalCode, int templateVersion) {
        this.disposalCode = disposalCode;
        this.templateVersion = templateVersion;
    }

    /**
     * AbstractDisposalCriteria Implementaion
     */
    protected int scoreImpl(DisposalReferenceValue disposal) {
        return (equals(disposalCode, disposal.getDisposalCode()) && templateVersion == disposal.getTemplateVersion()) ? DISPOSAL_CODE_MATCH_SCORE
                + TEMPLATE_VERSION_MATCH_SCORE
                : 0;
    }
}
