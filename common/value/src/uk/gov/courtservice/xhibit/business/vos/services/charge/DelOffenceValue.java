package uk.gov.courtservice.xhibit.business.vos.services.charge;

// jdk
import java.util.Calendar;
import java.util.Collection;

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
 * @author Sarah Tong
 * @version $Id: DelOffenceValue.java,v 1.3 2006/06/05 12:28:43 bzjrnl Exp $
 */

public class DelOffenceValue extends CSAbstractValue {
    private Integer offenceID;

    private Integer courtID;

    private Integer caseID;

    private Integer chargeID;

    private Collection defendantIDs;

    private boolean inCourt; // used for court logging

    private Calendar courtLogDate;
    private static final long serialVersionUID = -5689968377783025016L;

    /**
     * set to true if results have been found for this offence and the court
     * clerk has confirmed they are to be deleted
     */
    private boolean deleteResults;

    public DelOffenceValue() {
    }

    public DelOffenceValue(Integer offenceID, Integer courtID, Integer caseID, Integer chargeID,
            Collection defendantIDs, boolean inCourt) {
        this.offenceID = offenceID;
        this.courtID = courtID;
        this.caseID = caseID;
        this.chargeID = chargeID;
        this.defendantIDs = defendantIDs;
        this.inCourt = inCourt;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public Integer getChargeID() {
        return chargeID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public Collection getDefendantIDs() {
        return defendantIDs;
    }

    public Integer getOffenceID() {
        return offenceID;
    }

    public void setOffenceID(Integer offenceID) {
        this.offenceID = offenceID;
    }

    public void setDefendantIDs(Collection defendantIDs) {
        this.defendantIDs = defendantIDs;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public boolean isInCourt() {
        return inCourt;
    }

    public boolean isDeleteResults() {
        return deleteResults;
    }

    public void setDeleteResults(boolean deleteResults) {
        this.deleteResults = deleteResults;
    }

    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }
}