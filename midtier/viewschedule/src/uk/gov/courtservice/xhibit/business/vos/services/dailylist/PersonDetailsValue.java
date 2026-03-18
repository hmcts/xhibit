package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class PersonDetailsValue {

    private String firstName;

    private String surname;

    private String middleName;

    public PersonDetailsValue() {
    }

    public PersonDetailsValue(String firstName, String surname, String middleName) {
        this.firstName = firstName;
        this.surname = surname;
        this.middleName = middleName;
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
}