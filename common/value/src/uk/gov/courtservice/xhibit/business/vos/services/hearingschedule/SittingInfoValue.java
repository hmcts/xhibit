package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;

/**
 * <p>
 * Title: SittingInfoValue
 * </p>
 * <p>
 * Description: Sitting Information Used for the Add BWH/Move Case
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

public class SittingInfoValue extends CSAbstractValue {
	
	static final long serialVersionUID = -2803760026191521840L;

    private SittingBasicValue sittingBasicValue;

    private String judgeName;

    public SittingInfoValue() {
    }

    public String getJudgeName() {
        return judgeName;
    }

    public SittingBasicValue getSittingBasicValue() {
        return sittingBasicValue;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public void setSittingBasicValue(SittingBasicValue sittingBasicValue) {
        this.sittingBasicValue = sittingBasicValue;
    }
}