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

public class DefenceAdvocateValue extends CSAbstractValue {
	private static final long serialVersionUID = -6408404390889694431L;
	Integer defendantId;

    Integer defendantAdvocateId;

    String firstName;

    String middleName;

    String surname;

    String fullname;

    public DefenceAdvocateValue() {
    }

    public DefenceAdvocateValue(Integer defendantId, Integer defendantAdvocateId, String firstName, String middleName,
            String surname, String fullname) {
        this.defendantId = defendantId;
        this.defendantAdvocateId = defendantAdvocateId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.fullname = fullname;

    }

    public Integer getDefendantId() {
        return defendantId;
    }

    public Integer getDefendantAdvocateId() {
        return defendantAdvocateId;
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

    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }

    public void setDefendantAdvocateId(Integer defendantAdvocateId) {
        this.defendantAdvocateId = defendantAdvocateId;
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