package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: A list of defendant that require authorisation
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
* 
 * @version 1.0
 */

public class AuthorisationRequestValue extends CSAbstractValue {
	
	static final long serialVersionUID = 5864520605751072345L;

	private String caseType; 
	
    private Integer caseId;
    

    private AuthorisationValue[] defendantsToAuthorise;

    private Calendar courtLogDate;

    public AuthorisationRequestValue() {
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    /**
     * Get/Set a date to be used for the court log event creation
     * 
     * @return
     */
    public Calendar getCourtLogDate() {
        return courtLogDate;
    }

    public void setCourtLogDate(Calendar courtLogDate) {
        this.courtLogDate = courtLogDate;
    }

    /**
     * Set the defendantOnCase id's to authorise
     * 
     * @param defendantsToAuthorise
     */
    public void setDefendantsToAuthorise(AuthorisationValue[] defendantsToAuthorise) {
        this.defendantsToAuthorise = defendantsToAuthorise;
    }

    /**
     * @return Array of defendantOnCase Id's
     */
    public AuthorisationValue[] getDefendantsToAuthorise() {
        return defendantsToAuthorise;
    }

}