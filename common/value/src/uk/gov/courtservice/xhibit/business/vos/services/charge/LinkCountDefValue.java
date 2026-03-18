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
 * @version $Id: LinkCountDefValue.java,v 1.4 2006/06/05 12:28:43 bzjrnl Exp $
 */

public class LinkCountDefValue extends CSAbstractValue {
    private Integer caseID;

    private Integer courtID;

    private Calendar courtLogDate;
    
    private static final long serialVersionUID = -7767936904359370344L;

    /**
     * This is a Collection of one dimensional Integer arrays, each array
     * containing two Integers. The first Integer will be the countID, the
     * second Integer will be the defendantID. e.g.
     * 
     * Vector countDefPairs = new Vector(); Integer[] pairOne = {new Integer(1),
     * new Integer(2)}; Integer[] pairTwo = {new Integer(1), new Integer(3)};
     * countDefPairs.add(pairOne); countDefPairs.add(pairTwo);
     */
    private boolean inCourt; // used for court logging

    private java.util.Collection defendantOnOffenceValues;

    /**
     * used for court logging. If true, defendant is being added to count
     * otherwise count is being added to defendant
     */
    private boolean addDefendantToCount;

    private String chargeType;

    public LinkCountDefValue() {
    }

    public LinkCountDefValue(Integer caseID, Integer courtID, Collection defendantOnOffenceBasicValues,
            boolean inCourt, boolean isAddDefendantToCount, String chargeType) {
        this.caseID = caseID;
        this.courtID = courtID;
        this.defendantOnOffenceValues = defendantOnOffenceBasicValues;
        this.inCourt = inCourt;
        this.addDefendantToCount = addDefendantToCount;
        this.chargeType = chargeType;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public boolean isInCourt() {
        return inCourt;
    }

    public void setIsInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

    public void setDefendantOnOffenceValues(java.util.Collection defendantOnOffenceValues) {
        this.defendantOnOffenceValues = defendantOnOffenceValues;
    }

    public java.util.Collection getDefendantOnOffenceValues() {
        return defendantOnOffenceValues;
    }

    public void setAddDefendantToCount(boolean addDefendantToCount) {
        this.addDefendantToCount = addDefendantToCount;
    }

    public boolean isAddDefendantToCount() {
        return addDefendantToCount;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeType() {
        return chargeType;
    }
}