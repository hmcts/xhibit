package uk.gov.courtservice.xhibit.business.vos.services.charge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jide Fakoya
 * @version 1.0
 */

public class CaseStatusValue extends CSAbstractValue {
    private Integer caseID;

    private boolean inCourt;
    private static final long serialVersionUID = -2445979049191618774L;

    public CaseStatusValue(Integer caseID, boolean inCourt) {
        this.caseID = caseID;
        this.inCourt = inCourt;
    }

    /**
     * 
     * @return
     */
    public Integer getCaseID() {
        return caseID;
    }

    /**
     * 
     * @param caseID
     */
    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    /**
     * 
     * @param inCourt
     */
    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    /**
     * 
     * @return
     */
    public boolean isInCourt() {
        return inCourt;
    }
}