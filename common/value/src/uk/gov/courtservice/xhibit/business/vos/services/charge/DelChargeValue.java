package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.Calendar;

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
 * @version $Id: DelChargeValue.java,v 1.3 2006/06/05 12:28:43 bzjrnl Exp $
 */

public class DelChargeValue extends CSAbstractValue {
    private Integer chargeID;

    private Integer courtID;

    private Integer caseID;

    private Integer crestChargeSeqNo; // required for delete indictment

    private Integer crestChargeID; // required for delete breach

    private boolean inCourt; // used for court logging

    private Calendar courtLogDate;
    private static final long serialVersionUID = 4491285665224953124L;

    /**
     * set to true if results have been found for this charge and the court
     * clerk has confirmed they are to be deleted
     */
    private boolean deleteResults;

    public DelChargeValue() {
    }

    public DelChargeValue(Integer chargeID, Integer courtID, Integer caseID, Integer crestChargeSeqNo,
            Integer crestChargeID, boolean inCourt) {
        this.chargeID = chargeID;
        this.courtID = courtID;
        this.caseID = caseID;
        this.crestChargeSeqNo = crestChargeSeqNo;
        this.crestChargeID = crestChargeID;
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

    public Integer getCrestChargeID() {
        return crestChargeID;
    }

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setCrestChargeSeqNo(Integer crestChargeSeqNo) {
        this.crestChargeSeqNo = crestChargeSeqNo;
    }

    public void setCrestChargeID(Integer crestChargeID) {
        this.crestChargeID = crestChargeID;
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