package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

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
 * @author Narges Berry
 * @version 1.0
 */

public class RespondentAdvocateValue extends CSAbstractValue {
	private static final long serialVersionUID = -8315690331447899426L;
    Integer respondentId;

    Integer respondentAdvocateId;

    String firstName;

    String middleName;

    String surname;

    String fullname;

    public RespondentAdvocateValue() {
    }

    public RespondentAdvocateValue(Integer respondentId, Integer respondentAdvocateId, String firstName,
            String middleName, String surname, String fullname) {
        this.respondentId = respondentId;
        this.respondentAdvocateId = respondentAdvocateId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.fullname = fullname;
    }

    public Integer getRespondenttId() {
        return respondentId;
    }

    public Integer getRespondentAdvocateId() {
        return respondentAdvocateId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setRespondentId(Integer respondentId) {
        this.respondentId = respondentId;
    }

    public void setRespondentAdvocateId(Integer appellantAdvocateId) {
        this.respondentAdvocateId = respondentAdvocateId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}