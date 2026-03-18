package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

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
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 * @version $Id: PersonValue.java,v 1.6 2015/12/01 21:56:34 atwells Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 17/02/03 - ARH - First release.
 * </P>
 * 
 */

public class PersonValue extends CSAbstractValue {
	private static final long serialVersionUID = -5692097820194849941L;
	public static final String COURT_CLERK = "CC";

    public static final String USHER = "U";

    public static final String COURT_REPORTER = "CR";

    public static final String JUDGE = "J";

    public static final String JUSTICE = "JP";

    public static final String DEFENCE = "D";

    public static final String PROSECUTION = "P";

    public static final String RESPONDENT = "R";

    public static final String OBJECTOR = "O";

    public static final String THIRD_PARTY = "TP";

    public static final String SOLICITOR = "S";

    public static final String ADVOCATE = "A";

    public static final String IN_PERSON = "I";
    
    public static final String NON_ATTENDANCE = "N";

    public static final String DEFENDANT = "DF";

    private String firstName;

    private String middleName;

    private String surname;

    private String fullName;

    private String personType;

    /** SheduledHearingAttendeeId/SHLegRepId */
    private Integer parentId;

    /** SheduledHearingAttendee.version/SHLegRepId.version */
    private Integer parentVersion;

    public PersonValue() {
    }

    public PersonValue(Integer id, Integer version) {
        super(id, version);
    }

    public PersonValue(Integer parentId, Integer parentVersion, Integer id, Integer version, String fullName,
            String personType) {
        this(id, version);
        this.parentId = parentId;
        this.parentVersion = parentVersion;
        this.fullName = fullName;
        this.personType = personType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonType() {
        return personType;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentVersion(Integer parentVersion) {
        this.parentVersion = parentVersion;
    }

    public Integer getParentVersion() {
        return parentVersion;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }
}