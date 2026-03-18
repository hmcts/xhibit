package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This class is not completed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class CLCaseValue extends CSAbstractValue {
    private String userId;

    private String caseDetails;
    private static final long serialVersionUID = -4520555352125756951L;

    public CLCaseValue() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public String getUserId() {
        return userId;
    }

    public String getCaseDetails() {
        return caseDetails;
    }
}