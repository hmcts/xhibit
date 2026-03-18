package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * Title: CaseHearingValue
 * </p>
 * <p>
 * Description: This is will be used for linking hearings. It holds the case and
 * some of its details and also related hearings.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class CaseHearingValue implements Serializable {
	private static final long serialVersionUID = -2623580953299886931L;

    private Integer caseID;

    private Integer caseNumber;

    private String caseType;

    private String caseSubType;

    private Integer courtID;

    private Collection hearingValues;

    /**
     * Default constructor
     */
    public CaseHearingValue() {
    }

    /**
     * Constructor that takes all the attributes as arguments
     * 
     * @param caseID
     * @param caseNumber
     * @param caseType
     * @param caseSubType
     * @param courtID
     * @param hearingValues
     */
    public CaseHearingValue(Integer caseID, Integer caseNumber, String caseType, String caseSubType, Integer courtID,
            Collection hearingValues) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.caseSubType = caseSubType;
        this.courtID = courtID;
        this.hearingValues = hearingValues;
    }

    // getters
    public Integer getCaseID() {
        return this.caseID;
    }

    public Integer getCaseNumber() {
        return this.caseNumber;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public String getCaseSubType() {
        return this.caseSubType;
    }

    public Integer getCourtID() {
        return this.courtID;
    }

    public Collection getHearingValues() {
        return this.hearingValues;
    }

    // setters
    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public void setHearingValues(Collection hearingValues) {
        this.hearingValues = hearingValues;
    }

}