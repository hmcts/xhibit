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

public class AppellantAdvocateValue extends CSAbstractValue {
	private static final long serialVersionUID = -2340288483276947513L;
    Integer appellantId;

    Integer appellantAdvocateId;

    String firstName;

    String middleName;

    String surname;

    String fullname;

    public AppellantAdvocateValue() {
    }

    public AppellantAdvocateValue(Integer appellantId, Integer appellantAdvocateId, String firstName,
            String middleName, String surname, String fullname) {
        this.appellantId = appellantId;
        this.appellantAdvocateId = appellantAdvocateId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.fullname = fullname;
    }

    public Integer getAppellantId() {
        return appellantId;
    }

    public Integer appellantAdvocateId() {
        return appellantAdvocateId;
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

    public void setAppellantId(Integer appellantId) {
        this.appellantId = appellantId;
    }

    public void setAppellantAdvocateId(Integer appellantAdvocateId) {
        this.appellantAdvocateId = appellantAdvocateId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void getSurname(String surname) {
        this.surname = surname;
    }

    public void getFullname(String fullname) {
        this.fullname = fullname;
    }

}