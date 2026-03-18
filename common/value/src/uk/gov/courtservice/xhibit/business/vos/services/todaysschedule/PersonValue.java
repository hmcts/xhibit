package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PersonValue
 * </p>
 * <p>
 * Description: JudgeValue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class PersonValue extends CSAbstractValue {
	private static final long serialVersionUID = 8738250636524920257L;
	Integer ID;

    private String firstName, middleName, surname;

    public PersonValue() {
    }

    public PersonValue(Integer ID, String firstName, String middleName, String surname) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    /**
     * 
     * @return
     */
    public String getFirstName() {
        if (firstName != null)
            return this.firstName;
        else
            return "";
    }

    /**
     * 
     * @return
     */
    public String getMiddleName() {
        if (middleName != null)
            return this.middleName;
        else
            return "";
    }

    /**
     * 
     * @return
     */
    public String getSurname() {
        if (surname != null)
            return this.surname;
        else
            return "";
    }

    /**
     * 
     * @return
     */
    public String getFullName() {
        return getFirstName() + " " + getInitial(middleName) + " " + getSurname();
    }

    /**
     * 
     * @param name
     * @return
     */
    private String getInitial(String name) {
        if (name != null && name.length() > 0)
            return name.substring(0, 1).toUpperCase();
        else
            return "";

    }
}