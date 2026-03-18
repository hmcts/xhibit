package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule;

// JDK
import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;

/**
 * <p>
 * Title: CaseInfoValue
 * </p>
 * <p>
 * Description: Case Information used for Add BWH
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

public class CaseInfoValue extends CSAbstractValue {
	private static final long serialVersionUID = -4255316673113295833L;
    private Collection defendants;

    private CaseBasicValue caseBasicValue;

    private String judgeName;

    public CaseInfoValue() {
    }

    public Collection getDefendants() {
        return defendants;
    }

    public void setDefendants(Collection defendants) {
        this.defendants = defendants;
    }

    public CaseBasicValue getCaseBasicValue() {
        return caseBasicValue;
    }

    public void setCaseBasicValue(CaseBasicValue caseBasicValue) {
        this.caseBasicValue = caseBasicValue;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

}