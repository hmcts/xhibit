package uk.gov.courtservice.xhibit.business.vos.services.charge;

// jdk
import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: BreachValue
 * </p>
 * <p>
 * Description: BreachValue is intended to represent breach entities as stored
 * in the Breach table, and the corresponding ChargeValue object (which contains
 * 1 or more OffenceValue objects
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Id: BreachValue.java,v 1.3 2006/06/05 12:28:42 bzjrnl Exp $
 */
public class BreachValue extends CSAbstractValue {
    private Integer breachID;

    private Integer caseID;

    private String originalSentence;

    private Calendar originalSentenceDate;

    private String originalCourtType;

    private Integer originalCourtID;

    private String originalCourtName;

    private String originalCourtShortName;

    private Calendar datePut;

    private String breachType;

    private String bringBack;

    private String hoCode;

    private String hoDescription;

    private boolean inCourt;

    private String plea;

    private Integer chargeID;

    private Integer refSystemCodeID;

    private Calendar courtLogDate;
    
    private String lastUpdatedBy;
    
    private static final long serialVersionUID = -5152913399338600345L;

    /**
     * @roseuid 3DBFBF3501F7
     */
    public BreachValue() {

    }

    /**
     * @param refOffenceID
     * @param breachType
     * @param chargeValue
     * @roseuid 3DB0033302DC
     */
    public BreachValue(Integer breachID, Integer caseID, String originalSentence, Calendar originalSentenceDate,
            String originalCourtType, Integer originalCourtID, String originalCourtName, Calendar datePut,
            String breachType, String bringBack, String hoCode, String hoDescription, boolean inCourt, String plea,
            Integer chargeID, Integer refSystemCodeID) {
        this(caseID, originalSentence, originalSentenceDate, originalCourtType, originalCourtID, datePut, breachType,
                bringBack, hoCode, hoDescription, inCourt);
        this.breachID = breachID;
        this.plea = plea;
        this.chargeID = chargeID;
        this.refSystemCodeID = refSystemCodeID;
        this.originalCourtName = originalCourtName;
    }

    public BreachValue(Integer caseID, String originalSentence, Calendar originalSentenceDate,
            String originalCourtType, Integer originalCourtID, Calendar datePut, String breachType, String bringBack,
            String hoCode, String hoDescription, boolean inCourt) {
        this.caseID = caseID;
        this.originalSentence = originalSentence;
        this.originalSentenceDate = originalSentenceDate;
        this.originalCourtType = originalCourtType;
        this.originalCourtID = originalCourtID;
        this.datePut = datePut;
        this.breachType = breachType;
        this.bringBack = bringBack;
        this.hoCode = hoCode;
        this.hoDescription = hoDescription;
        this.inCourt = inCourt;
    }

    /**
     * accessor methods
     * 
     * @roseuid 3DB01C4F02C1
     */

    public Integer getBreachID() {
        return breachID;
    }

    public String getBreachType() {
        return breachType;
    }

    public String getBringBack() {
        return bringBack;
    }

    public Calendar getDatePut() {
        return datePut;
    }

    public Integer getOriginalCourtID() {
        return originalCourtID;
    }

    public String getOriginalCourtType() {
        return originalCourtType;
    }

    public String getOriginalSentence() {
        return originalSentence;
    }

    public Calendar getOriginalSentenceDate() {
        return originalSentenceDate;
    }

    public void setBreachID(Integer breachID) {
        this.breachID = breachID;
    }

    public void setBreachType(String breachType) {
        this.breachType = breachType;
    }

    public void setBringBack(String bringBack) {
        this.bringBack = bringBack;
    }

    public void setDatePut(Calendar datePut) {
        this.datePut = datePut;
    }

    public void setOriginalCourtID(Integer originalCourtID) {
        this.originalCourtID = originalCourtID;
    }

    public void setOriginalCourtType(String originalCourtType) {
        this.originalCourtType = originalCourtType;
    }

    public void setOriginalSentence(String originalSentence) {
        this.originalSentence = originalSentence;
    }

    public void setOriginalSentenceDate(Calendar originalSentenceDate) {
        this.originalSentenceDate = originalSentenceDate;
    }

    public String getHoCode() {
        return hoCode;
    }

    public String getHoDescription() {
        return hoDescription;
    }

    public void setHoDescription(String hoDescription) {
        this.hoDescription = hoDescription;
    }

    public void setHoCode(String hoCode) {
        this.hoCode = hoCode;
    }

    public boolean isInCourt() {
        return inCourt;
    }

    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public String getPlea() {
        return plea;
    }

    public Integer getRefSystemCodeID() {
        return refSystemCodeID;
    }

    public void setRefSystemCodeID(Integer refSystemCodeID) {
        this.refSystemCodeID = refSystemCodeID;
    }

    public void setPlea(String plea) {
        this.plea = plea;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public Integer getChargeID() {
        return chargeID;
    }

    public String getOriginalCourtName() {
        return originalCourtName;
    }

    public void setOriginalCourtName(String originalCourtName) {
        this.originalCourtName = originalCourtName;
    }

    public String getOriginalCourtShortName() {
        return originalCourtShortName;
    }

    public void setOriginalCourtShortName(String originalCourtShortName) {
        this.originalCourtShortName = originalCourtShortName;
    }

    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
