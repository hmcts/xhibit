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
 * @author Pete Raymond
 * @version 1.0
 */

public class ObjectorAdvocateValue extends CSAbstractValue {
	private static final long serialVersionUID = -4502264946432787020L;
	Integer objectorId;

    Integer objectorAdvocateId;

    String firstName;

    String middleName;

    String surname;

    String fullname;

    public ObjectorAdvocateValue() {
    }

    public ObjectorAdvocateValue(Integer objectorId, Integer objectorAdvocateId, String firstName, String middleName,
            String surname, String fullname) {
        this.objectorId = objectorId;
        this.objectorAdvocateId = objectorAdvocateId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.fullname = fullname;
    }

    public Integer getObjectorId() {
        return objectorId;
    }

    public Integer objectorAdvocateId() {
        return objectorAdvocateId;
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

    public void setObjectorId(Integer objectortId) {
        this.objectorId = objectorId;
    }

    public void setObjectorAdvocateId(Integer appellantAdvocateId) {
        this.objectorAdvocateId = objectorAdvocateId;
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