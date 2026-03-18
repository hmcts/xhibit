package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseSittingInfoValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 */

public class CaseSittingInfoValue extends CSAbstractValue {

    /** @todo Doco */
	private static final long serialVersionUID = -9007089871853624293L;
    private CaseInfoValue caseInfoValue;

    private SittingInfoValue sittingInfoValue;

    public CaseSittingInfoValue() {
    }

    public CaseInfoValue getCaseInfoValue() {
        return caseInfoValue;
    }

    public SittingInfoValue getSittingInfoValue() {
        return sittingInfoValue;
    }

    public void setCaseInfoValue(CaseInfoValue caseInfoValue) {
        this.caseInfoValue = caseInfoValue;
    }

    public void setSittingInfoValue(SittingInfoValue sittingInfoValue) {
        this.sittingInfoValue = sittingInfoValue;
    }
}